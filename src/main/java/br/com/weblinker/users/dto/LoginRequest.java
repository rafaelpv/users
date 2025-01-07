package br.com.weblinker.users.dto;

import lombok.Getter;

@Getter
public class LoginRequest {

    private String username;
    private String password;
}
