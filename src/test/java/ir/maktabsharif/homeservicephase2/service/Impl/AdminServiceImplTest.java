package ir.maktabsharif.homeservicephase2.service.Impl;

import ir.maktabsharif.homeservicephase2.entity.job.Job;
import ir.maktabsharif.homeservicephase2.entity.service.MainService;
import ir.maktabsharif.homeservicephase2.entity.user.Worker;
import ir.maktabsharif.homeservicephase2.entity.user.enums.WorkerStatus;
import ir.maktabsharif.homeservicephase2.exception.AlphabetException;
import ir.maktabsharif.homeservicephase2.exception.JobIsExistException;
import ir.maktabsharif.homeservicephase2.exception.MainServiceIsExistException;
import ir.maktabsharif.homeservicephase2.exception.MainServiceIsNotExistException;
import ir.maktabsharif.homeservicephase2.service.AdminService;
import ir.maktabsharif.homeservicephase2.service.JobService;
import ir.maktabsharif.homeservicephase2.service.MainServiceService;
import ir.maktabsharif.homeservicephase2.service.WorkerService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AdminServiceImplTest {

    @Autowired
    private AdminService ADMIN_SERVICE;
    @Autowired
    private MainServiceService MAIN_SERVICE_SERVICE;
    @Autowired
    private JobService JOB_SERVICE;
    @Autowired
    private WorkerService WORKER_SERVICE;

    @Test
    @Order(1)
    void createNewMainServiceWithExceptionForInvalidInput() {
        assertThrows(AlphabetException.class, () -> {
            ADMIN_SERVICE.createMainService("1");
        });
    }

    @Test
    @Order(2)
    void createNewMainService() {
        ADMIN_SERVICE.createMainService("build");
        ADMIN_SERVICE.createMainService("cleaning");
        Optional<MainService> optionalMainService =
                MAIN_SERVICE_SERVICE.findByName("cleaning");
        assertEquals("cleaning", optionalMainService.get().getName());
    }

    @Test
    @Order(3)
    void createDuplicateMainService() {
        assertThrows(MainServiceIsExistException.class, () -> {
            ADMIN_SERVICE.createMainService("cleaning");
        });
    }

    @Test
    @Order(4)
    void addJobWithoutMainService() {
        assertThrows(MainServiceIsNotExistException.class, () -> {
            ADMIN_SERVICE.addJob("pipeing", "room",
                    30000L, "nice!");
        });
    }

    @Test
    @Order(5)
    void addJob() {
        ADMIN_SERVICE.addJob("cleaning", "room",
                30000L, "nice!");
        ADMIN_SERVICE.addJob("build", "wall",
                120000L, "stone");
        Optional<Job> room = JOB_SERVICE.findByName("room");
        assertEquals(30000L, room.get().getBasePrice());
    }

    @Test
    @Order(6)
    void addDuplicateJob() {
        assertThrows(JobIsExistException.class, () -> {
            ADMIN_SERVICE.addJob("cleaning", "room",
                    30000L, "nice!");
        });
    }

    @Test
    @Order(8)
    void addWorkerToJob() {
        Optional<Worker> worker =
                WORKER_SERVICE.findByUsername("pedadashi@gmail.com");
        ADMIN_SERVICE.addWorkerToJob("room", "pedadashi@gmail.com");
        ADMIN_SERVICE.addWorkerToJob("wall", "milad.ah@yahoo.com");
        Job job = JOB_SERVICE.findByName("room").get();
        worker.get().getJobSet().forEach(jobb -> {
            if (Objects.equals(jobb.getId(), job.getId()))
                assertEquals(jobb.getId(), job.getId());
        });
    }


    @Test
    @Order(9)
    void findAllMainService() {
        List<MainService> allMainService = ADMIN_SERVICE.findAllMainService();
        int count = (int) allMainService.stream().
                filter(Objects::nonNull).count();
        assertEquals(2, count);
    }


    @Test
    @Order(10)
    void findAllJob() {
        List<Job> allJob = ADMIN_SERVICE.findAllJob();
        int count = (int) allJob.stream().
                filter(Objects::nonNull).count();
        assertEquals(2, count);
    }

    @Test
    @Order(11)
    void editJob() {
        Job job = JOB_SERVICE.findByName("wall").get();
        ADMIN_SERVICE.editJob("wall", 125000L, "wood");
        Job newJob = JOB_SERVICE.findByName("wall").get();
        assertEquals(job.getId(), newJob.getId());
    }

    @Test
    @Order(12)
    void findAllWorkers() {
        List<Worker> allWorkers = ADMIN_SERVICE.findAllWorkers();
        int count = (int) allWorkers.stream().
                filter(Objects::nonNull).count();
        assertEquals(2, count);
    }

    @Test
    @Order(7)
    void changeWorkerStatus() {
        WORKER_SERVICE.save(new Worker("pedram", "dadashi",
                "pedadashi@gmail.com", "1234@qwer"));
        ADMIN_SERVICE.changeWorkerStatus("pedadashi@gmail.com",
                WorkerStatus.CONFIRMED);
        WORKER_SERVICE.save(new Worker("milad", "ahmadian",
                "milad.ah@yahoo.com", "4567#poiu"));
        ADMIN_SERVICE.changeWorkerStatus("milad.ah@yahoo.com",
                WorkerStatus.CONFIRMED);
        assertEquals(WORKER_SERVICE.findByUsername("pedadashi@gmail.com").
                get().getStatus(), WorkerStatus.CONFIRMED);
    }
}
