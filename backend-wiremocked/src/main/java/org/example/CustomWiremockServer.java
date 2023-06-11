package org.example;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import wiremock.org.apache.hc.core5.http.ContentType;
import wiremock.org.apache.hc.core5.http.HttpHeaders;
import wiremock.org.apache.hc.core5.http.HttpStatus;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.example.JwtTransformer.USER_PUBLIC_RSA_KEY;
import static org.example.JwtTransformer.getRsaPublicKey;

public class CustomWiremockServer {

    public static final String USER_JWT_TRANSFORMER_NAME = "user-jwt-transformer";


    public static void main(String[] args) {
        WireMockConfiguration options = new WireMockConfiguration();
        options.usingFilesUnderClasspath("wiremock");
        options.jettyAcceptors(200);
        options.asynchronousResponseThreads(200);
        options.asynchronousResponseEnabled(true);
        options.containerThreads(300);
        options.extensions(new JwtTransformer());
        WireMockServer wm = new WireMockServer(options.port(2345));
        wm.stubFor(
                get(urlPathMatching("/user-jwt"))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withBody("Original body")
                                        .withTransformers(USER_JWT_TRANSFORMER_NAME)));

        RSAKey jwk = new RSAKey.Builder(getRsaPublicKey(USER_PUBLIC_RSA_KEY)).keyID("keyId").build();
        JWKSet jwkSet = new JWKSet(jwk);
        wm.stubFor(
                get("/user-jwk")
                        .atPriority(1)
                        .willReturn(
                                aResponse()
                                        .withHeader(
                                                HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType())
                                        .withStatus(HttpStatus.SC_OK)
                                        .withBody(jwkSet.toString())));


        wm.start();
        System.out.println("******************************************************************************");
        System.out.println(" Custom WireMock Server ");
        System.out.println("******************************************************************************");        System.out.println();
        System.out.println("Files root: " + options.filesRoot().getUri().toASCIIString());
        System.out.println("Port: " + options.portNumber());
        System.out.println("RequestJournalDisabled: " + options.requestJournalDisabled());
        System.out.println("MaxrequestJournalEntries: " + options.maxRequestJournalEntries());
        System.out.println("Container-threads: " + options.containerThreads());
        System.out.println("jetty-acceptor-threads: " + options.jettySettings().getAcceptors());
        System.out.println("jetty-accept-queue-size: " + options.jettySettings().getAcceptQueueSize());
        System.out.println("async-response-enabled: " + options.getAsynchronousResponseSettings().isEnabled());
        System.out.println("async-response-threads:" + options.getAsynchronousResponseSettings().getThreads());
        System.out.println();

    }




}
