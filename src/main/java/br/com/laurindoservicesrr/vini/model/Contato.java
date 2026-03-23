package br.com.laurindoservicesrr.vini.model;

public record Contato(
        Integer id,
        String cidade,
        String bairro,
        String pessoa,
        String doc,
        String telefone,
        String coordenador
) {}