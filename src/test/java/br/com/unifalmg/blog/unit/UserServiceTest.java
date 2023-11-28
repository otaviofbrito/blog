package br.com.unifalmg.blog.unit;

import br.com.unifalmg.blog.entity.User;
import br.com.unifalmg.blog.exception.UserNotFoundException;
import br.com.unifalmg.blog.repository.UserRepository;
import br.com.unifalmg.blog.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repository;

    @Test
    @DisplayName("#findById > When the id is null > Throw an exception")
    void findByIdWhenTheIdIsNullThrowAnException() {
        assertThrows(IllegalArgumentException.class, () ->
                service.findById(null));
    }

    @Test
    @DisplayName("#findById > When the id is not null > When a user is found > Return the user")
    void findByIdWhenTheIdIsNotNullWhenAUserIsFoundReturnTheUser() {
        when(repository.findById(1)).thenReturn(Optional.of(User.builder()
                        .id(1)
                        .name("Fellipe")
                        .username("felliperey")
                .build()));
        User response = service.findById(1);
        assertAll(
                () -> assertEquals(1, response.getId()),
                () -> assertEquals("Fellipe", response.getName()),
                () -> assertEquals("felliperey", response.getUsername())
        );
    }

    @Test
    @DisplayName("#findById > When the id is not null > When no user is found > Throw an exception")
    void findByIdWhenTheIdIsNotNullWhenNoUserIsFoundThrowAnException() {
        when(repository.findById(2)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () ->
                service.findById(2));
    }

    @Test
    @DisplayName("#getAllUsers > When no user is found > return empty list")
    void getAllUsersWhenNoUserIsFoundReturnEmptyList(){
        when(repository.findAll()).thenReturn(new ArrayList<>());
        List<User> userList = service.getAllUsers();
        assertEquals(0, userList.size());
    }

    @Test
    @DisplayName("getAllUsers > When at least one user is found > return a list of users")
    void getAllUsersWhenUserIsFoundReturnList(){
        when(repository.findAll()).thenReturn(new ArrayList<>(){
            {
                add(new User(1, "Fake User", "fakeuser", "fakeuser@email.com",
                        "12345", null , null));
            }
        });

        List<User> userList = service.getAllUsers();
        assertAll(
                () -> {assertEquals(1, userList.size());},
                () -> {assertEquals(1, userList.get(0).getId());}
        );

    }

    @Test
    @DisplayName("getAllUsers > When more than one user are found > return a list of users")
    void getAllUsersWhenMoreThanOneAreFoundReturnList(){
        when(repository.findAll()).thenReturn(new ArrayList<>(){
            {
                add(new User(1, "Fake User", "fakeuser", "fakeuser@email.com",
                        "12345", null , null));
                add(new User(55, "Fake User2", "fakeuser2", "fakeuser2@email.com",
                        "556677", null , null));
            }
        });

        List<User> userList = service.getAllUsers();
        assertAll(
                () -> {assertEquals(2, userList.size());},
                () -> {assertEquals(1, userList.get(0).getId());},
                () -> {assertEquals(55, userList.get(1).getId());},
                () -> {assertEquals("fakeuser", userList.get(0).getUsername());},
                () -> {assertEquals("fakeuser2", userList.get(1).getUsername());}
        );

    }

}
