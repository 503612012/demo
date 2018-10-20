package com.skyer.realm;

import com.skyer.contants.AppConst;
import com.skyer.enumerate.ResultEnum;
import com.skyer.mapper.MenuMapper;
import com.skyer.service.RoleMenuService;
import com.skyer.service.UserRoleService;
import com.skyer.service.UserService;
import com.skyer.vo.Menu;
import com.skyer.vo.RoleMenu;
import com.skyer.vo.User;
import com.skyer.vo.UserRole;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

public class MyShiroRealm extends AuthorizingRealm {

    @Resource
    private MenuMapper menuMapper;
    @Resource
    private UserService userService;
    @Resource
    private UserRoleService userRoleService;
    @Resource
    private RoleMenuService roleMenuService;

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        User user = (User) principals.getPrimaryPrincipal();
        List<String> permissions = new ArrayList<>();
        List<UserRole> roles = userRoleService.getByUserId(user.getId());
        for (UserRole userRole : roles) {
            List<RoleMenu> menus = roleMenuService.getByRoleId(userRole.getRoleId());
            for (RoleMenu roleMenu : menus) {
                Menu menu = menuMapper.getById(roleMenu.getMenuId());
                permissions.add(menu.getMenuCode());
            }
        }
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