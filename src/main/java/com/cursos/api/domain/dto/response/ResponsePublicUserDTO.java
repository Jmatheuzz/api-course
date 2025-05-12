package com.cursos.api.domain.dto.response;

import com.cursos.api.domain.enums.Role;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Setter
@Getter
public class ResponsePublicUserDTO {
    private String id;
    private String name;
    private String email;
    private Boolean verified;
    private String photo;
    private Role role;

}
