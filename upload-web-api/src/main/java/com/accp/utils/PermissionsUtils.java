package com.accp.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import com.accp.controller.annotation.PermissionsCatalog;
import com.accp.controller.annotation.SysRequiresPermissions;
import com.accp.entity.SysPermissions;
import com.accp.vo.CatalogVo;

@Component
public class PermissionsUtils {

	@Autowired
	private ApplicationContext context;

	public List<Object> getPermissionsByUser(List<SysPermissions> list) {
		Map<String, Object> map = context.getBeansWithAnnotation(Controller.class);
		List<CatalogVo> catalogs = new ArrayList<CatalogVo>();
		Map<String, Object[]> permMethods = new HashMap<String, Object[]>();
		List<Object> result = new ArrayList<>();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			Object bean = entry.getValue();
			Class cls = bean.getClass();
			List<Method> lists = MethodUtils.getMethodsListWithAnnotation(cls.getSuperclass(),
					SysRequiresPermissions.class);
			for (Method method : lists) {
				SysRequiresPermissions permAnnotation = method.getAnnotation(SysRequiresPermissions.class);
				for (SysPermissions p : list) {
					boolean bol = method.getDeclaringClass().getSimpleName().equals("BaseController");
					if (bol) {
						try {
							Method getMap = cls.getMethod("getMap");
							Map<String, String> permMap = (Map<String, String>) getMap.invoke(bean);
							if (p.getPermission().equals(permMap.get(method.toString()).split(",")[0])) {
								permMethods.put(permMap.get(method.toString()), new Object[] { method, cls });
							}
						} catch (NoSuchMethodException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						if (p.getPermission().equals(permAnnotation.value())) {
							permMethods.put(permAnnotation.value(), new Object[] { method, cls });
						}
					}
				}
			}
		}
		Map<String, CatalogVo> exists = new HashMap<String, CatalogVo>();
		CatalogVo catalogsVo = new CatalogVo();
		for (Map.Entry<String, Object[]> permMap : permMethods.entrySet()) {
			Object[] objs = permMap.getValue();
			Method method = (Method) objs[0];
			Class<?> cls = (Class<?>) objs[1];
			PermissionsCatalog catalog = AnnotationUtils.findAnnotation(cls, PermissionsCatalog.class);
			if (catalog != null) {
				each(0, catalog, catalogsVo, exists);
				CatalogVo vo = null;
				for (CatalogVo c : catalogs) {
					if (catalog.path()[catalog.path().length - 2].equals(c.getName())) {
						vo = c;
						break;
					}
				}
				if (vo == null) {
					vo = this.createVo(catalog, null, 0);
					catalogs.add(vo);
				}

				CatalogVo chidlrenVo = null;
				for (CatalogVo children : vo.getChildren()) {
					if (catalog.path()[catalog.path().length - 1].equals(children.getName())) {
						chidlrenVo = children;
						break;
					}
				}
				if (chidlrenVo == null) {
					chidlrenVo = this.createVo(catalog, null, 1);
					vo.getChildren().add(chidlrenVo);
				}
				SysRequiresPermissions perm = method.getDeclaredAnnotation(SysRequiresPermissions.class);
				CatalogVo lastChildren = this.createVo(catalog, perm, 2);
				if (perm.level() == 3 && lastChildren != null) {
					chidlrenVo.getChildren().add(lastChildren);
				}
				if (perm.level() == 2 && lastChildren != null) {
					vo.getChildren().add(lastChildren);
				}
				chidlrenVo.getPerms().add(permMap.getKey());
			} else {
				SysRequiresPermissions perm = method.getDeclaredAnnotation(SysRequiresPermissions.class);
				CatalogVo vo = this.createVo(null, perm, 2);
				if (vo != null) {
					catalogs.add(vo);
				}
				vo.getPerms().add(permMap.getKey());
			}
		}
		result.add(catalogs);
		result.add(catalogsVo.getChildren());
		return result;
	}

