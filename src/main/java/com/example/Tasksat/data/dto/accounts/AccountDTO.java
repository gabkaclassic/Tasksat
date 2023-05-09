package com.example.Tasksat.data.dto.accounts;

import com.example.Tasksat.data.entities.accounts.Account;
import com.example.Tasksat.data.entities.accounts.admins.Admin;
import com.example.Tasksat.data.entities.accounts.users.User;
import com.example.Tasksat.data.entities.accounts.workers.Worker;
import lombok.Data;

@Data
public class AccountDTO {

    private String login;
    private String id;
    private String type;

    private AccountDTO(String id, String login) {
        this.id = id;
        this.login = login;
    }
    public AccountDTO(User user) {
        this(user.getId(), user.getLogin());
        this.type = "User";
    }
    public AccountDTO(Worker worker) {
        this(worker.getId(), worker.getLogin());
        this.type = "Worker";
    }
    public AccountDTO(Admin admin) {
        this(admin.getId(), admin.getLogin());
        this.type = "Admin";
    }
}
