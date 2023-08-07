package ir.maktabsharif.homeservicephase2.service;

import ir.maktabsharif.homeservicephase2.entity.offer.Offer;
import ir.maktabsharif.homeservicephase2.entity.user.Worker;
import ir.maktabsharif.homeservicephase2.entity.user.enums.WorkerStatus;

import java.io.File;
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

    @Override
    void editPassword(Worker worker, String newPassword);

    void changeWorkerStatus(Long workerId, WorkerStatus workerStatus);

    void signUp(Worker worker, File image);

    void confirmWorkerAccount(Worker worker, WorkerStatus workerStatus);

    void createOfferForOrder(Long workerId, Long orderId, Offer offer);

    int changeWorkerActivation(Long workerId, Boolean isActive);

}
