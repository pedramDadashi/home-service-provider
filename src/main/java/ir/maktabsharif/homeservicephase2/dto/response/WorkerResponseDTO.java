package ir.maktabsharif.homeservicephase2.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import ir.maktabsharif.homeservicephase2.entity.user.enums.WorkerStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class WorkerResponseDTO {

    Long workerId;
    String firstname;
    String lastname;
    String email;
    Boolean isActive;
    WorkerStatus workerStatus;
    double score;
    Long credit;
    int numberOfOperation;
    int numberOfDoneOperation;
    @JsonFormat(pattern = "yyyy-MM-dd' 'HH:mm:ss")
    LocalDateTime registrationTime;

}
