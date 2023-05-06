package com.example.Tasksat.data.entities.accounts.workers;

import com.example.Tasksat.data.entities.accounts.Account;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import static com.example.Tasksat.data.entities.accounts.Authority.WORKER;
@Document("workers")
@Data
public class Worker extends Account {

    public Worker() {
        authorities.add(WORKER);
    }
}
