package ir.maktabsharif.homeservicephase2.repository;

import ir.maktabsharif.homeservicephase2.base.repository.BaseRepository;
import ir.maktabsharif.homeservicephase2.entity.service.MainService;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MainServiceRepository extends BaseRepository<MainService, Long> {

    Optional<MainService> findByName(String mainServiceName);

    void deleteByName(String mainServiceName);

}
