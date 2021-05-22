## 1. Java 백엔드 실행

### Git 연동

```bash
$ git clone https://lab.ssafy.com/s04-final/s04p31b206.git
$ cd s04p31b206
```

### 프로젝트 빌드

```bash
$ cd yumyum-back
$ sudo chmod 777 ./gradlew
$ ./gradlew build
```

### 프로젝트 백그라운드 실행

```bash
$ screen -S api_back
$ cd yumyum-back
$ cd build
$ cd libs
$ java -jar yumyum-back-1.0-SNAPSHOT.jar
```

## 3. Python 백엔드 실행

### Git 연동

```bash
$ git clone https://lab.ssafy.com/s04-final/s04p31b206.git
$ cd s04p31b206
```

### 관련 라이브러리 설치

```bash
$ pip install flask
$ pip install tensorflow
$ pip install keras
$ pip install numpy
$ pip install pillow
```

### 프로젝트 백그라운드 실행

```bash
$ cd AI/yumyum-classify
$ python main.py
```