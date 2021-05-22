package com.yumyum.domain.map.application;

import com.yumyum.domain.map.dao.PlaceDao;
import com.yumyum.domain.map.dto.PlaceResponse;
import com.yumyum.domain.map.entity.Place;
import com.yumyum.domain.user.application.RegexChecker;
import com.yumyum.global.error.exception.InvalidParameterException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaceSearchService {

    private final PlaceDao placeDao;
    private final RegexChecker regexChecker;

    public List<PlaceResponse> findAll(){
        List<PlaceResponse> list = new ArrayList<>();
        final List<Place> pList = placeDao.findAll();
        for(Place place : pList){
            list.add(new PlaceResponse(place));
        }
        return list;
    }

    public List<PlaceResponse> findByKeyword(final String type, final String keyword){
        regexChecker.stringCheck("Type", type);
        regexChecker.stringCheck("Keyword", type);
        if(!type.equals("address") && !type.equals("name")){
            throw new InvalidParameterException("Type is Invalid");
        }

        List<PlaceResponse> list = new ArrayList<>();

        switch (type){
            case "address":
                final List<Place> aList = placeDao.findByAddressContainingIgnoreCase(keyword);
                for(Place place : aList){
                    list.add(new PlaceResponse(place));
                }
                break;
            case "name":
                final List<Place> nList = placeDao.findByNameContainingIgnoreCase(keyword);
                for(Place place : nList){
                    list.add(new PlaceResponse(place));
                }
                break;
            default:
                break;
        }

        return list;
    }
}
