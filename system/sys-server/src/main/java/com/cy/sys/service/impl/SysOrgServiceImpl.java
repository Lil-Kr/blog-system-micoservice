package com.cy.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cy.common.utils.apiUtil.ApiResp;
import com.cy.common.utils.dateUtil.DateUtil;
import com.cy.common.utils.keyUtil.IdWorker;
import com.cy.sys.common.holder.RequestHolder;
import com.cy.sys.dao.SysOrgMapper;
import com.cy.sys.pojo.dto.org.OrgLevelDto;
import com.cy.sys.pojo.entity.SysOrg;
import com.cy.sys.pojo.param.org.OrgDeleteParam;
import com.cy.sys.pojo.param.org.OrgGetChildrenParam;
import com.cy.sys.pojo.param.org.OrgListAllParam;
import com.cy.sys.pojo.param.org.OrgParam;
import com.cy.sys.service.ISysOrgService;
import com.cy.sys.util.org.OrgUtil;
import com.cy.sys.util.org.LevelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author Lil-Kr
 * @since 2020-11-25
 */
@Service
@Slf4j
public class SysOrgServiceImpl extends ServiceImpl<SysOrgMapper, SysOrg> implements ISysOrgService {

    @Resource
    private SysOrgMapper sysOrgMapper1;

    @Resource
    private SysTreeService sysTreeService1;

    /**
     * 添加组织信息
     * @param param
     * @return
     */
    @Override
    public ApiResp add(OrgParam param) throws Exception {
        /**
         * 检查组织名是否相同
         */
        if (checkOrgExist(param.getParentSurrogateId(),param.getName(),param.getSurrogateId())) {// 检查
            return ApiResp.failure("待添加的组织名不能重复");
        }

        /**计算层级**/
        String level = LevelUtil.calculateLevel(getLevel(param.getParentId()),param.getParentId());
        Long surrogateId = IdWorker.getsnowFlakeId(); // surrogateId
        String currentTime = DateUtil.getNowDateTime();// 当前时间
        SysOrg org = SysOrg.builder()
                .surrogateId(surrogateId)
                .number("ORG"+ surrogateId)
                .parentId(param.getParentSurrogateId())
                .seq(param.getSeq())
                .level(level)
                .name(param.getName())
                .remark(param.getRemark())
                .createTime(currentTime)
                .updateTime(currentTime)
                .operator(RequestHolder.getCurrentUser().getLoginAccount())
                .operateIp("127.0.0.1")
                .build();

        sysOrgMapper1.insert(org);
        return ApiResp.success("添加成功");
    }

    /**
     * 更新组织信息
     * @param param
     * @return
     */
    @Override
    public ApiResp edit(OrgParam param) throws Exception {
        if (checkOrgExist(param.getParentSurrogateId(),param.getName(),param.getSurrogateId())) {// 检查组织名是否重复
            return ApiResp.failure("待更新的组织名不能重复");
        }

        // 检查待更新的组织是否存在
        SysOrg before = sysOrgMapper1.selectById(param.getId());
        if (Objects.isNull(before)) {
            return ApiResp.failure("待更新的组织不存在");
        }

        // 更新当前组织
        SysOrg after = SysOrg.builder()
                .id(before.getId())
                .surrogateId(before.getSurrogateId())
                .name(param.getName())
                .parentId(param.getParentSurrogateId())// 上级组织id
                .seq(param.getSeq())
                .level(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()))
                .remark(param.getRemark())
                .updateTime(DateUtil.getNowDateTime())
                .operator(RequestHolder.getCurrentUser().getLoginAccount())
                .operateIp("127.0.0.1")
                .build();

