package com.project.easysign.util;

import com.project.easysign.exception.DecryptFailException;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.RSAPublicKeyStructure;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Service;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;

@Slf4j
@Service
public class RsaUtil {
    private static final String SPEC = "RSA";
    private static final String ALGO = "SHA256withRSA";

    public static boolean decrypt(String encryptedText, String decrypted, String stringPublicKey)  {
        boolean result = false;

        try {

            // PEM에서 DER 형식으로 변환
//            String pemContent = stringPublicKey.replace("-----BEGIN RSA PUBLIC KEY-----", "").replace("-----END RSA PUBLIC KEY-----", "").trim();
            byte[] der = Base64.getDecoder().decode(stringPublicKey);

            // DER에서 RSAPublicKeyStructure를 얻음
            ASN1Sequence seq = ASN1Sequence.getInstance(der);
            RSAPublicKeyStructure rsaStruct = new RSAPublicKeyStructure(seq);

            BigInteger modulus = rsaStruct.getModulus();
            BigInteger publicExponent = rsaStruct.getPublicExponent();

            // RSAPublicKeySpec를 사용하여 PublicKey 객체를 생성
            RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, publicExponent);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            log.info("publicKey");
            PublicKey publicKey = factory.generatePublic(spec);

            Signature ecdsaVerify = Signature.getInstance(ALGO);

            log.info("init verify");
            ecdsaVerify.initVerify(publicKey);

            log.info("update verify");
            ecdsaVerify.update(decrypted.getBytes("UTF-8"));

            log.info("verify");
            result = ecdsaVerify.verify(Base64.getDecoder().decode(encryptedText));
        } catch (SignatureException | NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | UnsupportedEncodingException e ) {
            throw new DecryptFailException();
        }

        return result;
    }
}
