package ma.emsi.voiture;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
@EnableFeignClients
@SpringBootApplication
public class VoitureApplication {
    public static void main(String[] args) { SpringApplication.run(VoitureApplication.class, args); }
}
