package com.example.Tasksat.data.entities.accounts.admins;

import com.example.Tasksat.data.entities.accounts.Account;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import static com.example.Tasksat.data.entities.accounts.Authority.ADMIN;

@Document("admins")
@Data
public class Admin extends Account {
    public Admin() {
        authorities.add(ADMIN);
    }
}
