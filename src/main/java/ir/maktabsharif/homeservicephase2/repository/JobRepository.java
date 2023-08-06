package ir.maktabsharif.homeservicephase2.repository;

import ir.maktabsharif.homeservicephase2.base.repository.BaseRepository;
import ir.maktabsharif.homeservicephase2.entity.job.Job;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobRepository extends BaseRepository<Job, Long> {

    Optional<Job> findByName(String jobName);

    void deleteByName(String jobName);

    @Query(" update Job j set j.basePrice = :basePrice, j.description = :description where j.name = :jobName")
    void editBasePriceAndDescription(String jobName, Long basePrice, String description);
}
