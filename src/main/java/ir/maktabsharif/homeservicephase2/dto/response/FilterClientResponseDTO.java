package ir.maktabsharif.homeservicephase2.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilterClientResponseDTO {

    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private Long credit;

}
