package org.cy.micoservice.blog.common.base;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/12/26
 * @Description:
 */
@Data
public class PageResponse<T> implements Serializable {

  @Serial
  private static final long serialVersionUID = 2611737943133650738L;

  private int page;
  private int size;
  private List<T> dataList;
  private boolean hasNext;

  public static <T> PageResponse<T> emptyPage() {
    PageResponse<T> PageResponse = new PageResponse<>();
    PageResponse.setHasNext(false);
    PageResponse.setDataList(Collections.emptyList());
    return PageResponse;
  }
}
