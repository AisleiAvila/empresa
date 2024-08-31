package com.dasad.empresa.infra.security;

import com.dasad.empresa.models.UsuarioModel;
import com.dasad.empresa.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    private static final Logger log = LogManager.getLogger(SecurityFilter.class);
    @Autowired
    AuthorizationService authorizationService;
    @Autowired
    UsuarioRepository usuarioRepository;
    @Value("${security.excludedUrls}")
    private String excludedUrls;

    public SecurityFilter() {
    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            log.info("Requisição recebida: " + request.getRequestURI());
            String requestUrl = request.getRequestURI();
            if (requestUrl.endsWith("/")) {
                requestUrl = requestUrl.substring(0, requestUrl.length() - 1);
            }

            if (this.excludedUrls != null && Arrays.asList(this.excludedUrls.split(",")).contains(requestUrl)) {
                filterChain.doFilter(request, response);
                return;
            }

            String token = this.recoverToken(request);
            String login = this.authorizationService.validateToken(token);
            if (login != null) {
                UsuarioModel user = (UsuarioModel)this.usuarioRepository.findByEmail(login).orElseThrow(() -> {
                    return new RuntimeException("Usuário não encontrado");
                });
                List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, (Object)null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);
            } else {
                response.setStatus(401);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"error\": \"Token de autenticação inválido\"}");
            }
        } catch (Exception var10) {
            response.setStatus(500);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"error\": \"Erro interno do servidor\"}");
        }

    }

    private String recoverToken(HttpServletRequest request) {
        String authHeader = request.getHeader("authorization");
        return authHeader == null ? null : authHeader.replace("Bearer ", "");
    }
}
