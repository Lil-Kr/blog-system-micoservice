package org.cy.micoservice.blog.message.provider.dao.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.cy.micoservice.blog.entity.message.model.provider.po.ChatRelation;

/**
 * @Author: Lil-K
 * @Date: 2025/12/26
 * @Description:
 */
@Mapper
public interface ChatRelationMapper extends BaseMapper<ChatRelation> {

  @Update("update t_chat_relation set content=#{content}, latest_msg_time = now() where relation_id = #{relationId}")
  void updateRelationByRelationId(@Param("relationId") Long relationId, @Param("content") String content);
}