package ir.maktabsharif.homeservicephase2.service.Impl;

import ir.maktabsharif.homeservicephase2.base.service.BaseServiceImpl;
import ir.maktabsharif.homeservicephase2.entity.service.MainService;
import ir.maktabsharif.homeservicephase2.exception.MainServiceIsNotExistException;
import ir.maktabsharif.homeservicephase2.repository.MainServiceRepository;
import ir.maktabsharif.homeservicephase2.service.MainServiceService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MainServiceServiceImpl extends BaseServiceImpl<MainService, Long, MainServiceRepository>
        implements MainServiceService {

    public MainServiceServiceImpl(MainServiceRepository repository) {
        super(repository);
    }

    @Override
    public List<MainService> findAll() {
        List<MainService> mainServiceList = repository.findAll();
        if (mainServiceList.isEmpty())
            throw new MainServiceIsNotExistException("there are no main services!");
        return mainServiceList;
    }

    @Override
    public Optional<MainService> findByName(String mainServiceName) {
        return repository.findByName(mainServiceName);
    }

    @Override
    public void deleteMainServiceByName(String mainServiceName) {
        repository.deleteByName(mainServiceName);
    }
}
