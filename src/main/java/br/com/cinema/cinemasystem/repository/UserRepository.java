package br.com.cinema.cinemasystem.repository;

import br.com.cinema.cinemasystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserById(Long id);

    Optional<User> findByEmail(String email);
}

