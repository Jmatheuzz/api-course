package com.cursos.api.domain.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Setter
@Getter
public class RequestUpdateUserDTO {
    private Long id;
    private String name;
    private String email;
}
