package com.madm.learnroute.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class User {
    private Integer id;
    private String name;
}
