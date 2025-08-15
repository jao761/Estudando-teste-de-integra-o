package jao761.projeto_teste_de_intregracao.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import jao761.projeto_teste_de_intregracao.TextoDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistrar;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class TextoControllerIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Long idTextoCriado;

    @BeforeEach
    void setup() throws Exception {
        var novoTexto = new TextoDto("Texto inicial");
        String json = objectMapper.writeValueAsString(novoTexto);

        String location = mockMvc.perform(post("/enviar-texto")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getHeader("Location");

        idTextoCriado = Long.parseLong(location.substring(location.lastIndexOf("/")+1));
    }

    @Test
    void deveCriarTexto() throws Exception {

        var novoTexto = new TextoDto("teste post criar texto");
        String json = objectMapper.writeValueAsString(novoTexto);

        mockMvc.perform(post("/enviar-texto")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void deveListarTextoPorId() throws Exception {

        mockMvc.perform(get("/visualizar-texto/{id}", idTextoCriado))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.texto").value("Texto inicial"));

    }

    @Test
    void deveListarTodosOsTextos() throws Exception {

        mockMvc.perform(get("/listar-textos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists());

    }

    //deve atualizar texto
    @Test
    void deveAtualizarTexto() throws Exception {
        var textoAtualizado = new TextoDto("Texto atualizado");
        String json = objectMapper.writeValueAsString(textoAtualizado);

        mockMvc.perform(put("/atualizar-texto/{id}", idTextoCriado)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.texto").value("Texto atualizado"));
    }

    @Test
    void deveDeletarTexto() throws Exception {

        mockMvc.perform(delete("/deletar-texto/{id}", idTextoCriado))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/visualizar-texto/{id}", idTextoCriado))
                .andExpect(status().isNotFound());
    }

}
