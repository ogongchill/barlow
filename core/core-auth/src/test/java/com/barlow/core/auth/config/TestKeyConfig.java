package com.barlow.core.auth.config;

import com.barlow.core.support.crypto.PrivateKeyAlgorithm;
import com.barlow.core.support.crypto.PublicKeyAlgorithm;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.security.spec.InvalidKeySpecException;

@TestConfiguration
public class TestKeyConfig {

    private static final String PRIVATE_KEY = """
            -----BEGIN PRIVATE KEY-----
            MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDxusvbZGhvt7nA
            +TkIn11Fxl4AhWZe8JUjin7fWu/1IST28kfoDex9/D6gId/cEneYz4oogGRzt/Ma
            JVRP4LqOgslPoWhl81OKQvASXibX4TS/O+WFCT7JwhcnR8K5ixoU7rNqtocKaDOO
            OyAE3a5QXFn4YBPBl4YDwvGBSvTyg6Zv/vwQ/E4ERBGafNM4OMKXZTqr5wuTopC0
            fK3EETYhlfEe8V8FeKO9H36DLpADcE4Ol5UKTBzHDLylzmtwr3b6LMeMNT2kgENB
            IAxqvB4d1uxH0PdQqJ8b/Lw43QNezKshI1vsRSyNDA2vUyoGI/hEbmpCtL1SI/Ev
            HL6RvDBzAgMBAAECggEAWxrH0jwZkGFyROSpNjK5+J42t4OPfaC21NpRzuR7I6wG
            Pnw51Z/JzgHPqJKbyFm9cX5m17TsJyDhIq2HBUPxi9yG+tgefK4kgiuQR5vsj9h1
            BM4SRv2hCHF6QHevUDa48cK1JDb+1lFHfQeTncHe/LKRxiaPy5rMC4bhdpVFlFCG
            Wq7gaPQkA9US0in4k3w1WQBMK1uDU8uQfNxmRoXfTnt3npDbYanrrOINkUTqyqG1
            cjRzCgHqKtd/mX7XH7f5I1EekYePF9U+T+I6FPznfQn4t+8Kf0fhiVwcgSePRm8g
            RJul/YAJwOpr6hnsP2r7Qynr7a77KMNYloVL4ZKiAQKBgQD49FmfQExAnGAlV+an
            UKTNF5OwI01coSESwgge6FNjcIhyMPOOQ6j6zWx4PHd7zsIMzAKeSUvwvMMp7zvz
            ZyFoe2GP9XiJkdFM1diFAll+Fc3wBIin2++yVuT7s9NojVUzXnjSkpZOY+aKSTH/
            qX+mqab6bB8ypDzqMfodUxd5wQKBgQD4khpoDUr4DjTco3bs9QCHC0Oz9AUdBisM
            2L4SNPTcDq0rFN+n2pbZaZud+ej+POzwFlO9BQXEWN+fuHQy0XjniFjbm9B44MPg
            WhxEtfZ1JOp0WG2jbLzldeRTTrpAK+brQ4RTxnzrVZZPfR6SHbem0agQHfGcLVEa
            Xx8KJNSvMwKBgHrZeXBQdlBb2kYTPkrKTMi4HbUJdp+DE6MCBKfb1Wp4mZxf84/8
            a2J4BFLA4+VAfhl6gsBBQywkudnzqA3QknfQirBTXYvlXPchf5sDMc46TElos1bB
            /WJv5sceUfCB7c1PjSe8FlfQjG8o93dF2SwRA8kHmc2Ppk+bIeVen3MBAoGBAJsa
            n7uZv9P1GWsr3QOTBZc+oiph8+beeHJ1tHSr8rZ1ufyN89lLfqF/UolK7eXmonpV
            lD74KVcRlciWUnt7VhQzci7mlTk4F0GhOM5vNLB4LowWuaMmMOCFpcmN8I4mVtsq
            sCGOU1iNeVN0YWUPgg9n95TG+oJXjKoF/NpZjRUdAoGBAL9A7zjSuoiQjTq03D2f
            hBI1m3v1qYv1o/PpRtDwYRpxEO3Gg9xvGNkYxiZXwQZ9obxgG6qS1X4xHf4/k1f/
            PNWRCLuF/9tu0LlzRfZuRKfV5MBgaW2md5ywbpsOYahDb+3ZkVszuyswF4Z0D0wB
            atccNVC/FIfioqTEHWKD3PIj
            -----END PRIVATE KEY-----
            """;

    private static final String PUBLIC_KEY = """
            -----BEGIN PUBLIC KEY-----
            MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA8brL22Rob7e5wPk5CJ9d
            RcZeAIVmXvCVI4p+31rv9SEk9vJH6A3sffw+oCHf3BJ3mM+KKIBkc7fzGiVUT+C6
            joLJT6FoZfNTikLwEl4m1+E0vzvlhQk+ycIXJ0fCuYsaFO6zaraHCmgzjjsgBN2u
            UFxZ+GATwZeGA8LxgUr08oOmb/78EPxOBEQRmnzTODjCl2U6q+cLk6KQtHytxBE2
            IZXxHvFfBXijvR9+gy6QA3BODpeVCkwcxwy8pc5rcK92+izHjDU9pIBDQSAMarwe
            HdbsR9D3UKifG/y8ON0DXsyrISNb7EUsjQwNr1MqBiP4RG5qQrS9UiPxLxy+kbww
            cwIDAQAB
            -----END PUBLIC KEY-----
            """;

    @Bean
    public PublicKeyAlgorithm publicKeyAlgorithm() {
        try {
            return PublicKeyAlgorithm.from(PUBLIC_KEY);
        } catch (InvalidKeySpecException e) {
            throw new BeanCreationException("public key generation failed",e);
        }
    }

    @Bean
    public PrivateKeyAlgorithm privateKeyAlgorithm() {
        try {
            return PrivateKeyAlgorithm.from(PRIVATE_KEY);
        } catch (InvalidKeySpecException e) {
            throw new BeanCreationException("private key generation failed",e);
        }
    }
}
