# Verifier
실제 customSign 서비스를 이용하는 기업들을 위한 서버입니다.<br/><br/>
## Verifier 인증 과정

### step1 - Holder가 VP(Verifiable Presentation)과 동의서를 제출

- `Verifier`는 Holder로 부터 VP(Verifiable Presentation)와 동의서를 전달 받는다.
- `VP`는 Holder의 VC(Verifiable Credentail)를 자신의 privateKey로 암호화한 jws와 VC, 그리고 블록체인으로 부터 privateKey와 쌍을 이루는 publicKey를 요청할 수 있는 did를 포함한다.
<br/>
### step2 - Holder 디지털 서명 확인
- `Holder`의 VP로부터 did를 추출한 후 블록체인 서버에 해당 did에 맞는 publicKey를 요청
- `Holder`의 privateKey로 암호화된 jws를 publicKey로 복호화한 후 VC와 동일한지 확인함으로써 Holder가 동의서를 직접 작성하였음을 증명한다.
- `디지털 서명` 확인에 성공하면 동의서를 제출할 수 있다.<br/>
<br/>
### Verifier Flow Chart
![](https://user-images.githubusercontent.com/83829352/265958095-5154bdc4-761a-42f2-9cb3-987d3aeddeb7.png)
<br/>
## DB Schema
![](https://user-images.githubusercontent.com/83829352/265958498-b3027dfa-ddc9-4542-ad1e-10fe4c0b2e52.png)
<br/>
## API 명세서
아래의 링크를 통해 확인해주세요.
<br/>
https://documenter.getpostman.com/view/26390728/2s9XxsVbmR


<br/>
### Stack
|                             Icon                              |   Stack   | Description                                      |
| :-----------------------------------------------------------: | :-------: | ------------------------------------------------ |
|  <img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white">    |  SPRINGBOOT   | API 서버 2대 제작                                |
|  <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">   |  MYSQL  |   AWS의 RDS MySQL                          |

### Deploy
|                               Icon                                |        Stack        | Description                        |
| :---------------------------------------------------------------: | :-----------------: | ---------------------------------- |
|   <img src="https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white">   |       DOCKER        | 컨테이너 생성, 배포 시 이미지 생성 |
|      <img src="https://img.shields.io/badge/amazonec2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white">     |         EC2         | 배포 서버                          |
