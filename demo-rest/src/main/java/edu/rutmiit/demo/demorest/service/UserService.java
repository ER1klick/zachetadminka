package edu.rutmiit.demo.demorest.service;

import edu.rutmiit.demo.demorest.storage.InMemoryStorage;
import edu.rutmiit.demo.stoyakapicontract.dto.UserRequest;
import edu.rutmiit.demo.stoyakapicontract.dto.UserResponse;
import edu.rutmiit.demo.stoyakapicontract.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final InMemoryStorage storage;
    public UserService(InMemoryStorage storage) { this.storage = storage; }

    public UserResponse registerUser(UserRequest request) {
        long newId = storage.userSequence.incrementAndGet();
        UserResponse newUser = new UserResponse(newId, request.username(), request.email(), request.phoneNumber(), request.firstName(), request.lastName(), 5.0, request.userType());
        storage.users.put(newId, newUser);
        return newUser;
    }

    public UserResponse findUserById(Long id) {
        UserResponse user = storage.users.get(id);
        if (user == null) { throw new ResourceNotFoundException("User", id); }
        return user;
    }
}