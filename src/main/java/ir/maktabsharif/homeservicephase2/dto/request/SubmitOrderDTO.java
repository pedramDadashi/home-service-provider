package ir.maktabsharif.homeservicephase2.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubmitOrderDTO {

    private String jobName;
    private Long clientProposedPrice;
    private String description;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime workStartDate;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime workEndDate;
    @NonNull
    private String AddressTitle;

}
