package com.accp.controller;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.accp.controller.annotation.PermissionsCatalog;
import com.accp.controller.base.BaseController;
import com.accp.entity.Student;
import com.accp.entity.SysUsers;
import com.accp.entity.Upload;
import com.accp.service.IUploadService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * <p>
 * 其它,文件上传,other,upload 前端控制器
 * </p>
 *
 * @author author
 * @since 2019-08-20
 */
@RestController
@RequestMapping("/system/upload")
@PermissionsCatalog(cname = { "其它", "文件上传" }, path = { "other", "upload" }, component = "list")
public class UploadController extends BaseController<Upload, IUploadService> {

	
	@RequestMapping("/downloadTemplate")
	public ResponseEntity<byte []> downloadTemplate() {
		try {
			FileInputStream fis = new FileInputStream("/Volumes/applesdcard/template.xlsx");
			byte [] bytes = new byte[fis.available()];
			fis.read(bytes);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentDispositionFormData("attachment", new String("学生导入模版.xlsx".getBytes("utf-8"),"iso-8859-1"));
			return new ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping("/uploadExcel")
	public void uploadExcel(MultipartFile file) {
		try {
			XSSFWorkbook work = new XSSFWorkbook(file.getInputStream());
			int sheetNumber = work.getNumberOfSheets();
			for(int i=0;i<sheetNumber;i++) {
				Sheet sheet = work.getSheetAt(i);
				int rows = sheet.getPhysicalNumberOfRows();
				Student stu = new Student();
				for(int j=0;j<rows;j++) {
					if(j==0) {
						continue;
					}
					Row row = sheet.getRow(j);
					row.getPhysicalNumberOfCells();
					Cell cellName = row.getCell(0);//
					stu.setName(cellName.getStringCellValue());
					Cell cellAge = row.getCell(1);
					stu.setAge((int)cellAge.getNumericCellValue());
					Cell cellBirthday = row.getCell(2);
					stu.setBirthday(cellBirthday.getDateCellValue());
				}
				System.out.println(stu.toString());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@RequestMapping("/exportExcel")
	public ResponseEntity<byte []> exportExcel(String name){
		QueryWrapper<SysUsers> query = new QueryWrapper<SysUsers>();
		query.lambda().eq(SysUsers::getUsername, name);
		//查询数据导出
		List<SysUsers> list = null;//service.list(query);
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
