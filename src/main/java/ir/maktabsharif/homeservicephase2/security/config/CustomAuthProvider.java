package ir.maktabsharif.homeservicephase2.security.config;

import ir.maktabsharif.homeservicephase2.entity.user.Users;
import ir.maktabsharif.homeservicephase2.service.Impl.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthProvider implements AuthenticationProvider {

    private final CustomUserDetailsService customUserDetailsService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Users users = (Users) customUserDetailsService
                .loadUserByUsername(authentication.getName()
                );
        if (passwordEncoder.matches(((String) authentication.getCredentials()), users.getPassword())) {
            return new UsernamePasswordAuthenticationToken(
                    users, null, users.getAuthorities()
            );
        }
        throw new BadCredentialsException("wrong information");
    }

    @Override
    public boolean supports(Class<?> authentication) {
//        TODO add token
        return true;
    }
}