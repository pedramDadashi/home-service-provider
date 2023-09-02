package ir.maktabsharif.homeservicephase2.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentRequestDTO {

    @NotNull
    String captcha;
    @NotNull
    String hidden;
    @NotNull
    @Pattern(regexp = "[0-9]{13}(?:[0-9]{3})?$",message = "card number must be 16 digit")
    String number;
    @NotNull
    @Pattern(regexp = "^[a-zA-Z]*$",message = "please check your name . you cant enter space or digit")
    String name;
    @Pattern(regexp = "\\b\\d{3}\\b",message = "your cvc must be 3 digit")
    String cvc;
    @NotNull
    String year;
    @NotNull
    String month;
    @Valid
    ClientIdOrderIdDTO clientIdOrderIdDTO;

}