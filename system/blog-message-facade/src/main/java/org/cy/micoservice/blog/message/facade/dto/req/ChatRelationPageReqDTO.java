package org.cy.micoservice.blog.message.facade.dto.req;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.cy.micoservice.blog.entity.base.model.provider.BasePageReqDTO;

import java.io.Serial;

/**
 * @Author: Lil-K
 * @Date: 2025/12/26
 * @Description: 对话关系分页查询
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ChatRelationPageReqDTO extends BasePageReqDTO {

    @Serial
    private static final long serialVersionUID = 8789599949053745097L;

    private Long userId;

    private String relationId;

    private String searchAfter;
}