        // 更新子组织信息
        this.updateWithChildOrg(before,after);
        return ApiResp.success("更新组织成功");
    }

    /**
     * 更新当前组织的子组织信息
     * @param before 旧组织
     * @param after 新组织
     */
    @Transactional
    public void updateWithChildOrg(SysOrg before, SysOrg after) {
        // 修改当前组织信息
        sysOrgMapper1.updateById(after);
        // 更新当前组织的子组织
        String newLevelPrefix = after.getLevel();// 0.1.3
        String oldLevelPrefix = before.getLevel();// 0.1
        if (!newLevelPrefix.equals(oldLevelPrefix)) {// 不一致需要做子组织的更新
            this.updateChildOrgTree(after);
        }
    }

    /**
     * 递归变更组织树层级, 并维护子组织的level
     */
    protected void updateChildOrgTree(SysOrg afterOrg) {
        List<SysOrg> orgList = sysOrgMapper1.selectChildOrgListByParentId(afterOrg.getSurrogateId());
        if (CollectionUtils.isEmpty(orgList)) {
            return;
        }

        orgList.forEach(org -> {
            org.setLevel(LevelUtil.calculateLevel(afterOrg.getLevel(),afterOrg.getId()));
            org.setUpdateTime(DateUtil.getNowDateTime());
            updateChildOrgTree(org);
        });
        // 操作db
        this.updateBatchById(orgList);
    }

    /**
     * 获取当前组织信息及子组织信息
     * @param dto
     * @return
     */
    @Override
    public ApiResp getChildrenOrgList(OrgGetChildrenParam dto) throws Exception {
        // 当前组织
        QueryWrapper<SysOrg> query1 = new QueryWrapper<>();
        query1.eq("surrogate_id",dto.getSurrogateId());
        SysOrg org = sysOrgMapper1.selectOne(query1);

        // 子组织
        QueryWrapper<SysOrg> query2 = new QueryWrapper<>();
        List<SysOrg> orgChildrenList = sysOrgMapper1.selectList(query2);
        orgChildrenList.add(org);

        // 排序
        Collections.sort(orgChildrenList, OrgUtil.orgByIdComparator);
        return ApiResp.success(orgChildrenList);
    }

    /**
     * 获取组织树信息
     * @return
     * @throws Exception
     */
    @Override
    public ApiResp orgTree() throws Exception {
        List<OrgLevelDto> dtoList = sysTreeService1.orgTree();
        return ApiResp.success(dtoList);
    }

    /**
     * 删除组织
     * @param param
     * @return
     */
    @Override
    public ApiResp delete(OrgDeleteParam param) throws Exception {
        QueryWrapper<SysOrg> query = new QueryWrapper<>();
        query.eq("surrogate_id", param.getSurrogateId());
        SysOrg org = sysOrgMapper1.selectOne(query);
        if (Objects.isNull(org)) {
            return ApiResp.failure("待删除的组织不存在");
        }

        // 检查要删除的组织下面是否还有子组织
        QueryWrapper<SysOrg> query1 = new QueryWrapper<>();
        query1.eq("parent_id", param.getSurrogateId());
        Integer count = sysOrgMapper1.selectCount(query1);
        if (count >= 1) {
            return ApiResp.failure("待删除的组织下存在子组织, 不能删除");
        }

        sysOrgMapper1.deleteById(org.getId());
        return ApiResp.success("删除组织成功");
    }

    /**
     * 查询所有组织信息
     * @return
     */
    @Override
    public ApiResp orgListAll(OrgListAllParam param) throws Exception {
        QueryWrapper<SysOrg> query1 = new QueryWrapper<>();
        if (Objects.nonNull(param.getNumber())) {
            query1.like("number",param.getNumber());
        }
        if (Objects.nonNull(param.getName())) {
            query1.like("name",param.getName());
        }
        query1.orderByAsc("surrogate_id");// 排序
        List<SysOrg> orgList = sysOrgMapper1.selectList(query1);
        Collections.sort(orgList, OrgUtil.orgComparator);
        return ApiResp.success(orgList);
    }

    /**
     * 分页查询组织信息
     * @param param
     * @return
     * @throws Exception
     */
    @Override
    public ApiResp orgListPage(OrgListAllParam param) throws Exception {
        Page<SysOrg> page = new Page<>(param.getCurrent(), param.getSize());
        QueryWrapper<SysOrg> queryPage = new QueryWrapper<>();
        if (Objects.nonNull(param.getNumber())) {
            queryPage.like("number",param.getNumber());
        }
        if (Objects.nonNull(param.getName())) {
            queryPage.like("name",param.getName());
        }
        IPage<SysOrg> iPage = sysOrgMapper1.selectPage(page, queryPage);
        return ApiResp.success(iPage);
    }

    /**
     * 校验同一级组织下不能有名称重复的组织
     * @param parentId
     * @param orgName
     * @param SurrogateId
     * @return true/false
     */
    private boolean checkOrgExist(Long parentId, String orgName, Long SurrogateId) {
        QueryWrapper<SysOrg> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id",parentId);
        if (Objects.nonNull(orgName)) {
            queryWrapper.eq("name",orgName);
        }
        if (Objects.nonNull(SurrogateId)) {
            queryWrapper.eq("surrogate_id",SurrogateId);
        }
        Integer count = sysOrgMapper1.selectCount(queryWrapper);
        if (count >= 1) {
            return true;
        }else {
            return false;
        }
    }

    /**
     * 获取当前组织所在层级的level
     * @param orgId
     * @return
     */
    private String getLevel(Long orgId) {
        SysOrg org = sysOrgMapper1.selectById(orgId);
        if (Objects.isNull(org)) {
            return null;
        }else {
            return org.getLevel();
        }
    }

}
