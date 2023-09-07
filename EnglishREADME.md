
<h1 align="center"> :mag: Verifier </h1>
<p align="center">
<img src="https://img.shields.io/github/contributors/2023-oss/OSS-BACKEND">
<img src="https://img.shields.io/github/languages/count/2023-oss/BACKEND">
<img alt="GitHub license" src="https://img.shields.io/github/issues/2023-oss/OSS-BACKEND">
<img alt="GitHub license" src="https://img.shields.io/github/issues-closed/2023-oss/OSS-BACKEND">
<img src="https://img.shields.io/github/license/2023-oss/OSS-BACKEND">
</p>
<br/>
 It is a web server for companies that use CustomSign services.
<br/>

[한국어 버전](https://github.com/2023-oss/OSS-BACKEND/blob/main/README.md) 

<br/>

## Service
1. The company saves the consent form from CustomSign.
2. The company can send the saved consent form to the user by QR code.
3. When a user submits a consent form after completing it, it is stored after authenticating whether it is a consent form created by the user.
4. The stored consent form is paged and can be inquired in the form of a list, 
	If you want to inquire a specific user's consent form, your personal information is not exposed because you can inquire through a consent form unique ID.

## How to install and run
recommends the environment below.
| service | version |
|--------- |--------|
|**SpringBoot**|v2.7.x|
|**Java**|v11|
|**MySQL**|v8.x.x|
|AWS RDS | RDB_service|
|AWS S3 | Storage_service|

### How to run
```
$ cd $REPOSITORY/$PROJECT_NAME/
$ ./gradlew build
$ cp $REPOSITORY/$PROJECT_NAME/build/libs/*.jar $REPOSITORY/
$ java -jar $REPOSITORY/$JAR_NAME
```


## Certification Process

### step1 - Holder submits consent form with VP(Verifiable Presentation)

- `Verifier` receives VP (Verifiable Presentation) and consent form from Holder.
- `VP` includes jws and VCs that encrypt Holder's VC (Verifiable Creditail) with their private key, and a did that can request a public key paired with a private key from the blockchain.

### step2 - Check Holder's digital signature
- After extracting the did from the VP of `Holder` , request the blockchain server for the public key that matches the did  
- It is proved that Holder wrote the consent form by decrypting jws encrypted with 'Holder's private key into public key and checking whether it is the same as `VC`.  
- If you succeed in verifying the `digital signature`, you can submit a consent form.<br/>

### Verifier Flow Chart
<center>
	<img src="https://user-images.githubusercontent.com/83829352/265958095-5154bdc4-761a-42f2-9cb3-987d3aeddeb7.png" width=500px/>
</center>

## DB Schema
<center>
	<img src="https://user-images.githubusercontent.com/83829352/265958498-b3027dfa-ddc9-4542-ad1e-10fe4c0b2e52.png"/>
</center>

## API 명세서
Please check the link below.
<br/>
[Verifier API](https://documenter.getpostman.com/view/26390728/2s9XxsVbmR)


