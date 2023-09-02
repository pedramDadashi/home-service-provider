package ir.maktabsharif.homeservicephase2.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentPageDTO {
    private Long price;
    private String captcha;
    private String hidden;
    private String image;
    ClientIdOrderIdDTO clientIdOrderIdDTO;

}
