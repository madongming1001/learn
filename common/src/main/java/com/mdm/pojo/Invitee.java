package com.mdm.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.relational.core.sql.In;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Invitee implements Comparable<Invitee> {
    private Long userId;
    public String nickName;
    public String avatarUrl;
    public AuthParam authParam;

    public Invitee(Long userId, String nickName, String avatarUrl) {
        this.userId = userId;
        this.nickName = nickName;
        this.avatarUrl = avatarUrl;
    }

    @Override
    public int compareTo(@NotNull Invitee invitee) {
        return this.userId.compareTo(invitee.getUserId());
    }
}
