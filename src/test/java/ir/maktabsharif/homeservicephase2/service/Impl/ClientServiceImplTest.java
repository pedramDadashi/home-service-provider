//package ir.maktabsharif.homeservicephase2.service.Impl;
//
//import ir.maktabsharif.homeservicephase2.entity.job.Job;
//import ir.maktabsharif.homeservicephase2.entity.offer.Offer;
//import ir.maktabsharif.homeservicephase2.entity.offer.TimeType;
//import ir.maktabsharif.homeservicephase2.entity.order.OrderStatus;
//import ir.maktabsharif.homeservicephase2.entity.service.MainService;
//import ir.maktabsharif.homeservicephase2.entity.user.Client;
//import ir.maktabsharif.homeservicephase2.entity.user.Worker;
//import ir.maktabsharif.homeservicephase2.exception.EmailFormatException;
//import ir.maktabsharif.homeservicephase2.exception.PasswordFormatException;
//import ir.maktabsharif.homeservicephase2.service.ClientService;
//import ir.maktabsharif.homeservicephase2.service.OfferService;
//import ir.maktabsharif.homeservicephase2.service.OrderService;
//import ir.maktabsharif.homeservicephase2.service.WorkerService;
//import org.junit.jupiter.api.MethodOrderer;
//import org.junit.jupiter.api.Order;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestMethodOrder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Objects;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//class ClientServiceImplTest {
//
//    @Autowired
//    private ClientService CLIENT_SERVICE;
//    @Autowired
//    private OrderService ORDER_SERVICE;
//    @Autowired
//    private OfferService OFFER_SERVICE;
//    @Autowired
//    private WorkerService WORKER_SERVICE;
//
//    @Test
//    @Order(2)
//    void findByUsername() {
//        assertEquals("ali",
//                CLIENT_SERVICE.findByUsername("ali.bon@gmail.com").get().getFirstname());
//    }
//
//    @Test
//    @Order(3)
//    void editPassword() {
//        Optional<Client> client = CLIENT_SERVICE.findByUsername("ali.bon@gmail.com");
//        CLIENT_SERVICE.editPassword(client.get(), "12345!qQwe");
//        Optional<Client> newclient = CLIENT_SERVICE.findByUsername("ali.bon@gmail.com");
//        assertEquals(newclient.get().getPassword(), "12345!qQwe");
//    }
//
//    @Test
//    @Order(1)
//    void signUp() {
//        Client client = new Client("ali", "bondar",
//                "ali.bon@gmail.com", "4582!pOikj");
//        CLIENT_SERVICE.signUp(client);
//        assertEquals("ali",
//                CLIENT_SERVICE.findByUsername("ali.bon@gmail.com").get().getFirstname());
//    }
//
//    @Test
//    @Order(4)
//    void findAllMainServices() {
//        List<MainService> allMainService = CLIENT_SERVICE.findAllMainServices();
//        int count = (int) allMainService.stream().
//                filter(Objects::nonNull).count();
//        assertEquals(2, count);
//    }
//
//    @Test
//    @Order(5)
//    void findAllJob() {
//        List<Job> allJob = CLIENT_SERVICE.findAllJob();
//        int count = (int) allJob.stream().
//                filter(Objects::nonNull).count();
//        assertEquals(2, count);
//    }
//
//    @Test
//    @Order(6)
//    void addOrder() {
//        CLIENT_SERVICE.addOrder(CLIENT_SERVICE.findByUsername("ali.bon@gmail.com").get(),
//                "room", 32000L, "nasty",
//                LocalDateTime.of(2024, 1, 1, 12, 12),
//                LocalDateTime.of(2024, 1, 1, 18, 12),
//                "khiaban sari,koche7,pelak12");
//        assertEquals(ORDER_SERVICE.findByClientEmailAndOrderStatus("ali.bon@gmail.com",
//                OrderStatus.WAITING_FOR_WORKER_SUGGESTION).get(0).getDescription(), "nasty");
//    }
//
//    @Test
//    @Order(9)
//    void findOfferListByOrderIdBasedOnProposedPrice() {
//        List<Offer> offerList =
//                CLIENT_SERVICE.findOfferListByOrderIdBasedOnProposedPrice(
//                        ORDER_SERVICE.findAll().get(0).getId());
//        assertTrue((offerList.get(offerList.size() - 1).getProposedPrice()) >
//                   (offerList.get(0).getProposedPrice()));
//    }
//
//    @Test
//    @Order(10)
//    void findOfferListByOrderIdBasedOnWorkerScore() {
//        Worker worker = WORKER_SERVICE.findByUsername("pedadashi@gmail.com").get();
//        worker.setScore(Byte.parseByte("4"));
//        WORKER_SERVICE.save(worker);
//        Worker worker1 = WORKER_SERVICE.findByUsername("milad.ah@yahoo.com").get();
//        worker1.setScore(Byte.parseByte("3"));
//        WORKER_SERVICE.save(worker1);
//        List<Offer> offerList =
//                CLIENT_SERVICE.findOfferListByOrderIdBasedOnWorkerScore(
//                        ORDER_SERVICE.findAll().get(0).getId());
//        assertTrue((offerList.get(offerList.size() - 1).getWorker().getScore()) <
//                   (offerList.get(0).getWorker().getScore()));
//    }
//
//    @Test
//    @Order(7)
//    void addOffers() {
//        Offer offer = new Offer("one", 31800L,
//                LocalDateTime.of(2024, 1, 1, 12, 13),
//                TimeType.HOUR, 4,
//                LocalDateTime.of(2024, 1, 1, 18, 11));
//        Optional<ir.maktabsharif.homeservicephase2.entity.order.Order> order =
//                Optional.of(ORDER_SERVICE.findAll().get(0));
//        offer.setOrder(order.get());
//        Optional<Worker> worker = WORKER_SERVICE.findByUsername("pedadashi@gmail.com");
//        offer.setWorker(worker.get());
//        OFFER_SERVICE.save(offer);
//        // -------
//        Offer offer1 = new Offer("two", 31500L,
//                LocalDateTime.of(2024, 1, 1, 12, 13),
//                TimeType.HOUR, 4,
//                LocalDateTime.of(2024, 1, 1, 18, 11));
//        offer1.setOrder(order.get());
//        Optional<Worker> worker1 = WORKER_SERVICE.findByUsername("milad.ah@yahoo.com");
//        offer1.setWorker(worker1.get());
//        OFFER_SERVICE.save(offer1);
//        assertEquals(2, OFFER_SERVICE.findAll().size());
//    }
//
//    @Test
//    @Order(8)
//    void acceptOffer() {
//        Long id = OFFER_SERVICE.findAll().get(0).getId();
//        CLIENT_SERVICE.acceptOffer(id);
//        assertTrue(OFFER_SERVICE.findById(id).get().getIsAccept());
//    }
//
//    @Test
//    @Order(11)
//    void changeOrderStatusAfterWorkerComes() {
//        Long id = ORDER_SERVICE.findAll().get(0).getId();
//        CLIENT_SERVICE.changeOrderStatusAfterWorkerComes(id);
//        assertEquals(ORDER_SERVICE.findById(id).get().getOrderStatus(),
//                OrderStatus.STARTED);
//    }
//
//    @Test
//    @Order(12)
//    void changeOrderStatusAfterStarted() {
//        Long id = ORDER_SERVICE.findAll().get(0).getId();
//        CLIENT_SERVICE.changeOrderStatusAfterStarted(id);
//        assertEquals(ORDER_SERVICE.findById(id).get().getOrderStatus(),
//                OrderStatus.DONE);
//    }
//
//    @Test
//    @Order(13)
//    void signUpWithInvalidEmail() {
//        assertThrows(EmailFormatException.class, () -> {
//            Client client = new Client("ali", "bondar",
//                    "ali.bongmail.com", "4582!pOikj");
//            CLIENT_SERVICE.signUp(client);
//        });
//    }
//
//    @Test
//    @Order(14)
//    void signUpWithInvalidPassword() {
//        assertThrows(PasswordFormatException.class, () -> {
//            Client client = new Client("ali", "bondar",
//                    "alii.bon@gmail.com", "4582ikj");
//            CLIENT_SERVICE.signUp(client);
//        });
//    }
//}