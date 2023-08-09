package ir.maktabsharif.homeservicephase2.service.Impl;

import ir.maktabsharif.homeservicephase2.base.service.BaseServiceImpl;
import ir.maktabsharif.homeservicephase2.entity.job.Job;
import ir.maktabsharif.homeservicephase2.exception.MainServiceIsNotExistException;
import ir.maktabsharif.homeservicephase2.repository.JobRepository;
import ir.maktabsharif.homeservicephase2.service.JobService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobServiceImpl extends BaseServiceImpl<Job, Long, JobRepository>
        implements JobService {

    public JobServiceImpl(JobRepository repository) {
        super(repository);
    }

    @Override
    public Optional<Job> findByName(String jobName) {
        return repository.findByName(jobName);
    }

    @Override
    public List<Job> findAll() {
        List<Job> jobList = repository.findAll();
        if (jobList.isEmpty())
            throw new MainServiceIsNotExistException("there are no jobs!");
        return jobList;
    }

    @Override
    public void deleteJobByName(String jobName) {
        repository.deleteByName(jobName);
    }

    @Override
    public void editBasePriceAndDescription(String jobName, Long basePrice, String description) {
        repository.editBasePriceAndDescription(jobName, basePrice, description);
    }
}
