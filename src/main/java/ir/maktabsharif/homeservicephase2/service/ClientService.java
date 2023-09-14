package ir.maktabsharif.homeservicephase2.service;

import ir.maktabsharif.homeservicephase2.dto.request.*;
import ir.maktabsharif.homeservicephase2.dto.response.*;
import ir.maktabsharif.homeservicephase2.entity.user.Client;
import ir.maktabsharif.homeservicephase2.entity.user.Users;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

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

    List<OfferResponseDTO> findOfferListByOrderIdBasedOnProposedPrice(Long orderId,Users users);

    List<OfferResponseDTO> findOfferListByOrderIdBasedOnWorkerScore(Long orderId,Users users);

    List<FilterUserResponseDTO> clientFilter(FilterUserDTO clientDTO);

    String addNewClient(ClientRegistrationDTO clientRegistrationDTO);

    ProjectResponse editPassword(ChangePasswordDTO changePasswordDTO, Long clientId);

    List<MainServiceResponseDTO> showAllMainServices();

    List<JobResponseDTO> showJobs(String mainServiceName);

    ProjectResponse addNewOrder(SubmitOrderDTO submitOrderDTO, Long clientId);

    ProjectResponse chooseWorkerForOrder(Long offerId,Users users);

    ProjectResponse changeOrderStatusToStarted(Long orderId,Users users);

    ProjectResponse changeOrderStatusToDone(Long orderId,Users users);

    ProjectResponse registerComment(CommentRequestDTO commentRequestDTO,Users users);

    List<FilterOrderResponseDTO> showAllOrders(Long clientId);

    ProjectResponse changeOrderStatusToPaidByOnlinePayment(ClientIdOrderIdDTO clientIdOrderIdDTO);

    ProjectResponse increaseClientCredit(ClientIdPriceDTO clientIdPriceDTO);

    ProjectResponse paidByInAppCredit(Long orderId, Users client);

    ModelAndView payByOnlinePayment(Long orderId, Users users, Model model);

    ModelAndView increaseAccountBalance(Long price, Long clientId, Model model);

    ProjectResponse addAddress(AddressDTO addressDTO, Long clientId);

    Long getClientCredit(Long clientId);

    List<FilterOrderResponseDTO> filterOrder(String orderStatus, Long clientid);
}
