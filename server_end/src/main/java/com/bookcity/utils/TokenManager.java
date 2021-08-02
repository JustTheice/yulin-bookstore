package com.bookcity.utils;

import com.bookcity.entity.User;

import java.util.HashMap;
import java.util.UUID;

/**
 *      管理服务器token
 */
public class TokenManager {
    //token仓库
    static HashMap<String, User> tokens = new HashMap<>();

    //创建一个token
    public static String createToken(User user){
        String token = String.valueOf(UUID.randomUUID());

        tokens.put(token, user);
        return token;
    }

    //查找token对应用户
    public static User find(String token){
        return tokens.get(token);
    }

    //删除token
    public static void delete(String token){
        tokens.remove(token);
    }

    //更新token
    public static void update(String token, User user){
        tokens.put(token, user);
    }

    //清空tokens
    public static void clear(){
        tokens.clear();
    }
}
