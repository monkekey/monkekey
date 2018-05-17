package com.hpd.butler.auth;

import com.hpd.butler.domain.Sysuser;

public interface AuthService {
    Sysuser register(Sysuser userToAdd);
    String login(String username, String password);
    String refresh(String oldToken);
}
