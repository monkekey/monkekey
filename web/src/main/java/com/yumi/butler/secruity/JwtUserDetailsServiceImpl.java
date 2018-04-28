package com.yumi.butler.secruity;

import com.yumi.butler.constant.CommonFlag;
import com.yumi.butler.domain.ISysuserRepository;
import com.yumi.butler.domain.Sysuser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private ISysuserRepository iSysuserRepository;

    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        Sysuser user = iSysuserRepository.findByAccount(account);

        if (user == null || !user.getFlag().equals(CommonFlag.VALID.getValue())) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", account));
        } else {
            JwtUser jwtUser = JwtUserFactory.create(user);
            return jwtUser;
        }
    }
}
