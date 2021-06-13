package com.yumyum.domain.user.api;

import com.yumyum.domain.feed.application.FileService;
import com.yumyum.domain.feed.application.UploadService;
import com.yumyum.domain.user.application.*;
import com.yumyum.domain.user.dao.UserDeleteDao;
import com.yumyum.domain.user.dao.UserFindDao;
import com.yumyum.domain.user.dto.FollowRequest;
import com.yumyum.domain.user.dto.SignUpRequest;
import com.yumyum.domain.user.dto.UpdateRequest;
import com.yumyum.domain.user.dto.UserResponse;
import com.yumyum.domain.user.entity.User;
import com.yumyum.global.common.response.Existence;
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
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserFindDao userFindDao;
    private final UserDeleteDao userDeleteDao;
    private final UserSignUpService userSignUpService;
    private final EmailExistService emailExistService;
    private final UserUpdateService userUpdateService;
    private final UserFollowService userFollowService;
    private final FollowSearchService followSearchService;
    private final FileService fileService;
    private final UploadService uploadService;

    @ApiOperation(value = "회원가입", notes = "이메일, 닉네임, 한줄 소개, 프로필 사진(필수x)을 받아 회원가입한다.")
    @PostMapping("/signup")
    public Object signUp(@RequestBody final SignUpRequest dto) {
        final UserResponse response = userSignUpService.doSignUp(dto);
        return HttpUtils.makeResponse("200", response, "success", HttpStatus.OK);
    }

    @ApiOperation(value = "이메일 중복 확인", notes = "해당 이메일로 가입한 계정이 있는지 확인한다.")
    @GetMapping("/email/{email}")
    public Object checkEmailExist(@PathVariable final String email) {
        final Existence response =  emailExistService.checkEmailExist(email);
        return HttpUtils.makeResponse("200", response, "success", HttpStatus.OK);
    }

    @ApiOperation(value = "로그인", notes = "소셜 로그인을 하면 이메일로 회원 정보를 반환한다.")
    @GetMapping("/login/{email}")
    public Object login(@PathVariable final String email) {
        final User user = userFindDao.findByEmail(email);
        return HttpUtils.makeResponse("200", new UserResponse(user), "success", HttpStatus.OK);
    }

    @ApiOperation(value = "프로필 이미지 업로드", notes = "프로필 이미지를 업로드 후 경로를 반환한다.")
    @PostMapping("/profile")
    public Object profileUpload(@RequestParam final MultipartFile file) throws IOException {
//        final String profilePath = fileService.uploadImage(file, "profile/");
        final String profilePath = uploadService.uploadImage(file, "profile/");
        return HttpUtils.makeResponse("200", profilePath, "success", HttpStatus.OK);
    }

    @ApiOperation(value = "회원 수정", notes = "회원 번호로 닉네임과 한줄 소개를 수정한다.")
    @PutMapping("")
    public Object updateUser(@RequestBody final UpdateRequest dto) {
        final UserResponse response = userUpdateService.updateUser(dto);
        return HttpUtils.makeResponse("200", response, "success", HttpStatus.OK);
    }

    @ApiOperation(value = "회원 조회", notes = "회원 번호로 회원 조회를 한다.")
    @GetMapping("/{userId}")
    public Object getUserInfo(@PathVariable final Long userId) {
        final User user = userFindDao.findById(userId);
        return HttpUtils.makeResponse("200", new UserResponse(user), "success", HttpStatus.OK);
    }

    @ApiOperation(value = "회원 삭제", notes = "회원 번호로 회원 탈퇴를 한다.")
    @DeleteMapping("/{userId}")
    public Object deleteUser(@PathVariable final Long userId) {
        userDeleteDao.deleteById(userId);
        return HttpUtils.makeResponse("200", null, "success", HttpStatus.OK);
    }

    @ApiOperation(value = "팔로우 목록 조회", notes = "유저가 팔로우하고 있는 회원 목록을 불러온다.")
    @GetMapping("/follow/list/{userId}")
    public Object getFollowList(@PathVariable final Long userId) {
        final List<UserResponse> response = followSearchService.getFollowList(userId, "host");
        return HttpUtils.makeResponse("200", response, "success", HttpStatus.OK);
    }

    @ApiOperation(value = "팔로워 목록 조회", notes = "유저를 팔로우하고 있는 회원 목록을 불러온다.")
    @GetMapping("/follow/follower/{userId}")
    public Object getFollowerList(@PathVariable final Long userId) {
        final List<UserResponse> response = followSearchService.getFollowList(userId, "follower");
        return HttpUtils.makeResponse("200", response, "success", HttpStatus.OK);
    }

    @ApiOperation(value = "팔로우", notes = "유저를 팔로우한다.")
    @PostMapping("/follow")
    public Object followUser(@RequestBody final FollowRequest dto) {
        userFollowService.doFollowUser(dto);
        return HttpUtils.makeResponse("200", null, "success", HttpStatus.OK);
    }

    @ApiOperation(value = "팔로우 취소", notes = "유저 팔로우를 취소한다.")
    @DeleteMapping("/follow/follower/{hostId}/{followerId}")
    public Object cancelFollowUser(@PathVariable final Long hostId, @PathVariable final Long followerId) {
        userFollowService.doCancelFollowUser(hostId, followerId);
        return HttpUtils.makeResponse("200", null, "success", HttpStatus.OK);
    }
}