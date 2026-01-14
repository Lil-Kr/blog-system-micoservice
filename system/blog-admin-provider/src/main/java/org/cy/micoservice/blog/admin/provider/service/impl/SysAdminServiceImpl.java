package org.cy.micoservice.blog.admin.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luciad.imageio.webp.WebPWriteParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.cy.micoservice.blog.admin.facade.dto.sys.admin.AdminLoginAdminReqDTO;
import org.cy.micoservice.blog.admin.provider.dao.SysAdminMapper;
import org.cy.micoservice.blog.admin.provider.service.RbacCacheService;
import org.cy.micoservice.blog.admin.provider.service.MessageLangService;
import org.cy.micoservice.blog.admin.provider.service.SysAdminService;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.common.base.api.PageResult;
import org.cy.micoservice.blog.common.enums.response.ApiReturnCodeEnum;
import org.cy.micoservice.blog.common.utils.DateUtil;
import org.cy.micoservice.blog.common.utils.IdWorker;
import org.cy.micoservice.blog.entity.admin.model.dto.admin.AdminDTO;
import org.cy.micoservice.blog.entity.admin.model.entity.sys.SysAdmin;
import org.cy.micoservice.blog.entity.admin.model.req.sys.admin.AdminListPageReq;
import org.cy.micoservice.blog.entity.admin.model.req.sys.admin.AdminRegisterReq;
import org.cy.micoservice.blog.entity.admin.model.req.sys.admin.AdminSaveReq;
import org.cy.micoservice.blog.entity.admin.model.req.sys.admin.AvatarUploadReq;
import org.cy.micoservice.blog.entity.admin.model.resp.sys.admin.SysAdminResp;
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
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.cy.micoservice.blog.admin.provider.common.constants.CommonConstants.LANG_ZH;
import static org.cy.micoservice.blog.common.enums.response.ApiReturnCodeEnum.*;

/**
 * @Author: Lil-K
 * @Date: 2025/3/6
 * @Description:
 */
@Service
@Slf4j
public class SysAdminServiceImpl extends ServiceImpl<SysAdminMapper, SysAdmin> implements SysAdminService {

	@Value("${upload.rootDir}")
	private String rootDir;

	@Value("${upload.uploadDir}")
	private String uploadDir;

	@Value("${upload.adminUserPath}")
	private String adminUserPath;

	@Autowired
	private MessageLangService msgService;

	@Autowired
	private SysAdminMapper adminMapper;

	@Autowired
	private RbacCacheService rbacCacheService;

	/**
	 * 后台管理员登录
	 * @param req
	 * @return
	 */
	@Override
	public ApiResp<SysAdmin> adminLogin(AdminLoginAdminReqDTO req) {
		SysAdmin admin = adminMapper.loginAdmin(req);
		if (Objects.isNull(admin)) {
			return ApiResp.failure(USER_INFO_ERROR);
		}

		admin.setUpdateTime(DateUtil.dateTimeNow());
		Integer update = adminMapper.updateAdminById(admin);
		if (update < 1) {
			return ApiResp.warning(USER_INFO_ERROR);
		}

		// update cache
		rbacCacheService.setAdminTokenCache(admin.getToken(), admin);
		rbacCacheService.setAdminIdCache(admin.getId(), admin);
		return ApiResp.success(msgService.getMessage(LANG_ZH, "admin.login.success"), SysAdmin.builder().token(admin.getToken()).build());
	}

	/**
	 * register admin
	 * @param req
	 * @return
	 */
	@Override
	public ApiResp<Integer> registerAdmin(AdminRegisterReq req) {
		SysAdmin admin = adminMapper.getAdminByAccount(req.getAccount());
		if (Objects.nonNull(admin)) {
			return ApiResp.failure(ApiReturnCodeEnum.INFO_NOT_EXIST);
		}

		SysAdmin user = AdminDTO.convertSaveAdminReq(req);
		int count = adminMapper.insert(user);
		if (count < 1) {
			return ApiResp.failure(ADD_ERROR);
		}

    // update cache
    rbacCacheService.setAdminTokenCache(user.getToken(), user);
    rbacCacheService.setAdminIdCache(user.getId(), user);
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

		SysAdmin user = AdminDTO.convertAddUserReq(req);
		int insert = adminMapper.insert(user);
		if (insert < 1) {
			return ApiResp.failure(ADD_ERROR);
		}

		// update cache
		rbacCacheService.setAdminTokenCache(user.getToken(), user);
    rbacCacheService.setAdminIdCache(user.getId(), user);
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
    wrapper.eq("surrogate_id", req.getId());
    SysAdmin before = adminMapper.selectOne(wrapper);
    if (Objects.isNull(before)) {
      return ApiResp.failure(UPDATE_ERROR);
    }

    SysAdmin user = AdminDTO.convertEditUserReq(before, req);
		int update = adminMapper.updateAdminById(user);
		if (update < 1) {
      return ApiResp.failure(UPDATE_ERROR);
		}

    // update cache
    rbacCacheService.setAdminTokenCache(user.getToken(), user);
    rbacCacheService.setAdminIdCache(user.getId(), user);
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
		SysAdmin user = adminMapper.selectOne(wrapper);
		if (Objects.isNull(user)) {
			return ApiResp.failure(INFO_NOT_EXIST);
		}
		int delete = adminMapper.delete(wrapper);
		if (delete < 1) {
      return ApiResp.failure(DEL_ERROR);
		}

    // remove catch
    rbacCacheService.removeAdminTokenCache(user.getToken());
    rbacCacheService.removeAdminIdCache(user.getId());
    return ApiResp.success();
	}

