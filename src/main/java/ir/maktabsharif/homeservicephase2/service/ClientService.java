package ir.maktabsharif.homeservicephase2.service;

import ir.maktabsharif.homeservicephase2.dto.request.*;
import ir.maktabsharif.homeservicephase2.dto.response.*;
import ir.maktabsharif.homeservicephase2.entity.user.Client;

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

    List<OfferResponseDTO>  findOfferListByOrderIdBasedOnProposedPrice(Long orderId);

    List<OfferResponseDTO>  findOfferListByOrderIdBasedOnWorkerScore(Long orderId);

    List<FilterClientResponseDTO> clientFilter(FilterClientDTO clientDTO);

    ProjectResponse addClient(UserRegistrationDTO clientRegistrationDTO);

    ProjectResponse loginClient(LoginDTO clientLoginDto);

    ProjectResponse editPassword(ChangePasswordDTO changePasswordDTO);

    List<MainServiceResponseDTO> showAllMainServices();

    List<JobResponseDTO> showJobs(Long mainServiceId);

    ProjectResponse addNewOrder(SubmitOrderDTO submitOrderDTO);

    ProjectResponse choseWorkerForOrder(Long offerId);

    ProjectResponse changeOrderStatusToStarted(Long orderId);

    ProjectResponse changeOrderStatusToDone(Long orderId);

    ProjectResponse addComment(CommentRequestDTO commentRequestDTO);

    List<OrderResponseDTO> showAllOrders(Long clientId);

    List<OfferResponseDTO> showAllOfferForOrder(Long orderId);
}
