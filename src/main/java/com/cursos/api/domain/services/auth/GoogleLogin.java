package com.cursos.api.domain.services.auth;



import com.cursos.api.domain.dto.response.ResponseLoginGoogleDTO;
import com.cursos.api.domain.models.UserModel;
import com.cursos.api.repositories.UserRepository;

import com.cursos.api.utils.JwtUtil;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

@Service
public class GoogleLogin {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private static final String CLIENT_ID = "121539775743-q082e88dc94u53stlu6m7qc7vrutbgu0.apps.googleusercontent.com";



    public ResponseLoginGoogleDTO verifyToken(String idTokenString) throws GeneralSecurityException, IOException, BadRequestException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();


        GoogleIdToken idToken = verifier.verify(idTokenString);
        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();

            String email = payload.getEmail();
            boolean emailVerified = payload.getEmailVerified();
            String name = (String) payload.get("name");

            if (!emailVerified) throw new BadRequestException("Email is not verified");

            Optional<UserModel> userOptional = userRepository.findByEmail(email);

            if (userOptional.isEmpty()) {
                UserModel userModel = UserModel.builder()
                        .email(email)
                        .name(name)
                        .verified(true)
                        .password(null)
                        .code(null)
                        .createdAt(LocalDateTime.now())
                        .build();
                UserModel userModelCreated = userRepository.save(userModel);
                return new ResponseLoginGoogleDTO(jwtUtil.generateToken(userModelCreated.getId().toString()), userModelCreated.getId().toString(), email);
            } else {
                return new ResponseLoginGoogleDTO(jwtUtil.generateToken(userOptional.get().getId().toString()), userOptional.get().getId().toString(), email);
            }

        }
        throw new BadRequestException("Invalid token");
    }

}

