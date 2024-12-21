package br.com.weblinker.users.integration;

import br.com.weblinker.users.models.Company;
import br.com.weblinker.users.models.User;
import br.com.weblinker.users.repositories.CompaniesRepository;
import br.com.weblinker.users.repositories.UsersRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

public class AbstractUsersClass {

    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected ObjectMapper objectMapper;  // Jackson ObjectMapper

    @Autowired
    protected UsersRepository usersRepository;

    @Autowired
    protected CompaniesRepository companiesRepository;

    @BeforeEach
    public void createCompany() {
        companiesRepository.save(new Company("My Company"));
    }

    @AfterEach
    public void resetDb() {
        usersRepository.deleteAll();
    }

    protected User createTestUser(String firstName, String lastName, String email, String phone) {
        User user = new User(firstName, lastName, email, phone);
        Company company = companiesRepository.findFirstByOrderByIdAsc().orElse(null);
        if (company == null) {
            company = companiesRepository.save(new Company("My Company"));
        }
        user.setCompanyId(company.getId());
        usersRepository.saveAndFlush(user);

        return user;
    }

    protected User createDeletedTestUser(String firstName, String lastName, String email, String phone) {
        User user = new User(firstName, lastName, email, phone);
        Company company = companiesRepository.findFirstByOrderByIdAsc().orElse(null);
        if (company == null) {
            company = companiesRepository.save(new Company("My Company"));
        }
        user.setCompanyId(company.getId());
        user.setDeletedAt(LocalDate.now().atStartOfDay());
        usersRepository.saveAndFlush(user);

        return user;
    }
}
