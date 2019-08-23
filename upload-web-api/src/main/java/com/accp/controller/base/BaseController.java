package com.accp.controller.base;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.accp.controller.annotation.PermissionsCatalog;
import com.accp.controller.annotation.SysRequiresPermissions;
import com.accp.entity.BaseEntity;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public class BaseController<E, T> extends BaseServiceController<E, T> {

	private Map<String, String> map = new HashMap<>();

	public Map<String, String> getMap() {

		return this.map;
	}

	public BaseController() {
		List<Method> lists = MethodUtils.getMethodsListWithAnnotation(this.getClass(), SysRequiresPermissions.class);
		PermissionsCatalog catalog = AnnotationUtils.findAnnotation(this.getClass(), PermissionsCatalog.class);
		for (Method m : lists) {
			boolean bol = m.getDeclaringClass().getSimpleName().equals("BaseController");
			if (bol) {
				SysRequiresPermissions perms = m.getDeclaredAnnotation(SysRequiresPermissions.class);
				if (catalog != null) {
					map.put(m.toString(), catalog.path()[0] + ":" + catalog.path()[1] + ":" + perms.name()+ "," + perms.cname());
				} else {
					map.put(m.toString(), perms.name() + "," + perms.cname());
				}
			}
		}
	}

	@SuppressWarnings("all")
	@PostMapping("/query")
	@SysRequiresPermissions(name = "query", cname = "查询")
	public final Object query(@RequestBody E e) {
		boolean bol = isPerm("query");
		if (!bol) {
			throw new RuntimeException("权限不足，请联系管理员。");
		}
		BaseEntity entity = (BaseEntity) e;
		QueryWrapper<E> query = new QueryWrapper<E>(e);
		return super.getService().list(query);
	}

	@SuppressWarnings("all")
	@PostMapping("/queryByPage")
	@SysRequiresPermissions(name = "queryByPage", cname = "分页查询")
	public final Object queryByPage(@RequestBody E e) {
		boolean bol = isPerm("queryByPage");
		if (!bol) {
			throw new RuntimeException("权限不足，请联系管理员。");
		}
		BaseEntity entity = (BaseEntity) e;
		QueryWrapper<E> query = new QueryWrapper<E>(e);
		return super.getService().page(new Page<E>(entity.getPage(), entity.getSize()), query);
	}

	@SuppressWarnings("unchecked")
	@PostMapping("/create")
	@SysRequiresPermissions(name = "create", cname = "创建", path = "create")
	public final Object create(@RequestBody E e) {
		boolean bol1 = isPerm("create");
		if (!bol1) {
			throw new RuntimeException("权限不足，请联系管理员。");
		}
		boolean bol = super.getService().save(e);
		if (bol == true) {
			return e;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@PostMapping("/update")
	@SysRequiresPermissions(name = "update", cname = "修改", path = "update")
	public final Object update(@RequestBody E e) {
		boolean bol1 = isPerm("update");
		if (!bol1) {
			throw new RuntimeException("权限不足，请联系管理员。");
		}
		boolean bol = super.getService().updateById(e);
		if (bol == true) {
			return e;
		}
		return null;
	}

	@GetMapping("/queryById")
	@SysRequiresPermissions(name = "queryById", cname = "根据编号查询")
	public final Object queryById(Integer id) {
		boolean bol1 = isPerm("queryById");
		if (!bol1) {
			throw new RuntimeException("权限不足，请联系管理员。");
		}
		return super.getService().getById(id);
	}

	@GetMapping("/remove")
	@SysRequiresPermissions(name = "remove", cname = "删除")
	public final Object remove(Integer id) {
		boolean bol1 = isPerm("remove");
		if (!bol1) {
			throw new RuntimeException("权限不足，请联系管理员。");
		}
		return super.getService().removeById(id);
	}

	public final boolean isPerm(String method) {
		PermissionsCatalog catalog = AnnotationUtils.findAnnotation(this.getClass(), PermissionsCatalog.class);
		if (catalog != null) {
			String perm = catalog.path()[0] + ":" + catalog.path()[1] + ":" + method;
			return SecurityUtils.getSubject().isPermitted(perm);
		} else {
			return SecurityUtils.getSubject().isPermitted(method);
		}
	}
}
