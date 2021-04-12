package com.mikkaeru.request.solicitation.dto;

import javax.validation.constraints.NotBlank;

public class ReviewRequest {

    private @NotBlank String documento;
    private @NotBlank String nome;
    private @NotBlank String idProposta;

    public ReviewRequest(String documento, String nome, String idProposta) {
        this.documento = documento;
        this.nome = nome;
        this.idProposta = idProposta;
    }

    public String getDocumento() {
        return documento;
    }

    public String getNome() {
        return nome;
    }

    public String getIdProposta() {
        return idProposta;
    }
}
