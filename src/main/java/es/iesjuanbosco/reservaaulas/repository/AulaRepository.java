package es.iesjuanbosco.reservaaulas.repository;

import es.iesjuanbosco.reservaaulas.entity.Aula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AulaRepository extends JpaRepository<Aula, Long> {
}
