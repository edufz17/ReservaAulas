package es.iesjuanbosco.reservaaulas.controller;

import es.iesjuanbosco.reservaaulas.entity.Reserva;
import es.iesjuanbosco.reservaaulas.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReservaController {
    private final ReservaRepository reservaRepository;

    @GetMapping("/reservas")
    public ResponseEntity<List<Reserva>> findAll() {
        return ResponseEntity.ok(reservaRepository.findAll());
    }

    @GetMapping("/reservas/{id}")
    public ResponseEntity<Reserva> findById(@PathVariable Long id) {
        return reservaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/reservas")
    public ResponseEntity<Reserva> create(@RequestBody Reserva reserva) {
        return ResponseEntity.ok(reservaRepository.save(reserva));
    }

    @PutMapping("/reservas/{id}")
    public ResponseEntity<Reserva> update(@PathVariable Long id, @RequestBody Reserva reserva) {
        if (!reservaRepository.existsById(id)) return ResponseEntity.notFound().build();
        reserva.setId(id);
        return ResponseEntity.ok(reservaRepository.save(reserva));
    }

    @DeleteMapping("/reservas/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!reservaRepository.existsById(id)) return ResponseEntity.notFound().build();
        reservaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
