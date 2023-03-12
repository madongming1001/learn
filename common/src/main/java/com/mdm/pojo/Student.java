package com.mdm.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author dongming.ma
 * @date 2023/3/11 11:06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {

    //唯一
    private String id;

    private String name;
}
