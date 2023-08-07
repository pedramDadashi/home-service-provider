package ir.maktabsharif.homeservicephase2.repository;

import ir.maktabsharif.homeservicephase2.entity.user.Worker;
import ir.maktabsharif.homeservicephase2.entity.user.enums.WorkerStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkerRepository extends UsersRepository<Worker> {

    @Override
     boolean existsByEmail(String email) ;

    @Override
    Optional<Worker> findByEmail(String email);

    @Override
    @Query(" update Worker w set w.password = :newPassword where w.email = :email")
    void editPassword(String email, String newPassword);

    @Override
    @Query("update Worker w set w.credit = :newCredit where w.email = :email")
    void updateCredit(String email, Long newCredit);

    @Query(" update Worker w set w.status = :expertStatus where w.email = :email ")
    void changeWorkerStatus(String email, WorkerStatus workerStatus);

    @Query(" update Worker w set w.isActive = :isActive where w.email = :email")
    int changeWorkerActivation(String email, Boolean isActive);

    @Query(" update Worker w set w.score = :newScore where w.email = :email")
    int updateScore(String email, Byte newScore);

}
