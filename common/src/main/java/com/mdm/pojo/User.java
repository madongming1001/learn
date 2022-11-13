package com.mdm.pojo;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Accessors(chain = true)
public class User implements Serializable {
    private Integer id;
    @NonNull
    private String name;
    private List<AuthParam> auth;

    public User(User user) {
        this.id = user.getId();
        this.name = user.getName();
    }

    public User(Integer id, String... name) {
        this.id = id;
        if (Objects.nonNull(name)) {
            this.auth = Arrays.stream(name).map(AuthParam::new).collect(Collectors.toList());
    }
}
}
