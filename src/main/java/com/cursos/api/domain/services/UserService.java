package com.cursos.api.domain.services;

import com.cursos.api.domain.dto.request.RequestUpdateUserDTO;
import com.cursos.api.domain.dto.response.ResponsePublicUserDTO;
import com.cursos.api.domain.exceptions.NotFoundException;
import com.cursos.api.domain.models.UserModel;
import com.cursos.api.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ResponsePublicUserDTO getUserById(@PathVariable Long id) {
        return modelMapper.map(userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found")), ResponsePublicUserDTO.class);
    }

    public ResponsePublicUserDTO updateUser(@PathVariable Long id, @RequestBody RequestUpdateUserDTO userDetails) {
        UserModel userModel = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        userModel.setName(userDetails.getName());
        userModel.setEmail(userDetails.getEmail());
        return modelMapper.map(userRepository.save(userModel), ResponsePublicUserDTO.class);
    }

    public void deleteUser(@PathVariable Long id) {
        UserModel userModel = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        userRepository.delete(userModel);
    }

}
