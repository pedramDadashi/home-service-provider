package ir.maktabsharif.homeservicephase2.entity.Address;

import ir.maktabsharif.homeservicephase2.base.entity.BaseEntity;
import ir.maktabsharif.homeservicephase2.entity.user.Client;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@EqualsAndHashCode(callSuper = true)
@ToString
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class Address  extends BaseEntity<Long> {

    @NonNull
    String title;
    @NonNull
    String province;
    @NonNull
    String city;
    @NonNull
    String avenue;
    @NonNull
    String postalCode;
    String houseNumber;
    String moreDescription;
    @ManyToOne(fetch = FetchType.EAGER)
    Client client;

    public Address(@NonNull String title, @NonNull String province, @NonNull String city, @NonNull String avenue,
                   @NonNull String postalCode, String houseNumber, String moreDescription) {
        this.title = title;
        this.province = province;
        this.city = city;
        this.avenue = avenue;
        this.postalCode = postalCode;
        this.houseNumber = houseNumber;
        this.moreDescription = moreDescription;
    }
}
