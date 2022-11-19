package com.mdm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author dongming.ma
 * @date 2022/11/19 13:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
public class Author {
    private Long id;
    private String name;
    private Integer age;
    private String intro;
    private List<Book> books;
}
