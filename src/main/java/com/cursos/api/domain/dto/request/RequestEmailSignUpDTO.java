package com.cursos.api.domain.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class RequestEmailSignUpDTO {
    private String email;
    private String code;
}
