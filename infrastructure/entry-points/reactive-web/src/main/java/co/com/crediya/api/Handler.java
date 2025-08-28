package co.com.crediya.api;

import co.com.crediya.api.dto.RegisterUserDto;
import co.com.crediya.api.mapper.UserMapperDtoMapper;
import co.com.crediya.model.user.User;
import co.com.crediya.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {

    private final UserUseCase userUseCase;
    private final UserMapperDtoMapper mapper;

    public Mono<ServerResponse> listenGETUseCase(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(userUseCase.getAllUsers(), User.class);
    }

    public Mono<ServerResponse> saveUser(ServerRequest request){
        return request.bodyToMono(RegisterUserDto.class)
                .flatMap(userDto ->
                        userUseCase.saveUser(mapper.mapToEntity(userDto))
                                .flatMap(savedUser -> ServerResponse.status(HttpStatus.CREATED)
                                        .bodyValue(savedUser))
                );
    }


    public Mono<ServerResponse> listenGETOtherUseCase(ServerRequest serverRequest) {
        // useCase2.logic();
        return ServerResponse.ok().bodyValue("");
    }

    public Mono<ServerResponse> listenPOSTUseCase(ServerRequest serverRequest) {
        // useCase.logic();
        return ServerResponse.ok().bodyValue("");
    }
}
