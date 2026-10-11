package com.ecommerce.userservice.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import io.swagger.v3.oas.models.OpenAPI;

class OpenAPIConfigTest {

    @Test
    void customOpenAPI_ShouldContainServiceMetadataAndSecurityScheme() {
        OpenAPIConfig config = new OpenAPIConfig();

        OpenAPI openAPI = config.customOpenAPI();

        assertNotNull(openAPI);
        assertEquals("User Service API", openAPI.getInfo().getTitle());
        assertEquals("0.1.0", openAPI.getInfo().getVersion());
        assertNotNull(openAPI.getComponents());
        assertNotNull(openAPI.getComponents().getSecuritySchemes().get("Bearer Authentication"));
        assertEquals(1, openAPI.getSecurity().size());
    }
}
