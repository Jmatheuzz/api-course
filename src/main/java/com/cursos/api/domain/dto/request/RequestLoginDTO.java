package com.cursos.api.domain.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class RequestLoginDTO {

    @NotNull(message = "O email é obrigatório.")
    @Email(message = "Email deve ser válido.")
    private String email;

    @NotNull(message = "A senha é obrigatório.")
    @Size(min = 7, message = "A senha deve ter pelo menos 7 caracteres.")
    private String password;

}
