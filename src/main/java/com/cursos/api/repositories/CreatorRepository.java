package com.cursos.api.repositories;

import com.cursos.api.domain.models.CreatorModel;
import com.cursos.api.domain.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CreatorRepository extends JpaRepository<CreatorModel, Long> {

    Optional<CreatorModel> findByCpfCnpj(String cpfCnpj);
    Optional<CreatorModel> findByUserId(Long userId);
}
