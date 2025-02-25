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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = UsersApplication.class)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude=SecurityAutoConfiguration.class)
@ActiveProfiles("test")
public class UsersUpdateUserTest extends AbstractUsersClass {

    @Test
    public void whenValidInput_thenUpdateUser() throws IOException, Exception {
        User user = createTestUser("John", "Doe", "john@doe.com", "123456789");
        User userUpdated = new User("John2", "Doe2", "john2@doe.com", "023456789");
        String userJson = objectMapper.writeValueAsString(userUpdated);

        mvc.perform(put("/users/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson));

        List<User> found = usersRepository.findAll();
        assertThat(found).extracting(User::getFirstName).containsOnly("John2");
        assertThat(found).extracting(User::getLastName).containsOnly("Doe2");
        assertThat(found).extracting(User::getEmail).containsOnly("john2@doe.com");
        assertThat(found).extracting(User::getPhone).containsOnly("023456789");
    }

    @Test
    public void whenNoInputs_thenStatus400() throws Exception {
        User user = createTestUser("John", "Doe", "john@doe.com", "123456789");

        mvc.perform(put("/users/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenInvalidInput_thenStatus400() throws Exception {
        User user = createTestUser("John", "Doe", "john@doe.com", "123456789");

        User userUpdated = new User("J", "Doe2", "john2@doe.com", "023456789");
        String userJson = objectMapper.writeValueAsString(userUpdated);

        mvc.perform(put("/users/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void whenUserNotFoundInput_thenStatus400() throws Exception {
        User user = new User("John", "Doe", "john@doe.com", "123456789");
        String userJson = objectMapper.writeValueAsString(user);

        mvc.perform(delete("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

}