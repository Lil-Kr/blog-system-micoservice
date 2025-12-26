package org.cy.micoservice.blog.message.facade.dto.req;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.cy.micoservice.blog.entity.base.model.BasePageReq;

import java.io.Serial;

/**
 * @Author idea
 * @Date Created at 2025/4/19
 * @Description 聊天记录分页查询
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ChatRecordPageReqDTO extends BasePageReq {

    @Serial
    private static final long serialVersionUID = 159229236141000627L;

    private Long relationId;
}
