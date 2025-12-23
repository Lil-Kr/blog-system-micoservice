package org.cy.micoservice.blog.audit.provider.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.cy.micoservice.blog.entity.audit.model.facade.entity.AuditLog;
import org.springframework.stereotype.Repository;

/**
 * @Author: Lil-K
 * @Date: 2025/12/16
 * @Description:
 */
@Repository
public interface AuditLogMapper extends BaseMapper<AuditLog> {

}