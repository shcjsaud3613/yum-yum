package com.yumyum.domain.feed.api;

import com.yumyum.domain.feed.application.*;
import com.yumyum.domain.feed.dao.FeedDeleteDao;
import com.yumyum.domain.feed.dto.*;
import com.yumyum.global.common.response.HttpUtils;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/feed")
@RequiredArgsConstructor
public class FeedController {

    private final FeedDeleteDao feedDeleteDao;
    private final FeedCreateService feedCreateService;
    private final FeedUpdateService feedUpdateService;
    private final FeedSearchService feedSearchService;
    private final FeedLikeService feedLikeService;
    private final FeedRecommendService feedRecommendService;
    private final FileService fileService;
    private final UploadService uploadService;

    @ApiOperation(value = "피드 등록", notes = "제목, 내용, 평점, 회원 번호, 장소 번호, 동영상, 썸네일로 피드를 등록한다.")
    @PostMapping("")
    public Object createFeed(@RequestBody final CreateFeedRequest dto) {
        feedCreateService.createFeed(dto);
        return HttpUtils.makeResponse("200", null, "success", HttpStatus.OK);
    }

    @ApiOperation(value = "동영상 및 썸네일 등록", notes = "동영상에서 썸네일을 추출하여 동영상과 함께 저장 후 경로를 반환한다.")
    @PostMapping("/video")
    public Object uploadVideo(@RequestParam MultipartFile file) throws IOException {
//        final FileDto response = fileService.uploadMedia(file, "media/");
        final FileDto response = uploadService.uploadMedia(file, "media/"); // S3 bucket의 static/ 폴더를 지정한 것.
        return HttpUtils.makeResponse("200", response, "success", HttpStatus.OK);
    }

    @ApiOperation(value = "피드 수정", notes = "피드 번호로 내용과 평점을 수정한다.")
    @PutMapping("")
    public Object updateFeed(@RequestBody final UpdateFeedRequest dto) {
        feedUpdateService.updateFeed(dto);
        return HttpUtils.makeResponse("200", null, "success", HttpStatus.OK);
    }

    @ApiOperation(value = "모든 피드 목록 조회", notes = "전체 피드 목록 조회, 로그인 회원 번호로 좋아요 여부를 확인한다.")
    @GetMapping("/list/{userId}")
    public Object feedList(@PathVariable final Long userId) {
        final List<FeedResponse> response = feedSearchService.findAll(userId);
        return HttpUtils.makeResponse("200", response, "success", HttpStatus.OK);
    }

    @ApiOperation(value = "피드 목록 검색", notes = "제목으로 피드 목록을 검색한다.")
    @GetMapping("/list/title/{title}/{userId}")
    public Object feedListByTitle(@PathVariable final String title, @PathVariable final Long userId) {
        final List<FeedResponse> response = feedSearchService.findByTitle(title, userId);
        return HttpUtils.makeResponse("200", response, "success", HttpStatus.OK);
    }

    @ApiOperation(value = "식당별 피드 목록 검색", notes = "음식점으로 피드 목록을 검색한다.")
    @GetMapping("/list/place/{placeId}/{userId}")
    public Object feedListByPlace(@PathVariable final Long placeId, @PathVariable final Long userId) {
        final List<FeedResponse> response = feedSearchService.findByPlace(placeId, userId);
        return HttpUtils.makeResponse("200", response, "success", HttpStatus.OK);
    }

    @ApiOperation(value = "유저 작성 피드 목록 조회", notes = "특정 유저가 작성한 피드 목록 조회, 로그인 회원 번호로 좋아요 여부를 확인한다.")
    @GetMapping("/list/{authorId}/{userId}")
    public Object userFeedList(@PathVariable final Long authorId, @PathVariable final Long userId) {
        final List<FeedResponse> response = feedSearchService.findByAuthor(authorId, userId);
        return HttpUtils.makeResponse("200", response, "success", HttpStatus.OK);
    }

    @ApiOperation(value = "유저 좋아요 피드 목록 조회", notes = "로그인 유저가 좋아요 누른 피드 목록을 조회한다.")
    @GetMapping("/list/like/{userId}")
    public Object userLikeFeedList(@PathVariable final Long userId) {
        final List<FeedResponse> response = feedSearchService.findByLike(userId);
        return HttpUtils.makeResponse("200", response, "success", HttpStatus.OK);
    }

    @ApiOperation(value = "단일 피드 조회", notes = "피드 번호로 피드 조회, 회원 번호로 좋아요 여부를 확인한다.")
    @GetMapping("/{feedId}/{userId}")
    public Object feedInfo(@PathVariable final Long feedId, @PathVariable final Long userId) {
        final FeedResponse response = feedSearchService.findById(feedId, userId);
        return HttpUtils.makeResponse("200", response, "success", HttpStatus.OK);
    }

    @ApiOperation(value = "피드 삭제", notes = "피드 번호로 피드를 삭제한다.")
    @DeleteMapping("/{feedId}")
    public Object deleteFeed(@PathVariable final Long feedId) {
        feedDeleteDao.deleteFeed(feedId);
        return HttpUtils.makeResponse("200", null, "success", HttpStatus.OK);
    }

    @ApiOperation(value = "피드 좋아요", notes = "피드 좋아요를 누른다.")
    @PostMapping("/like")
    public Object likeFeed(@RequestBody final LikeFeedRequest dto) {
        feedLikeService.doLikeFeed(dto);
        return HttpUtils.makeResponse("200", null, "success", HttpStatus.OK);
    }

    @ApiOperation(value = "피드 좋아요 취소", notes = "피드 좋아요를 취소한다.")
    @DeleteMapping("/like/{feedId}/{userId}")
    public Object cancelLikeFeed(@PathVariable final Long feedId, @PathVariable final Long userId) {
        feedLikeService.doCancelLikeFeed(feedId, userId);
        return HttpUtils.makeResponse("200", null, "success", HttpStatus.OK);
    }

    @ApiOperation(value = "이미지 식별")
    @PostMapping("/ai")
    public Object imageClassification(@RequestParam MultipartFile file) {
//        feedLikeService.doCancelLikeFeed(feedId, userId);
        return HttpUtils.makeResponse("200", null, "success", HttpStatus.OK);
    }

    @ApiOperation(value = "피드 추천 목록 조회", notes = "사용자 맞춤 추천 피드 목록 조회, 로그인 회원 번호로 좋아요 여부를 확인한다.")
    @GetMapping("/list/recommend/{userId}")
    public Object feedRecommendList(@PathVariable final Long userId) {
        final List<FeedResponse> response = feedRecommendService.getRecommendList(userId);
        return HttpUtils.makeResponse("200", response, "success", HttpStatus.OK);
    }
}