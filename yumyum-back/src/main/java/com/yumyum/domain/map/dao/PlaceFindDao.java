package com.yumyum.domain.map.dao;

import com.yumyum.domain.map.entity.Place;
import com.yumyum.domain.map.exception.PlaceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaceFindDao {

    private final PlaceDao placeDao;

    public Place findById(final Long id){
        final Optional<Place> place = placeDao.findById(id);
        place.orElseThrow(() -> new PlaceNotFoundException(id));
        return place.get();
    }
}
