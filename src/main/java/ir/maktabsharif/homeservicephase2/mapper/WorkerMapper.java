package ir.maktabsharif.homeservicephase2.mapper;

import ir.maktabsharif.homeservicephase2.dto.request.UserRegistrationDTO;
import ir.maktabsharif.homeservicephase2.dto.response.FilterWorkerResponseDTO;
import ir.maktabsharif.homeservicephase2.dto.response.WorkerResponseDTO;
import ir.maktabsharif.homeservicephase2.entity.user.Worker;
import ir.maktabsharif.homeservicephase2.entity.user.enums.Role;
import ir.maktabsharif.homeservicephase2.entity.user.enums.WorkerStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WorkerMapper {

    private final PasswordEncoder passwordEncoder;

    public WorkerResponseDTO convertToDTO(Worker worker) {
        WorkerResponseDTO workerResponseDTO = new WorkerResponseDTO();
        workerResponseDTO.setFirstname(worker.getFirstname());
        workerResponseDTO.setLastname(worker.getLastname());
        workerResponseDTO.setEmailAddress(worker.getEmail());
        workerResponseDTO.setWorkerStatus(worker.getStatus());
        return workerResponseDTO;
    }

    public Worker convertToNewWorker(UserRegistrationDTO userRegistrationDTO) {
        Worker worker = new Worker();
        worker.setFirstname(userRegistrationDTO.getFirstname());
        worker.setLastname(userRegistrationDTO.getLastname());
        worker.setEmail(userRegistrationDTO.getEmail());
        worker.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));
        worker.setStatus(WorkerStatus.AWAITING);
        worker.setCredit(0L);
        worker.setIsActive(false);
        worker.setRole(Role.WORKER);
        return worker;
    }

    public Worker convertToWorker(UserRegistrationDTO userRegistrationDTO) {
        Worker worker = new Worker();
        worker.setFirstname(userRegistrationDTO.getFirstname());
        worker.setLastname(userRegistrationDTO.getLastname());
        worker.setEmail(userRegistrationDTO.getEmail());
        worker.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));
        return worker;
    }

    public FilterWorkerResponseDTO convertToFilterDTO(Worker worker) {
        FilterWorkerResponseDTO fwrDTO = new FilterWorkerResponseDTO();
        fwrDTO.setId(worker.getId());
        fwrDTO.setFirstname(worker.getFirstname());
        fwrDTO.setLastname(worker.getLastname());
        fwrDTO.setEmail(worker.getEmail());
        fwrDTO.setIsActive(worker.getIsActive());
        fwrDTO.setWorkerStatus(worker.getStatus());
        fwrDTO.setRate(worker.getScore());
        fwrDTO.setCredit(worker.getCredit());
        return fwrDTO;
    }
}
