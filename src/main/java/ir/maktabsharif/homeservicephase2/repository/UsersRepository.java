package ir.maktabsharif.homeservicephase2.repository;

import ir.maktabsharif.homeservicephase2.base.repository.BaseRepository;
import ir.maktabsharif.homeservicephase2.entity.user.Users;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsersRepository<E extends Users> extends BaseRepository<E,Long> {

    boolean existsByEmail(String email);

    Optional<E> findByEmail(String email);

    void editPassword(String email, String newPassword);

    void updateCredit(String email, Long newCredit);

}
