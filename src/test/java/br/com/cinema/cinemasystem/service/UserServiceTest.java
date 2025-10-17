package br.com.cinema.cinemasystem.service;


import br.com.cinema.cinemasystem.model.User;
import br.com.cinema.cinemasystem.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.mockito.Mockito.when;

public class UserServiceTest {

    private UserService userService;
    private UserRepository userRepositoryMock;

    @BeforeEach
    public void setup(){
        this.userRepositoryMock = Mockito.mock(UserRepository.class);
        this.userService = new UserService(this.userRepositoryMock);
    }

    @Test
    public void deveEncontrarUsuarioPeloId(){
        User usuarioTeste = new User();
        usuarioTeste.setId(1L);
        usuarioTeste.setName("Maurício");

        when(userRepositoryMock.findById(1L)).thenReturn(Optional.of(usuarioTeste));

        User userFound = userService.findUserById(1L);

        Assertions.assertNotNull(userFound);
        Assertions.assertEquals(1,userFound.getId());
        Assertions.assertEquals("Maurício", userFound.getName());

    }
}
