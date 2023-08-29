package ir.maktabsharif.homeservicephase2.service;

import ir.maktabsharif.homeservicephase2.dto.request.FilterClientDTO;
import ir.maktabsharif.homeservicephase2.dto.response.FilterClientResponseDTO;
import ir.maktabsharif.homeservicephase2.entity.job.Job;
import ir.maktabsharif.homeservicephase2.entity.offer.Offer;
import ir.maktabsharif.homeservicephase2.entity.service.MainService;
import ir.maktabsharif.homeservicephase2.entity.user.Client;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ClientService extends UsersService<Client> {

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

    List<MainService> findAllMainService();

    List<Job> findAllJob();

    void addOrder(Client client, String jobName, Long proposedPrice,
                  String description, LocalDateTime executionTime,
                  LocalDateTime updateTime, String address);

    List<Offer> findOfferListByOrderIdBasedOnProposedPrice(Long orderId);

    List<Offer> findOfferListByOrderIdBasedOnWorkerScore(Long orderId);

    void acceptOffer(Long offerId);

    void changeOrderStatusAfterWorkerComes(Long orderId);

    void changeOrderStatusAfterStarted(Long orderId);

    List<FilterClientResponseDTO> clientFilter(FilterClientDTO clientDTO);
}
