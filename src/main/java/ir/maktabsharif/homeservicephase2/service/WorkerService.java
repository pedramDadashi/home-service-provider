package ir.maktabsharif.homeservicephase2.service;

import ir.maktabsharif.homeservicephase2.dto.request.*;
import ir.maktabsharif.homeservicephase2.dto.response.*;
import ir.maktabsharif.homeservicephase2.entity.user.Worker;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


public interface WorkerService extends UsersService<Worker> {
    @Override
    void save(Worker worker);

    @Override
    void delete(Worker worker);

    @Override
    Optional<Worker> findById(Long aLong);

    @Override
    List<Worker> findAll();

    @Override
    Optional<Worker> findByUsername(String email);

    ProjectResponse editPassword(ChangePasswordDTO changePasswordDTO,Long workerId);

//    void changeWorkerStatus(String workerUsername, WorkerStatus workerStatus);

    List<FilterUserResponseDTO> workerFilter(FilterUserDTO workerDTO);

//    ProjectResponse addWorker(UserRegistrationDTO workerRegistrationDTO);

    String addNewWorker(UserRegistrationDTO workerRegistrationDTO) throws IOException;

//    ProjectResponse loginWorker(LoginDTO workerLoginDto);

    List<MainServiceResponseDTO> showAllMainServices();

    List<JobResponseDTO> showJobs(ChooseMainServiceDTO dto);

    List<LimitedOrderResponseDTO> showRelatedOrders(Long workerId);

    ProjectResponse submitAnOffer(OfferRequestDTO offerRequestDTO,Long workerId);

    double getWorkerRate(Long workerId);

    List<OfferResponseDTO> showAllOffersWaiting(Long workerId);

    List<OfferResponseDTO> showAllOffersAccepted(Long workerId);

    List<OfferResponseDTO> showAllOffersRejected(Long workerId);

    Long getWorkerCredit(Long workerId);

    List<FilterUserResponseDTO> allWorker(FilterUserDTO userDTO);
}
