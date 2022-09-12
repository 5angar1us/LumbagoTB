package com.example.TradeBoot.api.http.auntification;

import com.example.TradeBoot.api.http.SignaturePayloadBuilder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
public class Auntification {

    public Auntification(Encoder encoder, TimeKeper timeKeper) {
        this.encoder = encoder;
        this.timeKeper = timeKeper;
    }

    public void Init(String apiKey, String secretKey, Optional<String> subAccountName) {
        this.apiKey = apiKey;
        this.secretKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), encoder.getHashAlgorithmName());
        this.subAccountName = subAccountName;
    }

    public AuntificationData CreateAuthInformation(SignaturePayloadBuilder signaturePayloadBuilder) {
        return new AuntificationData(
                this.apiKey,
                CreateSignature(signaturePayloadBuilder),
                timeKeper.getLastTimeMillis()
        );
    }

    private String apiKey;
    private SecretKeySpec secretKey;

    public Optional<String> getSubAccountName() {
        return subAccountName;
    }

    private Optional<String> subAccountName;

    private Encoder encoder;
    private TimeKeper timeKeper;

    private String CreateSignature(SignaturePayloadBuilder signaturePayloadBuilder) {
        long time = timeKeper.currentTimeMillis();
        String signaturePayload = signaturePayloadBuilder.CreateSignaturePayload(time);
        return encoder.encode(signaturePayload, this.secretKey);
    }

    private String CreateSignaturePayload(long timeStamp, String httpMethodName, String uri, String body) {
        return timeStamp + httpMethodName + uri + body;
    }


    public class AuntificationData {
        public AuntificationData(String apiKey, String signature, long timeStamp) {
            this.apiKey = apiKey;
            this.signature = signature;
            TimeStamp = timeStamp;
        }

        public String getApiKey() {
            return apiKey;
        }

        public String getSignature() {
            return signature;
        }

        public long getTimeStamp() {
            return TimeStamp;
        }

        private final String apiKey;
        private final String signature;
        private final long TimeStamp;
    }
}
