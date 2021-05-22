package com.yumyum.domain.map.api;

import com.yumyum.domain.map.application.PlaceCreateService;
import com.yumyum.domain.map.application.PlaceSearchService;
import com.yumyum.domain.map.dao.PlaceFindDao;
import com.yumyum.domain.map.dto.PlaceResponse;
import com.yumyum.domain.map.entity.Place;
import com.yumyum.global.common.response.HttpUtils;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/place")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceFindDao placeFindDao;
    private final PlaceCreateService placeCreateService;
    private final PlaceSearchService placeSearchService;

//    @ApiOperation(value = "맛집 등록", notes = "주소, 전화번호, 장소명, 좌표값을 받아 맛집을 등록한다.")
//    @PostMapping("")
//    public Object createPlace(@RequestBody final PlaceRequest dto) {
//        placeCreateService.createPlace(dto);
//        return HttpUtils.makeResponse("200", null, "success", HttpStatus.OK);
//    }

    @ApiOperation(value = "맛집 목록 조회", notes = "모든 맛집 목록을 불러온다.")
    @GetMapping("/list")
    public Object placeList() {
        final List<PlaceResponse> response = placeSearchService.findAll();
        return HttpUtils.makeResponse("200", response, "success", HttpStatus.OK);
    }

    @ApiOperation(value = "맛집 목록 검색", notes = "주소 혹은 장소명으로 맛집 목록을 검색한다.")
    @GetMapping("/list/{type}/{keyword}")
    public Object placeListByKeyword(@PathVariable final String type, @PathVariable final String keyword) {
        final List<PlaceResponse> response = placeSearchService.findByKeyword(type, keyword);
        return HttpUtils.makeResponse("200", response, "success", HttpStatus.OK);
    }

    @ApiOperation(value = "맛집 조회", notes = "맛집 번호로 맛집 정보를 조회한다.")
    @GetMapping("/{placeId}")
    public Object placeInfo(@PathVariable final Long placeId) {
        final Place place =  placeFindDao.findById(placeId);
        return HttpUtils.makeResponse("200", new PlaceResponse(place), "success", HttpStatus.OK);
    }
}