package org.cy.micoservice.blog.infra.console.service.impl.rbac;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.common.base.api.PageResult;
import org.cy.micoservice.blog.common.constants.CommonConstants;
import org.cy.micoservice.blog.common.enums.biz.DeleteStatusEnum;
import org.cy.micoservice.blog.common.enums.response.ApiReturnCodeEnum;
import org.cy.micoservice.blog.common.security.impl.AES128GCMCrypto;
import org.cy.micoservice.blog.common.utils.BeanCopyUtils;
import org.cy.micoservice.blog.common.utils.DateUtil;
import org.cy.micoservice.blog.entity.infra.console.model.entity.sys.SysAdmin;
import org.cy.micoservice.blog.entity.infra.console.model.req.sys.admin.*;
import org.cy.micoservice.blog.entity.infra.console.model.resp.sys.admin.SysAdminResp;
import org.cy.micoservice.blog.framework.id.starter.service.IdService;
import org.cy.micoservice.blog.framework.identiy.starter.config.AuthProperties;
import org.cy.micoservice.blog.framework.identiy.starter.uitls.JWTUtil;
import org.cy.micoservice.blog.infra.console.config.InfraApplicationProperties;
import org.cy.micoservice.blog.infra.console.dao.rbac.SysAdminMapper;
import org.cy.micoservice.blog.infra.console.service.MessageLangService;
import org.cy.micoservice.blog.infra.console.service.RbacCacheService;
import org.cy.micoservice.blog.infra.console.service.SysAdminService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.cy.micoservice.blog.common.enums.response.ApiReturnCodeEnum.*;
import static org.cy.micoservice.blog.infra.facade.dto.admin.AdminDTO.convertAddUserReq;
import static org.cy.micoservice.blog.infra.facade.dto.admin.AdminDTO.convertEditUserReq;

/**
 * @Author: Lil-K
 * @Date: 2025/3/6
 * @Description:
 */
@Service
@Slf4j
public class SysAdminServiceImpl extends ServiceImpl<SysAdminMapper, SysAdmin> implements SysAdminService, InitializingBean {

  // @Value("${upload.rootDir}")
  // private String rootDir;
  //
  // @Value("${upload.uploadDir}")
  // private String uploadDir;
  //
  // @Value("${upload.adminUserPath}")
  // private String adminUserPath;

  @Autowired
  private AuthProperties authProperties;
  @Autowired
  private InfraApplicationProperties infraApplicationProperties;
  @Autowired
  private MessageLangService msgService;
  @Autowired
  private SysAdminMapper adminMapper;
  @Autowired
  private RbacCacheService rbacCacheService;
  @Autowired
  private IdService idService;

  private AES128GCMCrypto aes128GCMCrypto;

  /**
   * register admin
   * @param req
   * @return
   */
  @Override
  public ApiResp<Integer> registerAdmin(AdminRegisterReq req) {
    SysAdmin before = adminMapper.getAdminByAccount(req.getAccount());
    if (Objects.nonNull(before)) {
      return ApiResp.failure(ApiReturnCodeEnum.INFO_NOT_EXIST);
    }

    SysAdmin after = BeanCopyUtils.convert(req, SysAdmin.class);
    after.setId(idService.getId());
    after.setDeleted(DeleteStatusEnum.ACTIVE.getCode());
    after.setCreateId(req.getAdminId());
    after.setUpdateId(req.getAdminId());
    after.setCreateTime(DateUtil.localDateTimeNow());
    after.setUpdateTime(DateUtil.localDateTimeNow());
    int count = adminMapper.insert(after);
    if (count < 1) {
      return ApiResp.failure(ADD_ERROR);
    }

    // update cache
    rbacCacheService.setUserTokenCache(after.getToken(), after);
    rbacCacheService.setUserAdminIdCache(after.getId(), after);
    return ApiResp.success(ApiReturnCodeEnum.SUCCESS);
  }

