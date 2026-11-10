package ma.emsi.voiture.config;
import ma.emsi.voiture.entities.Voiture;
import ma.emsi.voiture.repositories.VoitureRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class InitialData {
    @Bean
    CommandLineRunner initialiserBaseH2(VoitureRepository repository) {
        return args -> {
            repository.save(new Voiture(null, "Toyota", "A 25 333", "Corolla", 1L, null));
            repository.save(new Voiture(null, "Renault", "B 6 3456", "Megane", 1L, null));
            repository.save(new Voiture(null, "Peugeot", "A 55 4444", "301", 2L, null));
        };
    }
}
