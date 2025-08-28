package co.com.crediya.r2dbc;

import co.com.crediya.model.role.Role;
import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.UserRepository;
import co.com.crediya.r2dbc.entity.UserEntity;
import co.com.crediya.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
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
        UserEntity userEntity = mapper.map(user, UserEntity.class);
        userEntity.setUserRoleId(user.getRole().getId());
        return this.repository
                .save(userEntity)
                .map(userEntitySaved -> {
                    User userSaved = mapper.map(userEntitySaved, User.class);
                    userSaved.setRole(Role.builder().id(userEntitySaved.getUserRoleId()).build());
                    return userSaved;
                });
    }

    @Override
    public Mono<User> findUserById(UUID id) {
        return this.findById(id);
    }

    @Override
    public Flux<User> getAllUsers() {
        return this.repository.findAll()
                .map(this::toEntity);
    }

    @Override
    public Mono<User> updateUser(User user) {
        return this.save(user);
    }

    @Override
    public void deleteUser(UUID id) {
    }
}
