package es.iesjuanbosco.reservaaulas.controller;

import es.iesjuanbosco.reservaaulas.entity.Aula;
import es.iesjuanbosco.reservaaulas.repository.AulaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AulaController {
    private final AulaRepository aulaRepository;

    @GetMapping("/aulas")
    public ResponseEntity<List<Aula>> findAll() {
        return ResponseEntity.ok(aulaRepository.findAll());
    }

    @GetMapping("/aulas/{id}")
    public ResponseEntity<Aula> findById(@PathVariable Long id) {
        Optional<Aula> aulaOptional = aulaRepository.findById(id);
        if (aulaOptional.isPresent()) {
            return ResponseEntity.ok(aulaOptional.get());
        } else return ResponseEntity.notFound().build();
/*
        Alternativa con programación funcional:
        return alumnoRepository.findById(id) //genera el Optional
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
*/
    }

    @PostMapping("/aulas")
    public ResponseEntity<Aula> create(@RequestBody Aula aula) {
        aula.setId(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(aulaRepository.save(aula));
    }

    @PutMapping("/aulas/{id}")
    public ResponseEntity<Aula> update(@PathVariable Long id, @RequestBody Aula aula) {
        if (!aulaRepository.existsById(id)) return ResponseEntity.notFound().build();
        aula.setId(id);
        return ResponseEntity.ok(aulaRepository.save(aula));
    }

    @DeleteMapping("aulas/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!aulaRepository.existsById(id)) return ResponseEntity.notFound().build();

        aulaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
