package br.com.laurindoservicesrr.vini;

import br.com.laurindoservicesrr.vini.model.Contato;
import br.com.laurindoservicesrr.vini.repository.ContatoRepository;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    private ContatoRepository repository = new ContatoRepository();
    private TableView<Contato> tabela = new TableView<>();
    private ObservableList<Contato> listaContatos = FXCollections.observableArrayList();

    // Inputs
    private TextField txtCidade, txtBairro, txtPessoa, txtDoc, txtTelefone, txtCoordenador, txtBusca;

    @Override
    public void start(Stage stage) {
        // --- CONFIGURAÇÃO DA TABELA ---
        configurarTabela();

        // --- FORMULÁRIO DE ENTRADA ---
        VBox form = criarFormulario();

        // --- BARRA DE BUSCA E AÇÕES ---
        HBox buscaLayout = criarBarraBusca();

        // --- LAYOUT PRINCIPAL ---
        VBox root = new VBox(15, form, buscaLayout, tabela);
        root.setPadding(new Insets(20));

        // Estilo básico via código bruto
        root.setStyle("-fx-background-color: #f4f4f4;");

        Scene scene = new Scene(root, 1000, 700);
        stage.setTitle("Laurindo Services RR - Gestão de Contatos");
        stage.setScene(scene);
        stage.show();

        // Carregar dados iniciais
        atualizarTabela();
    }

    private VBox criarFormulario() {
        txtCidade = new TextField(); txtCidade.setPromptText("Cidade");
        txtBairro = new TextField(); txtBairro.setPromptText("Bairro");
        txtPessoa = new TextField(); txtPessoa.setPromptText("Nome da Pessoa");
        txtDoc = new TextField(); txtDoc.setPromptText("Documento/CPF");
        txtTelefone = new TextField(); txtTelefone.setPromptText("Telefone");
        txtCoordenador = new TextField(); txtCoordenador.setPromptText("Coordenador");

        Button btnSalvar = new Button("Cadastrar no Banco");
        btnSalvar.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-weight: bold;");
        btnSalvar.setOnAction(e -> salvarContato());

        HBox campos = new HBox(10, txtCidade, txtBairro, txtPessoa, txtDoc, txtTelefone, txtCoordenador);
        return new VBox(10, new Label("Novo Cadastro:"), campos, btnSalvar);
    }

    private HBox criarBarraBusca() {
        txtBusca = new TextField();
        txtBusca.setPromptText("Pesquisar por nome ou cidade...");
        txtBusca.setPrefWidth(300);
        txtBusca.textProperty().addListener((obs, old, novo) -> filtrarDados(novo));

        Button btnExportar = new Button("Gerar Relatório Excel");
        btnExportar.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");
        btnExportar.setOnAction(e -> System.out.println("Chamando ExcelService..."));

        return new HBox(15, new Label("Filtro:"), txtBusca, btnExportar);
    }

    private void configurarTabela() {
        TableColumn<Contato, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Contato, String> colPessoa = new TableColumn<>("Pessoa");
        colPessoa.setCellValueFactory(new PropertyValueFactory<>("pessoa"));

        TableColumn<Contato, String> colCidade = new TableColumn<>("Cidade");
        colCidade.setCellValueFactory(new PropertyValueFactory<>("cidade"));

        TableColumn<Contato, String> colTelefone = new TableColumn<>("Telefone");
        colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));

        TableColumn<Contato, String> colCoord = new TableColumn<>("Coordenador");
        colCoord.setCellValueFactory(new PropertyValueFactory<>("coordenador"));

        tabela.getColumns().addAll(colId, colPessoa, colCidade, colTelefone, colCoord);
        tabela.setItems(listaContatos);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void salvarContato() {
        Contato novo = new Contato(
                null,
                txtCidade.getText(), txtBairro.getText(), txtPessoa.getText(),
                txtDoc.getText(), txtTelefone.getText(), txtCoordenador.getText()
        );
        repository.inserir(novo);
        limparCampos();
        atualizarTabela();
    }

    private void atualizarTabela() {
        listaContatos.setAll(repository.buscar(""));
    }

    private void filtrarDados(String texto) {
        listaContatos.setAll(repository.buscar(texto));
    }

    private void limparCampos() {
        txtCidade.clear(); txtBairro.clear(); txtPessoa.clear();
        txtDoc.clear(); txtTelefone.clear(); txtCoordenador.clear();
    }

    public static void main(String[] args) {
        launch(args);
    }
}