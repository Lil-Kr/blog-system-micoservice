package org.cy.micoservice.blog.admin.pojo.dto.blog;

import org.cy.micoservice.blog.admin.common.holder.RequestHolder;
import org.cy.micoservice.blog.admin.pojo.entity.blog.BlogDiary;
import org.cy.micoservice.blog.admin.pojo.req.blog.diary.DiarySaveReq;
import org.cy.micoservice.blog.common.utils.DateUtil;
import org.cy.micoservice.blog.common.utils.IdWorker;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author: Lil-K
 * @Date: 2025/5/8
 * @Description:
 */
public class BlogDiaryDTO {

  /**
   * convert req to save
   * @param req
   * @return
   */
  public static BlogDiary convertAddDiaryEntity(DiarySaveReq req) {
    BlogDiary diary = new BlogDiary();
    BeanUtils.copyProperties(req, diary);

    diary.setId(IdWorker.getSnowFlakeId());
    Date nowDateTime = DateUtil.localDateTimeToDate(LocalDateTime.now());
    Long id = RequestHolder.getCurrentUser().getSurrogateId();
    diary.setCreatorId(id);
    diary.setOperator(id);
    diary.setCreateTime(nowDateTime);
    diary.setUpdateTime(nowDateTime);
    return diary;
  }

  public static BlogDiary convertEditDiaryEntity(DiarySaveReq req) {
    BlogDiary diary = new BlogDiary();
    BeanUtils.copyProperties(req, diary);
    diary.setOperator(RequestHolder.getCurrentUser().getSurrogateId());
    diary.setUpdateTime(DateUtil.localDateTimeToDate(LocalDateTime.now()));
    return diary;
  }
}