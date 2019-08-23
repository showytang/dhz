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
 * 系统管理,角色管理,sys,sysroles
 * </p>
 *
 * @author author
 * @since 2019-08-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="SysRoles对象", description="系统管理,角色管理,sys,sysroles")
public class SysRoles extends BaseEntity<SysRoles> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "编号")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "名称")
    private String role;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "是否启用")
    private Boolean available;


    public static final String ID = "id";

    public static final String ROLE = "role";

    public static final String DESCRIPTION = "description";

    public static final String AVAILABLE = "available";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
