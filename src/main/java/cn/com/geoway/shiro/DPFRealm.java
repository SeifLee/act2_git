package cn.com.geoway.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * 自定义权限Realm
 * 
 * @author lvxiaofei
 * @version 1.0 , 2017-04-05 16:15:30
 */
public class DPFRealm extends AuthorizingRealm{

	/** 注入用户dao */
	
	/** 注入权限dao */
	
	/**
	 * 授权方法
	 * 
	 * @param principals 参数: 检查授权信息的主要规则
	 * @return 返回值: 与此主体关联的授权信息
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		return null;
	}

	/**
	 * 认证方法
	 * 
	 * @param token 参数: 包含用户主体和证书的验证令牌
	 * @return 返回值: 一个认证信息对象，只有当验证成功时，才包含由身份验证产生的帐户数据. 
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		return null;
	}

	

}
