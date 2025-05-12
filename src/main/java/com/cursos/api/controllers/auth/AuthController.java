package com.cursos.api.controllers.auth;


import com.cursos.api.domain.dto.request.RequestEmailSignUpDTO;
import com.cursos.api.domain.dto.request.RequestLoginDTO;
import com.cursos.api.domain.dto.request.RequestSignUpDTO;
import com.cursos.api.domain.dto.response.ResponseEmailSignUpDTO;
import com.cursos.api.domain.dto.response.ResponseLoginDTO;
import com.cursos.api.domain.dto.response.ResponseSignUpDTO;
import com.cursos.api.domain.services.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação e Cadastro", description = "Serviços de autenticação e cadastro")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Operation(description = "Serviço para criar conta")
    @PostMapping("/signup")
    public ResponseEntity<ResponseSignUpDTO> signUp(@RequestBody RequestSignUpDTO data) throws Exception {
        ResponseSignUpDTO response = authService.signUp(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(description = "Serviço para obter token")
    @PostMapping("/login")
    public ResponseEntity<ResponseLoginDTO> login(@Valid @RequestBody RequestLoginDTO data) throws BadRequestException {
        ResponseLoginDTO response = authService.login(data);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(description = "Serviço para validar email")
    @PostMapping("/validation-email-signup")
    public ResponseEntity<ResponseEmailSignUpDTO> validationEmailSignUp(@RequestBody RequestEmailSignUpDTO data) throws Exception {
        ResponseEmailSignUpDTO response = authService.validationEmailSignUp(data);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

