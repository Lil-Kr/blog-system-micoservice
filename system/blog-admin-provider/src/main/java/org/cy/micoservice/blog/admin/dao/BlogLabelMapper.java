package org.cy.micoservice.blog.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.cy.micoservice.blog.admin.pojo.entity.blog.BlogLabel;
import org.cy.micoservice.blog.admin.pojo.req.blog.label.BlogLabelListReq;
import org.cy.micoservice.blog.admin.pojo.req.blog.label.BlogLabelPageReq;
import org.cy.micoservice.blog.admin.pojo.req.blog.label.BlogLabelReq;
import org.cy.micoservice.blog.admin.pojo.resp.blog.BlogLabelResp;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/3/24
 * @Description:
 */
@Repository
public interface BlogLabelMapper extends BaseMapper<BlogLabel> {

	Integer editBySurrogateId(@Param("param") BlogLabelReq req);

	Integer deleteBySurrogateId(Long surrogateId);

	List<BlogLabel> labelList(@Param("param") BlogLabelListReq req);

	Integer deleteBatch(List<Long> list);

	List<BlogLabelResp> pageList(@Param("param") BlogLabelPageReq req);

	Integer getCountByList(@Param("param") BlogLabelPageReq req);
}
