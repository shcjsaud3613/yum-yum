package com.yumyum.domain.feed.application;

import com.yumyum.domain.feed.dao.FeedDao;
import com.yumyum.domain.feed.dto.CreateFeedRequest;
import com.yumyum.domain.map.application.PlaceCreateService;
import com.yumyum.domain.map.dto.PlaceRequest;
import com.yumyum.domain.map.entity.Place;
import com.yumyum.domain.user.application.RegexChecker;
import com.yumyum.domain.user.dao.UserFindDao;
import com.yumyum.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedCreateService {

    private final FeedDao feedDao;
    private final UserFindDao userFindDao;
    private final PlaceCreateService placeCreateService;
    private final RegexChecker regexChecker;

    public void createFeed(final CreateFeedRequest dto){
        final User user = userFindDao.findById(dto.getUserId());
        final PlaceRequest placeRequest = dto.getPlaceRequest();

        if(!placeRequest.getName().equals("")){
            final Place place = placeCreateService.createPlace(placeRequest);
            feedDao.save(dto.toEntity(user, place));
        }else{
            feedDao.save(dto.toEntity(user, null));
        }
    }

//    public void createFeed(final CreateFeedRequest dto){
//        final User user = userFindDao.findById(dto.getUserId());
//        final PlaceRequest placeRequest = dto.getPlaceRequest();
//
//        if(dto.getIsCompleted() == null) dto.setIsCompleted(false);
//
//        if(!dto.getIsCompleted()){
//            if(dto.getTitle() == null) dto.setTitle("");
//            if(dto.getContent() == null) dto.setContent("");
//            if(dto.getScore() == null) dto.setScore(0L);
//        }else{
//            regexChecker.stringCheck("Title", dto.getTitle());
//            regexChecker.stringCheck("Content", dto.getContent());
//        }
//
//        if(placeRequest != null){
//            final Place place = placeCreateService.createPlace(placeRequest);
//            feedDao.save(dto.toEntity(user, place));
//        }else{
//            feedDao.save(dto.toEntity(user, null));
//        }
//    }
}
