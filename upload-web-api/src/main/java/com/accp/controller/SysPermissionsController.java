package com.accp.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import com.accp.controller.annotation.PermissionsCatalog;
import org.springframework.web.bind.annotation.RestController;
import com.accp.controller.base.BaseController;
import com.accp.entity.SysPermissions;
import com.accp.service.ISysPermissionsService;

/**
 * <p>
 * 系统管理,权限管理,sys,syspermissions 前端控制器
 * </p>
 *
 * @author author
 * @since 2019-08-20
 */
@RestController
@RequestMapping("/system/syspermissions")
@PermissionsCatalog(cname={"系统管理","权限管理"},path= {"sys","syspermissions"},component="list")
public class SysPermissionsController extends BaseController<SysPermissions,ISysPermissionsService> {

}

