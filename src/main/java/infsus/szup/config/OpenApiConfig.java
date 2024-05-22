package infsus.szup.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(info = @Info(
        contact = @Contact(
                name = "SZUP"
        ),
        description = "Dokumentacija za projekt SZUP",
        title = "SZUP",
        version = "1.0"
))
public class OpenApiConfig {
}
