package com.mikkaeru.request.card;

import com.mikkaeru.request.card.model.ResultWallet;

public class AssociateWalletResponse {

    private final String id;
    private final ResultWallet resultado;

    public AssociateWalletResponse(String id, ResultWallet resultado) {
        this.id = id;
        this.resultado = resultado;
    }

    public String getId() {
        return id;
    }

    public ResultWallet getResultado() {
        return resultado;
    }
}
