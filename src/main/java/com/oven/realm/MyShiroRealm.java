package com.oven.realm;

import com.oven.constant.AppConst;
import com.oven.enumerate.ResultEnum;
import com.oven.core.menu.service.MenuService;
import com.oven.core.user.service.UserService;
import com.oven.core.user.vo.User;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import javax.annotation.Resource;
import java.util.List;

/**
 * 自定义Realm
 *
 * @author Oven
 */
public class MyShiroRealm extends AuthorizingRealm {

    @Resource
    private MenuService menuService;
    @Resource
    private UserService userService;

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        User user = (User) principals.getPrimaryPrincipal();
        List<String> permissions = menuService.getAllMenuCodeByUserId(user.getId());
        authorizationInfo.addStringPermissions(permissions);
        return authorizationInfo;
    }

    /**
     * 身份认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String userName = String.valueOf(token.getUsername());
        // 从数据库获取对应用户名的用户
        User user = userService.getByUserName(userName);
        // 账号不存在
        if (user == null) {
            throw new UnknownAccountException(ResultEnum.NO_THIS_USER.getValue());
        }

        Md5Hash md5 = new Md5Hash(token.getPassword(), AppConst.MD5_SALT, 2);
        // 密码错误
        if (!md5.toString().equals(user.getPassword())) {
            throw new IncorrectCredentialsException(ResultEnum.PASSWORD_WRONG.getValue());
        }

        // 账号锁定
        if (user.getStatus().equals(1)) {
            throw new LockedAccountException(ResultEnum.USER_DISABLE.getValue());
        }
        ByteSource salt = ByteSource.Util.bytes(AppConst.MD5_SALT);
        return new SimpleAuthenticationInfo(user, user.getPassword(), salt, getName());
    }

}