package org.cy.micoservice.blog.admin.dao;

import org.cy.micoservice.blog.admin.pojo.entity.blog.BlogContentMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author: Lil-K
 * @Date: 2024/5/26
 * @Description:
 */
@Repository
public interface BlogContentMongoMapper extends MongoRepository<BlogContentMongo, String> {
}
