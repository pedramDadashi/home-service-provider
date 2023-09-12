package ir.maktabsharif.homeservicephase2.security.config;


import ir.maktabsharif.homeservicephase2.service.Impl.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static ir.maktabsharif.homeservicephase2.entity.user.enums.Role.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.cors(AbstractHttpConfigurer::disable);
        httpSecurity.logout(AbstractHttpConfigurer::disable);
        httpSecurity.authorizeHttpRequests((requests) -> requests
                                .requestMatchers("/registration/**").permitAll()
                                .requestMatchers("/manager/**").hasAuthority(MANAGER.name())
                                .requestMatchers("/admin/**").hasAuthority(ADMIN.name())
//                                .requestMatchers("/admin/**").hasAnyAuthority(ADMIN.name(),MANAGER.name())
                                .requestMatchers("/client/**").hasAuthority(CLIENT.name())
                                .requestMatchers("/worker/**").hasAuthority(WORKER.name())
                                .anyRequest().authenticated()

//                .logout()
//                .clearAuthentication(true)
//                .invalidateHttpSession(true)
//                .permitAll();

                )
                .authenticationProvider(new CustomAuthProvider(
                        (CustomUserDetailsService) userDetailsService(), passwordEncoder()))
                .httpBasic(basic -> {
                })
        ;
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


//    @Bean
//    public DaoAuthenticationProvider daoAuthenticationProvider() {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setPasswordEncoder(passwordEncoder());
//        provider.setUserDetailsService(userDetailsService());
//        return provider;
//    }

}
