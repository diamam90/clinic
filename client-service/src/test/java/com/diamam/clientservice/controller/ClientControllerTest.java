package com.diamam.clientservice.controller;

import com.diamam.clientservice.exception.ObjectNotFoundException;
import com.diamam.clientservice.mapper.ClientMapperImpl;
import com.diamam.clientservice.model.CreateClientRequest;
import com.diamam.clientservice.model.PatchClientRequest;
import com.diamam.clientservice.model.UpdateClientRequest;
import com.diamam.clientservice.service.ClientService;
import com.diamam.clientservice.stub.ClientRequestStub;
import com.diamam.clientservice.stub.ClientStub;
import org.hamcrest.core.CombinableMatcher;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.diamam.clientservice.stub.ClientStub.alex;
import static com.diamam.clientservice.stub.ClientStub.ivan;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ClientController.class)
@Import({ClientMapperImpl.class})
class ClientControllerTest {

    @Autowired
    MockMvc mvc;
    @MockBean
    ClientService clientService;

    @Nested
    class GetById {
        @Test
        void shouldReturnClientById() throws Exception {
            when(clientService.getById("213dqsd1231")).thenReturn(ivan());
            mvc.perform(MockMvcRequestBuilders.get("/api/v1/clients?clientId=213dqsd1231"))
                    .andExpectAll(status().isOk(),
                            content().json(// language=JSON
                                    """
                                            {
                                              "id": "213dqsd1231",
                                              "firstName": "Ivan",
                                              "lastName": "Ivanov",
                                              "middleName": "Ivanovich",
                                              "age": 34
                                            }
                                            """, true));
        }

        @Test
        void getByIdThrowsObjectNotFound() throws Exception {
            when(clientService.getById("213dqsd1231"))
                    .thenThrow(new ObjectNotFoundException("213dqsd1231"));
            mvc.perform(MockMvcRequestBuilders.get("/api/v1/clients?clientId=213dqsd1231"))
                    .andExpectAll(status().isNotFound(),
                            content().json(// language=JSON
                                    """
                                            {
                                              "detail": "Client with id {213dqsd1231} was not found",
                                              "status": 404
                                            }
                                            """));
        }

        @Test
        void shouldReturn404WhenClientByIdNotFound() throws Exception {
            when(clientService.getById("213dqsd1231"))
                    .thenThrow(new ObjectNotFoundException("213dqsd1231"));
            mvc.perform(MockMvcRequestBuilders.get("/api/v1/clients?clientId=213dqsd1231"))
                    .andExpectAll(status().isNotFound(),
                            content().json(// language=JSON
                                    """
                                            {
                                              "detail": "Client with id {213dqsd1231} was not found",
                                              "status": 404
                                            }
                                            """));
        }
    }

    @Nested
    class Create {
        @Test
        void shouldCreateClient() throws Exception {
            var createRequestJson = // language=json
                    """
                            {
                               "firstName" : "Alex",
                               "lastName": "Alexandrov",
                               "middleName": "Alexandrovich"
                            }
                            """;

            var createRequest = new CreateClientRequest("Alex", "Alexandrov", "Alexandrovich");
            when(clientService.create(createRequest)).thenReturn(alex());
            mvc.perform(post("/api/v1/clients")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(createRequestJson))
                    .andExpect(status().isCreated())
                    .andExpect(content().json(// language=JSON
                            """
                                    {
                                      "id": "qwde123fsdr23",
                                      "firstName": "Alex",
                                      "lastName": "Alexandrov",
                                      "middleName": "Alexandrovich",
                                      "age": null
                                    }
                                    """, true));
        }

        @Test
        void invalidCreateRequest() throws Exception {
            var createRequestJson =//language=json
                    """
                            {
                               "firstName" : "Al",
                               "lastName": "Alexandrov Alexandrov Alexandrov",
                               "middleName": "Al"
                            }
                            """;
            mvc.perform(post("/api/v1/clients")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(createRequestJson))
                    .andExpectAll(status().isBadRequest(),
                            jsonPath("detail").value(
                                    new CombinableMatcher<>(new StringContains("Имя должно быть от 3 до 20 символов"))
                                            .and(new StringContains("Фамилия должна быть от 1 до 20 символов"))
                                            .and(new StringContains("Отчество должно быть от 3 до 20 символов"))));
        }
    }

