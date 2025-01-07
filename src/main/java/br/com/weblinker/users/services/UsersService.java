package br.com.weblinker.users.services;

import br.com.weblinker.users.exceptions.ResourceNotFoundException;
import br.com.weblinker.users.models.Company;
import br.com.weblinker.users.models.User;
import br.com.weblinker.users.repositories.CompaniesRepository;
import br.com.weblinker.users.repositories.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsersService implements UserDetailsService {

    private final Logger LOG = LoggerFactory.getLogger(UsersService.class);

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private CompaniesRepository companiesRepository;

    public Page<User> findAll(Pageable pageable) {
        return usersRepository.findAll(pageable);
    }


    public User findMe() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return (User) authentication.getPrincipal();
    }

    public User findById(Long id) {
        return usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
    }

    public User create(User user) {
        LOG.info("Creating user: " + user);

        // TODO here maybe we do not need this
        Company company = companiesRepository.findFirstByOrderByIdAsc().orElse(null);
        if (company == null) {
            companiesRepository.save(new Company("My Company"));
        }

        user.setCompany(company);

        return usersRepository.save(user);
    }

    public User update(Long userId, User user) {
        LOG.info("Updating user: ID " + user.getId() + ", Data: " + user);

        User entity = this.findById(userId);
        entity.setFirstName(user.getFirstName() != null ? user.getFirstName() : entity.getFirstName());
        entity.setLastName(user.getLastName() != null ? user.getLastName() : entity.getLastName());
        entity.setEmail(user.getEmail() != null ? user.getEmail() : entity.getEmail());
        entity.setPhone(user.getPhone() != null ? user.getPhone() : entity.getPhone());

        return usersRepository.save(entity);
    }

    public void delete(Long userId) {
        LOG.info("Deleting user: ID " + userId);

        User entity = this.findById(userId);

        usersRepository.delete(entity);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = usersRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found!");
        }

        return user;
    }
}
