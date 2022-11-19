package com.mdm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author dongming.ma
 * @date 2022/11/19 13:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
public class Book {
    private Long id;
    private String name;
    private String category;
    private Integer score;
    private String intro;
}
