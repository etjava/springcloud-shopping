package com.etjava.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@TableName("t_user")
@Data
public class User {

    @TableId(type= IdType.AUTO)
    private Integer id;
    private String username;
    private String password;
    private Date registerDate;
    private String address;
    private String phoneNumber;
    // 姓名
    private String name;

}