    @Nested
    class Update {
        @Test
        void shouldUpdateClient() throws Exception {
            UpdateClientRequest request = ClientRequestStub.updatedIvan();
            when(clientService.update("123das12", request)).thenReturn(ivan());
            mvc.perform(post("/api/v1/clients/123das12")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(  //language=json
                                    """
                                            {
                                                "firstName": "Ivan",
                                                "lastName": "Ivanov",
                                                "middleName": "Ivanovich",
                                                "age": 34,
                                                "passport": "1000 1231231",
                                                "phone": "88002000600",
                                                "email": "ivan@ivanov@yandex.ru"
                                            }
                                            """
                            ))
                    .andExpectAll(
                            status().isOk(),
                            content().json(//language=json
                                    """                                 
                                                                 {
                                                                   "id": "213dqsd1231",
                                                                   "firstName": "Ivan",
                                                                   "lastName": "Ivanov",
                                                                   "middleName": "Ivanovich",
                                                                   "age": 34
                                                                  }
                                            """, true));
        }

        @Test
        void updateClientThrowsObjectNotFound() throws Exception {
            UpdateClientRequest request = ClientRequestStub.updatedIvan();
            when(clientService.update("123das12", request)).thenThrow(new ObjectNotFoundException("123das12"));
            mvc.perform(post("/api/v1/clients/123das12")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(  //language=json
                                    """
                                            {
                                                "firstName": "Ivan",
                                                "lastName": "Ivanov",
                                                "middleName": "Ivanovich",
                                                "age": 34,
                                                "passport": "1000 1231231",
                                                "phone": "88002000600",
                                                "email": "ivan@ivanov@yandex.ru"
                                            }
                                            """
                            ))
                    .andExpectAll(status().isNotFound(),
                            content().json(// language=JSON
                                    """
                                            {
                                              "detail": "Client with id {123das12} was not found",
                                              "status": 404
                                            }
                                            """));
        }
    }

    @Nested
    class Patch {

        @Test
        void shouldPatchClient() throws Exception {
            PatchClientRequest request = ClientRequestStub.patchedIvan();
            when(clientService.patch("123das12", request)).thenReturn(ClientStub.ivan());
            mvc.perform(patch("/api/v1/clients/123das12")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(  //language=json
                                    """
                                            {
                                                "firstName": "Ivan",
                                                "lastName": "Ivanov",
                                                "middleName": "Ivanovich",
                                                "age": 34,
                                                "passport": "1000 1231231",
                                                "phone": "88002000600",
                                                "email": "ivan@ivanov@yandex.ru"
                                            }
                                            """
                            ))
                    .andExpectAll(
                            status().isOk(),
                            content().json(//language=json
                                    """                                 
                                                                 {
                                                                   "id": "213dqsd1231",
                                                                   "firstName": "Ivan",
                                                                   "lastName": "Ivanov",
                                                                   "middleName": "Ivanovich",
                                                                   "age": 34
                                                                  }
                                            """, true));
        }

        @Test
        void patchClientThrowsObjectNotFound() throws Exception {
            PatchClientRequest request = ClientRequestStub.patchedIvan();
            when(clientService.patch("123das12", request)).thenThrow(new ObjectNotFoundException("123das12"));
            mvc.perform(patch("/api/v1/clients/123das12")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(  //language=json
                                    """
                                            {
                                                "firstName": "Ivan",
                                                "lastName": "Ivanov",
                                                "middleName": "Ivanovich",
                                                "age": 34,
                                                "passport": "1000 1231231",
                                                "phone": "88002000600",
                                                "email": "ivan@ivanov@yandex.ru"
                                            }
                                            """
                            ))
                    .andExpectAll(status().isNotFound(),
                            content().json(// language=JSON
                                    """
                                            {
                                              "detail": "Client with id {123das12} was not found",
                                              "status": 404
                                            }
                                            """));
        }
    }

    @Nested
    class Delete {
        @Test
        void deleteThrowsObjectNotFound() throws Exception {
            mvc.perform(MockMvcRequestBuilders.delete("/api/v1/clients/213dqsd1231"))
                    .andExpect(status().isNoContent());
            verify(clientService).delete("213dqsd1231");
        }
    }
}