  /**
   * insert admin-user
   * @param req
   * @return
   */
  @Override
  public ApiResp<String> add(AdminSaveReq req) {
    List<SysAdminResp> checkRes = adminMapper.selectAdminInfoExist(req);
    if (CollectionUtils.isNotEmpty(checkRes)) {
      return ApiResp.failure(DATA_INFO_REPEAT);
    }

    SysAdmin user = convertAddUserReq(req);
    int insert = adminMapper.insert(user);
    if (insert < 1) {
      return ApiResp.failure(ADD_ERROR);
    }

    // update cache
    rbacCacheService.setUserTokenCache(user.getToken(), user);
    rbacCacheService.setUserAdminIdCache(user.getId(), user);
    return ApiResp.success();
  }

  /**
   * edit admin-user
   * @param req
   * @return
   */
  @Override
  public ApiResp<String> edit(AdminSaveReq req) {
    QueryWrapper<SysAdmin> wrapper = new QueryWrapper<>();
    wrapper.eq("id", req.getId());
    SysAdmin before = adminMapper.selectOne(wrapper);
    if (Objects.isNull(before)) {
      return ApiResp.failure(UPDATE_ERROR);
    }

    SysAdmin user = convertEditUserReq(before, req);
    int update = adminMapper.updateAdminById(user);
    if (update < 1) {
      return ApiResp.failure(UPDATE_ERROR);
    }

    // update cache
    rbacCacheService.setUserTokenCache(user.getToken(), user);
    rbacCacheService.setUserAdminIdCache(user.getId(), user);
    return ApiResp.success();
  }

  /**
   * delete admin-user
   * @param surrogateId
   * @return
   */
  @Override
  public ApiResp<String> delete(Long surrogateId) {
    QueryWrapper<SysAdmin> wrapper = new QueryWrapper<>();
    wrapper.eq("id", surrogateId);
    SysAdmin admin = adminMapper.selectOne(wrapper);
    if (Objects.isNull(admin)) {
      return ApiResp.failure(INFO_NOT_EXIST);
    }
    int delete = adminMapper.delete(wrapper);
    if (delete < 1) {
      return ApiResp.failure(DEL_ERROR);
    }

    // remove catch
    rbacCacheService.removeUserTokenCache(admin.getToken());
    rbacCacheService.removeUserAdminIdCache(admin.getId());
    return ApiResp.success();
  }

  @Override
  public SysAdmin getUserById(Long id) {
    return adminMapper.getAdminById(id);
  }

  @Override
  public SysAdminResp getUserBySurrogateId(Long surrogateId) {
    return adminMapper.getAdminBySurrogateId(surrogateId);
  }

  @Override
  public ApiResp<SysAdmin> adminLogin(AdminLoginReq req) throws Exception {
    SysAdmin admin = adminMapper.loginAdmin(req);
    if (Objects.isNull(admin)) {
      return ApiResp.failure(USER_INFO_ERROR);
    }

    admin.setUpdateTime(DateUtil.localDateTimeNow());
    Integer update = adminMapper.updateAdminById(admin);
    if (update < 1) {
      return ApiResp.warning(USER_INFO_ERROR);
    }

    // update cache
    rbacCacheService.setUserTokenCache(admin.getToken(), admin);
    rbacCacheService.setUserAdminIdCache(admin.getId(), admin);

    Map<String, Long> map = new HashMap<>();
    map.put(CommonConstants.DEFAULT_AUTH_KEY_USER_ID, admin.getId());
    String token = this.aes128GCMCrypto.encrypt(JWTUtil.generateToken(JSONObject.toJSONString(map), authProperties.getSecretKey()));
    return ApiResp.success(SysAdmin.builder().token(token).build());
  }

  @Override
  public PageResult<SysAdminResp> pageList(AdminListPageReq req) {
    List<SysAdminResp> list =  adminMapper.pageAdminList(req);
    Integer count = adminMapper.countAdminList(req);
    if (CollectionUtils.isNotEmpty(list)) {
      return new PageResult<>(list, count);
    } else {
      return new PageResult<>(new ArrayList<>(0), 0);
    }
  }

