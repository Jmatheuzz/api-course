package com.cursos.api.domain.services.auth;

import com.cursos.api.domain.exceptions.NotFoundException;
import com.cursos.api.domain.models.UserModel;
import com.cursos.api.repositories.UserRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String username) throws NotFoundException {
        Optional<UserModel> user = userRepository.findByEmail(username);
        if (user.isPresent()) return new UserDetailsImpl(user.get());
        else throw new NotFoundException("Not found user");
    }

}
