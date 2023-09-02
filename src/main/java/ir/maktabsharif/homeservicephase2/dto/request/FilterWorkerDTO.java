package ir.maktabsharif.homeservicephase2.dto.request;

import ir.maktabsharif.homeservicephase2.entity.user.enums.WorkerStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterWorkerDTO {

    private String userType;
    private String firstname;
    private String lastname;
    private String email;
    private String username;
    private Boolean isActive;
    private WorkerStatus workerStatus;
    private Integer rate;
    private Long credit;

}
