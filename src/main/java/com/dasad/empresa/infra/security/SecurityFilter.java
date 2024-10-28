package com.dasad.empresa.infra.security;

import com.dasad.empresa.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    private static final Logger log = LogManager.getLogger(SecurityFilter.class);
    @Autowired
    AuthorizationService authorizationService;
    @Autowired
    @Lazy
    UsuarioRepository usuarioRepository;
    @Value("${security.excludedUrls}")
    private String excludedUrls;

    public SecurityFilter() {
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        try {
            log.info("Requisição recebida: " + request.getRequestURI());
            String requestUrl = request.getRequestURI();
            if (requestUrl.endsWith("/")) {
                requestUrl = requestUrl.substring(0, requestUrl.length() - 1);
            }

            if (this.excludedUrls != null && (Arrays.asList(this.excludedUrls.split(",")).contains(requestUrl) ||
                    requestUrl.startsWith("/swagger-ui") ||
                    requestUrl.startsWith("/v3/api-docs") ||
                    requestUrl.startsWith("/senha/validar-reset-token"))) {
                filterChain.doFilter(request, response);
                return;
            }

            String token = this.recoverToken(request);
            String login = this.authorizationService.validateToken(token);
            if (login != null) {
                com.dasad.empresa.model.UsuarioModel user = this.usuarioRepository.findByEmail(login).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
                List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("Antes de filterChain.doFilter");
                filterChain.doFilter(request, response);
                log.info("Depois de filterChain.doFilter");
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