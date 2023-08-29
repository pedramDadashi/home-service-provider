package ir.maktabsharif.homeservicephase2.service;

import ir.maktabsharif.homeservicephase2.dto.request.FilterWorkerDTO;
import ir.maktabsharif.homeservicephase2.dto.response.FilterWorkerResponseDTO;
import ir.maktabsharif.homeservicephase2.entity.offer.Offer;
import ir.maktabsharif.homeservicephase2.entity.user.Worker;
import ir.maktabsharif.homeservicephase2.entity.user.enums.WorkerStatus;

import java.io.File;
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

    @Override
    void editPassword(Worker worker, String newPassword);

    void changeWorkerStatus(String workerUsername, WorkerStatus workerStatus);

    void signUp(Worker worker, File image);

    void createOfferForOrder(Long workerId, Long orderId, Offer offer);

    boolean isExistByEmail(String email);

    List<FilterWorkerResponseDTO> workerFilter(FilterWorkerDTO workerDTO);
}
