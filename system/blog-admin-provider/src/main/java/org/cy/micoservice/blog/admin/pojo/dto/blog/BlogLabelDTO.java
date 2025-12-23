package org.cy.micoservice.blog.admin.pojo.dto.blog;

import org.cy.micoservice.blog.admin.common.holder.RequestHolder;
import org.cy.micoservice.blog.admin.pojo.entity.blog.BlogLabel;
import org.cy.micoservice.blog.admin.pojo.req.blog.label.BlogLabelListReq;
import org.cy.micoservice.blog.admin.pojo.req.blog.label.BlogLabelReq;
import org.cy.micoservice.blog.admin.pojo.resp.blog.BlogLabelResp;
import org.cy.micoservice.blog.common.utils.DateUtil;
import org.cy.micoservice.blog.common.utils.IdWorker;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Lil-K
 * @Date: 2024/3/31
 * @Description:
 */
public class BlogLabelDTO {

  /** ======================= blog-label  ======================= **/
  public static BlogLabel convertSaveLabelReq(BlogLabelReq baseReq) {
    BlogLabel req = new BlogLabel();
    BeanUtils.copyProperties(baseReq, req);

    req.setSurrogateId(IdWorker.getSnowFlakeId());
    Date nowDateTime = DateUtil.localDateTimeToDate(LocalDateTime.now());

    req.setCreatorId(RequestHolder.getCurrentUser().getSurrogateId());
    req.setOperator(RequestHolder.getCurrentUser().getSurrogateId());
    req.setCreateTime(nowDateTime);
    req.setUpdateTime(nowDateTime);
    return req;
  }

  public static BlogLabel convertEditLabelReq(BlogLabelReq baseReq) {
    BlogLabel req = new BlogLabel();
    BeanUtils.copyProperties(baseReq, req);

    Date nowDateTime = DateUtil.localDateTimeToDate(LocalDateTime.now());
    req.setOperator(RequestHolder.getCurrentUser().getSurrogateId());
    req.setUpdateTime(nowDateTime);
    return req;
  }


  public static BlogLabel convertDelLabelReq(BlogLabelReq baseReq) {
    BlogLabel req = new BlogLabel();
    BeanUtils.copyProperties(baseReq, req);
    return req;
  }

  public static BlogLabel convertQueryLabelReq(BlogLabelListReq baseReq) {
    BlogLabel req = new BlogLabel();
    BeanUtils.copyProperties(baseReq, req);
    return req;
  }

  /**
   *
   * @param blogLabels
   * @return
   */
  public static List<BlogLabelResp> convertLabelsToVO(List<BlogLabel> blogLabels) {
    return blogLabels.stream()
      .map(blogLabel -> {
        BlogLabelResp req = new BlogLabelResp();
        BeanUtils.copyProperties(blogLabel, req);
        return req;
      })
      .collect(Collectors.toList());
  }

  /** ======================= blog-topic  ======================= **/
}
