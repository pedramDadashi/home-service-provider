package ir.maktabsharif.homeservicephase2;

import ir.maktabsharif.homeservicephase2.entity.user.Admin;
import ir.maktabsharif.homeservicephase2.service.AdminService;
import ir.maktabsharif.homeservicephase2.service.Impl.AdminServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class HomeServicePhase2Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext run
                = SpringApplication.run(HomeServicePhase2Application.class, args);

        AdminService adminService = (AdminService) run.getBean("adminServiceImpl");
        Admin admin=new Admin("m.reza","abdo","reazreza@gmail.com"
                ,"1234@qwer",true,true);
        adminService.save(admin);

    }
}
