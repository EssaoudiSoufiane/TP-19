package ma.emsi.voiture.repositories;

import ma.emsi.voiture.entities.Voiture;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
public interface VoitureRepository extends JpaRepository<Voiture, Long> {
    List<Voiture> findByClientId(Long clientId);
}
