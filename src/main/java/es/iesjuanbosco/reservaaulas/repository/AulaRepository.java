package es.iesjuanbosco.reservaaulas.repository;

import es.iesjuanbosco.reservaaulas.entity.Aula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AulaRepository extends JpaRepository<Aula, Long> {

    List<Aula> findByTieneOrdenadores(Boolean tieneOrdenadores);

    List<Aula> findByCapacidadGreaterThan(Integer capacidad);

    List<Aula> findByTieneOrdenadoresAndCapacidadGreaterThan(
            Boolean tieneOrdenadores,
            Integer capacidad);
}
