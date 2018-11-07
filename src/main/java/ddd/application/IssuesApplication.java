package ddd.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication(scanBasePackages = "ddd")
@EntityScan(basePackages = "ddd")
public class IssuesApplication {

    public static void main(String[] args) {

        SpringApplication application = new SpringApplication(IssuesApplication.class);
        application.setAdditionalProfiles("demo");
        application.run();
    }

}