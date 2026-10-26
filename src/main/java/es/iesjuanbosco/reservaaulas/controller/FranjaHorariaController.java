package es.iesjuanbosco.reservaaulas.controller;

import es.iesjuanbosco.reservaaulas.entity.FranjaHoraria;
import es.iesjuanbosco.reservaaulas.repository.FranjaHorariaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FranjaHorariaController {
    private final FranjaHorariaRepository franjaHorariaRepository;

    @GetMapping("/franjasHorarias")
    public ResponseEntity<List<FranjaHoraria>> findAll() {
        return ResponseEntity.ok(franjaHorariaRepository.findAll());
    }

    @GetMapping("/franjasHorarias/{id}")
    public ResponseEntity<FranjaHoraria> findById(@PathVariable Long id) {
        return franjaHorariaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/franjasHorarias")
    public ResponseEntity<FranjaHoraria> create(@RequestBody FranjaHoraria franjaHoraria) {
        franjaHoraria.setId(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(franjaHorariaRepository.save(franjaHoraria));
    }

    @PutMapping("/franjasHorarias/{id}")
    public ResponseEntity<FranjaHoraria> update(
            @PathVariable Long id,
            @RequestBody FranjaHoraria franjaHoraria) {
        if (!franjaHorariaRepository.existsById(id)) return ResponseEntity.notFound().build();
        franjaHoraria.setId(id);
        return ResponseEntity.ok(franjaHorariaRepository.save(franjaHoraria));
    }

    @DeleteMapping("/franjasHorarias/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!franjaHorariaRepository.existsById(id)) return ResponseEntity.notFound().build();
        franjaHorariaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
