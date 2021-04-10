package com.mikkaeru.request.dto;

import javax.validation.constraints.NotBlank;

public class ReviewResponse {

    // TODO resultadoSolicitacao - irá ser um enum com os seguintes valores {COM_RESTRICAO, SEM_RESTRICAO}
    // TODO utilizar o método (valueOf) do enum para realizar uma validação.

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

    public String getResultadoSolicitacao() {
        return resultadoSolicitacao;
    }
}
