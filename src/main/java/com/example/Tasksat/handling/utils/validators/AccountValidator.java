package com.example.Tasksat.handling.utils.validators;

import com.example.Tasksat.handling.utils.validators.data.AccountData;

import java.util.List;

public interface AccountValidator {

    boolean validate(AccountData data, List<String> violations);

}
