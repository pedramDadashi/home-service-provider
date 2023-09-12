package ir.maktabsharif.homeservicephase2.mapper;

import ir.maktabsharif.homeservicephase2.dto.request.AddressDTO;
import ir.maktabsharif.homeservicephase2.entity.Address.Address;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddressMapper {

    public Address convertToAddress(AddressDTO dto) {
        return new Address(
                dto.getTitle(),
                dto.getProvince(),
                dto.getCity(),
                dto.getAvenue(),
                dto.getPostalCode(),
                dto.getHouseNumber(),
                dto.getMoreDescription()
        );
    }

    public AddressDTO convertToDTO(Address address) {
        return new AddressDTO(
                address.getTitle(),
                address.getProvince(),
                address.getCity(),
                address.getAvenue(),
                address.getPostalCode(),
                address.getHouseNumber(),
                address.getMoreDescription()
        );
    }

}
