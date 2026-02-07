package org.cy.micoservice.blog.test.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cy.micoservice.blog.test.dao.TestUserMapper;
import org.cy.micoservice.blog.test.pojo.TestUser;
import org.cy.micoservice.blog.test.service.TestUserService;
import org.springframework.stereotype.Service;

/**
 * @Author: Lil-K
 * @Date: 2026/1/25
 * @Description: TestUser Service Impl
 */
@Service
public class TestUserServiceImpl extends ServiceImpl<TestUserMapper, TestUser> implements TestUserService {

  @Override
  public TestUser getUserById(Long id) {
    return super.getById(id);
  }

  @Override
  public boolean addUser(TestUser user) {
    return super.save(user);
  }

  @Override
  public boolean updateUser(TestUser user) {
    return super.updateById(user);
  }

  @Override
  public boolean deleteUserById(Long id) {
    return super.removeById(id);
  }
}
