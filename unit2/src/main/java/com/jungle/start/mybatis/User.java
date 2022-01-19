package com.jungle.start.mybatis;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class User {
    List<Order> orders;
    String name;
    Integer age;

}
