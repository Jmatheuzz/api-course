package com.cursos.api.controllers.auth;


import com.cursos.api.domain.dto.request.RequestLoginGoogle;
import com.cursos.api.domain.dto.response.ResponseLoginGoogleDTO;
import com.cursos.api.domain.services.auth.GoogleLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("/auth/google")
public class LoginGoogleController {

    @Autowired
    private GoogleLogin googleLogin;

    @PostMapping("/verify")
    ResponseEntity<ResponseLoginGoogleDTO> verifyToken(@RequestBody RequestLoginGoogle data) throws GeneralSecurityException, IOException {
        ResponseLoginGoogleDTO response = googleLogin.verifyToken(data.token());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
