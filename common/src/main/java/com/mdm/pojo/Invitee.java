package com.mdm.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Invitee {

    private String userId;
    public String nickName;
    public String avatarUrl;
}
