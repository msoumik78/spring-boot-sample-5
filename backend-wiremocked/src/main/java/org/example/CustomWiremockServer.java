package org.example;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

public class CustomWiremockServer {
    public static void main(String[] args) {
        WireMockConfiguration options = new WireMockConfiguration();
        options.usingFilesUnderClasspath("wiremock");
        options.jettyAcceptors(200);
        options.asynchronousResponseThreads(200);
        options.asynchronousResponseEnabled(true);
        options.containerThreads(300);
        WireMockServer wm = new WireMockServer(options.port(2345));
        wm.start();
        System.out.println(" /$$       /$$ /$$                     /$$      /$$                     /$$      \n| $$  /$  | $$|__/                    | $$$    /$$$                    | $$      \n| $$ /$$$ | $$ /$$  /$$$$$$   /$$$$$$ | $$$$  /$$$$  /$$$$$$   /$$$$$$$| $$   /$$\n| $$/$$  $$ $$| $$ /$$__  $$ /$$__  $$| $$ $$/$$ $$ /$$__  $$ /$$_____/| $$  /$$/\n| $$$$_   $$$$| $$| $$  \\__/|$$$$$$$$| $$  $$$| $$| $$  \\$$| $$      | $$$$$$/ \n| $$$/ \\  $$$| $$| $$      | $$_____/| $$\\  $ | $$|$$  | $$| $$      | $$_  $$ \n| $$/   \\  $$| $$| $$      |  $$$$$$$| $$ \\/  | $$| $$$$$$/|  $$$$$$$| $$ \\  $$\n|__/     \\__/|__/|__/       \\_______/|__/     |__/ \\______/  \\_______/|__/  \\__/");
        System.out.println();
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