  @Override
  public SysAdmin getAdminById(Long id) {
	  return adminMapper.getAdminById(id);
  }

	@Override
	public SysAdminResp getAdminBySurrogateId(Long surrogateId) {
		return adminMapper.getAdminBySurrogateId(surrogateId);
	}

	@Override
	public PageResult<SysAdminResp> pageList(AdminListPageReq req) {
		List<SysAdminResp> list =  adminMapper.pageAdminList(req);
		Integer count = adminMapper.countAdminList(req);
		if (CollectionUtils.isNotEmpty(list)) {
			return new PageResult<>(list, count);
		} else {
			return PageResult.emptyPage();
		}
	}

	/**
	 * upload avatar
	 * @param req
	 * @return
	 */
	@Override
	public ApiResp<String> uploadAvatar(AvatarUploadReq req) throws Exception {
		MultipartFile avatar = req.getAvatarFile();
		long maxSizeInBytes = 1 * 1024 * 1024; // 1MB
		if (avatar == null || avatar.getSize() > maxSizeInBytes) {
			return ApiResp.failure(msgService.getMessage(LANG_ZH, "admin.upload.avatar.size.error1"));
		}
		String imageOriginalFullName = Optional.ofNullable(avatar.getOriginalFilename()).orElse("");
		String[] imageFileNames = imageOriginalFullName.split("\\.");
		if (imageFileNames.length > 2) {
			return ApiResp.failure(msgService.getMessage(LANG_ZH, "admin.upload.avatar.size.error2"));
		}

		String imageName = imageFileNames[0];
		String imageTypeSuffix = "webp";

		StringBuffer resourcePath = new StringBuffer(rootDir);
		resourcePath.append(uploadDir);

		/**
		 * create Path object
		 */
		Path rootPath = Paths.get(resourcePath.toString());
		if (!Files.exists(rootPath)) {
			Files.createDirectories(rootPath);
		}

		String imageReName = imageName + "_" + IdWorker.getSnowFlakeId() + "." + imageTypeSuffix;
		SysAdmin currentAdmin = rbacCacheService.getAdminIdCache(req.getAdminId());
		resourcePath.append(adminUserPath).append("/")
			.append(currentAdmin.getAccount()).append("/")
			.append(imageReName);

		try(InputStream inputStream = avatar.getInputStream()) {
			/**
			 * write image to disk
			 */
			BufferedImage originalImage = ImageIO.read(inputStream);
			Iterator<ImageWriter> writers = ImageIO.getImageWritersByMIMEType("image/webp");
			if (!writers.hasNext()) {
				return ApiResp.failure("No writers found for format: webp");
			}

			ImageWriter writer = writers.next();
			try (ImageOutputStream ios = ImageIO.createImageOutputStream(Files.newOutputStream(Paths.get(resourcePath.toString())))) {
				WebPWriteParam writeParam = new WebPWriteParam(writer.getLocale());
				writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
				writeParam.setCompressionType(writeParam.getCompressionTypes()[WebPWriteParam.LOSSY_COMPRESSION]); // lossy compression
				writeParam.setCompressionQuality(0.75f);
				writer.setOutput(ios);
				writer.write(null, new IIOImage(originalImage, null, null), writeParam);
			} catch (Exception e) {
				log.info("avatar format webp error: {}", e.getMessage());
				return ApiResp.failure();
			} finally {
				writer.dispose();
			}

			/**
			 * update from DB
			 */
			StringBuffer imageUrl = new StringBuffer(uploadDir)
				.append(adminUserPath)
				.append("/").append(currentAdmin.getAccount())
				.append("/").append(imageReName);
			req.setAdminId(currentAdmin.getId());
			req.setAvatar(imageUrl.toString());
			// update from DB
			int update = adminMapper.updateAvatar(req);
			if (update < 1) {
				return ApiResp.failure(msgService.getMessage(LANG_ZH, "admin.upload.avatar.error"));
			}
			return ApiResp.success(msgService.getMessage(LANG_ZH, "admin.upload.avatar.success"));
		} catch (Exception e) {
			log.info("upload image error: {}", e.getMessage());
			return ApiResp.failure(msgService.getMessage(LANG_ZH, "admin.upload.avatar.error"));
		}
	}

}