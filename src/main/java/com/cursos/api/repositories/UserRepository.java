package com.cursos.api.repositories;

import com.cursos.api.domain.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Long> {

    Optional<UserModel> findByEmail(String email);
    Optional<UserModel> findByEmailAndCode(String email, String code);
}
