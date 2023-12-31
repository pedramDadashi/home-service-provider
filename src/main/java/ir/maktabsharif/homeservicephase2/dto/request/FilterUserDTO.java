package ir.maktabsharif.homeservicephase2.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilterUserDTO {

     String userType;
     String firstname;
     String lastname;
     String username;
     Boolean isActive;
     String userStatus;

     @JsonFormat(pattern = "yyyy-MM-dd' 'HH:mm:ss")
     LocalDateTime minUserCreationAt;
     @JsonFormat(pattern = "yyyy-MM-dd' 'HH:mm:ss")
     LocalDateTime maxUserCreationAt;

     Integer minNumberOfOperation;
     Integer maxNumberOfOperation;

     Integer minNumberOfDoneOperation;
     Integer maxNumberOfDoneOperation;

     Double minScore;
     Double maxScore;

     Long minCredit;
     Long maxCredit;

}
