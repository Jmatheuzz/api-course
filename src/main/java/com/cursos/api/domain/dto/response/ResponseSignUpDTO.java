package com.cursos.api.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ResponseSignUpDTO {
    private Long id;
    private String name;
    private String email;
}
