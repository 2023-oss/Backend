<h1 align="center"> :mag: Verifier </h1>
<p align="center">
<img src="https://img.shields.io/github/contributors/2023-oss/OSS-BACKEND">
<img src="https://img.shields.io/github/languages/count/2023-oss/BACKEND">
<img alt="GitHub license" src="https://img.shields.io/github/issues/2023-oss/OSS-BACKEND">
<img alt="GitHub license" src="https://img.shields.io/github/issues-closed/2023-oss/OSS-BACKEND">
<img src="https://img.shields.io/github/license/2023-oss/OSS-BACKEND">
</p>
<br/>
 CustomSign 서비스를 이용하는 기업들을 위한 웹서버입니다.

<br/>
<br/>

[English Document](https://github.com/2023-oss/OSS-BACKEND/blob/main/EnglishREADME.md)

<br/>

## Service
1. 기업은 CustomSign에서 동의서 양식을 저장합니다.
2. 저장한 동의서 양식을 QR코드로 사용자에게 전송할 수 있습니다.
3. 사용자가 동의서를 작성한 후 제출하면 사용자가 직접 작성한 동의서인지 인증하는 과정을 거친 후 저장됩니다.
4. 저장된 동의서는 페이징 처리되어 목록 형태로 조회 가능하고, 
	특정 사용자의 동의서를 조회하고 싶은 경우에는 동의서 고유 ID를 통해 조회할 수 있기 때문에 사용자의 개인정보가 노출되지 않습니다. 

## 설치 및 실행 방법
아래의 환경을 권장합니다.
| service | version |
|--------- |--------|
|**SpringBoot**|v2.7.x|
|**Java**|v11|
|**MySQL**|v8.x.x|
|AWS RDS | RDB_service|
|AWS S3 | Storage_service|

### 실행 방법
```
$ cd $REPOSITORY/$PROJECT_NAME/
$ ./gradlew build
$ cp $REPOSITORY/$PROJECT_NAME/build/libs/*.jar $REPOSITORY/
$ java -jar $REPOSITORY/$JAR_NAME
```


## 인증 과정

### step1 - Holder가 VP(Verifiable Presentation)과 동의서를 제출

- `Verifier`는 Holder로 부터 VP(Verifiable Presentation)와 동의서를 전달
- `VP`는 Holder의 VC(Verifiable Credentail)를 자신의 privateKey로 암호화한 jws와 VC, 그리고 블록체인으로 부터 privateKey와 쌍을 이루는 publicKey를 요청할 수 있는 did를 포함
- 
### step2 - Holder 디지털 서명 확인
- `Holder`의 VP로부터 did를 추출한 후 블록체인 서버에 해당 did에 맞는 publicKey를 요청
- `Holder`의 privateKey로 암호화된 jws를 publicKey로 복호화한 후 VC와 동일한지 확인함으로써 Holder가 동의서를 직접 작성하였음을 증명
- `디지털 서명` 확인에 성공하면 동의서를 제출 가능
<br/>

### Verifier Flow Chart
<center>
	<img src="https://user-images.githubusercontent.com/83829352/265958095-5154bdc4-761a-42f2-9cb3-987d3aeddeb7.png" width=500px/>
</center>

## DB Schema
<center>
	<img src="https://user-images.githubusercontent.com/83829352/265958498-b3027dfa-ddc9-4542-ad1e-10fe4c0b2e52.png"/>
</center>

## API 명세서
아래의 링크를 통해 확인해주세요.
<br/>
[Verifier API](https://documenter.getpostman.com/view/26390728/2s9XxsVbmR)


