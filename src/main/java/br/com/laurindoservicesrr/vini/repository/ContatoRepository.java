package br.com.laurindoservicesrr.vini.repository;

import br.com.laurindoservicesrr.vini.model.Contato;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContatoRepository {

    private Connection getConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/nome_do_seu_banco";
        String user = "postgres";
        String password = "sua_senha";
        return DriverManager.getConnection(url, user, password);
    }

    public void inserir(Contato c) {
        String sql = "INSERT INTO contatos (cidade, bairro, pessoa, doc, telefone, coordenador) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, c.cidade());
            stmt.setString(2, c.bairro());
            stmt.setString(3, c.pessoa());
            stmt.setString(4, c.doc());
            stmt.setString(5, c.telefone());
            stmt.setString(6, c.coordenador());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Contato> buscar(String buscaRapida) {
        List<Contato> contatos = new ArrayList<>();
        // O ILIKE no PostgreSQL é para busca case-insensitive (ignora maiúsculas/minúsculas)
        String sql = "SELECT * FROM contatos WHERE cidade ILIKE ? OR bairro ILIKE ? OR pessoa ILIKE ?";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            String p = "%" + buscaRapida + "%";
            stmt.setString(1, p);
            stmt.setString(2, p);
            stmt.setString(3, p);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                contatos.add(new Contato(
                        rs.getInt("id"),
                        rs.getString("cidade"),
                        rs.getString("bairro"),
                        rs.getString("pessoa"),
                        rs.getString("doc"),
                        rs.getString("telefone"),
                        rs.getString("coordenador")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contatos;
    }
}