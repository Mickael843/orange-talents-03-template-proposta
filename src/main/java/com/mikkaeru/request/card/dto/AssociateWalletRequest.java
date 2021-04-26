package com.mikkaeru.request.card.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class AssociateWalletRequest {

    @Email
    @NotBlank
    private final String email;
    @NotBlank
    private final String carteira;

    public AssociateWalletRequest(String email, String carteira) {
        this.email = email;
        this.carteira = carteira;
    }

    public String getEmail() {
        return email;
    }

    public String getCarteira() {
        return carteira;
    }
}
