package com.cursos.api.auth;



import com.cursos.api.domain.models.UserModel;
import com.cursos.api.repositories.UserRepository;
import com.cursos.api.domain.services.auth.JwtTokenService;
import com.cursos.api.domain.services.auth.UserDetailsImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
public class UserAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            if (checkIfEndpointIsNotPublic(request)) {
                String token = recoveryToken(request);
                if (token != null) {
                    String subject = jwtTokenService.getSubjectFromToken(token);
                    Optional<UserModel> user = userRepository.findByEmail(subject);
                    UserDetailsImpl userDetails = null;
                    if (user.isPresent())  userDetails = new UserDetailsImpl(user.get());


                    if (userDetails == null) throw new Exception("Usuário não encontrado");

                    Authentication authentication =
                            new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());


                    SecurityContextHolder.getContext().setAuthentication(authentication);

                } else {
                    throw new BadCredentialsException("O token está ausente.");
                }
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    private String recoveryToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }


    private boolean checkIfEndpointIsNotPublic(HttpServletRequest request) {

        String requestURI = request.getRequestURI();
        ///swagger-ui/index.html
        if (requestURI.contains("swagger-ui") || requestURI.contains("api-docs")) {
            return false;
        }
        return !Arrays.asList(SecurityConfig.ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED).contains(requestURI);


    }

}