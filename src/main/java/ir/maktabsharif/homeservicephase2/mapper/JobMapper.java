package ir.maktabsharif.homeservicephase2.mapper;

import ir.maktabsharif.homeservicephase2.dto.request.JobRequestDTO;
import ir.maktabsharif.homeservicephase2.dto.response.JobResponseDTO;
import ir.maktabsharif.homeservicephase2.entity.job.Job;
import ir.maktabsharif.homeservicephase2.entity.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JobMapper {

    private final MainServiceMapper mainServiceMapper;


    public JobResponseDTO convertToDTO(Job job) {
        JobResponseDTO jobResponseDTO = new JobResponseDTO();
        jobResponseDTO.setMainServiceResponseDTO(
                mainServiceMapper.convertToDTO(job.getMainService()));
        jobResponseDTO.setName(job.getName());
        jobResponseDTO.setBasePrice(job.getBasePrice());
        jobResponseDTO.setDescription(job.getDescription());
        return jobResponseDTO;
    }

    public Job convertToJob(JobRequestDTO jobRequestDTO) {
        Job job = new Job();
        job.setMainService(new MainService(jobRequestDTO.getMainServiceRequest()));
        job.setName(jobRequestDTO.getName());
        job.setBasePrice(jobRequestDTO.getBasePrice());
        job.setDescription(jobRequestDTO.getDescription());
        return job;
    }

}
