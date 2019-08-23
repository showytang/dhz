package com.accp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.accp.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;
/**
 * <p>
 * 系统管理,权限管理,sys,syspermissions
 * </p>
 *
 * @author author
 * @since 2019-08-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="SysPermissions对象", description="系统管理,权限管理,sys,syspermissions")
public class SysPermissions extends BaseEntity<SysPermissions> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "权限")
    private String permission;

    @ApiModelProperty(value = "角色编号")
    private Integer roleid;


    public static final String ID = "id";

    public static final String PERMISSION = "permission";

    public static final String ROLEID = "roleid";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
