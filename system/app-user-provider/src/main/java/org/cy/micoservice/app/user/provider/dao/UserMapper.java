package org.cy.micoservice.app.user.provider.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.cy.micoservice.app.entity.user.model.provider.pojo.User;
import org.cy.micoservice.app.entity.user.model.provider.resp.UserResp;
import org.springframework.stereotype.Repository;

/**
 * @Author: Lil-K
 * @Date: 2025/3/7
 * @Description:
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

  User getUserById(Long userId);

  UserResp getUserBySurrogateId(Long surrogateId);
}
