package ir.maktabsharif.homeservicephase2.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import ir.maktabsharif.homeservicephase2.entity.Address.Address;
import ir.maktabsharif.homeservicephase2.entity.order.enums.OrderStatus;
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
public class FilterOrderResponseDTO {

    Address address;
    String description;
    Long orderProposedPrice;
    OrderStatus orderStatus;
    String jobName;
    String mainServiceName;
    @JsonFormat(pattern = "yyyy-MM-dd' 'HH:mm:ss")
    LocalDateTime orderCreationDate;
    String durationOfWork;
    Long acceptedOfferId;

    public FilterOrderResponseDTO(Address address, String description, Long orderProposedPrice,
                                  OrderStatus orderStatus, String jobName, String mainServiceName,
                                  LocalDateTime orderCreationDate, String durationOfWork) {
        this.address = address;
        this.description = description;
        this.orderProposedPrice = orderProposedPrice;
        this.orderStatus = orderStatus;
        this.jobName = jobName;
        this.mainServiceName = mainServiceName;
        this.orderCreationDate = orderCreationDate;
        this.durationOfWork = durationOfWork;
    }
}
