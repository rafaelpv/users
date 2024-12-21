package br.com.weblinker.users;

import br.com.weblinker.users.controllers.UsersController;
import br.com.weblinker.users.models.User;
import br.com.weblinker.users.services.UsersService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = UsersController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class UsersRestControllerUnitTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private UsersService usersService;


    @Test
    public void givenUsers_whenGetUsers_thenReturnJsonArray() throws Exception {
        User user = new User("John", "Doe", "john@doe.com", "123456789");
        List<User> allUsers = Arrays.asList(user);

        Pageable pageable = Pageable.ofSize(10);
        Page<User> usersPage = new PageImpl<>(allUsers, pageable, allUsers.size());

        given(usersService.findAll(any(Pageable.class))).willReturn(usersPage);

        mvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName", is(user.getFirstName())));
    }
}
