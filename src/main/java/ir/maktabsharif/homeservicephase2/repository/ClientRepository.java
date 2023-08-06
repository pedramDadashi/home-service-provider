package ir.maktabsharif.homeservicephase2.repository;


import ir.maktabsharif.homeservicephase2.entity.user.Client;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends UsersRepository<Client> {

    @Override
    boolean existsByEmail(String email);

    @Override
    Optional<Client> findByEmail(String email);

    @Override
    @Query(" update Client c set c.password = :newPassword where c.email = :email")
    void editPassword(String email, String newPassword);

    @Override
    @Query("update Client c set c.credit = :newCredit where c.id = :email")
    void updateCredit(String email, Long newCredit);

}
