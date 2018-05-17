package com.hpd.butler.auth;

import com.hpd.butler.constant.CommonFlag;
import com.hpd.butler.secruity.JwtTokenUtil;
import com.hpd.butler.domain.ISysuserRepository;
import com.hpd.butler.domain.Sysuser;
import com.hpd.butler.secruity.JwtUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;
    private JwtTokenUtil jwtTokenUtil;
    private ISysuserRepository iSysuserRepository;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    public AuthServiceImpl(
            AuthenticationManager authenticationManager,
            UserDetailsService userDetailsService,
            JwtTokenUtil jwtTokenUtil,
            ISysuserRepository iSysuserRepository) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.iSysuserRepository = iSysuserRepository;
    }

    @Override
    public Sysuser register(Sysuser userToAdd) {
        final String account = userToAdd.getAccount();
        if(iSysuserRepository.findByAccount(account)!=null) {
            return null;
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        final String rawPassword = userToAdd.getPassword();
        userToAdd.setPassword(encoder.encode(rawPassword));
        String salt = UUID.randomUUID().toString().replace("-","").substring(10);
        userToAdd.setSalt(salt);
        userToAdd.setFlag(CommonFlag.VALID.getValue());

        return iSysuserRepository.saveAndFlush(userToAdd);
    }

    @Override
    public String login(String username, String password) {
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
        // Perform the security
        final Authentication authentication = authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Reload password post-security so we can generate token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        final String token = jwtTokenUtil.generateToken(userDetails);
        return token;
    }

    @Override
    public String refresh(String oldToken) {
        final String token = oldToken.substring(tokenHead.length());
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())){
            return jwtTokenUtil.refreshToken(token);
        }
        return null;
    }
}
