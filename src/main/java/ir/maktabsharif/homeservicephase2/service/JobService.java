package ir.maktabsharif.homeservicephase2.service;

import ir.maktabsharif.homeservicephase2.base.service.BaseService;
import ir.maktabsharif.homeservicephase2.entity.job.Job;

import java.util.List;
import java.util.Optional;

public interface JobService extends BaseService<Job,Long> {
    @Override
    void save(Job job);

    @Override
    void delete(Job job);

    @Override
    Optional<Job> findById(Long aLong);

    @Override
    List<Job> findAll();

    Optional<Job> findByName(String jobName);

    void deleteSubServiceByName(String jobName);

    void editBasePriceAndDescription(String jobName, Long basePrice, String description);

}
