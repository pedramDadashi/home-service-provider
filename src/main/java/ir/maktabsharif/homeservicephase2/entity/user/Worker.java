package ir.maktabsharif.homeservicephase2.entity.user;


import ir.maktabsharif.homeservicephase2.entity.offer.Offer;
import ir.maktabsharif.homeservicephase2.entity.service.Job;
import ir.maktabsharif.homeservicephase2.entity.user.enums.Role;
import ir.maktabsharif.homeservicephase2.entity.user.enums.WorkerStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static lombok.AccessLevel.PRIVATE;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class Worker extends Users {
    @Lob
    byte[] image;
    double score;
    int rateCounter;
    String province;
    @Enumerated(value = EnumType.STRING)
    WorkerStatus status;
    @ManyToMany(mappedBy = "workerSet", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    Set<Job> jobSet = new HashSet<>();
    @OneToMany(mappedBy = "worker")
    List<Offer> offerList = new ArrayList<>();

    public Worker(String firstname, String lastname, String email, String password,
                  String province,byte[] image) {
        super(firstname, lastname, email, password, Role.WORKER);
        this.score = 0;
        this.rateCounter = 0;
        this.status = WorkerStatus.NEW;
        this.province = province;
        this.image = image;
    }

    public void addJob(Job job) {
        this.jobSet.add(job);
        job.getWorkerSet().add(this);
    }

    public void deleteJob(Job job) {
        this.jobSet.remove(job);
        job.getWorkerSet().remove(this);
    }

    public void rate(int score) {
        this.score = ((this.score * this.rateCounter) + score) / (this.rateCounter++);
    }

    public void delay(int hours) {
        this.score = this.score - hours;
        checkRate();
    }

    private void checkRate() {
        if (this.score < 0) {
            this.setIsActive(false);
            this.status = WorkerStatus.AWAITING;
            this.score = 0;
        }
    }
}
