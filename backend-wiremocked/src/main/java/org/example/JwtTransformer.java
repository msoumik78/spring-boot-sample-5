package org.example;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ResponseDefinitionTransformer;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import wiremock.org.apache.commons.codec.binary.Base64;
import wiremock.org.apache.commons.io.IOUtils;

import javax.xml.xpath.XPathConstants;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Date;

import static com.nimbusds.jose.JWSAlgorithm.RS512;
import static java.util.Date.from;
import static org.example.CustomWiremockServer.USER_JWT_TRANSFORMER_NAME;

public class JwtTransformer extends ResponseDefinitionTransformer {

    public static final String USER_PRIVATE_RSA_KEY = "/mock_user_private_rsa_key";
    public static final String USER_PUBLIC_RSA_KEY = "/mock_user_public_rsa_key";



    @Override
    public ResponseDefinition transform(Request request, ResponseDefinition responseDefinition, FileSource fileSource, Parameters parameters) {
        try {
            return new ResponseDefinitionBuilder().withStatus(200).withBody(generateJWT()).build();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getName() {
        return USER_JWT_TRANSFORMER_NAME;
    }

    @Override
    public boolean applyGlobally() {
        return false;
    }



    private String generateJWT() throws JOSEException {
        final JWTClaimsSet.Builder claimsBuilder = new JWTClaimsSet.Builder().issueTime(new Date())
                .issueTime(from(Instant.now()))
                .claim("userType", "CUSTOMER")
                .claim("userId", "123456")
                .issuer("https://xyz.com");
        SignedJWT signedJWT = new SignedJWT(new JWSHeader.Builder(RS512).keyID("keyId").build(),
                claimsBuilder.build());
        signedJWT.sign(new RSASSASigner(getRsaPrivateKey(USER_PRIVATE_RSA_KEY)));
        return signedJWT.serialize();
    }

    private static RSAPrivateKey getRsaPrivateKey(String privateRsaKey) {
        try {
            final String key = IOUtils.resourceToString(privateRsaKey, Charset.defaultCharset());
            String publicKeyPEM =
                    key.replace("-----BEGIN PRIVATE KEY-----", "")
                            .replaceAll(System.lineSeparator(), "")
                            .replace("-----END PRIVATE KEY-----", "");
            byte[] encoded = Base64.decodeBase64(publicKeyPEM);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static RSAPublicKey getRsaPublicKey(String publicRsaKey) {
        try {
            final String key = IOUtils.resourceToString(publicRsaKey, Charset.defaultCharset());
            String publicKeyPEM =
                    key.replace("-----BEGIN PUBLIC KEY-----", "")
                            .replaceAll(System.lineSeparator(), "")
                            .replace("-----END PUBLIC KEY-----", "");
            byte[] encoded = Base64.decodeBase64(publicKeyPEM);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | IOException e) {
            throw new IllegalArgumentException(e);
        }
    }


}
