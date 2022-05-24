package org.example.functional_tests;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.example.api.PetApi;
import org.example.invoker.ApiClient;
import org.example.model.Pet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static io.restassured.config.RestAssuredConfig.config;
import static org.example.invoker.JacksonObjectMapper.jackson;

public class PetApiTests {

    private static PetApi api;

    @BeforeAll
    static void createApi() {
        api = ApiClient.api(ApiClient.Config.apiConfig().reqSpecSupplier(
                () -> new RequestSpecBuilder()
                        .setConfig(config().objectMapperConfig(objectMapperConfig().defaultObjectMapper(jackson())))
                        .addFilters(List.of(new RequestLoggingFilter(), new ResponseLoggingFilter(), new ErrorLoggingFilter()))
                        .setBaseUri("https://petstore.swagger.io/v2"))).pet();
    }

    @Test
    void addPetTest() {
        Pet pet = new Pet();
        pet.setId(123L);
        pet.setName("tv_test");
        pet.setStatus(Pet.StatusEnum.AVAILABLE);

        api.addPet().body(pet).execute(response -> response.then().statusCode(200));
    }
}
