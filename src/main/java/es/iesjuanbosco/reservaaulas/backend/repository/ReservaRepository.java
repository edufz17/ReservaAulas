package es.iesjuanbosco.reservaaulas.backend.repository;

import es.iesjuanbosco.reservaaulas.backend.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    boolean existsByAulaIdAndFranjaHorariaIdAndFechaReservaBetween(
            Long aulaId,
            Long franjaHorariaId,
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin
    );
}
