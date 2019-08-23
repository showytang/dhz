package com.accp.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import com.accp.controller.annotation.PermissionsCatalog;
import org.springframework.web.bind.annotation.RestController;
import com.accp.controller.base.BaseController;
import com.accp.entity.SysRoles;
import com.accp.service.ISysRolesService;

/**
 * <p>
 * 系统管理,角色管理,sys,sysroles 前端控制器
 * </p>
 *
 * @author author
 * @since 2019-08-20
 */
@RestController
@RequestMapping("/system/sysroles")
@PermissionsCatalog(cname={"系统管理","角色管理"},path= {"sys","sysroles"},component="list")
public class SysRolesController extends BaseController<SysRoles,ISysRolesService> {

}

