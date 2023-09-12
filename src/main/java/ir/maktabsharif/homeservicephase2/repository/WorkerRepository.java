package ir.maktabsharif.homeservicephase2.repository;

import ir.maktabsharif.homeservicephase2.base.repository.BaseRepository;
import ir.maktabsharif.homeservicephase2.entity.user.Worker;
import ir.maktabsharif.homeservicephase2.entity.user.enums.WorkerStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkerRepository extends BaseRepository<Worker, Long> {

    boolean existsByEmail(String email);

    Optional<Worker> findByEmail(String email);

    @Modifying
    @Query(" update Worker w set w.password = :newPassword where w.id = :workerId")
    void editPassword(Long workerId, String newPassword);

    @Query("select w from Worker w where w.status= :workerStatus")
    List<Worker> findAllByWorkerStatus(WorkerStatus workerStatus);

    @Modifying
    @Query("update Worker w set w.credit = :newCredit where w.id = :workerId")
    void updateCredit(Long workerId, Long newCredit);

    @Modifying
    @Query("update Worker w set w.isActive = :isActive where w.id = :workerId")
    void changeActivation(Long workerId, boolean isActive);

}
