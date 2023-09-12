package ir.maktabsharif.homeservicephase2.mapper;

import ir.maktabsharif.homeservicephase2.dto.request.JobRequestDTO;
import ir.maktabsharif.homeservicephase2.dto.response.JobResponseDTO;
import ir.maktabsharif.homeservicephase2.entity.service.Job;
import ir.maktabsharif.homeservicephase2.entity.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JobMapper {

    private final MainServiceMapper mainServiceMapper;


    public JobResponseDTO convertToDTO(Job job) {
        return new JobResponseDTO(
                job.getMainService().getName(),
                job.getId(),
                job.getName(),
                job.getDescription(),
                job.getBasePrice()
        );
    }

    public Job convertToJob(JobRequestDTO jrDTO) {
        return new Job(
                jrDTO.getName(),
                jrDTO.getBasePrice(),
                jrDTO.getDescription(),
                new MainService(jrDTO.getMainServiceRequest())
        );
    }

}
