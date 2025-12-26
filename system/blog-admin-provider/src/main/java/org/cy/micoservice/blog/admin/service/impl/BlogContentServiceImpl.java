package org.cy.micoservice.blog.admin.service.impl;

import org.cy.micoservice.blog.entity.admin.model.entity.SysUser;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.luciad.imageio.webp.WebPWriteParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.cy.micoservice.blog.admin.common.holder.RequestHolder;
import org.cy.micoservice.blog.admin.dao.BlogContentImageMapper;
import org.cy.micoservice.blog.admin.dao.BlogContentMapper;
import org.cy.micoservice.blog.admin.dao.BlogContentMongoMapper;
import org.cy.micoservice.blog.admin.pojo.dto.blog.BlogContentDTO;
import org.cy.micoservice.blog.admin.pojo.entity.blog.*;
import org.cy.micoservice.blog.admin.pojo.req.blog.content.BlogContentPageReq;
import org.cy.micoservice.blog.admin.pojo.req.blog.content.BlogContentReq;
import org.cy.micoservice.blog.admin.pojo.req.blog.content.BlogRichEditorImageReq;
import org.cy.micoservice.blog.admin.pojo.resp.blog.BlogCategoryResp;
import org.cy.micoservice.blog.admin.pojo.resp.blog.BlogContentGroupResp;
import org.cy.micoservice.blog.admin.pojo.resp.blog.BlogContentResp;
import org.cy.micoservice.blog.admin.pojo.resp.blog.PrevAndNext;
import org.cy.micoservice.blog.admin.service.BlogContentService;
import org.cy.micoservice.blog.admin.service.CacheService;
import org.cy.micoservice.blog.admin.service.MessageLangService;
import org.cy.micoservice.blog.common.utils.DateUtil;
import org.cy.micoservice.blog.common.utils.IdWorker;
import org.cy.micoservice.blog.common.base.ApiResp;
import org.cy.micoservice.blog.common.base.PageResult;
import org.cy.micoservice.blog.common.enums.response.ApiReturnCodeEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import static org.cy.micoservice.blog.admin.common.constants.CommonConstants.IMAGE_TYPE_SUFFIX;
import static org.cy.micoservice.blog.admin.common.constants.CommonConstants.LANG_ZH;
import static org.cy.micoservice.blog.common.enums.response.ApiReturnCodeEnum.INFO_NOT_EXIST;

/**
 * @Author: Lil-K
 * @Date: 2024/5/24
 * @Description:
 */
@Service
@Slf4j
public class BlogContentServiceImpl implements BlogContentService {


  @Value("${upload.rootDir}")
  private String rootDir;

  @Value("${upload.uploadDir}")
  private String uploadDir;

  @Value("${upload.blogContentImagePath}")
  private String blogContentImagePath;

  @Autowired
  private BlogContentMapper blogContentMapper;

  @Autowired
  private BlogContentMongoMapper blogContentMongoMapper;

  @Autowired
  private BlogContentImageMapper blogContentImageMapper;

  @Autowired
  private MessageLangService msgService;

  @Autowired
  private CacheService cacheService;

  @Override
  public ApiResp<String> add(BlogContentReq req) {
    BlogContent blogContent = BlogContentDTO.convertAddBlogContentReq(req);

    // insert into mysql
    int insert = blogContentMapper.insert(blogContent);
    if (insert < 1) {
      return ApiResp.failure(ApiReturnCodeEnum.ADD_ERROR);
    }

    if (StringUtils.isBlank(req.getContentText())) {
      return ApiResp.success("添加博客成功, 但文章内容没有任何值");
    }

    BlogContentMongo blogContentMongo = BlogContentMongo.builder()
      .id(String.valueOf(blogContent.getSurrogateId()))
      .contentText(req.getContentText())
      .build();
    // insert into mongodb
    saveBlogContentMongo(blogContentMongo);
    return ApiResp.success();
  }

  /**
   * 保存博客内容
   * @param entity
   * @return
   */
  @Override
  public BlogContentMongo saveBlogContentMongo(BlogContentMongo entity) {
    return blogContentMongoMapper.save(entity);
  }

