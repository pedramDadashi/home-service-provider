package ir.maktabsharif.homeservicephase2.base.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.AUTO;
import static lombok.AccessLevel.PRIVATE;

@Data
@MappedSuperclass
@FieldDefaults(level = PRIVATE)
public abstract class BaseEntity<ID extends Serializable> implements Serializable {
    @Id
    @GeneratedValue(strategy = AUTO)
    ID id;
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDateTime registrationTime = LocalDateTime.now();
}

