package es.iesjuanbosco.reservaaulas.backend.repository;

import es.iesjuanbosco.reservaaulas.backend.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
}
