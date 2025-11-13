package br.com.cinema.cinemasystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.cinema.cinemasystem.model.Ingressos;

@Repository
public interface IngressosRepository extends JpaRepository<Ingressos, String> {
}
