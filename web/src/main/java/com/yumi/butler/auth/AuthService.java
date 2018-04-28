package com.yumi.butler.auth;

import com.yumi.butler.domain.Sysuser;

public interface AuthService {
    Sysuser register(Sysuser userToAdd);
    String login(String username, String password);
    String refresh(String oldToken);
}
