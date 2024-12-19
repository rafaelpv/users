package br.com.weblinker.users.controllers;

import br.com.weblinker.users.models.User;
import br.com.weblinker.users.services.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "Users endpoints")
public class UsersController {

    private final UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @Operation(summary = "Get all users")
    @GetMapping("")
    public List<User> findAll() {
        return usersService.findAll();
    }

    @Operation(summary = "Get user by ID")
    @GetMapping("/{userId}")
    public User findById(@PathVariable Long userId) {
        return usersService.findById(userId);
    }

    @Operation(summary = "Create an user")
    @PostMapping("")
    public ResponseEntity<User> create(@RequestBody User user) {
        User createdUser = usersService.create(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @Operation(summary = "Update an user")
    @PutMapping("/{userId}")
    public User update(@PathVariable Long userId, @RequestBody User user) {
        return usersService.update(userId, user);
    }

    @Operation(summary = "Delete an user")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable Long userId) {
        usersService.delete(userId);

        return ResponseEntity.noContent().build();
    }
}
