package com.accp.controller;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accp.controller.annotation.PermissionsCatalog;
import com.accp.controller.base.BaseController;
import com.accp.entity.SysUsers;
import com.accp.service.ISysUsersService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * <p>
 * 系统管理,用户管理,sys,sysusers 前端控制器
 * </p>
 *
 * @author author
 * @since 2019-08-20
 */
@RestController
@RequestMapping("/system/sysusers")
@PermissionsCatalog(cname={"系统管理","用户管理"},path= {"sys","sysusers"},component="list")
public class SysUsersController extends BaseController<SysUsers,ISysUsersService> {
	
	@Autowired
	ISysUsersService service;
	
	@RequestMapping("/exportExcel")
	public ResponseEntity<byte []> exportExcel(String name){
		QueryWrapper<SysUsers> query = new QueryWrapper<SysUsers>();
		query.lambda().eq(SysUsers::getUsername, name);
		List<SysUsers> list = service.list(query);
		XSSFWorkbook work = new XSSFWorkbook();
		Sheet sheet = work.createSheet();
		Row titleRow = sheet.createRow(0);
		titleRow.createCell(0).setCellValue("编号");
		titleRow.createCell(1).setCellValue("用户名称");
		titleRow.createCell(2).setCellValue("密码");
		for(int i=0;i<list.size();i++) {
			Row row = sheet.createRow(i+1);
			Cell cellId = row.createCell(0);
			cellId.setCellValue(list.get(i).getId());
			Cell cellUserName = row.createCell(1);
			cellUserName.setCellValue(list.get(i).getUsername());
			Cell cellPassword = row.createCell(2);
			cellPassword.setCellValue(list.get(i).getPassword());
		}
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		try {
			work.write(bao);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentDispositionFormData("attachment", new String("学生数据.xlsx".getBytes("utf-8"),"iso-8859-1"));
			return new ResponseEntity(bao.toByteArray(),headers,HttpStatus.OK);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}

