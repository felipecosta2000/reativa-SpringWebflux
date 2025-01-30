package com.felipe.costa.webflux.service;


import com.felipe.costa.webflux.entity.User;
import com.felipe.costa.webflux.mapper.UserMapper;
import com.felipe.costa.webflux.model.request.UserRequest;
import com.felipe.costa.webflux.repository.UserRepository;
import com.felipe.costa.webflux.service.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final UserRepository userRepository;

    public Mono<User> save(final UserRequest request) {
      return  repository.save(mapper.toEntity(request));

    }

    public Mono<User> findById(final String id) {

        return handleNotFound(repository.findById(id), id);
    }

    public Flux<User> findAll() {
        return userRepository.findAll();
    }

    public Mono<User> update(final String id, final UserRequest request) {
        return findById(id)
                .map(entity -> mapper.toEntity(request, entity))
                .flatMap(repository::save);
    }

    public Mono<User> delete(final String id) {
        return handleNotFound(repository.findAndRemove(id), id);
    }

    private <T> Mono<T> handleNotFound(Mono<T> mono, String id) {
        return mono.switchIfEmpty(Mono.error(new ObjectNotFoundException(
                        toString().formatted("Object not found. Id: %s, Type: %s", id, User.class.getSimpleName())

                )

        ));

    }

}
