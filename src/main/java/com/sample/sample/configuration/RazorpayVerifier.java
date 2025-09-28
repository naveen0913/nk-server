package com.sample.sample.configuration;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public class RazorpayVerifier {

        public static boolean verifySignature(String payload, String secret, String expectedSignature) {
            try {
                Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
                SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
                sha256_HMAC.init(secret_key);
                byte[] hash = sha256_HMAC.doFinal(payload.getBytes(StandardCharsets.UTF_8));
                String actual = bytesToHex(hash); // lowercase hex
                return actual.equals(expectedSignature);
            } catch (Exception e) {
                throw new RuntimeException("Unable to verify signature", e);
            }
        }

        private static String bytesToHex(byte[] bytes) {
            StringBuilder sb = new StringBuilder(bytes.length * 2);
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        }


}
