package hr.fer.openapidemo;

import org.junit.jupiter.api.Test;
import org.openapitools.OpenApiGeneratorApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = OpenApiGeneratorApplication.class)
@AutoConfigureMockMvc
class ProductsApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getProducts_returnsOk() throws Exception {
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.page").exists());
    }

    @Test
    void getUsers_withoutToken_returnsForbidden() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isForbidden());
    }

    @Test
    void getUserById_withoutToken_returnsForbidden() throws Exception {
        mockMvc.perform(get("/users/99999"))
                .andExpect(status().isForbidden());
    }
}