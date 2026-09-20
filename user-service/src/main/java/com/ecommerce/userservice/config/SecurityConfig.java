package com.ecommerce.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ecommerce.userservice.security.JwtAuthFilter;

import jakarta.servlet.http.HttpServletResponse;

/**
 * Define a política de segurança HTTP da aplicação.
 *
 * <p>
 * Configura autenticação stateless, integra o filtro JWT e libera apenas os
 * endpoints públicos necessários para cadastro, autenticação e documentação.
 *
 * @since 1.0
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    /**
     * Cria o encoder usado para armazenar senhas com hash forte.
     *
     * @return instância de {@link PasswordEncoder} baseada em BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Expõe o gerenciador de autenticação configurado pelo Spring Security.
     *
     * @param authConfig configuração de autenticação do Spring.
     * @return o gerenciador de autenticação resolvido pelo framework.
     * @throws Exception quando o gerenciamento de autenticação não puder ser
     *                   obtido.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Monta a cadeia principal de filtros e regras de autorização da aplicação.
     *
     * @param http objeto de configuração HTTP fornecido pelo Spring Security.
     * @return a cadeia de filtros construída.
     * @throws Exception quando a construção da cadeia de segurança falhar.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/natural-persons/register", "/legal-entities/register", "/auth/login",
                                "/auth/refresh", "/auth/logout", "/v3/api-docs/**", "/swagger-ui/**",
                                "/swagger-ui.html")
                        .permitAll()
                        .requestMatchers("/users/**").authenticated()
                        .anyRequest().authenticated())
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint()))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    /**
     * Cria a resposta usada quando a autenticação falha antes de chegar ao
     * controlador.
     *
     * @return manipulador padrão de entrada não autorizada.
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
        };
    }
}
