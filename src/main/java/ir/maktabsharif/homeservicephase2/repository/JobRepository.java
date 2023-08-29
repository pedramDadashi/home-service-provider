package ir.maktabsharif.homeservicephase2.repository;

import ir.maktabsharif.homeservicephase2.base.repository.BaseRepository;
import ir.maktabsharif.homeservicephase2.entity.job.Job;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends BaseRepository<Job, Long> {

    Optional<Job> findByName(String jobName);

    void deleteByName(String jobName);

    @Query("select j from Job j where j.mainService.id = :mainServiceId")
    List<Job> findByMainServiceId(Long mainServiceId);
}
