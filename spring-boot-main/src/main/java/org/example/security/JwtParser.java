package org.example.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.JWSAlgorithmFamilyJWSKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.util.DefaultResourceRetriever;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import lombok.RequiredArgsConstructor;
import org.example.config.JwkProperties;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;

@Component
public class JwtParser {

    private final DefaultJWTProcessor<SecurityContext> jwtProcessor;

    private final JwkProperties jwkProperties;

    public JwtParser(JwkProperties jwkPropertiesProvided) {
        jwkProperties = jwkPropertiesProvided;
        jwtProcessor = new DefaultJWTProcessor<>();
        final DefaultResourceRetriever resourceRetriever = new DefaultResourceRetriever(
                jwkProperties.getConnectTimeout(), jwkProperties.getReadTimeout());
        try {
            final JWKSource<SecurityContext> keySource = new RemoteJWKSet<>( //NOSONAR
                    new URL(jwkProperties.getUrl()), resourceRetriever);

            jwtProcessor.setJWSKeySelector(new JWSAlgorithmFamilyJWSKeySelector<>(
                    JWSAlgorithm.Family.RSA, keySource));
        } catch (final MalformedURLException e) {
            throw new IllegalStateException("Unable to access remote jwk server endpoint", e);
        }
    }

    public JWTClaimsSet parse(final String authUserHeader)  {
        if (jwtProcessor == null) {
            throw new RuntimeException(getClass().getSimpleName() + " wasn't configured and therefore shouldn't be used.", null);
        }
        try {
            final JWTClaimsSet claimsSet = jwtProcessor.process(authUserHeader, null);
            return claimsSet;
        } catch (ParseException | JOSEException | BadJOSEException ex) {
            throw new RuntimeException(ex.getLocalizedMessage(), ex);
        }
    }

}
