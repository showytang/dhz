package com.accp.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import lombok.Data;

@SuppressWarnings("all")
@Data
public class BaseEntity<T> extends Model implements Serializable {
	@TableField(exist=false)
	private Integer page;
	@TableField(exist=false)
	private Integer size;
}

