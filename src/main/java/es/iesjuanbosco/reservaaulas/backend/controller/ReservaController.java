package es.iesjuanbosco.reservaaulas.backend.controller;

import es.iesjuanbosco.reservaaulas.backend.entity.Reserva;
import es.iesjuanbosco.reservaaulas.backend.service.ReservaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;

    @GetMapping("/reservas")
    public ResponseEntity<List<Reserva>> findAll() {
        return ResponseEntity.ok(reservaService.findAll());
    }

    @GetMapping("/reservas/{id}")
    public ResponseEntity<Reserva> findById(@PathVariable Long id) {
        return reservaService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/reservas")
    public ResponseEntity<Reserva> create(@RequestBody Reserva reserva) {
        Reserva guardada = reservaService.create(reserva);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
    }

    @PutMapping("/reservas/{id}")
    public ResponseEntity<Reserva> update(@PathVariable Long id, @RequestBody Reserva reserva) {
        return reservaService.update(id, reserva)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/reservas/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean eliminada = reservaService.delete(id);
        if (!eliminada) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
