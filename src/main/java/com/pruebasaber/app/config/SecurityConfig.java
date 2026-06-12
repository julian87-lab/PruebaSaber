package com.pruebasaber.app.config;

import com.pruebasaber.app.security.CustomLoginSuccessHandler;
import com.pruebasaber.app.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final CustomLoginSuccessHandler customLoginSuccessHandler;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService,
                          CustomLoginSuccessHandler customLoginSuccessHandler) {
        this.customUserDetailsService = customUserDetailsService;
        this.customLoginSuccessHandler = customLoginSuccessHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .userDetailsService(customUserDetailsService)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/js/**", "/img/**").permitAll()
                .requestMatchers("/login", "/error").permitAll()

                .requestMatchers("/dashboard/admin/**").hasRole("ADMIN")
                .requestMatchers("/dashboard/coordinador/**").hasRole("COORDINADOR")
                .requestMatchers("/dashboard/docente/**").hasRole("DOCENTE")
                .requestMatchers("/dashboard/estudiante/**").hasRole("ESTUDIANTE")

                .requestMatchers("/usuarios/**").hasRole("ADMIN")

                .requestMatchers("/estudiantes").hasAnyRole("ADMIN", "COORDINADOR", "DOCENTE")
                .requestMatchers("/estudiantes/detalle/**").hasAnyRole("ADMIN", "COORDINADOR", "DOCENTE")
                .requestMatchers("/estudiantes/importar").hasRole("ADMIN")
                .requestMatchers("/estudiantes/guardar").hasRole("ADMIN")
                .requestMatchers("/estudiantes/editar/**").hasRole("ADMIN")
                .requestMatchers("/estudiantes/actualizar/**").hasRole("ADMIN")
                .requestMatchers("/estudiantes/desactivar/**").hasRole("ADMIN")
                .requestMatchers("/estudiantes/activar/**").hasRole("ADMIN")
                .requestMatchers("/resultados/**").hasAnyRole("ADMIN", "COORDINADOR", "DOCENTE")
                .requestMatchers("/docente/**").hasRole("DOCENTE")

                .requestMatchers("/mis-resultados", "/mis-resultados/**").hasRole("ESTUDIANTE")
                .requestMatchers("/mi-perfil", "/mi-perfil/**").hasRole("ESTUDIANTE")

                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .successHandler(customLoginSuccessHandler)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            )
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}