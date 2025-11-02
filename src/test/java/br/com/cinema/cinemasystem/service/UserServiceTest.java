package br.com.cinema.cinemasystem.service;


import br.com.cinema.cinemasystem.exception.EmailAlreadyExistsException;
import br.com.cinema.cinemasystem.model.User;
import br.com.cinema.cinemasystem.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepositoryMock;

    @InjectMocks
    private UserService userService;

    @Test
    public void deveCriarUsuarioComSucesso(){

        User userTeste = new User();
        userTeste.setName("Teste");
        userTeste.setEmail("teste@email.com");
        userTeste.setPassword("123456");

        User userSalvo = new User();
        userSalvo.setId(1L);
        userSalvo.setName("Teste");
        userSalvo.setEmail("teste@email.com");

        when(userRepositoryMock.findByEmail(userTeste.getEmail())).thenReturn(Optional.empty());

        when(userRepositoryMock.save(any(User.class))).thenReturn(userSalvo);

        User resposta = userService.createUser(userTeste);

        Assertions.assertNotNull(resposta);
        Assertions.assertEquals(1L,resposta.getId());
        Assertions.assertEquals("Teste", resposta.getName());

    }


    @Test
    public void naoDeveCriarUsuarioComEmailDuplicado(){
        User userTeste = new User();
        userTeste.setEmail("emailjaexiste@email.com");

        User userExistente = new User();

        when(userRepositoryMock.findByEmail(userTeste.getEmail())).thenReturn(Optional.of(userExistente));

        Assertions.assertThrows(EmailAlreadyExistsException.class, () -> userService.createUser(userTeste));
        verify(userRepositoryMock, never()).save(any(User.class));
    }

    @Test
    public void deveEncontrarUsuarioPeloEmail(){
        User usuarioTeste = new User();
        usuarioTeste.setEmail("teste@gmail.com");
        usuarioTeste.setName("teste");

        when(userRepositoryMock.findByEmail("teste@gmail.com")).thenReturn(Optional.of(usuarioTeste));

        User userFound = userService.findUserByEmail("teste@gmail.com");

        Assertions.assertNotNull(userFound);
        Assertions.assertEquals("teste@gmail.com", userFound.getEmail());
    }
}

