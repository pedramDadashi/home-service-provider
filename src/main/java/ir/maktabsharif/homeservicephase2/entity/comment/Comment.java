package ir.maktabsharif.homeservicephase2.entity.comment;

import ir.maktabsharif.homeservicephase2.base.entity.BaseEntity;
import ir.maktabsharif.homeservicephase2.entity.order.Order;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Comment extends BaseEntity<Long> {

    private double score;
    private String textComment;
    @OneToOne
    private Order order;

    public Comment(double score, String comment) {
        this.score = score;
        this.textComment = comment;
    }

    @Override
    public String toString() {
        return "Comment{" +
               "score=" + score +
               ", comment='" + textComment + '\'' +
               ", order=" + order +
               '}';
    }
}
