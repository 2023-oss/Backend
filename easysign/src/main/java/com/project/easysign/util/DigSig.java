package com.project.easysign.util;
import org.json.simple.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author metamug.com
 */
public class DigSig {

    private static final String SPEC = "SHA256withRSA";
    private static final String ALGO = "SHA256withRSA";

    private JSONObject sender() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, UnsupportedEncodingException, SignatureException {

        ECGenParameterSpec ecSpec = new ECGenParameterSpec(SPEC);
        KeyPairGenerator g = KeyPairGenerator.getInstance("EC");
        g.initialize(ecSpec, new SecureRandom());
        KeyPair keypair = g.generateKeyPair();
        PublicKey publicKey = keypair.getPublic();
        PrivateKey privateKey = keypair.getPrivate();

        String plaintext = "Hello";

        //...... sign
        Signature ecdsaSign = Signature.getInstance(ALGO);
        ecdsaSign.initSign(privateKey);
        ecdsaSign.update(plaintext.getBytes("UTF-8"));
        byte[] signature = ecdsaSign.sign();
        String pub = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        String sig = Base64.getEncoder().encodeToString(signature);
        System.out.println(sig);
        System.out.println(pub);

        JSONObject obj = new JSONObject();
        obj.put("publicKey", pub);
        obj.put("signature", sig);
        obj.put("message", plaintext);
        obj.put("algorithm", ALGO);

        return obj;
    }

    private boolean receiver(JSONObject obj) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, UnsupportedEncodingException, SignatureException {

        Signature ecdsaVerify = Signature.getInstance(obj.get("algorithm").toString());
        KeyFactory kf = KeyFactory.getInstance("EC");

        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(obj.get("publicKey").toString()));

        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        ecdsaVerify.initVerify(publicKey);
        ecdsaVerify.update(obj.get("message").toString().getBytes("UTF-8"));
        boolean result = ecdsaVerify.verify(Base64.getDecoder().decode(obj.get("signature").toString()));

        return result;
    }

    public static void main(String[] args){
        try {DigSig digiSig = new DigSig();
            JSONObject obj = digiSig.sender();
            boolean result = digiSig.receiver(obj);
            System.out.println(result);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(DigSig.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidAlgorithmParameterException ex) {
            Logger.getLogger(DigSig.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(DigSig.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(DigSig.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SignatureException ex) {
            Logger.getLogger(DigSig.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(DigSig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}