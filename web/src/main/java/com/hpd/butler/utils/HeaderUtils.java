package com.hpd.butler.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Created by teddyzhou on 2017/10/29.
 */
public class HeaderUtils {
    public static String getCurrentUser(){
        if (null !=SecurityContextHolder.getContext().getAuthentication()){
            UserDetails currentAuth = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(null!=currentAuth){
                return currentAuth.getUsername();
            }
        }

        return "";
    }
}
