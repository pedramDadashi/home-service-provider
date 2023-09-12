package ir.maktabsharif.homeservicephase2.repository;


import ir.maktabsharif.homeservicephase2.base.repository.BaseRepository;
import ir.maktabsharif.homeservicephase2.entity.user.Client;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends BaseRepository<Client,Long> {


    Optional<Client> findByEmail(String email);

    @Query(" update Client c set c.password = :newPassword where c.email = :email")
    void editPassword(String email, String newPassword);

    @Modifying
    @Query("update Client c set c.credit = :newCredit where c.id = :clientId")
    void updateCredit(Long clientId, Long newCredit);

}
