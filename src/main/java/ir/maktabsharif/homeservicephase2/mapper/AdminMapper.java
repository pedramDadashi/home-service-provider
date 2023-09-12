package ir.maktabsharif.homeservicephase2.mapper;

import ir.maktabsharif.homeservicephase2.dto.request.AdminRegistrationDTO;
import ir.maktabsharif.homeservicephase2.dto.request.ManagerRegistrationDTO;
import ir.maktabsharif.homeservicephase2.entity.user.Admin;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static ir.maktabsharif.homeservicephase2.entity.user.enums.Role.ADMIN;
import static ir.maktabsharif.homeservicephase2.entity.user.enums.Role.MANAGER;

@Component
@RequiredArgsConstructor
public class AdminMapper {

    private final PasswordEncoder passwordEncoder;

    public Admin convertToNewAdmin(AdminRegistrationDTO dto) {
        return new Admin(
                dto.getFirstname(),
                dto.getLastname(),
                dto.getEmail(),
                passwordEncoder.encode(dto.getPassword()),
                ADMIN
        );
    }

    public Admin convertToNewManager(ManagerRegistrationDTO dto) {
        return new Admin(
                dto.getFirstname(),
                dto.getLastname(),
                dto.getEmail(),
                passwordEncoder.encode(dto.getPassword()),
                MANAGER
        );
    }
}
