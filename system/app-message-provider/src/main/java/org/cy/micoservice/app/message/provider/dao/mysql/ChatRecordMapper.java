package org.cy.micoservice.app.message.provider.dao.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.cy.micoservice.app.entity.message.model.provider.pojo.mysql.ChatRecord;
import org.springframework.stereotype.Repository;

/**
 * @Author: Lil-K
 * @Date: 2025/12/26
 * @Description:
 */
@Repository
public interface ChatRecordMapper extends BaseMapper<ChatRecord> {
}
