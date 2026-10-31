package es.iesjuanbosco.reservaaulas.backend.repository;

import es.iesjuanbosco.reservaaulas.backend.entity.FranjaHoraria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FranjaHorariaRepository extends JpaRepository<FranjaHoraria, Long> {
}
