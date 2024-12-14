package com.chen.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.mapper.Oauth2BasicUserMapper;
import com.chen.mapper.PermMapper;
import com.chen.mapper.ThirdAccountMapper;
import com.chen.mapper.UserMapper;
import com.chen.pojo.user.CustomGrantedAuthority;
import com.chen.pojo.user.Oauth2ThirdAccount;
import com.chen.pojo.user.Oauth2UserinfoResult;

import com.chen.pojo.user.User;
import com.chen.utils.result.CommonCode;
import com.chen.utils.result.ResponseResult;
import com.chen.utils.util.SecurityConstants;
import com.github.houbb.heaven.util.lang.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.net.http.HttpRequest;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.chen.utils.util.SecurityConstants.OAUTH_LOGIN_TYPE;
import static com.chen.utils.util.SecurityConstants.TOKEN_UNIQUE_ID;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl extends ServiceImpl<Oauth2BasicUserMapper, User> implements UserDetailService{

    private final UserMapper userMapper;

    private final PermMapper permMapper;

    private final ThirdAccountMapper thirdAccountMapper;

    private final PasswordEncoder passwordEncoder;

    //返回认证用户信息对象
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userMapper.findByName(username);

        if(user==null){
            throw new UsernameNotFoundException("用户未找到");
        }

        //根据uid查找角色  角色对应权限
        List<String> permissions=permMapper.getAuthority(user.getUid());

        // 通过角色菜单关联表查出对应的菜单
//        List<SysRoleAuthority> roleId = permMapper.getRole(user.getUid());
//        List<Integer> menusId = Optional.ofNullable(roleId).orElse(Collections.emptyList()).stream().map(SysRoleAuthority::getAuthorityId).collect(Collectors.toList());
//        if (ObjectUtils.isEmpty(menusId)) {
//            return user;
//        }

        //权限列表
        Set<CustomGrantedAuthority> permList=Optional.ofNullable(permissions).orElse(Collections.emptyList()).stream().map(CustomGrantedAuthority::new).collect(Collectors.toSet());

        user.setAuthorities(permList);

        //为用户赋予权限标识
        return user;
    }


    @Override
    public ResponseResult<String> createUserIpLocation(String ip, HttpRequest headers){

        return new ResponseResult<>(CommonCode.SUCCESS,"");
    }

    @Override
    public User findByName(String username) {

        return userMapper.findByName(username);
    }

    //在数据库中，新增一位用户，且密码以加盐形式保存
    @Override
    public int register(User user){

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if(!StringUtil.isBlank(user.getEmail())){
            userMapper.createUser(user);
            return userMapper.registerByEmail(user);
        }else{
            userMapper.createUser(user);
            return userMapper.registerByPhone(user);
        }


    }

    @Override
    public String saveByThirdAccount(Oauth2ThirdAccount thirdAccount) {
        User basicUser = new User();
        basicUser.setUname(thirdAccount.getName());
        basicUser.setUser_img(thirdAccount.getAvatarUrl());
        basicUser.setDeleted((byte) 0);
        basicUser.setSourceFrom(thirdAccount.getType());
        this.save(basicUser);
        return basicUser.getUid();
    }

    @Override
    public Oauth2UserinfoResult getLoginUserInfo() {
        Oauth2UserinfoResult result = new Oauth2UserinfoResult();

        // 获取当前认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 其它非token方式获取用户信息
        if (!(authentication instanceof JwtAuthenticationToken jwtAuthenticationToken)) {
            BeanUtils.copyProperties(authentication.getPrincipal(), result);
            result.setSub(authentication.getName());
            return result;
        }

        // 获取jwt解析内容
        Jwt token = jwtAuthenticationToken.getToken();

        // 获取当前登录类型
        String loginType = token.getClaim(OAUTH_LOGIN_TYPE);
        // 获取用户唯一Id
        String uniqueId = token.getClaimAsString(TOKEN_UNIQUE_ID);
        // 基础用户信息id
        String basicUserId = null;

        // 获取Token中的权限列表
        List<String> claimAsStringList = token.getClaimAsStringList(SecurityConstants.AUTHORITIES_KEY);

        // 如果登录类型不为空则代表是三方登录，获取三方用户信息
        if (!ObjectUtils.isEmpty(loginType)) {
            // 根据三方登录类型与三方用户的唯一Id查询用户信息
            LambdaQueryWrapper<Oauth2ThirdAccount> wrapper = Wrappers.lambdaQuery(Oauth2ThirdAccount.class)
                    .eq(Oauth2ThirdAccount::getUniqueId, uniqueId)
                    .eq(Oauth2ThirdAccount::getType, loginType);
            Oauth2ThirdAccount oauth2ThirdAccount = thirdAccountMapper.selectOne(wrapper);
            if (oauth2ThirdAccount != null) {
                basicUserId = oauth2ThirdAccount.getUserId();
                // 复制三方用户信息
                BeanUtils.copyProperties(oauth2ThirdAccount, result);
            }
        } else {
            // 为空则代表是使用当前框架提供的登录接口登录的，转为基础用户信息
            basicUserId = uniqueId;
        }

        if (basicUserId == null) {
            // 如果用户id为空，代表获取三方用户信息失败
            result.setSub(authentication.getName());
            return result;
        }

        // 查询基础用户信息
        User basicUser = userMapper.findByUid(basicUserId);
        if (basicUser != null) {
            BeanUtils.copyProperties(basicUser, result);
        }

        // 填充权限信息
        if (!ObjectUtils.isEmpty(claimAsStringList)) {
            Set<SimpleGrantedAuthority> authorities = claimAsStringList.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());
            // 否则设置为token中获取的
            result.setAuthorities(authorities);
        }

        result.setSub(authentication.getName());
        return result;
    }

    @Override
    public Oauth2UserinfoResult syncUserLog() {

        Oauth2UserinfoResult user=getLoginUserInfo();
        int level=user.getLevel();
        int exp_point=user.getExp_point();

        switch (level){
            case 0:
                if (exp_point>=1000){
                    user.setLevel(user.getLevel()+1);
                    user.setExp_point(0);
                }
                break;
            case 1:
                if (exp_point>=2000){
                    user.setLevel(user.getLevel()+1);
                    user.setExp_point(0);
                }
                break;
            case 2:
                if (exp_point>=3000){
                    user.setLevel(user.getLevel()+1);
                    user.setExp_point(0);
                }
                break;
            case 3:
                if (exp_point>=4000){
                    user.setLevel(user.getLevel()+1);
                    user.setExp_point(0);
                }
                break;
            case 4:
                if (exp_point>=5000){
                    user.setLevel(user.getLevel()+1);
                    user.setExp_point(0);
                }
                break;
            case 5:
                if (exp_point>=6000){
                    user.setLevel(user.getLevel()+1);
                    user.setExp_point(0);
                }
                break;
            case 6:
                if (exp_point>=7000){
                    user.setLevel(user.getLevel()+1);
                    user.setExp_point(0);
                }
                break;
            case 7:
                if (exp_point>=8000){
                    user.setLevel(user.getLevel()+1);
                    user.setExp_point(0);
                }
                break;
            case 8:
                if (exp_point>=9000){
                    user.setLevel(user.getLevel()+1);
                    user.setExp_point(0);
                }
                break;
            case 9:
                if (exp_point>=10000){
                    user.setLevel(user.getLevel()+1);
                    user.setExp_point(0);
                }
                break;
            case 10:
                if (exp_point>=11000){
                    user.setLevel(user.getLevel()+1);
                    user.setExp_point(0);
                }
                break;
            case 11:
                if (exp_point>=12000){
                    user.setLevel(user.getLevel()+1);
                    user.setExp_point(0);
                }
                break;
            case 12:
                if (exp_point>=13000){
                    user.setLevel(user.getLevel()+1);
                    user.setExp_point(0);
                }
                break;
            case 13:
                if (exp_point>=14000){
                    user.setLevel(user.getLevel()+1);
                    user.setExp_point(0);
                }
                break;
            case 14:
                if (exp_point>=15000){
                    user.setLevel(user.getLevel()+1);
                    user.setExp_point(0);
                }
                break;
            case 15:
                if (exp_point>=16000){
                    user.setLevel(user.getLevel()+1);
                    user.setExp_point(0);
                }
                break;
            case 16:
                if (exp_point>=17000){
                    user.setLevel(user.getLevel()+1);
                    user.setExp_point(0);
                }
                break;
            case 17:
                if (exp_point>=18000){
                    user.setLevel(user.getLevel()+1);
                    user.setExp_point(0);
                }
                break;
            case 18:
                if (exp_point>=19000){
                    user.setLevel(user.getLevel()+1);
                    user.setExp_point(0);
                }
                break;
            case 19:
                if (exp_point>=20000){
                    user.setLevel(user.getLevel()+1);
                    user.setExp_point(0);
                }
                break;
            case 20:
                if (exp_point>=21000){
                    user.setLevel(user.getLevel()+1);
                    user.setExp_point(0);
                }
                break;
            case 21:
                if (exp_point>=22000){
                    user.setLevel(user.getLevel()+1);
                    user.setExp_point(0);
                }
                break;
            case 22:
                if (exp_point>=23000){
                    user.setLevel(user.getLevel()+1);
                    user.setExp_point(0);
                }
                break;
            case 23:
                if (exp_point>=24000){
                    user.setLevel(user.getLevel()+1);
                    user.setExp_point(0);
                }
                break;
            case 24:
                if (exp_point>=25000){
                    user.setLevel(user.getLevel()+1);
                    user.setExp_point(0);
                }
                break;
            case 25:
                if (exp_point>=26000){
                    user.setLevel(user.getLevel()+1);
                    user.setExp_point(0);
                }
                break;
            case 26:
                if (exp_point>=27000){
                    user.setLevel(user.getLevel()+1);
                    user.setExp_point(0);
                }
                break;

        }

        return user;
    }


}
