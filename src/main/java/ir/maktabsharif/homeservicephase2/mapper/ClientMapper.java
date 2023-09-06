package ir.maktabsharif.homeservicephase2.mapper;

import ir.maktabsharif.homeservicephase2.dto.request.UserRegistrationDTO;
import ir.maktabsharif.homeservicephase2.dto.response.ClientResponseDTO;
import ir.maktabsharif.homeservicephase2.dto.response.FilterClientResponseDTO;
import ir.maktabsharif.homeservicephase2.entity.user.Client;
import ir.maktabsharif.homeservicephase2.entity.user.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClientMapper {

    private final PasswordEncoder passwordEncoder;

    public ClientResponseDTO convertToDTO(Client client) {
        ClientResponseDTO clientResponseDTO = new ClientResponseDTO();
        clientResponseDTO.setFirstname(client.getFirstname());
        clientResponseDTO.setLastname(client.getLastname());
        clientResponseDTO.setEmailAddress(client.getEmail());
        return clientResponseDTO;
    }

    public Client convertToNewClient(UserRegistrationDTO userRegistrationDTO) {
        Client client = new Client();
        client.setFirstname(userRegistrationDTO.getFirstname());
        client.setLastname(userRegistrationDTO.getLastname());
        client.setEmail(userRegistrationDTO.getEmail());
        client.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));
        client.setRole(Role.CLIENT);
        client.setCredit(0L);
        client.setIsActive(true);
        return client;
    }

    public Client convertToClient(UserRegistrationDTO userRegistrationDTO) {
        Client client = new Client();
        client.setFirstname(userRegistrationDTO.getFirstname());
        client.setLastname(userRegistrationDTO.getLastname());
        client.setEmail(userRegistrationDTO.getEmail());
        client.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));
        client.setRole(Role.CLIENT);
        return client;
    }

    public FilterClientResponseDTO convertToFilterDTO(Client client) {
        FilterClientResponseDTO fcrDTO = new FilterClientResponseDTO();
        fcrDTO.setId(client.getId());
        fcrDTO.setFirstname(client.getFirstname());
        fcrDTO.setLastname(client.getLastname());
        fcrDTO.setEmail(client.getEmail());
        fcrDTO.setCredit(client.getCredit());
        return fcrDTO;
    }
}
