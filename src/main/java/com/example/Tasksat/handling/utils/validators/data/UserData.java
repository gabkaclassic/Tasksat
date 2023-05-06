package com.example.Tasksat.handling.utils.validators.data;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserData extends AccountData {

    private String email;
    public UserData(String login, String password, String email) {
        super(login, password);
        this.email = email;
    }
}
