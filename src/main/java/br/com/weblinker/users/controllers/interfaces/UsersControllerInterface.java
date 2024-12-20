package br.com.weblinker.users.controllers.interfaces;

import br.com.weblinker.users.dto.CreateUserRequest;
import br.com.weblinker.users.dto.UpdateUserRequest;
import br.com.weblinker.users.dto.UserResponse;
import br.com.weblinker.users.models.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Users endpoints")
public interface UsersControllerInterface {

    @Operation(
        summary = "Get all users",
        description = "Fetches a list of all users in the system",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "List of users returned successfully",
                content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = UserResponse.class))
                )
            )
        }
    )
    @GetMapping("")
    public List<UserResponse> findAll();

    @Operation(summary = "Get user information by ID",
        description = "Fetches user details by user ID",
        responses = {
            @ApiResponse(responseCode = "200", description = "User details", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class)),
            })
        })
    public UserResponse findById(@PathVariable Long userId);

    @Operation(
        summary = "Create a new user",
        description = "Creates a new user in the system and returns the user details.",
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "User created successfully",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid input",
                content = @Content(mediaType = "application/json")
            )
        }
    )
    public ResponseEntity<UserResponse> create(@Valid @RequestBody CreateUserRequest userRequest);

    @Operation(
        summary = "Update an existing user",
        description = "Updates an existing user with the provided user details",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "User updated successfully",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid user data",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "404",
                description = "User not found",
                content = @Content(mediaType = "application/json")
            )
        }
    )
    public UserResponse update(@PathVariable Long userId, @Valid @RequestBody UpdateUserRequest userRequest);

    @Operation(
        summary = "Delete a user",
        description = "Deletes the user with the specified user ID",
        responses = {
            @ApiResponse(
                responseCode = "204",
                description = "User deleted successfully",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "404",
                description = "User not found",
                content = @Content(mediaType = "application/json")
            )
        }
    )
    public ResponseEntity<Void> delete(@PathVariable Long userId);
}
