package com.example.demo.config; 


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.demo.service.AuthenticationService;
import com.example.demo.model.PlayerEntity;
import com.example.demo.model.PlayerRepository;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private AuthenticationService authenticationService;

    private PasswordEncoder passwordEncoder;  


    @Autowired
    public void AuthenticationService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;  // injecting
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // Enable CORS and disable CSRF
            // I can enable CRSF once I handle key configuration
            // for now as a shorthand and debugging I disabled crsf
            .cors().and()
            .csrf().disable()
            
            // setting up authorization rules - permit api calls to execute command to all for now
            .authorizeRequests()
                // public access
                .antMatchers("/login", "/create-user", "/css/**", "/js/**").permitAll()

                // require auth for api execute command
                .antMatchers("/api/execute-command").authenticated()

                // all other paths need auth
                .anyRequest().authenticated()

            .and()
            .formLogin()
                .loginPage("/login") // specifying custom login page
                .defaultSuccessUrl("/home", true)  // Always redirect to /home after login
                .permitAll(); // allow everyone to access the login page
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // custom auth since we get password from psql db
        auth.userDetailsService(username -> {
            PlayerEntity player = playerRepository.findByUsername(username);

            if (player != null) {
                // spring security automatically compares the entered password with the stored password
                return User.withUsername(username)
                        .password(player.getPassword())  // stored hashed password
                        .roles("USER") // asign roles as necessary
                        .build();
            } else {
                throw new UsernameNotFoundException("User not found");
            }
        }).passwordEncoder(passwordEncoder); // Password encoder used to compare entered password securely
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}