package fan.yangyi.javacloudstorage;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JavaCloudStorageApplication {

    public static void main(String[] args) throws JsonProcessingException {
        SpringApplication.run(JavaCloudStorageApplication.class, args);
    }

}
