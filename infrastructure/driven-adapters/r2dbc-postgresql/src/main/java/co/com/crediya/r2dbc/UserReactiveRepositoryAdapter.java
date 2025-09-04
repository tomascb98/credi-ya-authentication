package co.com.crediya.r2dbc;

import co.com.crediya.model.role.Role;
import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.UserRepository;
import co.com.crediya.r2dbc.entity.UserEntity;
import co.com.crediya.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    
    public UserReactiveRepositoryAdapter(UserReactiveRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, User.class));
    }

    @Override
    public Mono<User> saveUser(User user) {
        log.info("Iniciando persistencia de usuario");
        
        UserEntity userEntity = mapper.map(user, UserEntity.class);
        userEntity.setUserRoleId(user.getRole().getId());
        
        log.debug("Entidad mapeada: userRoleId={}", userEntity.getUserRoleId());
        
        return repository
                .save(userEntity)
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
        return repository.findUserByEmail(email);
    }

    @Override
    public Mono<User> findUserByDocumentNumber(String documentNumber) {
        return repository.findUserByDocumentNumber(documentNumber);
    }
}
