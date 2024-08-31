package com.dasad.empresa.infra.security;

import com.dasad.empresa.models.UsuarioModel;
import com.dasad.empresa.repository.UsuarioRepository;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public CustomUserDetailService() {
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsuarioModel usuario = (UsuarioModel)this.usuarioRepository.findByEmail(username).orElseThrow(() -> {
            return new UsernameNotFoundException("Usuário não encontrado");
        });
        return new User(usuario.getEmail(), usuario.getSenha(), new ArrayList());
    }
}