  /**
   * 获取博客内容
   * @param surrogateId
   * @return
   */
  private BlogContentMongo getBlogContentMongo(Long surrogateId) {
    return blogContentMongoMapper.findById(String.valueOf(surrogateId)).orElse(null);
  }

  /**
   * 分页查询博客列表
   * @param req
   * @return
   */
  @Override
  public PageResult<BlogContentResp> pageContentList(BlogContentPageReq req) {
    List<BlogContentResp> pageList = blogContentMapper.pageContentList(req);
    Integer count = blogContentMapper.pageContentCount(req);
    if (CollectionUtils.isEmpty(pageList)) {
      return new PageResult<>(new ArrayList<>(0), 0);
    }
    pageList.forEach(item -> {
      // label info
      List<BlogLabel> labelList = Arrays.stream(item.getLabelIds().split(","))
        .map(Long::valueOf)
        .map(cacheService::getLabelCache)
        .collect(Collectors.toList());
      item.setBlogLabelList(labelList);

      // category info
      BlogCategoryResp categoryVO = cacheService.getBlogCategoryCache(item.getCategoryId());
      item.setCategoryName(categoryVO.getName());
      item.setCategoryColor(categoryVO.getColor());

      // topic info
      if (Objects.nonNull(item.getTopicId())) {
        BlogTopic topic = cacheService.getTopicCache(item.getTopicId());
        item.setTopicName(topic.getName());
        item.setTopicColor(topic.getColor());
      }

      // original
      item.setOriginalType(cacheService.getDictDetailCache(item.getOriginal()).getType());

      // recommend
      item.setRecommendType(cacheService.getDictDetailCache(item.getRecommend()).getType());
    });
    return new PageResult<>(pageList, count);
  }

  @Override
  public PageResult<BlogContentResp> contentList(BlogContentPageReq req) {
    List<BlogContentResp> list = blogContentMapper.contentList(req);
    if (CollectionUtils.isEmpty(list)) {
      return new PageResult<>(new ArrayList<>(0), 0);
    }

    list.forEach(item -> {
      // 标签信息
      List<BlogLabel> labelList = Arrays.stream(item.getLabelIds().split(","))
        .map(Long::valueOf)
        .map(cacheService::getLabelCache)
        .collect(Collectors.toList());
      item.setBlogLabelList(labelList);

      // 分类信息
      item.setCategoryName(cacheService.getBlogCategoryCache(item.getCategoryId()).getName());
      item.setCategoryColor(cacheService.getBlogCategoryCache(item.getCategoryId()).getColor());

      // 所属专题
      item.setTopicName(cacheService.getTopicCache(item.getTopicId()).getName());
      item.setTopicColor(cacheService.getTopicCache(item.getTopicId()).getColor());

      // 是否原创
      item.setOriginalType(cacheService.getDictDetailCache(item.getOriginal()).getType());

      // 是否推荐
      item.setRecommendType(cacheService.getDictDetailCache(item.getRecommend()).getType());
    });
    return new PageResult<>(list, list.size());
  }

  /**
   * 获取单条博客信息, 包括博客内容
   * @param blogId
   * @return
   */
  @Override
  public ApiResp<BlogContentResp> getBlog(Long blogId) {
    QueryWrapper<BlogContent> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("surrogate_id", blogId);
    BlogContent blogContent = blogContentMapper.selectOne(queryWrapper);
    if (Objects.isNull(blogContent)) {
      return ApiResp.failure(INFO_NOT_EXIST);
    }

    // get blog info from mongodb
    BlogContentMongo blogContentMongo = getBlogContentMongo(blogId);
    if (Objects.isNull(blogContentMongo) || ! String.valueOf(blogContent.getSurrogateId()).equals(blogContentMongo.getId())) {
      return ApiResp.failure(INFO_NOT_EXIST);
    }

    // get post blog and next blog
    PrevAndNext prev = this.prevBlog(blogId);
    PrevAndNext next = this.nextBlog(blogId);

    BlogContentResp resp = new BlogContentResp();
    BeanUtils.copyProperties(blogContent, resp);
    resp.setContentText(blogContentMongo.getContentText());
    resp.setPrev(prev);
    resp.setNext(next);

    return ApiResp.success(resp);
  }

