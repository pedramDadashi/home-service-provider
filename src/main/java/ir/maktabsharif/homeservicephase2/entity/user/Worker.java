package ir.maktabsharif.homeservicephase2.entity.user;



import ir.maktabsharif.homeservicephase2.entity.job.Job;

import ir.maktabsharif.homeservicephase2.entity.offer.Offer;
import ir.maktabsharif.homeservicephase2.entity.user.enums.WorkerStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;

import java.util.List;
import java.util.Set;


@Entity
@NoArgsConstructor
@Getter
@Setter

public class Worker extends Users {

    private String image;
    private Byte score;
    @Enumerated(value = EnumType.STRING)
    private WorkerStatus status;
    @ManyToMany(mappedBy = "workerSet")
    private Set<Job> jobSet = new HashSet<>();
    @OneToMany(mappedBy = "worker")
    private List<Offer> offerList = new ArrayList<>();

    public Worker(String firstname, String lastname, String email
            , String password) {
        super(firstname, lastname, email, password, Boolean.FALSE);
        this.score=0;
        this.status=WorkerStatus.NEW;
    }


    public void addJob(Job job) {
        this.jobSet.add(job);
        job.getWorkerSet().add(this);
    }

    public void deleteJob(Job job) {
        this.jobSet.remove(job);
        job.getWorkerSet().remove(this);
    }

    @Override
    public String toString() {
        return "Worker {" +
               "firstname = '" + getFirstname() + '\'' +
               ", lastname = '" + getLastname() + '\'' +
               ", email = '" + getEmail() + '\'' +
               ", username = '" + getEmail() + '\'' +
               ", score = " + score +
               ", workerStatus = '" + status + '\'' +
               ", credit = " + getCredit() +
               "} ";
    }

}
