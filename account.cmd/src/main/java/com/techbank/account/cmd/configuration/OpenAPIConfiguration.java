package com.techbank.account.cmd.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfiguration {

    @Bean
    public OpenAPI openAPI(@Value("${techbank.openapi.dev-url}") String devUrl) {
        var devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Server URL in Development environment");

        return new OpenAPI()
                .info(new Info()
                        .title("Bank Account Command API Project")
                        .version("1.0"))
                .servers(List.of(devServer));
    }

}
