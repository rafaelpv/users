package br.com.weblinker.users.controllers;


import br.com.weblinker.users.security.JwtUtil;
import br.com.weblinker.users.models.User;
import br.com.weblinker.users.repositories.UsersRepository;
import br.com.weblinker.users.services.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final Logger LOG = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        usersRepository.save(user); // Certifique-se de que a senha está sendo criptografada antes de salvar
        return ResponseEntity.ok("Usuário registrado com sucesso");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            final UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                    loginRequest.getEmail(), loginRequest.getPassword(), new ArrayList<>());

            final String jwt = jwtUtil.generateToken(userDetails);
            return ResponseEntity.ok(jwt);
        } catch (AuthenticationException e) {
            LOG.error(e.getMessage());
            return ResponseEntity.status(401).body("Credenciais inválidas");
        }
    }
}

