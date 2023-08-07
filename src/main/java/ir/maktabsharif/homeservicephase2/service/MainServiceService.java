package ir.maktabsharif.homeservicephase2.service;

import ir.maktabsharif.homeservicephase2.base.service.BaseService;
import ir.maktabsharif.homeservicephase2.entity.service.MainService;

import java.util.List;
import java.util.Optional;

public interface MainServiceService extends BaseService<MainService,Long> {
    @Override
    void save(MainService mainService);

    @Override
    void delete(MainService mainService);

    @Override
    Optional<MainService> findById(Long aLong);

    @Override
    List<MainService> findAll();

    Optional<MainService> findByName(String mainServiceName);

    void deleteMainServiceByName(String mainServiceName);

}
