package org.aimindflow.common.satoken.utils;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.context.model.SaStorage;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.aimindflow.common.core.constant.UserConstants;

import java.util.Set;

/**
 * 登录鉴权助手
 *
 * @author HezaoHezao
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginHelper {

    public static final String USER_KEY = "userId";
    public static final String DEPT_KEY = "deptId";
    public static final String USER_NAME_KEY = "username";
    public static final String REAL_NAME_KEY = "realName";
    public static final String TENANT_KEY = "tenantId";

    /**
     * 登录系统 基于 设备类型
     * 针对相同用户会自动踢下线前一个token
     *
     * @param loginUser 登录用户信息
     * @param device    设备类型
     */
    public static void loginByDevice(LoginUser loginUser, String device) {
        SaStorage storage = SaHolder.getStorage();
        storage.set(USER_KEY, loginUser.getUserId());
        storage.set(USER_NAME_KEY, loginUser.getUsername());
        storage.set(REAL_NAME_KEY, loginUser.getRealName());
        storage.set(DEPT_KEY, loginUser.getDeptId());
        storage.set(TENANT_KEY, loginUser.getTenantId());
        StpUtil.login(loginUser.getUserId(), device);
        StpUtil.getTokenSession().set(USER_KEY, loginUser);
    }

    /**
     * 获取用户(多级缓存)
     */
    public static LoginUser getLoginUser() {
        LoginUser loginUser = (LoginUser) StpUtil.getTokenSession().get(USER_KEY);
        if (loginUser != null) {
            return loginUser;
        }
        SaSession session = StpUtil.getTokenSession();
        if (session.get(USER_KEY) == null) {
            return null;
        }
        loginUser = new LoginUser();
        loginUser.setUserId(session.get(USER_KEY, Long.class));
        loginUser.setUsername(session.get(USER_NAME_KEY, String.class));
        loginUser.setRealName(session.get(REAL_NAME_KEY, String.class));
        loginUser.setDeptId(session.get(DEPT_KEY, Long.class));
        loginUser.setTenantId(session.get(TENANT_KEY, String.class));
        return loginUser;
    }

    /**
     * 获取用户
     */
    public static LoginUser getLoginUser(String token) {
        return (LoginUser) StpUtil.getTokenSessionByToken(token).get(USER_KEY);
    }

    /**
     * 获取用户id
     */
    public static Long getUserId() {
        LoginUser loginUser = getLoginUser();
        if (loginUser == null) {
            String loginId = StpUtil.getLoginIdDefaultNull();
            if (loginId == null) {
                return null;
            }
            return Long.parseLong(loginId);
        }
        return loginUser.getUserId();
    }

    /**
     * 获取部门ID
     */
    public static Long getDeptId() {
        LoginUser loginUser = getLoginUser();
        if (loginUser == null) {
            return null;
        }
        return loginUser.getDeptId();
    }

    /**
     * 获取用户账户
     */
    public static String getUsername() {
        LoginUser loginUser = getLoginUser();
        if (loginUser == null) {
            return null;
        }
        return loginUser.getUsername();
    }

    /**
     * 获取用户真实姓名
     */
    public static String getRealName() {
        LoginUser loginUser = getLoginUser();
        if (loginUser == null) {
            return null;
        }
        return loginUser.getRealName();
    }

    /**
     * 获取租户ID
     */
    public static String getTenantId() {
        LoginUser loginUser = getLoginUser();
        if (loginUser == null) {
            return null;
        }
        return loginUser.getTenantId();
    }

    /**
     * 是否为管理员
     *
     * @param userId 用户ID
     * @return 结果
     */
    public static boolean isAdmin(Long userId) {
        return UserConstants.ADMIN_ID.equals(userId);
    }

    /**
     * 是否为管理员
     */
    public static boolean isAdmin() {
        return isAdmin(getUserId());
    }

    /**
     * 验证用户是否具备某权限
     *
     * @param permission 权限字符串
     * @return 用户是否具备某权限
     */
    public static boolean hasPermi(String permission) {
        return hasPermi(getLoginUser(), permission);
    }

    /**
     * 验证用户是否具备某权限, 如果验证未通过，则抛出异常: NotPermissionException
     *
     * @param permission 权限字符串
     */
    public static void checkPermi(String permission) {
        StpUtil.checkPermission(permission);
    }

    /**
     * 验证用户是否具备某权限
     *
     * @param loginUser  登录用户
     * @param permission 权限字符串
     * @return 用户是否具备某权限
     */
    public static boolean hasPermi(LoginUser loginUser, String permission) {
        if (loginUser == null || permission == null) {
            return false;
        }
        return isAdmin(loginUser.getUserId()) || loginUser.getMenuPermission().contains(permission);
    }

    /**
     * 验证用户是否拥有某个角色
     *
     * @param role 角色标识
     * @return 用户是否具备某角色
     */
    public static boolean hasRole(String role) {
        return hasRole(getLoginUser(), role);
    }

    /**
     * 验证用户是否拥有某个角色, 如果验证未通过，则抛出异常: NotRoleException
     *
     * @param role 角色标识
     */
    public static void checkRole(String role) {
        StpUtil.checkRole(role);
    }

    /**
     * 验证用户是否拥有某个角色
     *
     * @param loginUser 登录用户
     * @param role      角色标识
     * @return 用户是否具备某角色
     */
    public static boolean hasRole(LoginUser loginUser, String role) {
        if (loginUser == null || role == null) {
            return false;
        }
        return isAdmin(loginUser.getUserId()) || loginUser.getRolePermission().contains(role);
    }

    /**
     * 登出
     */
    public static void logout() {
        try {
            StpUtil.logout();
        } catch (Exception ignored) {
        }
    }
}