package com.madm.learnroute.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@Accessors(chain = true)
public class User {
    private Integer id;
    private String name;
    private List<AuthParam> auth;
}
