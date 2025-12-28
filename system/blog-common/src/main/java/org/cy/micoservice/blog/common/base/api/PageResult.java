package org.cy.micoservice.blog.common.base.api;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/3/30
 * @Description: 分页结果组件
 */
public class PageResult<T> implements Serializable {

  @Serial
  private static final long serialVersionUID = -8163365823187941281L;

  private List<T> list;

  /**
   * 总记录数
   */
  private Integer total;

  public PageResult() {
  }

  public PageResult(List<T> list, Integer total) {
    this.list = list;
    this.total = total;
  }

  public List<T> getList() {
    return list;
  }

  public void setList(List<T> list) {
    this.list = list;
  }

  public Integer getTotal() {
    return total;
  }

  public void setTotal(Integer total) {
    this.total = total;
  }

  public static <T> PageResult<T> emptyPage() {
    return new PageResult<>(null, 0);
  }
}
