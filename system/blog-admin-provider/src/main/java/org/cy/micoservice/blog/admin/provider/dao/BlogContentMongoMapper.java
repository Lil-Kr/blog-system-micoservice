package org.cy.micoservice.blog.admin.provider.dao;

import org.cy.micoservice.blog.entity.admin.model.entity.blog.BlogContentMongo;
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
