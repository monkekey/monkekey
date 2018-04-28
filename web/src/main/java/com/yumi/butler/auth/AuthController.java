package com.yumi.butler.auth;

import com.yumi.butler.common.RequestResult;
import com.yumi.butler.domain.*;
import com.yumi.butler.domain.*;
import com.yumi.butler.secruity.JwtAuthenticationRequest;
import com.yumi.butler.secruity.JwtAuthenticationResponse;
import io.swagger.annotations.Api;
import com.yumi.butler.vo.UserVo;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@Api(value = "AuthController", description = "验证接口")
public class AuthController {
    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private ISysuserRepository iSysuserRepository;
    @Autowired
    private ISysuserdetailRepository iSysuserdetailRepository;
    @Autowired
    private ISysuserroleRepository iSysuserroleRepository;
    @Autowired
    private ISysroleRepository iSysroleRepository;

    @Autowired
    private AuthService authService;

    @RequestMapping(value = "${jwt.route.authentication.path}", method = RequestMethod.POST)
    public RequestResult createAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest) throws AuthenticationException{
        final String token = authService.login(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        UserVo userVo = new UserVo();
        Sysuser sysuser = iSysuserRepository.findByAccount(authenticationRequest.getUsername());
        if(null != sysuser){
            Sysuserdetail sysuserdetail = iSysuserdetailRepository.findByUid(sysuser.getId());

            List<Long> roleidList = new ArrayList<>();
            List<Sysuserrole> sysuserroleList = iSysuserroleRepository.findByUid(sysuser.getId());
            for (Sysuserrole sysuserrole : sysuserroleList){
                roleidList.add(sysuserrole.getRoleId());
            }
            List<Sysrole> sysroleList = iSysroleRepository.findByIdIn(roleidList);

            try {
                PropertyUtils.copyProperties(userVo, sysuser);

                if(null != sysuserdetail){
                    PropertyUtils.copyProperties(userVo, sysuserdetail);
                    userVo.setId(sysuser.getId());

                }
                userVo.setPassword("");
                userVo.setSysroleList(sysroleList);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        Map<String, Object> result = new HashedMap();
        result.put("token", token);//new JwtAuthenticationResponse(token)
        result.put("userinfo", userVo);
        // Return the token
        return RequestResult.success(result);
    }

    @RequestMapping(value = "${jwt.route.authentication.refresh}", method = RequestMethod.GET)
    public RequestResult refreshAndGetAuthenticationToken(
            HttpServletRequest request) throws AuthenticationException{
        String token = request.getHeader(tokenHeader);
        String refreshedToken = authService.refresh(token);
        if(refreshedToken == null) {
            return RequestResult.success("");
        } else {
            return RequestResult.success(new JwtAuthenticationResponse(refreshedToken));
        }
    }

    @RequestMapping(value = "${jwt.route.authentication.register}", method = RequestMethod.POST)
    public RequestResult login(@RequestBody Sysuser sysuser)throws AuthenticationException{
        return RequestResult.success(authService.register(sysuser));
    }
}
