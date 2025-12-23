package org.cy.micoservice.blog.audit.provider.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cy.micoservice.blog.audit.provider.dao.AuditLogMapper;
import org.cy.micoservice.blog.audit.provider.service.AuditLogService;
import org.cy.micoservice.blog.entity.audit.model.facade.entity.AuditLog;
import org.springframework.stereotype.Service;

/**
 * @Author: Lil-K
 * @Date: 2025/12/16
 * @Description:
 */
@Service
public class AuditLogServiceImpl extends ServiceImpl<AuditLogMapper, AuditLog> implements AuditLogService {
}
