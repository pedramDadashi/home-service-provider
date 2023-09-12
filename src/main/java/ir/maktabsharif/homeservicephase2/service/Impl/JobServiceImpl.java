package ir.maktabsharif.homeservicephase2.service.Impl;

import ir.maktabsharif.homeservicephase2.base.service.BaseServiceImpl;
import ir.maktabsharif.homeservicephase2.dto.response.JobResponseDTO;
import ir.maktabsharif.homeservicephase2.entity.service.Job;
import ir.maktabsharif.homeservicephase2.exception.MainServiceIsNotExistException;
import ir.maktabsharif.homeservicephase2.mapper.JobMapper;
import ir.maktabsharif.homeservicephase2.repository.JobRepository;
import ir.maktabsharif.homeservicephase2.service.JobService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JobServiceImpl extends BaseServiceImpl<Job, Long, JobRepository>
        implements JobService {

    private final JobMapper jobMapper;

    public JobServiceImpl(JobRepository repository, JobMapper jobMapper) {
        super(repository);
        this.jobMapper = jobMapper;
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
    public List<JobResponseDTO> findByMainServiceName(String mainServiceName) {
        List<Job> jobList = repository.findByMainServiceName(mainServiceName);
        List<JobResponseDTO> jrDTOS = new ArrayList<>();
        if (jobList.isEmpty())
            return jrDTOS;
        jobList.forEach(j -> jrDTOS.add(jobMapper.convertToDTO(j)));
        return jrDTOS;
    }
    @Override
    public List<JobResponseDTO> findByMainServiceId(Long mainServiceId) {
        List<Job> jobList = repository.findByMainServiceId(mainServiceId);
        List<JobResponseDTO> jrDTOS = new ArrayList<>();
        if (jobList.isEmpty())
            return jrDTOS;
        jobList.forEach(j -> jrDTOS.add(jobMapper.convertToDTO(j)));
        return jrDTOS;
    }

}
