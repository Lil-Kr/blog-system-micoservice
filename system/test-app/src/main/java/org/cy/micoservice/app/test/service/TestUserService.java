package org.cy.micoservice.app.test.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.cy.micoservice.app.test.pojo.TestUser;

/**
 * @Author: Lil-K
 * @Date: 2026/1/25
 * @Description: TestUser Service
 */
public interface TestUserService extends IService<TestUser> {

    /**
     * 根据 ID 查询用户
     * @param id 用户ID
     * @return 用户信息
     */
    TestUser getUserById(Long id);

    /**
     * 新增用户
     * @param user 用户信息
     * @return 是否成功
     */
    boolean addUser(TestUser user);

    /**
     * 更新用户
     * @param user 用户信息
     * @return 是否成功
     */
    boolean updateUser(TestUser user);

    /**
     * 根据 ID 删除用户
     * @param id 用户ID
     * @return 是否成功
     */
    boolean deleteUserById(Long id);
}
