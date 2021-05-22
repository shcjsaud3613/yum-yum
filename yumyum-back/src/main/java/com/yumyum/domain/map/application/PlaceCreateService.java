package com.yumyum.domain.map.application;

import com.yumyum.domain.map.dao.PlaceDao;
import com.yumyum.domain.map.dto.PlaceRequest;
import com.yumyum.domain.map.entity.Place;
import com.yumyum.domain.user.application.RegexChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaceCreateService {

    private final PlaceDao placeDao;
    private final RegexChecker regexChecker;

    public Place createPlace(final PlaceRequest dto) {
        regexChecker.stringCheck("Address", dto.getAddress());
        regexChecker.stringCheck("Name", dto.getName());

//        if(dto.getPhone() == null) dto.setPhone("");
//        else regexChecker.phoneCheck(dto.getPhone());

        if(placeDao.existsByAddressAndName(dto.getAddress(), dto.getName())){
            final Optional<Place> obj = placeDao.findByAddressAndName(dto.getAddress(), dto.getName());
            return obj.get();
        }

        final Place place = placeDao.save(dto.toEntity());
        return place;
    }
}
