package com.cy.auth.pojo.dto.aclmodule;

import com.cy.auth.pojo.dto.acl.AclDto;
import com.cy.auth.pojo.entity.AclModule;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * <p>
 *  权限模块Dto
 * </p>
 *
 * @author Lil-K
 * @since 2020-11-26
 */
@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AclModuleDto extends AclModule {

    private List<AclModuleDto> aclModuleDtoList = Lists.newArrayList();

    /**
     * 权限点数据
     */
    private List<AclDto> aclDtoList = Lists.newArrayList();

    /**
     * 将权限模块数据转换为一颗树形结构
     * @return
     */
    public static AclModuleDto adapt(AclModule aclModule){
        AclModuleDto dto = new AclModuleDto();
        BeanUtils.copyProperties(aclModule,dto);
        return dto;
    }
}
