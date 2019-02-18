package com.cchao.sleeping.model.javabean.user;

import lombok.Data;

/**
 * @author : cchao
 * @version 2019-02-18
 */
@Data
public class UserBean {
    String token;
    String email;
    String nikeName;
    String avatar;
    int age;
}
