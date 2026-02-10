package org.cy.micoservice.blog.common.utils;

import java.util.Random;

/**
 * @Author: Lil-K
 * @Date: 2025/12/26
 * @Description: 用户id生成工具
 */
public class IdGenerateUtil {

  // 生成随机用户ID
  public static Long generateUserId() {
    Random random = new Random();
    return random.nextLong(1000 * 10000, 100 * 10000 * 10000);
  }

  // 生成随机笔记ID
  public static Long generateNoteId() {
    Random random = new Random();
    return random.nextLong(1000 * 10000, 100 * 10000 * 10000);
  }


  // 生成对话关系ID
  public static Long generateRelationId() {
    Random random = new Random();
    return random.nextLong(1000 * 10000, 100 * 10000 * 10000);
  }

  // 生成对话记录ID
  public static Long generateChatRecordId() {
    Random random = new Random();
    return random.nextLong(1000 * 10000, 100 * 10000 * 10000);
  }

  //生成评论id
  public static Long generateCommentId() {
    Random random = new Random();
    return random.nextLong(1000 * 10000, 100 * 10000 * 10000);
  }
}