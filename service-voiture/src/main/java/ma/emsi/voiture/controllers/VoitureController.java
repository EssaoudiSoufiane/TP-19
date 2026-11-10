package ma.emsi.voiture.controllers;
import java.net.URI;
import java.util.List;
import ma.emsi.voiture.entities.Voiture;
import ma.emsi.voiture.services.VoitureService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
public class VoitureController {
    private final VoitureService service;
    public VoitureController(VoitureService service) { this.service = service; }
    @GetMapping("/voitures")
    public List<Voiture> findAll(@RequestParam(defaultValue = "true") boolean details) {
        return details ? service.findAllWithClients() : service.findAll();
    }
    @GetMapping({"/voitures/{id}", "/voiture/{id}"})
    public Voiture findById(@PathVariable Long id) { return service.findById(id); }
    @GetMapping("/voitures/client/{id}")
    public List<Voiture> findByClient(@PathVariable Long id) { return service.findByClientId(id); }
    @PostMapping("/voitures/{clientId}")
    public ResponseEntity<Voiture> save(@PathVariable Long clientId, @RequestBody Voiture voiture) {
        Voiture saved = service.create(clientId, voiture);
        return ResponseEntity.created(URI.create("/voitures/" + saved.getId())).body(saved);
    }
    @PutMapping("/voitures/{id}")
    public Voiture update(@PathVariable Long id, @RequestBody Voiture voiture) {
        return service.update(id, voiture);
    }
}
