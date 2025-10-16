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

    public User updateUser(Long id, User userDetails){
        User existingUser = findUserById(id);

        if(userDetails.getName() != null ){
            existingUser.setName(userDetails.getName());
        }
        if(userDetails.getEmail() != null){
            existingUser.setEmail(userDetails.getEmail());
        }
        if(userDetails.getPassword() != null){
            existingUser.setPassword(userDetails.getPassword());
        }
        return userRepository.save(existingUser);
    }

    public void deleteUser(Long id){
        User user = userRepository.findUserById(id);

        userRepository.delete(user);
    }
}
