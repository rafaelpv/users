package br.com.weblinker.users.services;

import java.util.List;

import br.com.weblinker.users.exceptions.NotFoundException;
import br.com.weblinker.users.models.User;
import br.com.weblinker.users.repositories.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersService {

    private final Logger LOG = LoggerFactory.getLogger(UsersService.class);

    private final UsersRepository usersRepository;

    @Autowired
    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public List<User> findAll() {
        LOG.info("Finding all users!");

        return usersRepository.findAll();
    }

    public User findById(Long id) {
        return usersRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found!"));
    }

    public User create(User user) {
        LOG.info("Creating user!");

        user.setCompanyId(1L);

        return usersRepository.save(user);
    }

    public User update(Long userId, User user) {
        LOG.info("Updating user " + user.getId() + "!");

        User entity = this.findById(userId);
        entity.setFirstName(user.getFirstName());
        entity.setLastName(user.getLastName());
        entity.setPhone(user.getPhone());

        return usersRepository.save(entity);
    }

    public void delete(Long userId) {
        LOG.info("Deleting user " + userId + "!");

        User entity = this.findById(userId);

        usersRepository.delete(entity);
    }
}
