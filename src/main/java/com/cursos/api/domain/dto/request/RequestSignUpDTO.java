package com.cursos.api.domain.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Setter
@Getter
public class RequestSignUpDTO {
    private String name;
    private String email;
    private String password;
    private String passwordConfirmation;
    private String role;
}
