package org.cy.micoservice.blog.common.base.provider;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/12/26
 * @Description: 分页响应体, provider层使用
 */
@Data
public class PageResponseDTO<T> implements Serializable {

  @Serial
  private static final long serialVersionUID = 2611737943133650738L;

  private int page;
  private int size;
  private List<T> dataList;
  private boolean hasNext;
  private Long searchOffset;

  public static <T> PageResponseDTO<T> emptyPage() {
    PageResponseDTO<T> PageResponseDTO = new PageResponseDTO<>();
    PageResponseDTO.setHasNext(false);
    PageResponseDTO.setDataList(Collections.emptyList());
    return PageResponseDTO;
  }
}
