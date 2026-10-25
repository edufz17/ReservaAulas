package es.iesjuanbosco.reservaaulas.repository;

import es.iesjuanbosco.reservaaulas.entity.FranjaHoraria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FranjaHorariaRepository extends JpaRepository<FranjaHoraria, Long> {
}
