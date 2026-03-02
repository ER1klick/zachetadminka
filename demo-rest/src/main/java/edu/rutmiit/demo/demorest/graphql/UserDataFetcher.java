package edu.rutmiit.demo.demorest.graphql;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import edu.rutmiit.demo.demorest.service.UserService;
import edu.rutmiit.demo.stoyakapicontract.dto.UserRequest;
import edu.rutmiit.demo.stoyakapicontract.dto.UserResponse;
import edu.rutmiit.demo.stoyakapicontract.Enums.UserType;


import java.util.Map;

@DgsComponent
public class UserDataFetcher {

    private final UserService userService;

    public UserDataFetcher(UserService userService) {
        this.userService = userService;
    }

    @DgsQuery
    public UserResponse userById(@InputArgument Long id) {
        return userService.findUserById(id);
    }

    @DgsMutation
    public UserResponse createUser(@InputArgument("input") Map<String, Object> input) {
        UserRequest request = new UserRequest(
                (String) input.get("username"),
                (String) input.get("password"),
                (String) input.get("email"),
                (String) input.get("phoneNumber"),
                (String) input.get("firstName"),
                (String) input.get("lastName"),
                UserType.valueOf((String) input.get("userType"))
        );
        return userService.registerUser(request);
    }
}