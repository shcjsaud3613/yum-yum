package com.yumyum.domain.map.dto;

import com.yumyum.domain.map.entity.Place;
import lombok.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlaceResponse {

    private Long id;

    private String address;

    private String phone;

    private String name;

    private Double locationY;

    private Double locationX;

    public PlaceResponse(Place place){
        this.id = place.getId();
        this.address = place.getAddress();
        this.phone = place.getPhone();
        this.name = place.getName();
        this.locationY = place.getLocationY();
        this.locationX = place.getLocationX();
    }

    public PlaceRequest toRequest(){
        return PlaceRequest.builder()
                .address(address.trim())
                .phone(phone.trim())
                .name(name)
                .locationY(locationY)
                .locationX(locationX)
                .build();
    }
}
