package ir.maktabsharif.homeservicephase2.repository;


import ir.maktabsharif.homeservicephase2.entity.user.Client;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends UsersRepository<Client> {

    @Query(" update Client c set c.password = :newPassword where c.email = :email")
    int editPassword(String email, String newPassword);

    boolean existsByEmail(String email);

    @Query("update Client c set c.credit = :newCredit where c.id = :email")
    int updateCredit(String email, Long newCredit);

    @Override
    Optional<Client> findByEmail(String email);

}
