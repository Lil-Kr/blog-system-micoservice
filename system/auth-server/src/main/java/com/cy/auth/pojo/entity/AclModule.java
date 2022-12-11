package com.cy.auth.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author Lil-K
 * @since 2020-11-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AclModule extends Model<AclModule> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 权限模块id,唯一主键
     */
    private Long surrogateId;

    /**
     * 权限模块number
     */
    private String number;

    /**
     * 权限模块名称
     */
    private String name;

    /**
     * 父id
     */
    private Long parentId;

    /**
     * 权限模块层级
     */
    private String level;

    /**
     * 顺序
     */
    private Integer seq;

    /**
     * 0正常, 1冻结
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 操作ip
     */
    private String operateIp;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更改时间
     */
    private String updateTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}