package com.accp.vo;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CatalogVo {
	private String index;
	private String name;
	private String path;
	private String component;
	private List<CatalogVo> children;
	private String cname;
	private List<String> perms;
	private boolean hidden;
}
