package com.jungle.start.mybatis.entity;

import lombok.Data;

import java.util.Date;

@Data
public class UserEntity {
    private Long id;
    private String name;
    private Date createTime;
    private String phone;
    private String nickname;

}
