package ir.maktabsharif.homeservicephase2.entity.service;

import ir.maktabsharif.homeservicephase2.base.entity.BaseEntity;
import ir.maktabsharif.homeservicephase2.entity.job.Job;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class MainService extends BaseEntity<Long> {

    @Column(nullable = false)
    private String name;
    @OneToMany(mappedBy = "mainService")
    private List<Job> jobList = new ArrayList<>();

    public MainService(String name) {
        this.name = name;
    }
}
