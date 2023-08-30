package ir.maktabsharif.homeservicephase2.dto.response;

import ir.maktabsharif.homeservicephase2.entity.user.enums.WorkerStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkerResponseDTO {

    private String firstname;
    private String lastname;
    private String emailAddress;
    private WorkerStatus workerStatus;

}