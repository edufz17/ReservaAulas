package es.iesjuanbosco.reservaaulas.backend.controller;

import es.iesjuanbosco.reservaaulas.backend.entity.Profesor;
import es.iesjuanbosco.reservaaulas.backend.repository.ProfesorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profesores")
public class ProfesorController {

    private final ProfesorRepository profesorRepository;

    @GetMapping
    public ResponseEntity<List<Profesor>> findAll() {
        return ResponseEntity.ok(profesorRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Profesor> findById(@PathVariable Long id) {
        return profesorRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Profesor> create(@RequestBody Profesor profesor) {
        profesor.setId(null);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(profesorRepository.save(profesor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Profesor> update(
            @PathVariable Long id,
            @RequestBody Profesor profesor) {

        if (!profesorRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        profesor.setId(id);
        return ResponseEntity.ok(profesorRepository.save(profesor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!profesorRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        profesorRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}