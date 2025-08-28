package co.com.crediya.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {
    @Value("${routes.paths.users}")
    private String usersPath;

    @Bean
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        // Usando @Value
        return route(POST(usersPath + "/register"), handler::saveUser);
        
        // O usando @ConfigurationProperties
        // return route(POST(routesProperties.getPaths().getUsers()), handler::saveUser);
    }
}