package org.cy.micoservice.blog.message.facade.dto.req;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.cy.micoservice.blog.entity.base.model.BasePageReq;

import java.io.Serial;

/**
 * @Author idea
 * @Date Created at 2025/4/19
 * @Description 对话关系分页查询VO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ChatRelationPageReqDTO extends BasePageReq {

    @Serial
    private static final long serialVersionUID = 8789599949053745097L;

    private Long userId;

    private String relationId;

    private String searchAfter;
}
