package com.example.Tasksat.handling.utils.validators;

import com.example.Tasksat.handling.utils.validators.data.UserData;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@Component("userValidator")
public class UserDataValidatorUtil extends AccountDataValidatorUtil {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("\\w+@\\w+\\.\\w+");

    public boolean validate(UserData data,
                            List<String> remarks) {

        return validLogin(data.getLogin(), remarks) && validPassword(data.getPassword(), remarks) && validEmail(data.getEmail(), remarks);
    }

    public boolean validEmail(String login, List<String> remarks) {

        boolean isCorrectEmailAddress = Objects.nonNull(login)
                && !login.isEmpty()
                && EMAIL_PATTERN.matcher(login).find();

        if (!isCorrectEmailAddress)
            remarks.add("Invalid email address");

        return isCorrectEmailAddress;
    }
}
