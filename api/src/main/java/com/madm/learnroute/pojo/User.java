package com.madm.learnroute.pojo;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Accessors(chain = true)
public class User {
    @NonNull
    private Integer id;
    @NonNull
    private String name;
    private List<AuthParam> auth;

    public User(User user) {
        this.id = user.getId();
        this.name = user.getName();
    }
}
