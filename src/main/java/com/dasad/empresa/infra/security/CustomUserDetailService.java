package com.dasad.empresa.infra.security;

import com.dasad.empresa.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    @Lazy
    private UsuarioRepository usuarioRepository;

    public CustomUserDetailService() {
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.dasad.empresa.model.UsuarioModel usuario = (com.dasad.empresa.model.UsuarioModel)this.usuarioRepository.findByEmail(username).orElseThrow(() -> {
            return new UsernameNotFoundException("Usuário não encontrado");
        });
        return new User(usuario.getEmail(), usuario.getSenha(), new ArrayList());
    }
}
