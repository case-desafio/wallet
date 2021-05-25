package com.demo.wallet.entity;

import javax.persistence.*;

@Entity
@Table(name = "USER_ACCOUNT")
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "MAIL", nullable = false, length = 75)
    private String mail;

    @Deprecated
    public UserAccount() {
    }

    public UserAccount(String mail) {
        this.mail = mail;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
