package ir.maktabsharif.homeservicephase2.service;

import ir.maktabsharif.homeservicephase2.entity.comment.Comment;
import ir.maktabsharif.homeservicephase2.entity.job.Job;
import ir.maktabsharif.homeservicephase2.entity.service.MainService;
import ir.maktabsharif.homeservicephase2.entity.user.Client;

import java.io.FileNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ClientService extends UsersService<Client>  {

    @Override
    void save(Client client);

    @Override
    void delete(Client client);

    @Override
    Optional<Client> findById(Long aLong);

    @Override
    List<Client> findAll();

    @Override
    Optional<Client> findByUsername(String email);

    @Override
    void editPassword(Client client, String newPassword);

    void signUp(Client client);

    List<MainService> findAllMainService() throws FileNotFoundException;

    List<Job> findAllJob() throws FileNotFoundException;

    void addOrder(Client client, String jobName, Long proposedPrice,
                  String description, LocalDateTime date, LocalDateTime time, String address);

}
