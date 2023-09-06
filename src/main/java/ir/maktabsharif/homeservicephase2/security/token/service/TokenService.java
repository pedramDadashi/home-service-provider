package ir.maktabsharif.homeservicephase2.security.token.service;

import ir.maktabsharif.homeservicephase2.security.token.entity.Token;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface TokenService {

    void saveToken(Token token);

    Optional<Token> getToken(String token);

    int setConfirmedAt(String token);

}