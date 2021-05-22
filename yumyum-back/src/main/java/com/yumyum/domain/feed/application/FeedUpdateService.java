package com.yumyum.domain.feed.application;

import com.yumyum.domain.feed.dao.FeedFindDao;
import com.yumyum.domain.feed.dto.UpdateFeedRequest;
import com.yumyum.domain.feed.entity.Feed;
import com.yumyum.domain.map.application.PlaceCreateService;
import com.yumyum.domain.map.dao.PlaceFindDao;
import com.yumyum.domain.map.dto.PlaceRequest;
import com.yumyum.domain.map.entity.Place;
import com.yumyum.domain.user.application.RegexChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedUpdateService {

    private final FeedFindDao feedFindDao;
    private final PlaceFindDao placeFindDao;
    private final PlaceCreateService placeCreateService;
    private final RegexChecker regexChecker;

    public void updateFeed(final UpdateFeedRequest dto){
        final Feed feed = feedFindDao.findById(dto.getId());
        final PlaceRequest placeRequest = dto.getPlaceRequest();

        if(!placeRequest.getName().equals("")){
            final Place place = placeCreateService.createPlace(placeRequest);
            feed.updateFeed(dto, place);
        }else{
            feed.updateFeed(dto);
        }
    }

//    public void updateFeed(final UpdateFeedRequest dto){
//        final PlaceResponse placeResponse = dto.getPlaceResponse();
//
//        if(dto.getIsCompleted()){
//            regexChecker.stringCheck("Title", dto.getTitle());
//            regexChecker.stringCheck("Content", dto.getContent());
//            final Place check = placeFindDao.findById(dto.getPlaceResponse().getId());
//        }
//
//        final Feed feed = feedFindDao.findById(dto.getId());
//
//        if(placeResponse != null){
//            if(placeResponse.getId() == null) placeResponse.setId(0L);
//
//            if(placeResponse.getId() == 0){
//                final Place place = placeCreateService.createPlace(placeResponse.toRequest());
//                feed.updateFeed(dto, place);
//            }else{
//                feed.updateFeed(dto);
//            }
//        }else{
//            feed.updateFeed(dto, null);
//        }
//    }
}