  /**
   * get post blog and next blog info
   * @param blogId
   * @return
   */
  private PrevAndNext prevBlog(Long blogId) {
    return Optional.ofNullable(blogContentMapper.prevBlog(blogId)).orElse(new PrevAndNext());
  }

  private PrevAndNext nextBlog(Long blogId) {
    return Optional.ofNullable(blogContentMapper.nextBlog(blogId)).orElse(new PrevAndNext());
  }

  @Override
  public ApiResp<String> edit(BlogContentReq req) {
    QueryWrapper<BlogContent> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("surrogate_id", req.getSurrogateId());
    BlogContent blogContent = blogContentMapper.selectOne(queryWrapper);
    if (Objects.isNull(blogContent)) {
      return ApiResp.failure();
    }

    BeanUtils.copyProperties(req, blogContent);
    blogContent.setUpdateTime(DateUtil.dateTimeNow());
    blogContent.setOperator(RequestHolder.getCurrentUser().getSurrogateId());
    blogContent.setLabelIds(req.getLabelIds().stream().map(String::valueOf).collect(Collectors.joining(",")));

    UpdateWrapper<BlogContent> updateWrapper = new UpdateWrapper<>();
    updateWrapper.eq("surrogate_id", req.getSurrogateId());
    int update = blogContentMapper.update(blogContent, updateWrapper);

    if (update < 1) {
      return ApiResp.failure();
    }

    if (StringUtils.isBlank(req.getContentText())) {
      return ApiResp.success("更新博客成功, 但文章内容没有任何值");
    }

    // update mongodb
    BlogContentMongo updateMongo = BlogContentMongo.builder().id(String.valueOf(req.getSurrogateId())).contentText(req.getContentText()).build();
    saveBlogContentMongo(updateMongo);
    return ApiResp.success();
  }

  /**
   * publish blog
   * @param req
   * @return
   */
  @Override
  public ApiResp<String> publishBlog(BlogContentReq req) {
    BlogContent content = new BlogContent();
    BeanUtils.copyProperties(req, content);
    Date nowDate = DateUtil.dateTimeNow();
    content.setPublishTime(nowDate);
    content.setUpdateTime(nowDate);
    Integer update = blogContentMapper.updateStatusBySurrogateId(content);
    if (update < 1) {
      return ApiResp.failure();
    }
    return ApiResp.success();
  }

  @Override
  public ApiResp<BlogContentResp> getContent(Long blogId) {
    BlogContentMongo blogContentMongo = getBlogContentMongo(blogId);
    BlogContentResp res = new BlogContentResp();
    res.setSurrogateId(blogId);

    if (Objects.isNull(blogContentMongo)) {
      return ApiResp.success(res);
    }

    res.setContentText(blogContentMongo.getContentText());
    return ApiResp.success(res);
  }

  @Override
  public ApiResp<String> delete(Long surrogateId) {
    QueryWrapper<BlogContent> query = new QueryWrapper<>();
    query.eq("surrogate_id", surrogateId);
    BlogContent blogContent = blogContentMapper.selectOne(query);
    if (Objects.isNull(blogContent)) {
      return ApiResp.warning(INFO_NOT_EXIST);
    }

    blogContentMapper.delete(query);
    BlogContentMongo blogContentMongo = BlogContentMongo.builder()
      .id(String.valueOf(blogContent.getSurrogateId()))
      .build();
    blogContentMongoMapper.delete(blogContentMongo);
    return ApiResp.success();
  }

