package ir.maktabsharif.homeservicephase2.repository;


import ir.maktabsharif.homeservicephase2.base.repository.BaseRepository;
import ir.maktabsharif.homeservicephase2.entity.user.Users;
import ir.maktabsharif.homeservicephase2.entity.user.enums.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends BaseRepository<Users, Long> {

    Optional<Users> findByEmail(String email);

    @Query("select u from Users u where u.role =:role ")
    List<Users> findAllByRole(Role role);

}