	public List<Object> getPermissionsAll() {
		Map<String, Object> map = context.getBeansWithAnnotation(Controller.class);
		List<CatalogVo> catalogs = new ArrayList<CatalogVo>();
		List<Object> result = new ArrayList<>();
		Map<String, CatalogVo> exists = new HashMap<String, CatalogVo>();
		CatalogVo catalogsVo = new CatalogVo();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			Object bean = entry.getValue();
			Class cls = bean.getClass();
			String packageName = cls.getPackage().getName();
			if (!StringUtils.contains(packageName, "com.accp")) {
				continue;
			}
			List<Method> lists = MethodUtils.getMethodsListWithAnnotation(cls.getSuperclass(),
					SysRequiresPermissions.class);
			PermissionsCatalog catalog = AnnotationUtils.findAnnotation(cls, PermissionsCatalog.class);
			if (catalog != null) {
				each(0, catalog, catalogsVo, exists);
				CatalogVo vo = null;
				for (CatalogVo c : catalogs) {
					if (catalog.path()[catalog.path().length - 2].equals(c.getName())) {
						vo = c;
						break;
					}
				}
				if (vo == null) {
					vo = this.createVo(catalog, null, catalog.path().length - 2);
					catalogs.add(vo);
				}
				CatalogVo chidlrenVo = null;
				for (CatalogVo children : vo.getChildren()) {
					if (catalog.path()[catalog.path().length - 1].equals(children.getName())) {
						chidlrenVo = children;
						break;
					}
				}
				if (chidlrenVo == null) {
					chidlrenVo = this.createVo(catalog, null, catalog.path().length - 1);
					vo.getChildren().add(chidlrenVo);
				}
				for (Method method : lists) {
					SysRequiresPermissions perm = method.getDeclaredAnnotation(SysRequiresPermissions.class);
					CatalogVo lastChildren = this.createVo(catalog, perm, catalog.path().length);
					if (perm.level() == 3 && lastChildren != null) {
						chidlrenVo.getChildren().add(lastChildren);
					}
					if (perm.level() == 2 && lastChildren != null) {
						CatalogVo parentObject = exists
								.get(catalog.path().length - 2 + "-" + catalog.path()[catalog.path().length - 2]);
						CatalogVo functionVo = new CatalogVo();
						functionVo.setIndex(catalog.path().length + 1 + "-" + perm.path());
						functionVo.setCname(perm.cname());
						functionVo.setPath(perm.path());
						parentObject.getChildren().add(functionVo);
						vo.getChildren().add(lastChildren);
					}
					boolean bol = method.getDeclaringClass().getSimpleName().equals("BaseController");
					if (bol) {
						try {
							Method getMap = cls.getMethod("getMap");
							Map<String, String> permMap = (Map<String, String>) getMap.invoke(bean);
							chidlrenVo.getPerms().add(permMap.get(method.toString()));
						} catch (NoSuchMethodException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						chidlrenVo.getPerms().add(perm.value() + "," + perm.cname());
					}
				}
			} else {
				for (Method method : lists) {
					SysRequiresPermissions perm = method.getDeclaredAnnotation(SysRequiresPermissions.class);
					CatalogVo vo = this.createVo(null, perm, catalog.path().length);
					if (vo != null) {
						catalogs.add(vo);
					}
					vo.getPerms().add(perm.value() + "," + perm.cname());
				}
			}

		}
		result.add(catalogs);
		result.add(catalogsVo.getChildren());
		return result;
	}

	public CatalogVo createVo(PermissionsCatalog catalog, SysRequiresPermissions perm, int index) {
		CatalogVo chidlrenVo = new CatalogVo();
		if (perm == null) {
			chidlrenVo.setName(catalog.path()[index]);
			chidlrenVo.setPath((catalog.path().length - 2 == index ? "/" : "") + catalog.path()[index]);
			chidlrenVo.setCname(catalog.cname()[index]);
			if (catalog.path().length - 2 == index) {
				chidlrenVo.setComponent("layout");
			} else if (catalog.path().length - 1 == index) {
				chidlrenVo.setComponent(
						"/" + catalog.path()[index - 1] + "/" + catalog.path()[index] + "/" + catalog.component());
			}
		} else {
			if (StringUtils.isNotEmpty(perm.path())) {
				chidlrenVo.setName(perm.name());
				chidlrenVo.setPath(perm.path());
				chidlrenVo.setCname(perm.cname());
				if (catalog == null) {
					chidlrenVo.setComponent("/" + perm.path());
				} else {
					chidlrenVo.setComponent(
							"/" + catalog.path()[index - 2] + "/" + catalog.path()[index - 1] + "/" + perm.path());
				}
			} else {
				return null;
			}
		}
		if (catalog != null) {
			chidlrenVo.setHidden(catalog.hidden());
		}
		if (perm != null) {
			chidlrenVo.setHidden(perm.hidden());
		}
		chidlrenVo.setPerms(new ArrayList<>());
		chidlrenVo.setChildren(new ArrayList<>());
		return chidlrenVo;
	}

	/**
	 * @param index
	 * @param catalog
	 * @param parent
	 * @param exists
	 */
	public static void each(int index, PermissionsCatalog catalog, CatalogVo parent, Map<String, CatalogVo> exists) {
		if (index < catalog.path().length) {
			if (index != 0) {
				CatalogVo parentVo = exists.get(index - 1 + "-" + catalog.path()[index - 1]);
				if (parentVo != null) {
					if (parentVo.getChildren() == null) {
						parentVo.setChildren(new ArrayList<CatalogVo>());
						CatalogVo vo = new CatalogVo();
						vo.setIndex(index + "-" + catalog.path()[index]);
						exists.put(index + "-" + catalog.path()[index], vo);
						vo.setCname(catalog.cname()[index]);
						vo.setPath(catalog.path()[index]);
						parentVo.getChildren().add(vo);
					} else {
						boolean bol = true;
						for (int i = 0; i < parentVo.getChildren().size(); i++) {
							if (parentVo.getChildren().get(i).getPath().equals(catalog.path()[index])) {
								bol = false;
							}
						}
						if (bol) {
							CatalogVo vo = new CatalogVo();
							exists.put(index + "-" + catalog.path()[index], vo);
							vo.setCname(catalog.cname()[index]);
							vo.setPath(catalog.path()[index]);
							vo.setIndex(index + "-" + catalog.path()[index]);
							parentVo.getChildren().add(vo);
						}
					}
				}
				each(++index, catalog, parentVo, exists);
			} else {
				CatalogVo parentVo = exists.get(index + "-" + catalog.path()[index]);
				if (parentVo == null) {
					CatalogVo vo = new CatalogVo();
					exists.put(index + "-" + catalog.path()[index], vo);
					vo.setCname(catalog.cname()[index]);
					vo.setPath(catalog.path()[index]);
					vo.setIndex(index + "-" + catalog.path()[index]);
					if (parent.getChildren() == null) {
						parent.setChildren(new ArrayList<CatalogVo>());
					}
					parent.getChildren().add(vo);
				} else {
					parent = parentVo;
				}
				each(++index, catalog, parent, exists);
			}
		}
	}

}
