package com.cursos.api.domain.services;

import com.cursos.api.domain.dto.request.RequestAddCreator;
import com.cursos.api.domain.dto.request.RequestUpdateUserDTO;
import com.cursos.api.domain.dto.response.ResponseAddCreator;
import com.cursos.api.domain.dto.response.ResponsePublicUserDTO;
import com.cursos.api.domain.exceptions.NotFoundException;
import com.cursos.api.domain.models.CreatorModel;
import com.cursos.api.domain.models.UserModel;
import com.cursos.api.repositories.CreatorRepository;
import com.cursos.api.repositories.UserRepository;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CreatorRepository creatorRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ResponsePublicUserDTO getUserById(@PathVariable Long id) {
        return modelMapper.map(userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found")), ResponsePublicUserDTO.class);
    }

    public ResponsePublicUserDTO updateUser(@PathVariable Long id, RequestUpdateUserDTO userDetails) {
        UserModel userModel = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        userModel.setName(userDetails.getName());
        userModel.setEmail(userDetails.getEmail());
        return modelMapper.map(userRepository.save(userModel), ResponsePublicUserDTO.class);
    }

    public void deleteUser(@PathVariable Long id) {
        UserModel userModel = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        userRepository.delete(userModel);
    }

    public ResponseAddCreator addCreator(@PathVariable Long id, RequestAddCreator data ) throws BadRequestException {
        UserModel userModel = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));

        Optional<CreatorModel> creatorModelUserIdOptional = creatorRepository.findByUserId(id);
        if (creatorModelUserIdOptional.isPresent()) throw new BadRequestException("Usuário já possui perfil creator");

        Optional<CreatorModel> creatorModelCpfCnpjOptional = creatorRepository.findByCpfCnpj(data.getCpfCnpj());
        if (creatorModelCpfCnpjOptional.isPresent()) throw new BadRequestException("Creator já cadastrado com esse CPF/CNPJ");


        CreatorModel creatorData = CreatorModel
                .builder()
                .cpfCnpj(data.getCpfCnpj())
                .user(userModel).build();
        CreatorModel creatorModel = creatorRepository.save(creatorData);
        return new ResponseAddCreator(creatorModel.getId(), userModel.getName());
    }

}
