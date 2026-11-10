package ma.emsi.voiture.services;

import ma.emsi.voiture.clients.ClientService;
import ma.emsi.voiture.entities.Voiture;
import ma.emsi.voiture.repositories.VoitureRepository;
import feign.FeignException;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import ma.emsi.voiture.models.Client;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
@Service
public class VoitureService {
    private final VoitureRepository repository;
    private final ClientService clientFeign;
    public VoitureService(VoitureRepository repository, ClientService clientFeign) {
        this.repository = repository;
        this.clientFeign = clientFeign;
    }
    // La liste reste disponible sans solliciter le service Client.
    public List<Voiture> findAll() { return repository.findAll(); }
    public List<Voiture> findAllWithClients() {
        List<Voiture> voitures = repository.findAll();
        Map<Long, Client> clients = new HashMap<>();
        for (Voiture voiture : voitures) {
            voiture.setClient(clients.computeIfAbsent(voiture.getClientId(), this::findClient));
        }
        return voitures;
    }
    public List<Voiture> findByClientId(Long clientId) {
        return repository.findByClientId(clientId);
    }
    public Voiture findById(Long id) {
        Voiture voiture = repository.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Voiture non trouvée"));
        voiture.setClient(findClient(voiture.getClientId()));
        return voiture;
    }
    public Voiture create(Long clientId, Voiture voiture) {
        Client client = findClient(clientId);
        voiture.setId(null);
        voiture.setClientId(clientId);
        voiture.setClient(client);
        return repository.save(voiture);
    }
    public Voiture update(Long id, Voiture input) {
        Voiture voiture = repository.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Voiture non trouvée"));
        if (input.getMarque() != null && !input.getMarque().isBlank()) voiture.setMarque(input.getMarque());
        if (input.getMatricule() != null && !input.getMatricule().isBlank()) voiture.setMatricule(input.getMatricule());
        if (input.getModel() != null && !input.getModel().isBlank()) voiture.setModel(input.getModel());
        return repository.save(voiture);
    }
    private Client findClient(Long clientId) {
        try {
            return clientFeign.clientById(clientId);
        } catch (FeignException.NotFound ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Client référencé introuvable", ex);
        } catch (FeignException ex) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Service Client indisponible", ex);
        }
    }
}
