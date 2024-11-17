package com.hhplus.io.testcontainer;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Import(DBInitializer.class)
public abstract class AcceptanceTest {

    private static final String ROOT = "test";
    private static final String ROOT_PASSWORD = "1234";

    @LocalServerPort
    protected int port;

    @Autowired
    private DBInitializer dataInitializer;

    @Container
    protected static MySQLContainer mySqlContainer;
    @Container
    protected static GenericContainer redisContainer;
    @Container
    protected static KafkaContainer kafkaContainer;


    @DynamicPropertySource
    private static void configureProperties(final DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", () -> ROOT);
        registry.add("spring.datasource.password", () -> ROOT_PASSWORD);

        registry.add("spring.data.redis.host", redisContainer::getHost);
        registry.add("spring.data.redis.port", () -> 6379);

        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
    }


    static {
        mySqlContainer = new MySQLContainer("mysql:8")
                .withDatabaseName("test")
                .withUsername(ROOT)
                .withPassword(ROOT_PASSWORD);
        mySqlContainer.start();

        redisContainer = new GenericContainer("redis:latest");
        redisContainer.start();

        kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));
        kafkaContainer.start();
    }

    @BeforeEach
    void delete() {
        dataInitializer.clear();
        RestAssured.port = port;
    }
}
