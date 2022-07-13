package com.mdm.pojo;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Accessors(chain = true)
public class User {
    private Integer id;
    @NonNull
    private String name;
    private List<AuthParam> auth;

    public User(User user) {
        this.id = user.getId();
        this.name = user.getName();
    }

    public User(Integer id, @NonNull String name) {
        this.id = id;
        this.name = name;
    }
}
