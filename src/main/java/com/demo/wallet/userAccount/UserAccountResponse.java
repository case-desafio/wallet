package com.demo.wallet.userAccount;

public class UserAccountResponse {
    private Long id;
    private String email;

    @Deprecated
    public UserAccountResponse() {
    }

    public UserAccountResponse(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}
