package org.cy.micoservice.blog.admin.common.post;

import jakarta.annotation.PostConstruct;
import org.cy.micoservice.blog.admin.common.constants.CommonConstants;
import org.cy.micoservice.blog.admin.dao.*;
import org.cy.micoservice.blog.admin.pojo.entity.blog.BlogLabel;
import org.cy.micoservice.blog.admin.pojo.entity.blog.BlogTopic;
import org.cy.micoservice.blog.admin.pojo.req.blog.category.BlogCategoryPageReq;
import org.cy.micoservice.blog.admin.pojo.req.blog.label.BlogLabelListReq;
import org.cy.micoservice.blog.admin.pojo.req.blog.topic.BlogTopicReq;
import org.cy.micoservice.blog.admin.pojo.resp.blog.BlogCategoryResp;
import org.cy.micoservice.blog.admin.service.CacheService;
import org.cy.micoservice.blog.entity.admin.model.entity.SysDict;
import org.cy.micoservice.blog.entity.admin.model.entity.SysDictDetail;
import org.cy.micoservice.blog.entity.admin.model.entity.SysUser;
import org.cy.micoservice.blog.entity.admin.model.resp.dic.SysDictDetailResp;
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
  private CacheService cacheService;

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
  private SysUserMapper userMapper;

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
    List<SysUser> users = userMapper.selectUserAllList();
    cacheService.initUserAdminIdCache(users);

    // cache blog-label data
    List<BlogLabel> labelList = blogLabelMapper.labelList(new BlogLabelListReq());
    cacheService.saveLabelCache(CommonConstants.CACHE_KEY_BLOG_LABEL_LIST, labelList);

    // cache blog category
    List<BlogCategoryResp> blogCategoryList = blogCategoryMapper.categoryList(new BlogCategoryPageReq());
    cacheService.saveBlogCategoryCache(blogCategoryList);

    // cache blog topic
    List<BlogTopic> blogTopics = blogTopicMapper.topicList(new BlogTopicReq());
    cacheService.saveBlogTopicCache(blogTopics);

    // cache dict data
    List<SysDict> dictList = dictMapper.selectDictList();
    List<SysDictDetailResp> dictDetailListVO = dictDetailMapper.dictDetailList();
    List<SysDictDetail> dictDetailList = dictDetailListVO.stream().map(item -> {
      SysDictDetail dictDetail = new SysDictDetail();
      BeanUtils.copyProperties(item, dictDetail);
      return dictDetail;
    }).collect(Collectors.toList());
    cacheService.saveDictDetailCache(dictList, dictDetailList);

  }

}
