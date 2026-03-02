package edu.rutmiit.demo.stoyakapicontract.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import edu.rutmiit.demo.stoyakapicontract.Enums.UserType;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.Objects;

@Relation(collectionRelation = "users", itemRelation = "user")
@JsonRootName(value = "user")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse extends RepresentationModel<UserResponse> {

    private final Long userId;
    private final String username;
    private final String email;
    private final String phoneNumber;
    private final String firstName;
    private final String lastName;
    private final Double rating;
    private final UserType userType;

    public UserResponse(Long userId, String username, String email, String phoneNumber, String firstName, String lastName, Double rating, UserType userType) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.rating = rating;
        this.userType = userType;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Double getRating() {
        return rating;
    }

    public UserType getUserType() {
        return userType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UserResponse that = (UserResponse) o;
        return Objects.equals(userId, that.userId) && Objects.equals(username, that.username) && Objects.equals(email, that.email) && Objects.equals(phoneNumber, that.phoneNumber) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(rating, that.rating) && userType == that.userType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userId, username, email, phoneNumber, firstName, lastName, rating, userType);
    }
}