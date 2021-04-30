package com.swx.flowable.bean.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Person implements Serializable {

    private Long id;

    private String username;

    private Integer age;

    private String email;

    private Date brithDate;

}
