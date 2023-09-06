package ir.maktabsharif.homeservicephase2.base.entity;


import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

import static jakarta.persistence.GenerationType.AUTO;


@MappedSuperclass
@Getter
@Setter

public abstract class BaseEntity<ID extends Serializable>
        implements Serializable {
    @Id
    @GeneratedValue(strategy = AUTO)
    private ID id;

}
