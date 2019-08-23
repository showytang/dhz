package com.accp.controller.base;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.accp.entity.SysPermissions;
import com.accp.entity.SysUsers;
import com.accp.realm.ShiroDbRealm;
import com.accp.service.ISysPermissionsService;
import com.accp.utils.PermissionsUtils;
import com.accp.vo.CatalogVo;

@RestController
public class PermissionsController {
	
	@Autowired
	PermissionsUtils permissionsUitls;
	
	@Autowired
	ISysPermissionsService permService;
	
	@GetMapping("/system/getPermissionsByRoleId")
	public List<String> getPermissionsByRoleId(Integer roleId) {
		QueryWrapper<SysPermissions> perQuery = new QueryWrapper<SysPermissions>();
		perQuery.lambda().in(SysPermissions::getRoleid, roleId);
		List<SysPermissions> perList = permService.list(perQuery);
		List<String> listPermissions = new ArrayList<String>();
		for(SysPermissions p : perList) {
			if(p.getPermission().equals("*")) {
				List<Object> listvo = permissionsUitls.getPermissionsAll();
				List<CatalogVo> catalogs = (List<CatalogVo>)listvo.get(0);
				for(CatalogVo vo : catalogs) {
					for(CatalogVo child : vo.getChildren()) {
						for(String item : child.getPerms()) {
							listPermissions.add(item.split(",")[0]);
						}
					}
				}
				return listPermissions;
			}
		}
		
		for (SysPermissions p : perList) {
			listPermissions.add(p.getPermission());
		}
		return listPermissions;
	}

	@GetMapping("/system/getPermissionsAll")
	public List<Object> getPermissions() {
		return permissionsUitls.getPermissionsAll();
	}
	
	@PostMapping("/system/savePermissions")
	public Object savePermissions(@RequestBody List<SysPermissions>  perms) 
	{
		try {
			if(perms!=null && perms.size()>0) {
				QueryWrapper<SysPermissions> query = new QueryWrapper<SysPermissions>();
				query.lambda().eq(SysPermissions::getRoleid, perms.get(0).getRoleid());
				permService.remove(query);
				permService.saveBatch(perms);
				RealmSecurityManager securityManager = (RealmSecurityManager) SecurityUtils.getSecurityManager();
				ShiroDbRealm shiroRealm = (ShiroDbRealm)securityManager.getRealms().iterator().next();
				shiroRealm.getAuthorizationCache().clear();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "00000";
		}
		return "00001";
	}

	
	@GetMapping("/system/getPermissionsByUser")
	public List<Object> getPermissionsByUser() {
		SysUsers user = (SysUsers)SecurityUtils.getSubject().getPrincipal();
		QueryWrapper<SysPermissions> query = new QueryWrapper<SysPermissions>();
		query.lambda().eq(SysPermissions::getRoleid, user.getRoleid());
		List<SysPermissions> list = permService.list(query);
		for(SysPermissions p : list) {
			if(p.getPermission().equals("*")) {
				return permissionsUitls.getPermissionsAll();
			}
		}
		return permissionsUitls.getPermissionsByUser(list);
	}
	
}
