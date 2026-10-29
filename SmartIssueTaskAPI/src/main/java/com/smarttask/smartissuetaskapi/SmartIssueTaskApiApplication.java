package com.smarttask.smartissuetaskapi;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SmartIssueTaskApiApplication {

    private static final Logger log =
            LoggerFactory.getLogger(SmartIssueTaskApiApplication.class);

    public static void main(String[] args) {
        String dbUrl = System.getenv("DB_URL");

        log.info("DATABASE PROBE: DB_URL present={}",
                dbUrl != null && !dbUrl.isBlank());

        if (dbUrl != null && dbUrl.startsWith("jdbc:mysql://")) {
            try {
                URI uri = URI.create(dbUrl.substring("jdbc:".length()));
                String host = uri.getHost();
                int port = uri.getPort() == -1 ? 3306 : uri.getPort();

                log.info("DATABASE PROBE: host={} port={}", host, port);

                try (Socket socket = new Socket()) {
                    socket.connect(new InetSocketAddress(host, port), 10000);

                    log.info(
                            "DATABASE PROBE SUCCESS: TCP connection to {}:{} established",
                            host, port);
                }
            } catch (Exception e) {
                log.error("DATABASE PROBE FAILED: {}",
                        e.getClass().getName(), e);
            }
        } else {
            log.warn(
                    "DATABASE PROBE SKIPPED: DB_URL is missing or does not start with jdbc:mysql://");
        }

        SpringApplication.run(SmartIssueTaskApiApplication.class, args);
    }
}