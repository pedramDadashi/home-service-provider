package ir.maktabsharif.homeservicephase2.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDTO {

    private String comment;
    private double score;

}
