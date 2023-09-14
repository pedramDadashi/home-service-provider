package ir.maktabsharif.homeservicephase2.mapper;

import ir.maktabsharif.homeservicephase2.dto.request.ClientRegistrationDTO;
import ir.maktabsharif.homeservicephase2.dto.response.FilterUserResponseDTO;
import ir.maktabsharif.homeservicephase2.entity.user.Client;
import ir.maktabsharif.homeservicephase2.entity.user.enums.ClientStatus;
import ir.maktabsharif.homeservicephase2.entity.user.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClientMapper {

    private final PasswordEncoder passwordEncoder;

    public Client convertToNewClient(ClientRegistrationDTO dto) {
        Client client = new Client();
        client.setFirstname(dto.getFirstname());
        client.setLastname(dto.getLastname());
        client.setEmail(dto.getEmail());
        client.setPassword(passwordEncoder.encode(dto.getPassword()));
        client.setRole(Role.CLIENT);
        client.setCredit(0L);
        client.setIsActive(false);
        client.setPaidCounter(0);
        client.setClientStatus(ClientStatus.HAS_NOT_ORDER_YET);
        return client;
    }

    public FilterUserResponseDTO convertToFilterDTO(Client client) {
        return new FilterUserResponseDTO(
                client.getRegistrationTime(),
                client.getRole().name(),
                client.getClientStatus().name(),
                client.getIsActive(),
                client.getId(),
                client.getFirstname(),
                client.getLastname(),
                client.getEmail(),
                client.getEmail(),
                client.getCredit(),
                -1L,
                client.getNumberOfOperation(),
                client.getPaidCounter()
        );
    }
}
