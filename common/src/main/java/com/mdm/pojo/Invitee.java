package com.mdm.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.relational.core.sql.In;

@Data
@AllArgsConstructor
public class Invitee implements Comparable<Invitee> {

    private String userId;
    public String nickName;
    public String avatarUrl;

    @Override
    public int compareTo(@NotNull Invitee invitee) {
        return this.userId.compareTo(invitee.getUserId());
    }
}
