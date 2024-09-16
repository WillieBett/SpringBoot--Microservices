package com.imbuka.productservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imbuka.productservice.dto.ProductRequest;
import com.imbuka.productservice.dto.ProductResponse;
import com.imbuka.productservice.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

    //The test will start the mongo db container by downloading the mongo 4.4.2 image
    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");

    @Autowired
    private MockMvc mockMvc;

    //to convert object into a string
    //object mapper will convert pojo object into json object
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    //get the replica set url and add it to the spring data mongodb uri at the time
    //of creating the test
    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }
    @Test
    void shouldCreateProduct() throws Exception {
        ProductRequest productRequest = getProductRequest();
        String productRequestString = objectMapper.writeValueAsString(productRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productRequestString))
                .andExpect(status().isCreated());
        Assertions.assertEquals(1, productRepository.findAll().size());
    }

    private ProductRequest getProductRequest() {
        return ProductRequest.builder()
                .name("iphone 13")
                .description("iphone 13")
                .price(BigDecimal.valueOf(1200))
                .build();
    }

    @Test
    void shouldGetAllProduct() throws Exception {

        ProductResponse productResponse = getProductResponse();
        String productResponseString = objectMapper.writeValueAsString(productResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productResponseString))
                .andExpect(status().isOk());
    }

    private ProductResponse getProductResponse() {
        return ProductResponse.builder()
                .name("Redmi note 11")
                .description("note 11")
                .price(BigDecimal.valueOf(1300))
                .build();
    }

}
