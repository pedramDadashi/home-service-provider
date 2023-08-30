package ir.maktabsharif.homeservicephase2.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDTO {

    private Long orderId;
    private String comment;
    private int score;

}
