package com.yumi.butler.secruity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.yumi.butler.domain.Sysuser;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public final class JwtUserFactory {

    private JwtUserFactory() {
    }

    public static JwtUser create(Sysuser user) {
        List<String> roleList = new ArrayList<String>();
        roleList.add("admin");

        return new JwtUser(
                String.valueOf(user.getId()),
                user.getAccount(),
                user.getPassword(),
                "",
                mapToGrantedAuthorities(roleList),
                DateUtils.addMinutes(new Date(),60)
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<String> authorities) {
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<GrantedAuthority>();
        GrantedAuthority grantedAuthority = null;
        for (String role : authorities){
            grantedAuthority = new SimpleGrantedAuthority(role);
            grantedAuthorityList.add(grantedAuthority);
        }

        return grantedAuthorityList;
    }
}

