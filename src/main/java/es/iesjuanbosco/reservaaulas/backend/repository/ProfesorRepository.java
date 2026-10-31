package es.iesjuanbosco.reservaaulas.backend.repository;

import es.iesjuanbosco.reservaaulas.backend.entity.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfesorRepository extends JpaRepository<Profesor, Long> {
}
