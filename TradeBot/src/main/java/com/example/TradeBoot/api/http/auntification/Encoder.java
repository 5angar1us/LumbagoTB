package com.example.TradeBoot.api.http.auntification;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

@SuppressWarnings("ALL")
public class Encoder {

    public Encoder(HashAlgorithm hashAlgorithm) {
        this.hashAlgorithm = hashAlgorithm;
    }

    private final HashAlgorithm hashAlgorithm;

    public String encode(String signaturePayload, SecretKeySpec secretKey) {

        try {
            Mac sha256_HMAC = this.hashAlgorithm.CreateInstance();
            sha256_HMAC.init(secretKey);
            sha256_HMAC.doFinal(signaturePayload.getBytes(StandardCharsets.UTF_8));
            var hash = sha256_HMAC.doFinal(signaturePayload.getBytes(StandardCharsets.UTF_8));
            var hashString = Hex.encodeHexString(hash);
            var signature = hashString.toLowerCase();

            return signature;

        } catch (NoSuchAlgorithmException | java.security.InvalidKeyException e) {
            RuntimeException error_during_encode_signature = new RuntimeException("Error during encode signature");
            error_during_encode_signature.addSuppressed(e);
            throw error_during_encode_signature;
        }
    }

    public String getHashAlgorithmName() {
        return hashAlgorithm.getAlgorithmName();
    }
}
