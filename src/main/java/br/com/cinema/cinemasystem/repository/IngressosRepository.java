package repository;

import br.com.cinema.cinemasystem.model.Ingressos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngressosRepository extends JpaRepository<Ingressos, String> {
}