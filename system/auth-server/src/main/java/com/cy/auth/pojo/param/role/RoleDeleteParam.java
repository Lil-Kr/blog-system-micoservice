package com.cy.auth.pojo.param.role;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@ToString
public class RoleDeleteParam {

    @NotNull(message = "surrogateId不能为空")
    private Long surrogateId;
}
