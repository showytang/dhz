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
 * 其它,文件上传,other,upload
 * </p>
 *
 * @author author
 * @since 2019-08-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="Upload对象", description="其它,文件上传,other,upload")
public class Upload extends BaseEntity<Upload> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "编号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "文件名称")
    private String name;

    @ApiModelProperty(value = "文件地址")
    private String url;

    @ApiModelProperty(value = "文件描述")
    private String description;


    public static final String ID = "id";

    public static final String NAME = "name";

    public static final String URL = "url";

    public static final String DESCRIPTION = "description";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
