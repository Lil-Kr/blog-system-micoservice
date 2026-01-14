package org.cy.micoservice.blog.admin.provider.common.post;

import jakarta.annotation.PostConstruct;
import org.cy.micoservice.blog.admin.provider.common.constants.CommonConstants;
import org.cy.micoservice.blog.admin.provider.dao.*;
import org.cy.micoservice.blog.admin.provider.service.RbacCacheService;
import org.cy.micoservice.blog.entity.admin.model.entity.blog.BlogLabel;
import org.cy.micoservice.blog.entity.admin.model.entity.blog.BlogTopic;
import org.cy.micoservice.blog.entity.admin.model.entity.sys.SysAdmin;
import org.cy.micoservice.blog.entity.admin.model.entity.sys.SysDict;
import org.cy.micoservice.blog.entity.admin.model.entity.sys.SysDictDetail;
import org.cy.micoservice.blog.entity.admin.model.req.blog.category.BlogCategoryPageReq;
import org.cy.micoservice.blog.entity.admin.model.req.blog.label.BlogLabelListReq;
import org.cy.micoservice.blog.entity.admin.model.req.blog.topic.BlogTopicReq;
import org.cy.micoservice.blog.entity.admin.model.resp.blog.BlogCategoryResp;
import org.cy.micoservice.blog.entity.admin.model.resp.sys.dic.SysDictDetailResp;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Lil-K
 * @Date: 2025/3/28
 * @Description: 初始化数据
 */
@Component
public class PostConstructComponent {

  @Autowired
  private RbacCacheService rbacCacheService;

  @Autowired
  private BlogTopicMapper blogTopicMapper;

  @Autowired
  private BlogLabelMapper blogLabelMapper;

  @Autowired
  private SysDictMapper dictMapper;

  @Autowired
  private SysDictDetailMapper dictDetailMapper;

  @Autowired
  private BlogCategoryMapper blogCategoryMapper;

  @Autowired
  private SysAdminMapper adminMapper;

  /**
   * 初始化:
   *  - [博客-标签]列表
   *  - [博客-分类]列表
   *  - [博客-专题]列表
   *  - 数据字典信息
   */
  @PostConstruct
  public void initBlogLabel() {
    // cache admin-user data
    List<SysAdmin> users = adminMapper.selectAdminAllList();
    rbacCacheService.initAdminIdCache(users);

    // cache blog-label data
    List<BlogLabel> labelList = blogLabelMapper.labelList(new BlogLabelListReq());
    rbacCacheService.saveLabelCache(CommonConstants.CACHE_KEY_BLOG_LABEL_LIST, labelList);

    // cache blog category
    List<BlogCategoryResp> blogCategoryList = blogCategoryMapper.categoryList(new BlogCategoryPageReq());
    rbacCacheService.saveBlogCategoryCache(blogCategoryList);

    // cache blog topic
    List<BlogTopic> blogTopics = blogTopicMapper.topicList(new BlogTopicReq());
    rbacCacheService.saveBlogTopicCache(blogTopics);

    // cache dict data
    List<SysDict> dictList = dictMapper.selectDictList();
    List<SysDictDetailResp> dictDetailListVO = dictDetailMapper.dictDetailList();
    List<SysDictDetail> dictDetailList = dictDetailListVO.stream().map(item -> {
      SysDictDetail dictDetail = new SysDictDetail();
      BeanUtils.copyProperties(item, dictDetail);
      return dictDetail;
    }).collect(Collectors.toList());
    rbacCacheService.saveDictDetailCache(dictList, dictDetailList);

  }

}
