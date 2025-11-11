package com.project.product_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.product_service.dto.ProductRequest;
import com.project.product_service.model.Product;
import com.project.product_service.repository.ProductRepository;
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
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mongodb.MongoDBContainer;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

    @Container
    static MongoDBContainer mongoDBContainer =  new MongoDBContainer("mongo:4.4.2");
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    ProductRepository productRepository;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    private ProductRequest getProductRequest() {
        return ProductRequest.builder()
                .name("IPhone 17")
                .description("IPhone 17")
                .price(BigDecimal.valueOf(1200))
                .build();
    }

	@Test
	void shouldCreateProduct() throws Exception {
        ProductRequest productRequest = getProductRequest();
        String productRequestString = objectMapper.writeValueAsString(productRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(productRequestString))
                .andExpect(status().isCreated());
        Assertions.assertEquals(1, productRepository.findAll().size());
	}



    @Test
    void shouldGetAllProducts() throws Exception {
        productRepository.deleteAll();

        Product product1 = Product.builder()
                .name("Product 1")
                .description("Desc 1")
                .price(BigDecimal.valueOf(100))
                .build();

        Product product2 = Product.builder()
                .name("Product 2")
                .description("Desc 2")
                .price(BigDecimal.valueOf(200))
                .build();

        productRepository.save(product1);
        productRepository.save(product2);

        String responseContent = mockMvc.perform(MockMvcRequestBuilders.get("/api/product")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Product> productsFromApi = Arrays.asList(objectMapper.readValue(responseContent, Product[].class));

        Assertions.assertEquals(2, productsFromApi.size());
        Assertions.assertTrue(productsFromApi.stream().anyMatch(p -> p.getName().equals("Product 1")));
        Assertions.assertTrue(productsFromApi.stream().anyMatch(p -> p.getName().equals("Product 2")));

    }
}
