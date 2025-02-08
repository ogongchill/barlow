package com.barlow.support.jwt.token;

import com.barlow.support.jwt.crypto.PrivateKeyAlgorithm;
import com.barlow.support.jwt.crypto.PublicKeyAlgorithm;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.security.spec.InvalidKeySpecException;

@TestConfiguration
public class TestKeyConfig {

    private static final String PRIVATE = """
            -----BEGIN PRIVATE KEY-----
            MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQDQMefUUvaCsG7Z
            lF/ET9fqNWwLRjx9TPJjqmLViX6cIK5t50k8X+IfEebRhLYeZBdVZNol3r6mc8oK
            kIVhhxIXYklGtVcbz1UJuqEzY7R4Q6DzoDWiOuDc25WPSJXfIfhCeuclDhRzemLK
            PowrBpxnnBREIaU283rXx2ZqXG/jrCB8q91AimskJXGs3GI/s7Cl2VH7I4f9y3MG
            /jQ1wdaYl6OykGAC0aA+X+O5B6qfMcIqkRQWhmsH/lEoSbgEtUZJ27fa4OaSzqZ9
            dKNfdUJkdSGFsnHOlQ81Thu+acitHeWvgxm9Ke/05yAxBSI5DkqXPD4b73FOiBCI
            DkGUt4OvAgMBAAECggEASOMv5oWlB9v88u+fF3tcfAJ38PqF8tgdhAcC0v9v8/j9
            GD0AxsJKx9XcmT19BObFxH+qX9e7p0qiUVCHYb2H9+fQ/5A9Dt9nRiCwju05IWGL
            178fB8zVLH0zvxYKYjPiVmPwyGhFgKg4LdX3/Jgz6ZIbBj9BOBvFxgVpNZNnGK1o
            BNhdDYEifrgxdsoIMemYr8ZYW5WthnCp5Gb/PoC4jGD0NMfTYAYEwKWjbwjwYtMS
            2ONZ5ZXSuLarV10KbqqB7UCIkecMQda0cD0oFcnglVV7i2+oqBd0KG4tGsyG1sz0
            nNZy2V781MMt2MZWq+IY/YyawnJVw3lkQ9yN2MSUAQKBgQD6SiA2bbN5yskef+Jr
            HPBYu5m/T3bo7EezZ5PQHcj3gMjy+AaT4aYgyk26GnwVCPhLttMs3XOy1xw247v4
            6XS7gMMjpHnOf+lOCcbG/C0Kmt5cJdaBAOMV9TffLLr5l6svX3q9w9h4plgQFYIw
            nsGRhDGIqX2arzqJnlTzAb5lHwKBgQDU8eqanl/ebWoRq2uENo8YytbeV5zPUAR1
            es8JV+Ie24ohqzrMwY5Ydhjmnx8MtT+lzi7gVuOs9d3F2VjiUrecA/sO1GYLHwCW
            22HldaXe5HpgUiqffVv30s47Cm88qK+joxmt6tZlO/4TAksSmrzHt5OKynz+Xibh
            RovDza3/cQKBgEVxIYTYZO5KpAhRRPmg+tIzopGgs4YmYDJXb3xTdzmsQpSe2fxX
            o+b/GhejV3/ikDk5jVeqTXp2Ej7sam/CHgAT1pBiBuWTOaDMN5lnx/tkQoNggCYk
            n9ysv4as+2dafEVVgVF7L+kxhcdYoFlrApukCLY5CD9NlLkv87PEbRXTAoGAWGRj
            AfOjz+yTDuqdIrCTDbOFJZ4wlU2dzDZCIpq8Xhq7Z+Zv7faaY9YB2BlNBjVmSikg
            BDeNIoqcmDf0UV4MoVJ1sF4yTYN89yHxrRH83Fzy/3CcCMA8oM58lL1sFz3eSoq1
            aiAFBti0Sqe1Ce/5VN26/O24nM3wTaq+8XQ7OcECgYAw1KEVveoivKFWnd2K5Oft
            dbwl2RAZNoP2liD8cd/HwzgK8OWW+zRBlN9N1jh3C6Ay5pfZEVlOo2qMOQBqj/ca
            LykJ1FamgSpq+N8Udmdh/DsYLcep/p9W+d3L79m46TZibA55dxeYISeOqx/ZE2vQ
            eYY4/6koefpxevfUUp7KIQ==
            -----END PRIVATE KEY-----
            """;

    private static final String PUBLIC = """
            -----BEGIN PUBLIC KEY-----
            MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0DHn1FL2grBu2ZRfxE/X
            6jVsC0Y8fUzyY6pi1Yl+nCCubedJPF/iHxHm0YS2HmQXVWTaJd6+pnPKCpCFYYcS
            F2JJRrVXG89VCbqhM2O0eEOg86A1ojrg3NuVj0iV3yH4QnrnJQ4Uc3piyj6MKwac
            Z5wURCGlNvN618dmalxv46wgfKvdQIprJCVxrNxiP7OwpdlR+yOH/ctzBv40NcHW
            mJejspBgAtGgPl/juQeqnzHCKpEUFoZrB/5RKEm4BLVGSdu32uDmks6mfXSjX3VC
            ZHUhhbJxzpUPNU4bvmnIrR3lr4MZvSnv9OcgMQUiOQ5Klzw+G+9xTogQiA5BlLeD
            rwIDAQAB
            -----END PUBLIC KEY-----
            """;

    @Bean
    PublicKeyAlgorithm publicKeyAlgorithm() {
        System.out.println("creating.......");
        try {
            return PublicKeyAlgorithm.from(PUBLIC);
        } catch (InvalidKeySpecException e) {
            throw new BeanCreationException("fail during create publicKey", e);
        }
    }

    @Bean
    PrivateKeyAlgorithm privateKeyAlgorithm() {
        try {
            return PrivateKeyAlgorithm.from(PRIVATE);
        } catch (InvalidKeySpecException e) {
            throw new BeanCreationException("fail during create privateKey", e);
        }
    }
}
