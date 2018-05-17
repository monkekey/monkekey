package com.hpd.butler.web;

import com.hpd.butler.common.RequestResult;
import com.hpd.butler.constant.CommonFlag;
import com.hpd.butler.domain.*;
import com.hpd.butler.utils.DESHelper;
import com.hpd.butler.utils.FileUtils;
import com.hpd.butler.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Created by zy on 2017/10/9.
 */
@RestController
@RequestMapping("/sys/user")
@Api(value = "UserController", description = "用户相关接口")
public class UserController {
    @Autowired
    private ISysuserRepository iSysuserRepository;
    @Autowired
    private ISysuserdetailRepository iSysuserdetailRepository;
    @Autowired
    private ISysuserroleRepository iSysuserroleRepository;
    @Autowired
    private IInnRepository iInnRepository;

    @Autowired
    private ISysuserDao iSysuserDao;
    @Autowired
    @Qualifier("entityManagerFactoryButler")
    private EntityManager entityManager;

    @Autowired
    private UserDetailsService userDetailsService;


    @ApiOperation(value = "User|列表")
    @GetMapping("")
    public RequestResult list(@RequestParam(value = "innCode", required = false, defaultValue = "") final String innCode,
                              @RequestParam(value = "keywords", required = false, defaultValue = "") final String keywords,
                              @RequestParam(value = "pageIdx", required = false, defaultValue = "0") Integer pageIdx,
                              @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        Pageable pageable = new PageRequest(pageIdx, pageSize, Sort.Direction.DESC, "id");

        if (!StringUtils.isEmpty(innCode)){
            if(!StringUtils.isEmpty(keywords)){
                return RequestResult.success(iSysuserRepository.findByInnCodeAndKeywords(innCode, keywords,pageable));
            }else{
                return RequestResult.success(iSysuserRepository.findByInnCode(innCode, pageable));
            }
        }else{
            Specification<Sysuser> spec = new Specification<Sysuser>() {
                public Predicate toPredicate(Root<Sysuser> root,
                                             CriteriaQuery<?> query, CriteriaBuilder cb) {
                    Predicate p = cb.equal(root.get("flag").as(Integer.class), CommonFlag.VALID.getValue());

                    if(!StringUtils.isEmpty(keywords)){
                        Predicate p1 = cb.like(root.get("name").as(String.class), "%"+keywords+"%");
                        Predicate p2 = cb.like(root.get("account").as(String.class), "%"+keywords+"%");
                        Predicate p3 = cb.like(root.get("mobile").as(String.class), "%"+keywords+"%");
                        query.where(cb.and(p, cb.or(p1,p2,p3)));
                    }else {
                        query.where(p);
                    }
                    return query.getRestriction();
                }
            };
            return RequestResult.success(iSysuserDao.findAll(spec,pageable));
        }
    }

    @ApiOperation(value = "User|详情")
    @GetMapping("/{uid}")
    public RequestResult getByRoomCode(@NotNull @PathVariable("uid") long uid) {
        Sysuser sysuser = iSysuserRepository.findOne(uid);
        if(null == sysuser){
            return RequestResult.fail("无法获取记录");
        }
        Sysuserdetail sysuserdetail = iSysuserdetailRepository.findByUid(sysuser.getId());

        try {
            UserVo userVo = new UserVo();
            PropertyUtils.copyProperties(userVo, sysuser);
            if (null!=sysuserdetail){
                PropertyUtils.copyProperties(userVo, sysuserdetail);
            }

            return RequestResult.success(userVo);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }


        return RequestResult.success(sysuser);
    }

