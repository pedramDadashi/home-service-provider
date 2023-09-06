package ir.maktabsharif.homeservicephase2.repository;


import ir.maktabsharif.homeservicephase2.base.repository.BaseRepository;
import ir.maktabsharif.homeservicephase2.entity.user.Users;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends BaseRepository<Users,Long> {

Optional<Users> findByEmail(String email);

}
