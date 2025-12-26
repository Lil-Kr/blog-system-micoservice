package org.cy.micoservice.blog.message.provider.dao.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.cy.micoservice.blog.entity.message.model.provider.po.ChatRecord;

/**
 * @Author: Lil-K
 * @Date: 2025/12/26
 * @Description:
 */
@Mapper
public interface ChatRecordMapper extends BaseMapper<ChatRecord> {
}
