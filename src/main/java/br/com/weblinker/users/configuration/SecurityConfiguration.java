package br.com.weblinker.users.configuration;

import br.com.weblinker.users.security.jwt.JwtTokenProvider;
import br.com.weblinker.users.security.jwt.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfiguration {

    private final JwtTokenProvider tokenProvider;

    @Autowired
    public SecurityConfiguration(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Basic authentication configuration
        http.httpBasic(Customizer.withDefaults());

        // Disable CSRF and set stateless session policy
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Authorization configuration for different URL patterns
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/auth/login",
                        "/auth/refresh",
                        "/actuator/**",
                        "/api-docs/**",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/users/v3/api-docs/**").permitAll()
                .requestMatchers("/users/me").authenticated()
                .requestMatchers("/users/**").hasAnyRole("ADMIN")
                .anyRequest().authenticated()
        );

        // CORS configuration
        http.cors(Customizer.withDefaults());

        // Registration of the JWT filter
        http.addFilterBefore(new JwtTokenFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
