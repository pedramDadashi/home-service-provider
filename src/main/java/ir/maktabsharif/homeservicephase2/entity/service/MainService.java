package ir.maktabsharif.homeservicephase2.entity.service;

import ir.maktabsharif.homeservicephase2.base.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class MainService extends BaseEntity<Long> {

    @Column(nullable = false)
    String name;
    @OneToMany(mappedBy = "mainService")
    List<Job> jobList = new ArrayList<>();

    public MainService(String name) {
        this.name = name;
    }

}
