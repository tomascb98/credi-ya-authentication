package co.com.crediya.api;

import co.com.crediya.api.advisor.GlobalExceptionHandler;
import co.com.crediya.api.dto.RegisterUserDto;
import co.com.crediya.api.mapper.UserMapperDtoMapper;
import co.com.crediya.model.exceptions.BusinessRuleException;
import co.com.crediya.model.exceptions.UserNotFoundException;
import co.com.crediya.model.exceptions.ValidationException;
import co.com.crediya.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class Handler {

    private final UserUseCase userUseCase;
    private final UserMapperDtoMapper mapper;
    private final GlobalExceptionHandler exceptionHandler;

    public Mono<ServerResponse> saveUser(ServerRequest request) {
        return request.bodyToMono(RegisterUserDto.class)
                .flatMap(userDto -> userUseCase.saveUser(mapper.mapToEntity(userDto)))
                .flatMap(savedUser -> 
                    ServerResponse.status(HttpStatus.CREATED)
                            .bodyValue(mapper.mapToDto(savedUser))
                )
                .onErrorResume(ValidationException.class, exceptionHandler::handleValidationException)
                .onErrorResume(BusinessRuleException.class, exceptionHandler::handleBusinessRuleException)
                .onErrorResume(IllegalArgumentException.class, exceptionHandler::handleIllegalArgumentException)
                .onErrorResume(Exception.class, exceptionHandler::handleGenericException);
    }
}
