package br.com.weblinker.users.controllers.interfaces;

import br.com.weblinker.users.dto.CreateUserRequest;
import br.com.weblinker.users.dto.UpdateUserRequest;
import br.com.weblinker.users.dto.UserResponse;
import br.com.weblinker.users.exceptions.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
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
    @Parameters({
        @Parameter(
            name = "page",
            description = "Page number (zero-based). Default is 0.",
            schema = @Schema(type = "integer", example = "0", minimum = "0")
        ),
        @Parameter(
            name = "size",
            description = "Number of elements per page. Default is 10, maximum is 50.",
            schema = @Schema(type = "integer", example = "10", minimum = "1", maximum = "50")
        ),
        @Parameter(
            name = "sort",
            description = "Sorting criteria in the format: `property,(asc|desc)`. Default direction is ascending. " +
                    "Supported properties: `firstName`, `lastName`, `phone`, `email`, `id`.",
            schema = @Schema(type = "string", example = "firstName,asc,email,desc")
        )
    })
    @GetMapping("")
    public List<UserResponse> getAll(@Parameter(hidden = true) Pageable pageable);

    @Operation(summary = "Get user information by ID",
        description = "Fetches user details by user ID",
        responses = {
            @ApiResponse(responseCode = "200", description = "User details", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class)),
            }),
            @ApiResponse(
                responseCode = "404",
                description = "User not found",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value = ApiError.USER_NOT_FOUND)
                )
            )
        })
    public UserResponse getById(@PathVariable Long userId);

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
                description = "Invalid user data provided",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value = ApiError.BAD_REQUEST)
                )
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
                description = "Invalid user data provided",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value = ApiError.BAD_REQUEST)
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "User not found",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value = ApiError.USER_NOT_FOUND)
                )
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
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value = ApiError.USER_NOT_FOUND)
                )
            )
        }
    )
    public ResponseEntity<Void> delete(@PathVariable Long userId);
}