    @ApiOperation(value = "新增|User")
    @PostMapping("")
    @Transactional
    public RequestResult add(@RequestBody UserVo userVo){
        Sysuser sysuser = new Sysuser();
        Sysuserdetail sysuserdetail = new Sysuserdetail();
        try {
            PropertyUtils.copyProperties(sysuser, userVo);
            PropertyUtils.copyProperties(sysuserdetail, userVo);
            if (StringUtils.isEmpty(sysuser.getPassword())){
                sysuser.setPassword("lf13579");
            }
            sysuser.setPassword(new BCryptPasswordEncoder().encode(sysuser.getPassword()));
            sysuser.setFlag(CommonFlag.VALID.getValue());
            sysuser = iSysuserRepository.saveAndFlush(sysuser);
            if(null !=sysuser){
                sysuserdetail.setUid(sysuser.getId());
                sysuserdetail.setUserName(sysuser.getName());
                sysuserdetail = iSysuserdetailRepository.saveAndFlush(sysuserdetail);
            }
            return RequestResult.success(sysuser);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return RequestResult.fail("添加出错");
    }

    @ApiOperation(value = "更新|User")
    @PutMapping("/{uid}")
    @Modifying
    @Transactional
    public RequestResult update(@NotNull @PathVariable("uid") long uid,
                                @RequestBody UserVo userVo){
        Sysuser sysuser = iSysuserRepository.findOne(uid);
        if(null == sysuser){
            return RequestResult.fail("无法获取记录");
        }

        Sysuserdetail sysuserdetail = iSysuserdetailRepository.findByUid(sysuser.getId());
        if(null == sysuserdetail){
            sysuserdetail = new Sysuserdetail();
        }

        try {
            String pwd = sysuser.getPassword();
            sysuser.setMobile(userVo.getMobile());
            sysuser.setName(userVo.getName());
            sysuser.setPassword(pwd);

            sysuserdetail.setUserName(sysuser.getName());
            sysuserdetail.setInnCode(userVo.getInnCode());
            sysuserdetail.setFaces(userVo.getFaces());
            sysuserdetail.setSex(userVo.getSex());
            sysuserdetail.setBirthDay(userVo.getBirthDay());
            sysuserdetail.setNature(userVo.getNature());
            sysuserdetail.setSpecialty(userVo.getSpecialty());

            sysuser = iSysuserRepository.saveAndFlush(sysuser);
            if(null !=sysuser) {
                sysuserdetail.setUid(sysuser.getId());
                iSysuserdetailRepository.saveAndFlush(sysuserdetail);
            }
            return RequestResult.success(sysuser);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return RequestResult.fail("更新出错");
    }

    @ApiOperation(value = "修改|User 密碼")
    @PostMapping("/mdypwd")
    @Transactional
    public RequestResult changePwd(@RequestParam(value = "account", required = true) String account,
                                   @RequestParam(value = "newPassword", required = true) String newPassword){
        if (StringUtils.isEmpty(newPassword)){
            return RequestResult.fail("密码不能为空!");
        }

        Authentication currentuser=SecurityContextHolder.getContext().getAuthentication();
        if(currentuser==null){
            // This would indicate bad coding somewhere
            throw new AccessDeniedException("未登录不可更改密码!");
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails currentAuth = (UserDetails)principal;

        Sysuser sysuser = iSysuserRepository.findByAccount(currentAuth.getUsername());
        if(null == sysuser || sysuser.getFlag() != CommonFlag.VALID.getValue()){
            return RequestResult.fail("此账户不存在或已禁用!");
        }

        String oldPassword = sysuser.getPassword();
        sysuser.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        iSysuserRepository.saveAndFlush(sysuser);

        if (!oldPassword.equals(sysuser.getPassword())){
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(currentAuth.getUsername());

            UsernamePasswordAuthenticationToken newAuthentication =
                    new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
            newAuthentication.setDetails(currentuser.getDetails());
        }

        return RequestResult.success("ok");
    }

    @ApiOperation(value = "删除|User")
    @DeleteMapping("")
    public RequestResult delete(@RequestParam(value = "id", required = true)  long uid) {
        Sysuser sysuser = iSysuserRepository.findOne(uid);

        if(null == sysuser){
            return RequestResult.fail("无法获取记录");
        }

        sysuser.setFlag(CommonFlag.INVALID.getValue());
        iSysuserRepository.saveAndFlush(sysuser);

        return  RequestResult.success("ok");
    }

    @ApiOperation(value = "init|User")
    @GetMapping("/init")
    @Transactional
    public RequestResult initUser(){
        String filepath = "E:\\teddyzhou\\job";
//        String newFilePath = "E:\\teddyzhou\\Lavande";
//        try {
//            String innName = "";
//            String fileName = "";
//            List<Inn> innList = null;
//            File file = new File(filepath);
//
//            if(file.isDirectory()){
//                String[] childFilePathList = file.list();
//                for (String childFilePath : childFilePathList){
//                    File childFile = new File(filepath + File.separator + childFilePath);
//                    if (!childFile.isDirectory()) {
//                        innName = childFile.getParentFile().getName();
//
//                        if(!StringUtils.isEmpty(innName)){
//                            innList = iInnRepository.findByInnNameLike("%"+innName.replace("广州","").replace("店","")+"%");
//                            if(null!=innList && innList.size()>0) {
//                                fileName = childFile.getName().substring(0,childFile.getName().lastIndexOf("."));
//
//                                File newFile = new File(newFilePath + File.separator + innList.get(0))
//                            }
//
//
//                            if(!StringUtils.isEmpty(fileName)){
//                                userMap = new HashMap<String, String>();
//                                userMap.put("account", PinyinUtil.makePinyin(fileName).split(",")[0]);
//                                userMap.put("username", fileName);
//                                allUserMap.get(innName).add(userMap);
//                            }
//                        }
//                    }else{
//                        readfile(allUserMap, filepath + File.separator + childFilePath);
//                    }
//                }
//            }
//        } catch (FileNotFoundException e) {
//            System.out.println("readfile()   Exception:" + e.getMessage());
//        }

//        从图片获取
        Map<String, List<Map<String, String>>> allUserMap = new HashMap<String, List<Map<String, String>>>();
        List<Inn> innList = null;
        Sysuser sysuser = null;
        Sysuserdetail sysuserdetail = null;
        Sysuserrole sysuserrole = null;
        List<Sysuser> sysuserList = new ArrayList<Sysuser>();
        List<Sysuserdetail> sysuserdetailsList = new ArrayList<Sysuserdetail>();
        Map<String,String> userInnMap = new HashMap<String ,String>();

        try {
            FileUtils.readfile(allUserMap, filepath);
            String salt = UUID.randomUUID().toString().replace("-","").substring(10);
            for (String key:allUserMap.keySet()){
                innList = iInnRepository.findByInnNameLike("%"+key.replace("广州","").replace("店","")+"%");
                if(null!=innList && innList.size()>0){
                    for (Map<String, String> userMap:allUserMap.get(key)){
                        sysuser = new Sysuser();
                        sysuser.setSalt(salt);
                        sysuser.setPassword(DESHelper.encrypt("lf13579", salt));
                        sysuser.setAccount(userMap.get("account"));
                        sysuser.setName(userMap.get("username"));
                        sysuser.setFlag(CommonFlag.VALID.getValue());
                        userInnMap.put(userMap.get("account"), innList.get(0).getInnCode());
                        sysuserList.add(sysuser);
                    }
                }else{
                    System.out.println(key);
                }
            }

            entityManager.flush();
            for (int i = 0; i < sysuserList.size(); i++) {
                sysuser = sysuserList.get(i);

                entityManager.persist(sysuser);
                if (i % 20 == 0) {
                    entityManager.flush();
                    entityManager.clear();
                }
            }
            entityManager.flush();
            entityManager.clear();


            entityManager.flush();
            for (int i = 0; i < sysuserList.size(); i++) {
                sysuser = sysuserList.get(i);
                sysuserdetail = new Sysuserdetail();
                sysuserdetail.setUid(sysuser.getId());
                sysuserdetail.setUserName(sysuser.getName());
                sysuserdetail.setInnCode(userInnMap.get(sysuser.getAccount()));

                entityManager.persist(sysuserdetail);
                if (i % 20 == 0) {
                    entityManager.flush();
                    entityManager.clear();
                }
            }
            entityManager.flush();
            entityManager.clear();


            entityManager.flush();
            for (int i = 0; i < sysuserList.size(); i++) {
                sysuser = sysuserList.get(i);
                sysuserrole = new Sysuserrole();
                sysuserrole.setUid(sysuser.getId());
                sysuserrole.setRoleId(3);

                entityManager.persist(sysuserrole);
                if (i % 20 == 0) {
                    entityManager.flush();
                    entityManager.clear();
                }
            }
            entityManager.flush();
            entityManager.clear();

            return RequestResult.success(sysuserList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return RequestResult.fail("error");
    }
}
