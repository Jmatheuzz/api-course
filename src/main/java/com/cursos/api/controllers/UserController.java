package com.cursos.api.controllers;

import com.cursos.api.domain.dto.request.RequestAddCreator;
import com.cursos.api.domain.dto.request.RequestUpdateUserDTO;
import com.cursos.api.domain.dto.response.ResponseAddCreator;
import com.cursos.api.domain.dto.response.ResponsePublicUserDTO;
import com.cursos.api.domain.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Tag(name = "Usuários", description = "Serviços de usuários")
@SecurityRequirement(name = "bearer-key")
public class UserController {

    @Autowired
    private UserService service;

    @Operation(description = "Serviço para buscar detalhes de usuário")
    @GetMapping("/{id}")
    public ResponseEntity<ResponsePublicUserDTO> getUserById(@PathVariable Long id) {
        ResponsePublicUserDTO response = service.getUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(description = "Serviço para editar usuário")
    @PutMapping("/{id}")
    public ResponseEntity<ResponsePublicUserDTO> updateUser(@PathVariable Long id, @RequestBody RequestUpdateUserDTO userDetails) {
        ResponsePublicUserDTO response = service.updateUser(id, userDetails);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(description = "Serviço para ativar função de criador de cursos")
    @PutMapping("/active-creator/{id}")
    public ResponseEntity<ResponseAddCreator> creatorActive(@PathVariable Long id, @RequestBody RequestAddCreator data){
        ResponseAddCreator response = service.addCreator(id, data);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(description = "Serviço para apagar usuário")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        service.deleteUser(id);
    }
}

