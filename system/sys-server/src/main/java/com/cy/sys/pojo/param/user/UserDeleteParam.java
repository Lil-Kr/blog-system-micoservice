package com.cy.sys.pojo.param.user;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@ToString
public class UserDeleteParam {

    @NotNull(message = "id不能为空")
    private Long id;

    @NotNull(message = "surrogateId不能为空")
    private Long surrogateId;
}
