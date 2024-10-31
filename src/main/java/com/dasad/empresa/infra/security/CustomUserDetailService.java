package com.dasad.empresa.infra.security;

import com.dasad.empresa.model.UsuarioModel;
import com.dasad.empresa.repository.UsuarioRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@Log4j2
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    @Lazy
    private UsuarioRepository usuarioRepository;

    public CustomUserDetailService() {
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Carregando usuário por username");
        UsuarioModel usuario = this.usuarioRepository.findByEmail(username).orElseThrow(() -> {
            log.error("Usuário não encontrado");
            return new UsernameNotFoundException("Usuário não encontrado");
        });
        log.info("Usuário encontrado");
        return new User(usuario.getEmail(), usuario.getSenha(), new ArrayList<>());
    }
}