  /**
   * 富文本编辑器中上传的图片
   * @param req
   * @return
   * @throws Exception
   */
  @Override
  public ApiResp<BlogRichEditorResp> uploadBlogContentImage(BlogRichEditorImageReq req) throws Exception {
    MultipartFile imageFile = req.getImage();
    // 检查文件大小, 限制为 2MB
    long maxSizeInBytes = 2 * 1024 * 1024; // 2MB
    if (imageFile == null || imageFile.getSize() > maxSizeInBytes) {
      return ApiResp.failure(msgService.getMessage(LANG_ZH, "blog.image.upload.error1"));
    }

    String imageOriginalFullName = imageFile.getOriginalFilename();
    String[] imageFileNames = imageOriginalFullName.split("\\.");
    if (imageFileNames.length > 2) {
      return ApiResp.failure(msgService.getMessage(LANG_ZH, "blog.image.upload.error2"));
    }

    String imageName = imageFileNames[0];

    SysUser currentUser = RequestHolder.getCurrentUser();
    StringBuffer resourcePath = new StringBuffer(rootDir);
    resourcePath.append(uploadDir)
      .append(blogContentImagePath).append("/")
      .append(currentUser.getAccount()).append("/")
      .append(DateUtil.getNowDateTimeForYMD()).append("/");

    /**
     * create Path into disk
     */
    Path rootPath = Paths.get(resourcePath.toString());
    if (!Files.exists(rootPath)) {
      Files.createDirectories(rootPath);
    }

    /**
     * re-name
     */
    StringBuffer imageReName = new StringBuffer(imageName).append("_")
      .append(IdWorker.getSnowFlakeId())
      .append(".")
      .append(IMAGE_TYPE_SUFFIX);

    /**
     * full-path
     */
    resourcePath.append(imageReName);

    try(InputStream inputStream = imageFile.getInputStream()) {
      /**
       * write image to disk
       */
      BufferedImage originalImage = ImageIO.read(inputStream);
      Iterator<ImageWriter> writers = ImageIO.getImageWritersByMIMEType("image/webp");
      if (!writers.hasNext()) {
        return ApiResp.failure("No writers found for format: webp");
      }

      ImageWriter writer = writers.next();
      // writer webp to disk
      try (ImageOutputStream ios = ImageIO.createImageOutputStream(Files.newOutputStream(Paths.get(resourcePath.toString())))) {
        WebPWriteParam writeParam = new WebPWriteParam(writer.getLocale());
        writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        writeParam.setCompressionType(writeParam.getCompressionTypes()[WebPWriteParam.LOSSY_COMPRESSION]); // lossy compression
        writeParam.setCompressionQuality(0.75f);
        writer.setOutput(ios);
        writer.write(null, new IIOImage(originalImage, null, null), writeParam);
      } catch (Exception e) {
        log.info("image format webp error: {}", e.getMessage());
        e.printStackTrace();
        return ApiResp.failure(e.getMessage());
      } finally {
        writer.dispose();
      }

      StringBuffer imageUrl = new StringBuffer(uploadDir)
        .append(blogContentImagePath).append("/")
        .append(currentUser.getAccount()).append("/")
        .append(DateUtil.getNowDateTimeForYMD()).append("/")
        .append(imageReName);
      BlogRichEditorResp res = new BlogRichEditorResp();
      res.setUrl(imageUrl.toString());
      return ApiResp.success(res);
    } catch (Exception e) {
      log.info("upload image error: {}", e.getMessage());
      return ApiResp.failure(e.getMessage());
    }
  }

  /**
   *
   * @return
   */
  @Override
  public ApiResp<List<BlogContentResp>> frontContentList() {
    List<BlogContentResp> res = blogContentMapper.frontContentList();
    if (CollectionUtils.isEmpty(res)) {
      return ApiResp.success(new ArrayList<>());
    }
    return ApiResp.success(res);
  }

  @Override
  public List<BlogContentGroupResp> frontContentByGroupCategory() {
    return blogContentMapper.frontContentByGroupCategory();
  }

  @Override
  public PageResult<BlogContentResp> frontContentPageList(BlogContentPageReq req) {
    req.setStatus(1);
    req.setIsOrder(1);
    List<BlogContentResp> pageList = blogContentMapper.pageFrontContentList(req);
    if (CollectionUtils.isEmpty(pageList)) {
      return new PageResult<>(new ArrayList<>(0), 0);
    }
    Integer count = blogContentMapper.pageFrontContentCount(req);

    return new PageResult<>(new ArrayList<>(pageList), count);
  }
}
