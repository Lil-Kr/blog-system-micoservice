package org.cy.micoservice.blog.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.cy.micoservice.blog.admin.pojo.entity.blog.BlogDiary;
import org.cy.micoservice.blog.admin.pojo.req.blog.diary.DiaryPageListReq;
import org.cy.micoservice.blog.admin.pojo.resp.blog.BlogDiaryPageListResp;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/5/8
 * @Description:
 */
@Repository
public interface BlogDiaryMapper extends BaseMapper<BlogDiary> {

  List<BlogDiaryPageListResp> pageDiaryList(@Param("param") DiaryPageListReq req);

  Integer countPageDiaryList(@Param("param") DiaryPageListReq req);

  Integer insertSelective(BlogDiary req);
}
