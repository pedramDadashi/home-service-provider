package ir.maktabsharif.homeservicephase2.mapper;

import ir.maktabsharif.homeservicephase2.dto.request.WorkerRegistrationDTO;
import ir.maktabsharif.homeservicephase2.dto.response.FilterUserResponseDTO;
import ir.maktabsharif.homeservicephase2.dto.response.WorkerResponseDTO;
import ir.maktabsharif.homeservicephase2.entity.user.Worker;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class WorkerMapper {

    private final PasswordEncoder passwordEncoder;

    public WorkerResponseDTO convertToDTO(Worker worker) {
        return new WorkerResponseDTO(
                worker.getId(),
                worker.getFirstname(),
                worker.getLastname(),
                worker.getEmail(),
                worker.getIsActive(),
                worker.getStatus(),
                worker.getScore(),
                worker.getCredit(),
                worker.getNumberOfOperation(),
                worker.getPaidCounter(),
                worker.getRegistrationTime()
        );
    }

    public Worker convertToNewWorker(WorkerRegistrationDTO workerRegistrationDTO) throws IOException {
        Worker worker = new Worker(
                workerRegistrationDTO.getFirstname(),
                workerRegistrationDTO.getLastname(),
                workerRegistrationDTO.getEmail(),
                passwordEncoder.encode(workerRegistrationDTO.getPassword()),
                workerRegistrationDTO.getProvince()
        );
            worker.setImage(workerRegistrationDTO.getFile().getBytes());

        return worker;
    }

    public FilterUserResponseDTO convertToFilterDTO(Worker worker) {
        return new FilterUserResponseDTO(
                worker.getRegistrationTime(),
                worker.getRole().name(),
                worker.getStatus().name(),
                worker.getIsActive(),
                worker.getId(),
                worker.getFirstname(),
                worker.getLastname(),
                worker.getEmail(),
                worker.getEmail(),
                worker.getCredit(),
                worker.getScore(),
                worker.getNumberOfOperation(),
                worker.getRateCounter()
        );
    }
}
