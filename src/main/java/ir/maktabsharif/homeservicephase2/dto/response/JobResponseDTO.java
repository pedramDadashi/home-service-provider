package ir.maktabsharif.homeservicephase2.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobResponseDTO {

    private MainServiceResponseDTO mainServiceResponseDTO;
    private String name;
    private String description;
    private Long basePrice;

}
