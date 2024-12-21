package br.com.weblinker.users.controllers;

import br.com.weblinker.users.controllers.interfaces.UsersControllerInterface;
import br.com.weblinker.users.dto.CreateUserRequest;
import br.com.weblinker.users.dto.UpdateUserRequest;
import br.com.weblinker.users.dto.UserResponse;
import br.com.weblinker.users.models.User;
import br.com.weblinker.users.services.UsersService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UsersController implements UsersControllerInterface {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UsersService usersService;

    @GetMapping("")
    public List<UserResponse> getAll(
            @PageableDefault(page = 0, size = 10)
            @SortDefault.SortDefaults({
                @SortDefault(sort = "firstName", direction = Sort.Direction.ASC)
            })
            Pageable pageable) {

        Page<User> users = usersService.findAll(pageable);

        return users.stream()
                .map(user -> modelMapper.map(user, UserResponse.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{userId}")
    public UserResponse getById(@PathVariable Long userId) {

        User user = usersService.findById(userId);

        return modelMapper.map(user, UserResponse.class);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public ResponseEntity<UserResponse> create(@Valid @RequestBody CreateUserRequest userRequest) {

        User user = modelMapper.map(userRequest, User.class);
        User createdUser = usersService.create(user);
        UserResponse userResponse = modelMapper.map(createdUser, UserResponse.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @PutMapping("/{userId}")
    public UserResponse update(@PathVariable Long userId, @Valid @RequestBody UpdateUserRequest userRequest) {

        User user = modelMapper.map(userRequest, User.class);
        User updatedUser = usersService.update(userId, user);

        return modelMapper.map(updatedUser, UserResponse.class);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable Long userId) {

        usersService.delete(userId);

        return ResponseEntity.noContent().build();
    }
}
