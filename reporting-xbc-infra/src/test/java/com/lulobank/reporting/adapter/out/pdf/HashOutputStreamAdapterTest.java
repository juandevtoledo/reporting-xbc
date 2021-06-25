package com.lulobank.reporting.adapter.out.pdf;

import com.google.common.hash.Hashing;
import com.lulobank.reporting.kernel.exception.BuildHashFileException;
import org.junit.Before;
import org.junit.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Map;

public class HashOutputStreamAdapterTest {

    private HashOutputStreamAdapter target;

    @Before
    public void setup() {
        target = new HashOutputStreamAdapter(Map.of(
                "MD5", Hashing.md5(),
                "SHA-1", Hashing.sha1(),
                "SHA_256", Hashing.sha256()));
    }

    @Test
    public void byteArrayOutputStreamIsFail() {
        Mono<ByteArrayOutputStream> response = target.generateTxtByContract(null);
        StepVerifier.create(response).expectError(BuildHashFileException.class).verifyThenAssertThat();
    }

    @Test
    public void byteArrayOutputStreamIsSuccess() {
        String hashSha1 = "SHA-1:da39a3ee5e6b4b0d3255bfef95601890afd80709";
        String hashSha256 = "SHA_256:e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";
        String hashMD5 = "MD5:d41d8cd98f00b204e9800998ecf8427e";
        Mono<ByteArrayOutputStream> response = target.generateTxtByContract(new ByteArrayOutputStream());
        StepVerifier.create(response).expectNextMatches(r -> r.toString().contains(hashSha1)&&
                        r.toString().contains(hashSha256)&&
                        r.toString().contains(hashMD5)
                ).verifyComplete();
    }
}