  /**
   * upload avatar
   * @param req
   * @return
   */
  @Override
  public ApiResp<String> uploadAvatar(AvatarUploadReq req) throws Exception {
    // MultipartFile avatar = req.getAvatarFile();
    // long maxSizeInBytes = 1 * 1024 * 1024; // 1MB
    // if (avatar == null || avatar.getSize() > maxSizeInBytes) {
    // 	return ApiResp.failure(msgService.getMessage(LANG_ZH, "admin.upload.avatar.size.error1"));
    // }
    // String imageOriginalFullName = Optional.ofNullable(avatar.getOriginalFilename()).orElse("");
    // String[] imageFileNames = imageOriginalFullName.split("\\.");
    // if (imageFileNames.length > 2) {
    // 	return ApiResp.failure(msgService.getMessage(LANG_ZH, "admin.upload.avatar.size.error2"));
    // }
    //
    // String imageName = imageFileNames[0];
    // String imageTypeSuffix = "webp";
    //
    // StringBuffer resourcePath = new StringBuffer(rootDir);
    // resourcePath.append(uploadDir);
    //
    // /**
    //  * create Path object
    //  */
    // Path rootPath = Paths.get(resourcePath.toString());
    // if (!Files.exists(rootPath)) {
    // 	Files.createDirectories(rootPath);
    // }
    //
    // String imageReName = imageName + "_" + IdWorker.getSnowFlakeId() + "." + imageTypeSuffix;
    // SysAdmin currentUser = req.getAdminId();
    // resourcePath.append(adminUserPath).append("/")
    // 	.append(currentUser.getAccount()).append("/")
    // 	.append(imageReName);
    //
    // try(InputStream inputStream = avatar.getInputStream()) {
    // 	/**
    // 	 * write image to disk
    // 	 */
    // 	BufferedImage originalImage = ImageIO.read(inputStream);
    // 	Iterator<ImageWriter> writers = ImageIO.getImageWritersByMIMEType("image/webp");
    // 	if (!writers.hasNext()) {
    // 		return ApiResp.failure("No writers found for format: webp");
    // 	}
    //
    // 	ImageWriter writer = writers.next();
    // 	try (ImageOutputStream ios = ImageIO.createImageOutputStream(Files.newOutputStream(Paths.get(resourcePath.toString())))) {
    // 		WebPWriteParam writeParam = new WebPWriteParam(writer.getLocale());
    // 		writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
    // 		writeParam.setCompressionType(writeParam.getCompressionTypes()[WebPWriteParam.LOSSY_COMPRESSION]); // lossy compression
    // 		writeParam.setCompressionQuality(0.75f);
    // 		writer.setOutput(ios);
    // 		writer.write(null, new IIOImage(originalImage, null, null), writeParam);
    // 	} catch (Exception e) {
    // 		log.info("avatar format webp error: {}", e.getMessage());
    // 		return ApiResp.failure();
    // 	} finally {
    // 		writer.dispose();
    // 	}
    //
    // 	/**
    // 	 * update from DB
    // 	 */
    // 	StringBuffer imageUrl = new StringBuffer(uploadDir)
    // 		.append(adminUserPath)
    // 		.append("/").append(currentUser.getAccount())
    // 		.append("/").append(imageReName);
    // 	req.setUserId(currentUser.getSurrogateId());
    // 	req.setAvatar(imageUrl.toString());
    // 	// update from DB
    // 	int update = userMapper.updateAvatar(req);
    // 	if (update < 1) {
    // 		return ApiResp.failure(msgService.getMessage(LANG_ZH, "admin.upload.avatar.error"));
    // 	}
    // 	return ApiResp.success(msgService.getMessage(LANG_ZH, "admin.upload.avatar.success"));
    // } catch (Exception e) {
    // 	log.info("upload image error: {}", e.getMessage());
    // 	return ApiResp.failure(msgService.getMessage(LANG_ZH, "admin.upload.avatar.error"));
    // }
    return ApiResp.success("");
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    this.aes128GCMCrypto = new AES128GCMCrypto(infraApplicationProperties.getDecryptSecretKey());
  }

}