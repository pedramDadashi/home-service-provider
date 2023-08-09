package ir.maktabsharif.homeservicephase2.repository;

import ir.maktabsharif.homeservicephase2.base.repository.BaseRepository;
import ir.maktabsharif.homeservicephase2.entity.user.Worker;
import ir.maktabsharif.homeservicephase2.entity.user.enums.WorkerStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkerRepository extends BaseRepository<Worker,Long> {

    @Query("select case when count(w)> 0 " +
           "then true else false end from Worker w where lower(w.email) like lower(:email)")
    boolean existsByEmail(String email) ;

    Optional<Worker> findByEmail(String email);

    @Query(" update Worker w set w.password = :newPassword where w.email = :email")
    void editPassword(String email, String newPassword);

    @Query("update Worker w set w.status = :workerStatus where w.email = :email")
    void changeWorkerStatus(String email, WorkerStatus workerStatus);

}
