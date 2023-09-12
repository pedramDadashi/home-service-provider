package ir.maktabsharif.homeservicephase2.mapper;

import ir.maktabsharif.homeservicephase2.dto.request.MainServiceRequestDTO;
import ir.maktabsharif.homeservicephase2.dto.response.MainServiceResponseDTO;
import ir.maktabsharif.homeservicephase2.entity.service.MainService;
import org.springframework.stereotype.Component;

@Component
public class MainServiceMapper {

    public MainServiceResponseDTO convertToDTO(MainService mainService) {
        return new MainServiceResponseDTO(
                mainService.getName()
        );
    }

    public MainService convertToMainService(MainServiceRequestDTO dto) {
        return new MainService(
                dto.getName()
        );
    }

}
