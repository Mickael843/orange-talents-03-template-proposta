package com.mikkaeru.request.dto;

import com.mikkaeru.request.model.SolicitationReviewStatus;

import javax.validation.constraints.NotBlank;

public class ReviewResponse {

    private @NotBlank String documento;
    private @NotBlank String nome;
    private @NotBlank String idProposta;
    private @NotBlank String resultadoSolicitacao;

    public ReviewResponse(String documento, String nome, String idProposta, String resultadoSolicitacao) {
        this.documento = documento;
        this.nome = nome;
        this.idProposta = idProposta;
        this.resultadoSolicitacao = resultadoSolicitacao;
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

    public SolicitationReviewStatus getResultadoSolicitacao() {
        return SolicitationReviewStatus.valueOf(resultadoSolicitacao);
    }
}
