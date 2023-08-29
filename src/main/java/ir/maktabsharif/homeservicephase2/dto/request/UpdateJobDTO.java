package ir.maktabsharif.homeservicephase2.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateJobDTO {

    private Long jobId;
    private Long mainServiceRequestID;
    private String name;
    private String description;
    private Long basePrice;

}
