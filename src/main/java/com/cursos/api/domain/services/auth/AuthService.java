package com.cursos.api.domain.services.auth;



import com.cursos.api.domain.dto.request.RequestEmailSignUpDTO;
import com.cursos.api.domain.dto.request.RequestLoginDTO;
import com.cursos.api.domain.dto.request.RequestSignUpDTO;
import com.cursos.api.domain.dto.response.ResponseEmailSignUpDTO;
import com.cursos.api.domain.dto.response.ResponseLoginDTO;
import com.cursos.api.domain.dto.response.ResponseSignUpDTO;
import com.cursos.api.domain.exceptions.NotFoundException;
import com.cursos.api.domain.services.mail.MailService;
import com.cursos.api.domain.enums.Role;
import com.cursos.api.domain.models.UserModel;
import com.cursos.api.repositories.UserRepository;
import com.cursos.api.utils.GenerateCodeValidation;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailService sendMailSignUpUseCase;

    @Value("${strings.subjectSignUpEmail}")
    private String subjectSignUpEmail;

    @Value("${strings.messageSignUpEmail}")
    private String messageSignUpEmail;


    @Transactional
    public ResponseLoginDTO login (RequestLoginDTO data) throws BadRequestException {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(data.getEmail(), data.getPassword());
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);


        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        if (!userDetails.getUserModel().getVerified()) throw new BadRequestException("Usuário não verificado");

        return new ResponseLoginDTO(jwtTokenService.generateToken(userDetails), userDetails.getUserModel().getId(), userDetails.getUserModel().getRole().name());
    }

    public ResponseSignUpDTO signUp (RequestSignUpDTO data) throws BadRequestException {
        if (!(data.getPassword().equals(data.getPasswordConfirmation()))) throw new BadRequestException("As senhas devem ser iguais.");
        String code = GenerateCodeValidation.generateCode();

        UserModel userModel = modelMapper.map(data, UserModel.class);
        userModel.setRole(Role.valueOf(data.getRole()));
        userModel.setCode(code);
        userModel.setPassword(passwordEncoder.encode(data.getPassword()));
        UserModel userModelCreated = userRepository.save(userModel);
        sendMailSignUpUseCase.sendSimpleMessage(userModelCreated.getEmail(), subjectSignUpEmail, messageSignUpEmail);
        return modelMapper.map(userModelCreated, ResponseSignUpDTO.class);

    }

    @Transactional
    public ResponseEmailSignUpDTO validationEmailSignUp (RequestEmailSignUpDTO data) throws NotFoundException {
        Optional<UserModel> userFind = userRepository.findByEmailAndCode(data.getEmail(), data.getCode());
        if (userFind.isEmpty()) throw new NotFoundException("Not found user");
        UserModel userModel = userFind.get();
        userModel.setVerified(true);
        userModel.setCode(null);
        userRepository.save(userModel);
        return new ResponseEmailSignUpDTO(true, userModel.getRole().name());

    }
}
