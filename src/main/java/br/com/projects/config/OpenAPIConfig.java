package br.com.projects.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Project Management API", version = "1.0", description = "API para gerenciamento de projetos"))
public class OpenAPIConfig {
}
