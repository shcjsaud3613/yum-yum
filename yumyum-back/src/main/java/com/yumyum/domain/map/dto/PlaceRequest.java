package com.yumyum.domain.map.dto;

import com.yumyum.domain.map.entity.Place;
import lombok.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlaceRequest {

    private String address;

    private String phone;

    private String name;

    private Double locationY;

    private Double locationX;

    public Place toEntity(){
        return Place.builder()
                .address(address.trim())
                .phone(phone.trim())
                .name(name)
                .locationY(locationY)
                .locationX(locationX)
                .build();
    }
}