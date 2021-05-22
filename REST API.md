# URL 형식

- `User`, `Place`, `Feed` 는 [`http://k4b206.p.ssafy.io:8081/yumyum`](http://k4b206.p.ssafy.io:8081/yumyum/user/1) 라는 공통 앞주소로 시작한다.
- `AI` 는 유일하게 [`http://k4b206.p.ssafy.io:5000`](http://k4b206.p.ssafy.io:8081/yumyum/user/1) 라는 앞주소로 시작한다.

# User

### 1. 회원가입

```kotlin
POST ("/user/signup")

1) **프로필 사진을 업로드 할 때**
**{
	"email": "ssafy@gmail.com"
	"nickname": "김싸피"
	"introduction": "한줄로 자신을 소개해주세요"
	"profilePath": "http://k4b206.p.ssafy.io/resources/profile/1620693596780my_profile.jpg"
}

2) 프로필 사진을 업로드 하지 않을 때
{
	"email": "ssafy@gmail.com"
	"nickname": "김싸피"
	"introduction": "안녕하세요"
	"profilePath": null
}**
```

```kotlin
1) **성공 (프로필 있음)
{
  "status": "200",
  "message": "success",
  "data": {
    "id": 1,
    "email": "ssafy@naver.com",
    "nickname": "김싸피",
    "introduction": "한줄로 자신을 소개해주세요.",
    "profilePath": "http://k4b206.p.ssafy.io/resources/profile/1620693596780my_profile.jpg"
  }
}

2**) **성공 (프로필 없음)
{
  "status": "200",
  "message": "success",
  "data": {
    "id": 1,
    "email": "ssafy@naver.com",
    "nickname": "김싸피",
    "introduction": "한줄로 자신을 소개해주세요.",
    "profilePath": ""
  }
}

3**) **입력값 유효하지 않음 (예: 이메일)
{
  "status": 200,
	"error": "Invalid Parameter",
	"code": "C001",
  "message": "Email is Invalid"
}

4**) **이메일 중복
{
  "status": 200,
	"error": "Entity is Duplicated",
	"code": "C004",
  "message": "Email is Duplicated"
}**
```

### 2. 이메일 중복 확인

```kotlin
GET ("/user/email/{email}")
```

```kotlin
1) **성공 (중복 없음)
{
  "status": "200",
  "message": "success",
  "data": {
		"existence": false
	}
}

2**) **성공 (중복 있음)
{
  "status": "200",
  "message": "success",
  "data": {
		"existence": true
	}
}**
```

### 3. 로그인

```kotlin
GET ("/user/login/{email}")
```

```kotlin
1) **성공
{
  "status": "200",
  "message": "success",
  "data": {
    "id": 1,
    "email": "ssafy@naver.com",
    "nickname": "김싸피",
    "introduction": "한줄로 자신을 소개해주세요.",
    "profilePath": "http://k4b206.p.ssafy.io/resources/profile/1620693596780my_profile.jpg"
  }
}**
```

### 4. 프로필 이미지 업로드

```kotlin
POST ("/user/profile")

사진 파일 전송
```

```kotlin
1) **성공
{
  "status": "200",
  "message": "success",
  "data": "http://k4b206.p.ssafy.io/resources/profile/1620693596780my_profile.jpg"
}**
```

### 5. 회원 수정

```kotlin
PUT ("/user")

{
  "id": 1,
  "**nickname": "김싸피"
	"introduction": "한줄로 자신을 소개해주세요"
	"profilePath": "http://k4b206.p.ssafy.io/resources/profile/1620693596780my_profile.jpg"**
}
```

```kotlin
1) **성공
{
  "status": "200",
  "message": "success",
  "data": {
    "id": 1,
    "email": "ssafy@naver.com",
    "nickname": "김싸피",
    "introduction": "한줄로 자신을 소개해주세요.",
    "profilePath": "http://k4b206.p.ssafy.io/resources/profile/1620693596780my_profile.jpg"
  }
}**
```

### 6. 회원 조회

```kotlin
GET ("/user/{userId}")
```

```kotlin
1) **성공
{
  "status": "200",
  "message": "success",
  "data": {
    "id": 1,
    "email": "ssafy@naver.com",
    "nickname": "김싸피",
    "introduction": "한줄로 자신을 소개해주세요.",
    "profilePath": "http://k4b206.p.ssafy.io/resources/profile/1620693596780my_profile.jpg"
  }
}**
```

### 7. 회원 삭제

```kotlin
DELETE ("/user/{userId}")
```

```kotlin
1) **성공
{
  "status": "200",
  "message": "success",
  "data": null
}**
```

### 8. 팔로우 목록 조회

```kotlin
GET ("/user/follow/list/{userId}")
```

```kotlin
1) **성공
{
  "status": "200",
  "message": "success",
  "data": [
    {
	    "id": 1,
	    "email": "ssafy@naver.com",
	    "nickname": "김싸피",
	    "introduction": "한줄로 자신을 소개해주세요.",
	    "profilePath": "http://k4b206.p.ssafy.io/resources/profile/1620693596780my_profile.jpg"
	  },
		{
	    "id": 2,
	    "email": "hong@naver.com",
	    "nickname": "홍길동",
	    "introduction": "안녕하세요.",
	    "profilePath": ""
	  },
	]
}**
```

### 9. 팔로워 목록 조회

```kotlin
GET ("/user/follow/follower/{userId}")
```

```kotlin
1) **성공
{
  "status": "200",
  "message": "success",
  "data": [
    {
	    "id": 1,
	    "email": "ssafy@naver.com",
	    "nickname": "김싸피",
	    "introduction": "한줄로 자신을 소개해주세요.",
	    "profilePath": "http://k4b206.p.ssafy.io/resources/profile/1620693596780my_profile.jpg"
	  },
		{
	    "id": 2,
	    "email": "hong@naver.com",
	    "nickname": "홍길동",
	    "introduction": "안녕하세요.",
	    "profilePath": ""
	  },
	]
}**
```

### 10. 팔로우

```kotlin
POST ("/user/follow")

{
	"hostId": 1, // 팔로우 당하는 쪽
	"followerId": 2 // 팔로우 하는 쪽
}
```

```kotlin
1) **성공
{
  "status": "200",
  "message": "success",
  "data": null
}**
```

### 11. 팔로우 취소

```kotlin
DELETE ("/user/follow/follower/{hostId}/{followerId}")
```

```kotlin
1) **성공
{
  "status": "200",
  "message": "success",
  "data": null
}**
```

# Place

### 1. 맛집 목록 조회

```kotlin
GET ("/place/list")
```

```kotlin
1) **성공
{
  "status": "200",
  "message": "success",
  "data": [
		{
	    "id": 1,
	    "address": "대전 유성구 덕명동",
	    "phone": "010-8282-5353",
	    "name": "대전 족발",
	    "locationY": 213.453532,
	    "locationX": 24.25235
	  }
	]
}**
```

### 2. 맛집 목록 검색

```kotlin
GET ("/place/list/{type}/{keyword}")

type: "address" 혹은 "name"
keyword: 검색 내용 (예: "대전")
```

```kotlin
1) **성공
{
  "status": "200",
  "message": "success",
  "data": [
		{
	    "id": 1,
	    "address": "대전 유성구 덕명동",
	    "phone": "010-8282-5353",
	    "name": "대전 족발",
	    "locationY": 213.453532,
	    "locationX": 24.25235
	  }
	]
}**
```

### 3. 맛집 조회

```kotlin
GET ("/place/{placeId}")
```

```kotlin
1) **성공
{
  "status": "200",
  "message": "success",
  "data": {
    "id": 1,
    "address": "대전 유성구 덕명동",
    "phone": "010-8282-5353",
    "name": "대전 족발",
    "locationY": 213.453532,
    "locationX": 24.25235
  }
}**
```

# Feed

### 1. 피드 등록

```kotlin
POST ("/feed")

1) **완성**
{
  "**title": "족발",
  "content": "정말 맛있어요~",
	"score": 5,
	"userId": 1,
	"placeRequest": {
		"address": "대전 유성구 덕명동",
    "phone": "010-8282-5353",
    "name": "대전 족발",
    "locationY": 213.453532,
    "locationX": 24.25235
	},
	"videoPath": "http://k4b206.p.ssafy.io/resources/media/1620693696005my_video.mp4",
  "thumbnailPath": "http://k4b206.p.ssafy.io/resources/media/1620693696005my_video_thumbnail.png",
  "isCompleted": true**
}

2) **임시저장 (최소입력)**
{
  "**title": "",
  "content": "",
	"score": 0,
	"userId": 1,
	"placeRequest": null,
	"videoPath": "http://k4b206.p.ssafy.io/resources/media/1620693696005my_video.mp4",
  "thumbnailPath": "http://k4b206.p.ssafy.io/resources/media/1620693696005my_video_thumbnail.png",
  "isCompleted": false**
}
```

```kotlin
1) **성공
{
  "status": "200",
  "message": "success",
  "data": null
}**
```

### 2. 동영상 및 썸네일 등록

```kotlin
POST ("/feed/video")

동영상 파일 전송
```

```kotlin
1) **성공
{
  "status": "200",
  "message": "success",
  "data": {
    "videoPath": "http://k4b206.p.ssafy.io/resources/media/1620693696005my_video.mp4",
    "thumbnailPath": "http://k4b206.p.ssafy.io/resources/media/1620693696005my_video_thumbnail.png",
  }
}**
```

### 3. 피드 수정

```kotlin
PUT ("/feed")

1) **완성본 수정 (혹은 완성본으로 수정)**
{
	"id": 1,
  "**title": "족발",
  "content": "정말 맛있어요~",
	"score": 5,
	"userId": 1,
	"placeResponse": {
		"id": 1,
		"address": "대전 유성구 덕명동",
    "phone": "010-8282-5353",
    "name": "대전 족발",
    "locationY": 213.453532,
    "locationX": 24.25235
	},
	"isCompleted": true**
}

2) **임시저장 수정 (장소 이미)**
{
	"id": 1,
  "**title": "족발",
  "content": "",
	"score": 5,
	"userId": 1,
	"placeResponse": {
		"id": 0,
		"address": ""
    "phone": "",
    "name": "",
    "locationY": ""
    "locationX": ""
	},
	"isCompleted": true**
}

2) **임시저장 수정 (장소 등록)**
{
	"id": 1,
  "**title": "족발",
  "content": "정말 맛있어요~",
	"score": 5,
	"userId": 1,
	"placeResponse": {
		"id": 0,
		"address": "대전 유성구 덕명동",
    "phone": "010-8282-5353",
    "name": "대전 족발",
    "locationY": 213.453532,
    "locationX": 24.25235
	},
	"isCompleted": false**
}

3) **임시저장 수정 (장소 등록 안함)**
{
	"id": 1,
  "**title": "족발",
  "content": "정말 맛있어요~",
	"score": 5,
	"userId": 1,
	"placeResponse": null
	"isCompleted": false**
}
```

```kotlin
1) **성공
{
  "status": "200",
  "message": "success",
  "data": null
}**
```

### 4. 모든 피드 목록 조회

```kotlin
GET ("/feed/list/{userId}")
```

```kotlin
1) **성공
{
  "status": "200",
  "message": "success",
  "data": [
    {
      "id": 1,
      "title": "족발",
      "content": "정말 맛있어요~",
      "score": 5,
      "user": {
        "id": 1,
        "email": "ssafy@naver.com",
        "nickname": "김싸피",
        "introduction": "한줄로 자신을 소개해주세요.",
        "profilePath": "",
        "createdDate": "2021-04-22T09:54:43",
        "modifiedDate": "2021-04-22T09:54:43"
      },
      "place": {
        "id": 1,
        "address": "대전 유성구 덕명동",
        "phone": "010-8282-5353",
        "name": "대전 족발",
        "locationY": 213.453532,
        "locationX": 24.25235
      },
      "videoPath": "http://k4b206.p.ssafy.io/resources/media/1620693696005my_video.mp4",
      "thumbnailPath": "http://k4b206.p.ssafy.io/resources/media/1620693696005my_video_thumbnail.png",
      "isCompleted": true,
      "likeCount": 2,
      "isLike": false,
      "createdDate": "2021-04-26T16:00:31",
      "modifiedDate": "2021-04-26T16:00:31"
    }
	]
}**
```

### 5. 피드 목록 검색

```kotlin
GET ("/feed/list/title/{title}/{userId}")

title: 검색할 피드 제목 (예: "족")
```

```kotlin
1) **성공
{
  "status": "200",
  "message": "success",
  "data": [
    {
      "id": 1,
      "title": "족발",
      "content": "정말 맛있어요~",
      "score": 5,
      "user": {
        "id": 1,
        "email": "ssafy@naver.com",
        "nickname": "김싸피",
        "introduction": "한줄로 자신을 소개해주세요.",
        "profilePath": "",
        "createdDate": "2021-04-22T09:54:43",
        "modifiedDate": "2021-04-22T09:54:43"
      },
      "place": {
        "id": 1,
        "address": "대전 유성구 덕명동",
        "phone": "010-8282-5353",
        "name": "대전 족발",
        "locationY": 213.453532,
        "locationX": 24.25235
      },
      "videoPath": "http://k4b206.p.ssafy.io/resources/media/1620693696005my_video.mp4",
      "thumbnailPath": "http://k4b206.p.ssafy.io/resources/media/1620693696005my_video_thumbnail.png",
      "isCompleted": true,
      "likeCount": 2,
      "isLike": false,
      "createdDate": "2021-04-26T16:00:31",
      "modifiedDate": "2021-04-26T16:00:31"
    }
	]
}**
```

### 6. 유저 작성 피드 목록 조회

```kotlin
GET ("/feed/list/{authorId}/{userId}")

authorId: 현재 방문한 회원의 ID
```

```kotlin
1) **성공
{
  "status": "200",
  "message": "success",
  "data": [
    {
      "id": 1,
      "title": "족발",
      "content": "정말 맛있어요~",
      "score": 5,
      "user": {
        "id": 1,
        "email": "ssafy@naver.com",
        "nickname": "김싸피",
        "introduction": "한줄로 자신을 소개해주세요.",
        "profilePath": "",
        "createdDate": "2021-04-22T09:54:43",
        "modifiedDate": "2021-04-22T09:54:43"
      },
      "place": {
        "id": 1,
        "address": "대전 유성구 덕명동",
        "phone": "010-8282-5353",
        "name": "대전 족발",
        "locationY": 213.453532,
        "locationX": 24.25235
      },
      "videoPath": "http://k4b206.p.ssafy.io/resources/media/1620693696005my_video.mp4",
      "thumbnailPath": "http://k4b206.p.ssafy.io/resources/media/1620693696005my_video_thumbnail.png",
      "isCompleted": true,
      "likeCount": 2,
      "isLike": false,
      "createdDate": "2021-04-26T16:00:31",
      "modifiedDate": "2021-04-26T16:00:31"
    }
	]
}**
```

### 7. 유저 좋아요 피드 목록 조회

```kotlin
GET ("/feed/list/like/{userId}")
```

```kotlin
1) **성공
{
  "status": "200",
  "message": "success",
  "data": [
    {
      "id": 1,
      "title": "족발",
      "content": "정말 맛있어요~",
      "score": 5,
      "user": {
        "id": 1,
        "email": "ssafy@naver.com",
        "nickname": "김싸피",
        "introduction": "한줄로 자신을 소개해주세요.",
        "profilePath": "",
        "createdDate": "2021-04-22T09:54:43",
        "modifiedDate": "2021-04-22T09:54:43"
      },
      "place": {
        "id": 1,
        "address": "대전 유성구 덕명동",
        "phone": "010-8282-5353",
        "name": "대전 족발",
        "locationY": 213.453532,
        "locationX": 24.25235
      },
      "videoPath": "http://k4b206.p.ssafy.io/resources/media/1620693696005my_video.mp4",
      "thumbnailPath": "http://k4b206.p.ssafy.io/resources/media/1620693696005my_video_thumbnail.png",
      "isCompleted": true,
      "likeCount": 2,
      "isLike": false,
      "createdDate": "2021-04-26T16:00:31",
      "modifiedDate": "2021-04-26T16:00:31"
    }
	]
}**
```

### 8. 피드 추천 목록 조회

```kotlin
GET ("/feed/recommend/{userId}")
```

```kotlin
1) **성공
{
  "status": "200",
  "message": "success",
  "data": [
    {
      "id": 1,
      "title": "족발",
      "content": "정말 맛있어요~",
      "score": 5,
      "user": {
        "id": 1,
        "email": "ssafy@naver.com",
        "nickname": "김싸피",
        "introduction": "한줄로 자신을 소개해주세요.",
        "profilePath": "",
        "createdDate": "2021-04-22T09:54:43",
        "modifiedDate": "2021-04-22T09:54:43"
      },
      "place": {
        "id": 1,
        "address": "대전 유성구 덕명동",
        "phone": "010-8282-5353",
        "name": "대전 족발",
        "locationY": 213.453532,
        "locationX": 24.25235
      },
      "videoPath": "http://k4b206.p.ssafy.io/resources/media/1620693696005my_video.mp4",
      "thumbnailPath": "http://k4b206.p.ssafy.io/resources/media/1620693696005my_video_thumbnail.png",
      "isCompleted": true,
      "likeCount": 2,
      "isLike": false,
      "createdDate": "2021-04-26T16:00:31",
      "modifiedDate": "2021-04-26T16:00:31"
    }
	]
}**
```

### 9. 단일 피드 조회

```kotlin
GET ("/feed/{feedId}/{userId}")
```

```kotlin
1) **성공
{
  "status": "200",
  "message": "success",
  "data": {
    "id": 1,
    "title": "족발",
    "content": "정말 맛있어요~",
    "score": 5,
    "user": {
      "id": 1,
      "email": "ssafy@naver.com",
      "nickname": "김싸피",
      "introduction": "한줄로 자신을 소개해주세요.",
      "profilePath": "",
      "createdDate": "2021-04-22T09:54:43",
      "modifiedDate": "2021-04-22T09:54:43"
    },
    "place": {
      "id": 1,
      "address": "대전 유성구 덕명동",
      "phone": "010-8282-5353",
      "name": "대전 족발",
      "locationY": 213.453532,
      "locationX": 24.25235
    },
    "videoPath": "http://k4b206.p.ssafy.io/resources/media/1620693696005my_video.mp4",
    "thumbnailPath": "http://k4b206.p.ssafy.io/resources/media/1620693696005my_video_thumbnail.png",
    "isCompleted": true,
    "likeCount": 2,
    "isLike": false,
    "createdDate": "2021-04-26T16:00:31",
    "modifiedDate": "2021-04-26T16:00:31"
  }
}**
```

### 10. 피드 삭제

```kotlin
DELETE ("/feed/{feedId}")
```

```kotlin
1) **성공
{
  "status": "200",
  "message": "success",
  "data": null
}**
```

### 11. 피드 좋아요

```kotlin
POST ("/feed/like")

{
	"feedId": 1,
	"userId": 1
}
```

```kotlin
1) **성공
{
  "status": "200",
  "message": "success",
  "data": null
}**
```

### 12. 피드 좋아요 취소

```kotlin
DELETE ("/feed/like/{feedId}/{userId}")
```

```kotlin
1) **성공
{
  "status": "200",
  "message": "success",
  "data": null
}**
```

# AI

### 1. 동영상으로 음식 식별 (단일 결과)

```kotlin
POST ("/video")

- formData 형식으로 전송

"video": 동영상 파일

```

```kotlin
1) **성공
{
    "predictions": "후라이드치킨",
    "success": true
}**
```

### 2. 동영상으로 음식 식별 (리스트 결과)

```kotlin
POST ("/video/list")

- formData 형식으로 전송

"video": 동영상 파일
```

```kotlin
1) **성공
{
    "predictions": [
			"후라이드치킨",
			"양념치킨",
		],
    "success": true
}**
```

