package com.example.TradeBoot.api.http.auntification;

import javax.crypto.Mac;
import java.security.NoSuchAlgorithmException;

public interface HashAlgorithm {

    String getAlgorithmName();

    Mac CreateInstance() throws NoSuchAlgorithmException;

    abstract class Abstract implements HashAlgorithm {
        public Abstract(String algorithmName) {
            this.algorithmName = algorithmName;
        }

        public String getAlgorithmName() {
            return algorithmName;
        }

        public Mac CreateInstance() throws NoSuchAlgorithmException {
            return Mac.getInstance(algorithmName);
        }

        private final String algorithmName;

    }

    class HmacSHa256 extends Abstract {
        public HmacSHa256() {
            super("HmacSHA256");
        }
    }
}
