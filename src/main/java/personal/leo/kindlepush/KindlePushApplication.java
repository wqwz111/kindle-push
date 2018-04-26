package personal.leo.kindlepush;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@EnableAsync
public class KindlePushApplication {

    public static void main(String[] args) {
        SpringApplication.run(KindlePushApplication.class, args);
    }
}
