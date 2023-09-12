package ir.maktabsharif.homeservicephase2.repository;


import ir.maktabsharif.homeservicephase2.base.repository.BaseRepository;
import ir.maktabsharif.homeservicephase2.entity.user.Admin;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends BaseRepository<Admin, Long> {


    Optional<Admin> findByEmail(String email);

//    @Query(" select a from Admin a  where a.manager = TRUE ")
//    Optional<Admin> findManager();

//    @Query("select a  from Admin a where a.manager")
//     Optional<Admin> findManager();
//     Iterable<Admin> findByEnabledTrue();

}
