package br.com.weblinker.users.integration;

import br.com.weblinker.users.UsersApplication;
import br.com.weblinker.users.models.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = UsersApplication.class)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude=SecurityAutoConfiguration.class)
@ActiveProfiles("test")
public class UsersCreateUserTest extends AbstractUsersClass {

    @Test
    public void whenValidInput_thenCreateUser() throws IOException, Exception {
        User user = new User("John", "Doe", "john@doe.com", "123456789");
        String userJson = objectMapper.writeValueAsString(user);

        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson));

        List<User> found = usersRepository.findAll();
        assertThat(found).extracting(User::getFirstName).containsOnly("John");
    }

    @Test
    public void whenInvalidInput_thenStatus400() throws Exception {
        User user = new User("J", "Doe", "john@doe.com", "123456789");
        String userJson = objectMapper.writeValueAsString(user);

        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void whenMissingInput_thenStatus400() throws Exception {
        String userJson = """
            {
                "firstName": "John",
                "email": "john@doe.com",
                "phone": "123456789"
            }
            """;

        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isBadRequest());
    }

}