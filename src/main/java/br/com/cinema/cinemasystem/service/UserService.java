package br.com.cinema.cinemasystem.service;


import br.com.cinema.cinemasystem.model.User;
import br.com.cinema.cinemasystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user){
        return userRepository.save(user);
    }

    public User findUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Email não encontrado com o id " + id));
    }

}
