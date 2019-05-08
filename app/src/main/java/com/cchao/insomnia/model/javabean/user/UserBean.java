package com.cchao.insomnia.model.javabean.user;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : cchao
 * @version 2019-02-18
 */
@Data
@Accessors(chain = true)
public class UserBean {
    boolean isVisitor;
    String token;
    String email;
    String nikeName;
    String avatar;
    int getLike;
    int age;
}
