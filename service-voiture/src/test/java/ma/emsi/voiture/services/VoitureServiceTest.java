package ma.emsi.voiture.services;

import java.util.List;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import ma.emsi.voiture.clients.ClientService;
import ma.emsi.voiture.entities.Voiture;
import ma.emsi.voiture.models.Client;
import ma.emsi.voiture.repositories.VoitureRepository;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class VoitureServiceTest {
    private static final List<Voiture> DATA = List.of(
        new Voiture(1L, "Renault", "A", "Clio", 1L, null),
        new Voiture(2L, "Dacia", "B", "Sandero", 1L, null),
        new Voiture(3L, "Peugeot", "C", "208", 2L, null));
    private VoitureRepository repository() {
        return (VoitureRepository) Proxy.newProxyInstance(
            VoitureRepository.class.getClassLoader(), new Class<?>[]{VoitureRepository.class},
            (proxy, method, args) -> {
                if (method.getName().equals("findAll")) return DATA;
                throw new AssertionError("Appel inattendu : " + method.getName());
            });
    }
    @Test
    void enrichitLaListeAvecUnSeulAppelParClient() {
        Map<Long, Integer> calls = new HashMap<>();
        ClientService feign = id -> {
            calls.merge(id, 1, Integer::sum);
            return new Client(id, id == 1L ? "Rabab" : "Amal", 23f);
        };
        List<Voiture> result = new VoitureService(repository(), feign).findAllWithClients();
        assertThat(result).hasSize(3);
        assertThat(result).allSatisfy(v -> assertThat(v.getClient().getId()).isEqualTo(v.getClientId()));
        assertThat(calls).containsExactlyInAnyOrderEntriesOf(Map.of(1L, 1, 2L, 1));
    }

    @Test
    void listeLocaleSansAppelDistant() {
        ClientService feign = id -> { throw new AssertionError("Aucun appel distant attendu"); };
        assertThat(new VoitureService(repository(), feign).findAll()).hasSize(3);
    }
}
