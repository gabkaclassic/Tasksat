package com.example.Tasksat.data.entities.accounts.users;

import com.example.Tasksat.data.entities.accounts.Account;
import com.example.Tasksat.data.entities.tasks.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import static com.example.Tasksat.data.entities.accounts.Authority.LOCKED;
import static com.example.Tasksat.data.entities.accounts.Authority.USER;

import java.util.HashSet;
import java.util.Set;

@Document("users")
@Data
@AllArgsConstructor
public class User extends Account {

    @Indexed(unique = true)
    private String email;

    @Indexed(unique = true)
    private String code;

    private Set<Task> completedTasks = new HashSet<>();

    public User() {
        authorities.add(LOCKED);
    }

    public void unlock() {
        code = null;
        authorities.clear();
        authorities.add(USER);
    }
}
