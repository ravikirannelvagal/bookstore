package com.task.bookstore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class Login {

    @NotNull(message = "Username cannot be empty")
    @Email(message = "Invalid email address")
    private String username;

    @NotNull(message = "Password cannot be empty")
    private String password;

    @JsonIgnore
    @AssertTrue(message = "Password should not be blank")
    public boolean isPasswordBlank() {
        return password != null && !"".equals(password);
    }

    @JsonIgnore
    @AssertTrue(message = "Username should not be blank")
    public boolean isUsernameBlank() {
        return username != null && !"".equals(username);
    }

    @JsonIgnore
    @AssertTrue(message = "Username should not belong to test.com domain")
    public boolean isUsernameDomainValid() {
        if(username.contains("@")) {
            String[] usernameParts = username.split("@");
            return !"test.com".equalsIgnoreCase(usernameParts[1]);
        }
        return true;
    }

    @JsonIgnore
    @AssertTrue(message = "Invalid Email address for username")
    public boolean isVaildEmailAddress(){
        if(this.username != null && !"".equals(this.username)) {
            String emailRegex = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
            return this.username.matches(emailRegex);
        }
        return true;
    }
}
