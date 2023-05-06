package com.example.Tasksat.handling.utils.validators;

import com.example.Tasksat.handling.utils.validators.data.AccountData;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@Component("accountValidator")
public abstract class AccountDataValidatorUtil implements AccountValidator {
    private static final Pattern PASSWORD_PATTERN_FIRST = Pattern.compile("\\w+");
    private static final Pattern PASSWORD_PATTERN_SECOND = Pattern.compile("\\d+");
    private static final Pattern PASSWORD_PATTERN_THREE = Pattern.compile("[^0-9a-zA-Z]+");
    private static final Pattern LOGIN_PATTERN = Pattern.compile("[a-zA-z]+");
    private static final int MIN_LENGTH_LOGIN = 2;
    private static final int MAX_LENGTH_LOGIN = 32;
    private static final int MIN_LENGTH_PASSWORD = 8;
    private static final int MAX_LENGTH_PASSWORD = 128;

    public boolean validate(AccountData data,
                            List<String> remarks) {

        return validLogin(data.getLogin(), remarks) && validPassword(data.getPassword(), remarks);
    }

    public boolean validPassword(String password, List<String> remarks) {

        if(Objects.isNull(password)
                || (password = StringUtils.trimAllWhitespace(password)).isEmpty()) {

            remarks.add("Enter your password");

            return false;
        }

        boolean isCorrectPassword = (password.length() >= MIN_LENGTH_PASSWORD)
                && (password.length() <= MAX_LENGTH_PASSWORD)
                && PASSWORD_PATTERN_FIRST.matcher(password).find()
                && PASSWORD_PATTERN_SECOND.matcher(password).find()
                && PASSWORD_PATTERN_THREE.matcher(password).find();

        if(!isCorrectPassword)
            remarks.add(String.format("The password must contain %d-%d characters, of which 1 digit and 1 specified character", MIN_LENGTH_PASSWORD, MAX_LENGTH_PASSWORD));

        return isCorrectPassword;
    }

    public boolean validLogin(String login, List<String> remarks) {

        boolean isCorrectLogin = Objects.nonNull(login)
                && LOGIN_PATTERN.matcher((login = login.trim())).find()
                && (login.length() >= MIN_LENGTH_LOGIN)
                && (login.length() <= MAX_LENGTH_LOGIN);

        if(!isCorrectLogin)
            remarks.add(String.format("The login can contain %d-%d characters and 1 letter", MIN_LENGTH_LOGIN, MAX_LENGTH_LOGIN));

        return isCorrectLogin;
    }
}
