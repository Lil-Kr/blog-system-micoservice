package org.cy.micoservice.blog.entity.base.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: 2024/4/4
 * @Description: BasePageReq
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Data
public class BasePageReq extends BaseReq implements Serializable {

  @Serial
  private static final long serialVersionUID = -5423164247137597240L;

  public interface GroupPageQuery{}

  /**
   */
  @NotNull(groups = {GroupPageQuery.class}, message = "current page number cant not be null")
  @Min(groups = {GroupPageQuery.class}, value = 1, message ="page number cant not less than 1")
  @Max(groups = {GroupPageQuery.class}, value = 10, message ="page number cant not greater than 10")
  private Integer currentPageNum;

  @NotNull(groups = {GroupPageQuery.class}, message = "page size cant not be null")
  @Max(groups = {GroupPageQuery.class}, value = 100, message = "page size cant not greater than 100")
  private Integer pageSize;
}