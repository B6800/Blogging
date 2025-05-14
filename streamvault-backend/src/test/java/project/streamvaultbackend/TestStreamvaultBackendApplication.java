package project.streamvaultbackend;

import org.springframework.boot.SpringApplication;

public class TestStreamvaultBackendApplication {

    public static void main(String[] args) {
        SpringApplication.from(StreamvaultBackendApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
