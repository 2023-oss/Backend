package com.project.easysign.util;

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.math.ec.custom.sec.SecP256K1Curve;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.security.auth.DestroyFailedException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
public class RsaUtil {
    public static String decrypt(String encryptedText, String stringPublicKey) throws DestroyFailedException {
        String decryptedText = null;
        Security.addProvider(new BouncyCastleProvider());

        try {
            // 평문으로 전달받은 공개키를 사용하기 위해 공개키 객체 생성
//            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] bytePublicKey = Base64.getDecoder().decode(stringPublicKey.getBytes());
//
//            PKCS8EncodedKeySpec publicKeySpec = new PKCS8EncodedKeySpec(bytePublicKey);
//            PrivateKey privateKey = keyFactory.generatePrivate(publicKeySpec);
            KeyFactory keyFactory = KeyFactory.getInstance("ECDSA", "BC");
            PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(bytePublicKey));

            // 만들어진 공개키 객체로 복호화 설정
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, publicKey);

            // 암호문을 평문화하는 과정
            byte[] encryptedBytes =  Base64.getDecoder().decode(encryptedText.getBytes());
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            decryptedText = new String(decryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DestroyFailedException();
        }

        return decryptedText;
    }

}
