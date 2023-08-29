package ir.maktabsharif.homeservicephase2.mapper;

import ir.maktabsharif.homeservicephase2.dto.request.MainServiceRequestDTO;
import ir.maktabsharif.homeservicephase2.dto.response.MainServiceResponseDTO;
import ir.maktabsharif.homeservicephase2.entity.service.MainService;
import org.springframework.stereotype.Component;

@Component
public class MainServiceMapper {

    public MainServiceResponseDTO convertToDTO(MainService mainService) {
        MainServiceResponseDTO mainServiceResponseDTO = new MainServiceResponseDTO();
        mainServiceResponseDTO.setName(mainService.getName());
        return mainServiceResponseDTO;
    }

    public MainService convertToMainService(MainServiceRequestDTO mainServiceRequestDTO) {
        MainService mainService = new MainService();
        mainService.setName(mainServiceRequestDTO.getName());
        return mainService;
    }

}
