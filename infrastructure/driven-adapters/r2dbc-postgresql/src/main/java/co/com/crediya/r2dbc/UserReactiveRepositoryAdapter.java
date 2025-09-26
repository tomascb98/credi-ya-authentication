package co.com.crediya.r2dbc;

import co.com.crediya.model.role.Role;
import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.UserRepository;
import co.com.crediya.r2dbc.entity.UserEntity;
import co.com.crediya.r2dbc.entity.RoleEntity;
import co.com.crediya.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.Array;
import java.util.Set;
import java.util.UUID;

@Repository
@Transactional
@Slf4j
public class UserReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        User/* change for domain model */,
        UserEntity/* change for adapter model */,
        UUID,
        UserReactiveRepository
> implements UserRepository {
    
    private final RoleReactiveRepositoryAdapter userRoleRepository;
    
    public UserReactiveRepositoryAdapter(UserReactiveRepository repository, ObjectMapper mapper, RoleReactiveRepositoryAdapter userRoleRepository) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, User.class));
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public Mono<User> saveUser(User user) {
        log.info("Iniciando persistencia de usuario");

        UserEntity userEntity = mapper.map(user, UserEntity.class);
        userEntity.setUserRoleId(user.getRole().getId());

        log.debug("Entidad mapeada: userRoleId={}", userEntity.getUserRoleId());

        return userRoleRepository.existsById(userEntity.getUserRoleId())
                .flatMap(isRoleValid -> repository.save(userEntity))
                .doOnNext(entity -> log.info("Usuario persistido en BD exitosamente"))
                .map(userEntitySaved -> {
                    User userSaved = mapper.map(userEntitySaved, User.class);
                    userSaved.setRole(Role.builder().id(userEntitySaved.getUserRoleId()).build());

                    log.debug("Objeto de dominio reconstruido: role={}", userSaved.getRole().getName());

                    return userSaved;
                });
    }

    @Override
    public Mono<User> findUserByEmail(String email) {
        return repository.findUserByEmail(email) // Mono<UserEntity>
                .zipWhen(entity -> userRoleRepository.findById(entity.getUserRoleId()))   // Mono<RoleEntity>
                .map(tuple -> {
                    UserEntity entity = tuple.getT1();
                    Role role = tuple.getT2();

                    User user = mapper.map(entity, User.class); // mapper síncrono
                    user.setRole(role);
                    return user;
                });
    }

    @Override
    public Mono<Boolean> findUserByDocumentNumber(String documentNumber) {
        return repository.existsUserEntityByDocumentNumber(documentNumber);
    }

    @Override
    public Flux<User> findUsersByDocumentNumber(Set<String> documentNumbers) {
        return repository.findUsersByDocumentNumberIn(documentNumbers.toArray(new String[0]))
                .map(entity -> mapper.map(entity, User.class));
    }
}
