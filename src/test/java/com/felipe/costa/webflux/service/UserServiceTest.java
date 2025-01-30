package com.felipe.costa.webflux.service;

import com.felipe.costa.webflux.entity.User;
import com.felipe.costa.webflux.mapper.UserMapper;
import com.felipe.costa.webflux.model.request.UserRequest;
import com.felipe.costa.webflux.repository.UserRepository;
import com.mongodb.internal.VisibleForTesting;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private UserMapper mapper;

    @InjectMocks
    private UserService service;

    @Test
    void testSave() {
        UserRequest request = new UserRequest("felipe", "felipe@gmail.com", "1234");
        User entity = User.builder().build();


       when(mapper.toEntity(any(UserRequest.class))).thenReturn(entity);
       when(repository.save(any(User.class))).thenReturn(Mono.just(User.builder().build()));


       Mono<User> result = service.save(request);

       StepVerifier.create(result)
               .expectNextMatches(user -> user.getClass() == User.class)
               .expectComplete()
               .verify();

       verify(repository, times(1)).save(any(User.class));

    }

    @Test
    void testFindById() {
        when(repository.findById(anyString())).thenReturn(Mono.just(User.builder().build()));
                //.id("1234");
            //build()));

        Flux<User> result = service.findAll();

        StepVerifier.create(result)
                .expectNextMatches(user -> user.getClass() == User.class && user.getId() == "1234")
               .expectComplete()
                .verify();

        verify(repository, times(1)).findById(anyString());

    }

    @Test
    void testFindAll() {
        when(repository.findAll()).thenReturn(Flux.just(User.builder().build()));

        Mono<User> result = service.findById("1234");

        StepVerifier.create(result)
                .expectNextMatches(user -> user.getClass() == User.class && user.getId() == "1234")
               .expectComplete()
                .verify();

        verify(repository, times(1)).findAll();
}

@Test
void testUpdate() {
    UserRequest request = new UserRequest("felipe", "felipe@gmail.com", "1234");
    User entity = User.builder().build();


    when(mapper.toEntity(any(UserRequest.class), any(User.class))).thenReturn(entity);
    when(repository.findById(anyString())).thenReturn(Mono.just(entity));
    when(repository.save(any(User.class))).thenReturn(Mono.just(entity));


    Mono<User> result = service.update("1234", request);

    StepVerifier.create(result)
            .expectNextMatches(user -> user.getClass() == User.class)
               .expectComplete()
            .verify();

    verify(repository, times(1)).save(any(User.class));

    }

    @Test
    void testDelete() {
        User entity = User.builder().build();
        when(repository.findAndRemove(anyString())).thenReturn(Mono.just(entity));


        Mono<User> result = service.delete("1234");

        StepVerifier.create(result)
                .expectNextMatches(user -> user.getClass() == User.class)
               .expectComplete()
                .verify();

        verify(repository, times(1)).findAndRemove(anyString());
    }
}