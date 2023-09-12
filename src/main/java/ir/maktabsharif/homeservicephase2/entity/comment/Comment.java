package ir.maktabsharif.homeservicephase2.entity.comment;

import ir.maktabsharif.homeservicephase2.base.entity.BaseEntity;
import ir.maktabsharif.homeservicephase2.entity.order.Order;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class Comment extends BaseEntity<Long> {

    int score;
    String textComment;
    @OneToOne
    Order order;

    public Comment(int score, String comment) {
        this.score = score;
        this.textComment = comment;
    }

}
