package edu.rutmiit.demo.demorest.controllers;

import edu.rutmiit.demo.demorest.assemblers.UserModelAssembler;
import edu.rutmiit.demo.demorest.service.UserService;
import edu.rutmiit.demo.stoyakapicontract.dto.StatusResponse;
import edu.rutmiit.demo.stoyakapicontract.dto.UserRequest;
import edu.rutmiit.demo.stoyakapicontract.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserService userService;
    private final UserModelAssembler UserModelAssembler;

    public UserController(UserService userService, UserModelAssembler UserModelAssembler) {
        this.userService = userService;
        this.UserModelAssembler = UserModelAssembler;
    }

    @PostMapping("/api/users")
    public ResponseEntity<EntityModel<UserResponse>> registerUser(@Valid @RequestBody UserRequest userRequest) {
        UserResponse user = userService.registerUser(userRequest);
        EntityModel<UserResponse> entityModel = UserModelAssembler.toModel(user);
        return ResponseEntity
                .created(entityModel.getRequiredLink("self").toUri())
                .body(entityModel);
    }

    @GetMapping("/api/users/{id}")
    public EntityModel<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse user = userService.findUserById(id);
        return UserModelAssembler.toModel(user);
    }

    @PutMapping("/api/users/{id}")
    public UserResponse updateUser(@PathVariable Long id, @Valid @RequestBody UserRequest userRequest) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @DeleteMapping("/api/users/{id}")
    public StatusResponse deleteUser(@PathVariable Long id) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}