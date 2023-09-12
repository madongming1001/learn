package com.madm.learnroute.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mdm.pojo.AuthParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author madongming
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UserDto implements Serializable {
    private Integer id;
    @NonNull
    @JsonProperty("name")//用于反序列化和序列化都为name jackson
    @JsonAlias(value = {"userName", "accountName"})//用于反序列化，把设置的值都映射为name jackson
    @JSONField(name = "name")//fastjson
    private String name;
    transient private List<AuthParam> auth;


    public UserDto(UserDto userDto) {
        this.id = userDto.getId();
        this.name = userDto.getName();
    }

    public UserDto(Integer id, String... name) {
        this.id = id;
        if (Objects.nonNull(name)) {
            this.auth = Arrays.stream(name).map(AuthParam::new).collect(Collectors.toList());
        }
    }
}
