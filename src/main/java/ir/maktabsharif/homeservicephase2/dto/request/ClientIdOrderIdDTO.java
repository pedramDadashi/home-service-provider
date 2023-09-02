package ir.maktabsharif.homeservicephase2.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults( level = AccessLevel.PRIVATE)
@Getter
@Setter
public class ClientIdOrderIdDTO {

    Long clientId;
    Long orderId;
}
