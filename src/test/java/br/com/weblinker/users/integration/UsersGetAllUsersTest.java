package br.com.weblinker.users.integration;

import br.com.weblinker.users.UsersApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = UsersApplication.class)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude=SecurityAutoConfiguration.class)
@ActiveProfiles("test")
public class UsersGetAllUsersTest extends AbstractUsersClass {

    @Test
    public void givenUsers_whenGetAllUsers_thenStatus200() throws Exception {
        createTestUser("John", "Doe", "john@doe.com", "123456789");
        createTestUser("Alice", "Smith", "alice@smith.com", "987654321");
        createTestUser("User", "Three", "user@three.com", "987654321");
        createDeletedTestUser("Zack", "Evans", "zack@evans.com", "999999999");

        // @formatter:off
        mvc.perform(get("/users").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(equalTo(2))))
                .andExpect(jsonPath("$[0].firstName", is("Alice")))
                .andExpect(jsonPath("$[1].firstName", is("John")));
        // @formatter:on
    }

    @Test
    public void givenUsers_whenGetAllUsersPageTwo_thenStatus200() throws Exception {
        createTestUser("John", "Doe", "john@doe.com", "123456789");
        createTestUser("Alice", "Smith", "alice@smith.com", "987654321");
        createTestUser("User3", "Three", "user@three.com", "987654321");
        createTestUser("User4", "Four", "user@four.com", "987654321");
        createTestUser("User5", "Five", "user@five.com", "987654321");
        createTestUser("User6", "Six", "user@six.com", "987654321");

        // @formatter:off
        mvc.perform(get("/users?page=2").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(equalTo(2))))
                .andExpect(jsonPath("$[0].firstName", is("User3")))
                .andExpect(jsonPath("$[1].firstName", is("User4")));
        // @formatter:on
    }

    @Test
    public void givenUsers_whenGetAllUsersSizeFour_thenStatus200() throws Exception {
        createTestUser("John", "Doe", "john@doe.com", "123456789");
        createTestUser("Alice", "Smith", "alice@smith.com", "987654321");
        createTestUser("User3", "Three", "user@three.com", "987654321");
        createTestUser("User4", "Four", "user@four.com", "987654321");
        createTestUser("User5", "Five", "user@five.com", "987654321");
        createTestUser("User6", "Six", "user@six.com", "987654321");

        // @formatter:off
        mvc.perform(get("/users?size=4").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(equalTo(4))))
                .andExpect(jsonPath("$[0].firstName", is("Alice")))
                .andExpect(jsonPath("$[1].firstName", is("John")))
                .andExpect(jsonPath("$[2].firstName", is("User3")))
                .andExpect(jsonPath("$[3].firstName", is("User4")));
        // @formatter:on
    }

    @Test
    public void givenUsers_whenGetAllUsersExceedingMaxPerPage_thenShowOnlyFiveAndReturnStatus200() throws Exception {
        createTestUser("John", "Doe", "john@doe.com", "123456789");
        createTestUser("Alice", "Smith", "alice@smith.com", "987654321");
        createTestUser("User3", "Three", "user@three.com", "987654321");
        createTestUser("User4", "Four", "user@four.com", "987654321");
        createTestUser("User5", "Five", "user@five.com", "987654321");
        createTestUser("User6", "Six", "user@six.com", "987654321");

        // @formatter:off
        mvc.perform(get("/users?size=10").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(equalTo(5))))
                .andExpect(jsonPath("$[0].firstName", is("Alice")))
                .andExpect(jsonPath("$[1].firstName", is("John")))
                .andExpect(jsonPath("$[2].firstName", is("User3")))
                .andExpect(jsonPath("$[3].firstName", is("User4")))
                .andExpect(jsonPath("$[4].firstName", is("User5")));
        // @formatter:on
    }

    @Test
    public void givenUsers_whenGetAllUsersSortByLastName_thenShowOnlyFiveAndReturnStatus200() throws Exception {
        createTestUser("John", "Doe", "john@doe.com", "123456789");
        createTestUser("Alice", "Smith", "alice@smith.com", "987654321");
        createTestUser("User3", "Three", "user@three.com", "987654321");
        createTestUser("User4", "Four", "user@four.com", "987654321");
        createTestUser("User5", "Five", "user@five.com", "987654321");
        createTestUser("User6", "Six", "user@six.com", "987654321");

        // @formatter:off
        mvc.perform(get("/users?size=5&sort=lastName,asc").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(equalTo(5))))
                .andExpect(jsonPath("$[0].firstName", is("John")))
                .andExpect(jsonPath("$[1].firstName", is("User5")))
                .andExpect(jsonPath("$[2].firstName", is("User4")))
                .andExpect(jsonPath("$[3].firstName", is("User6")))
                .andExpect(jsonPath("$[4].firstName", is("Alice")));
        // @formatter:on
    }
}