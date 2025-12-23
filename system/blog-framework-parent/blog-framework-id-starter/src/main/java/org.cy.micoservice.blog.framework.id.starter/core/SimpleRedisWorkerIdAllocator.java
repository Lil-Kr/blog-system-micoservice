package org.cy.micoservice.blog.framework.id.starter.core;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;

/**
 * @Author: Lil-K
 * @Description: 基于Redis的最简workerId分配器，支持原子性循环重置：
 * - 使用Lua脚本执行INCR操作，并在溢出时将计数器重置为(base+1)，以便从下一个槽位继续分配。
 * - 无需注册机制，无需租约管理。
 */
public class SimpleRedisWorkerIdAllocator extends AbstractWorkerIdAllocator  {

  private final StringRedisTemplate redisTemplate;
  private final String counterKey;

  private final DefaultRedisScript<Long> script;

  public SimpleRedisWorkerIdAllocator(StringRedisTemplate redisTemplate, String namespace, int maxWorker) {
    super(namespace, maxWorker);
    Assert.notNull(redisTemplate, "redisTemplate must not be null");
    this.redisTemplate = redisTemplate;
    this.counterKey = "gn:id:worker:" + getNamespace() + ":counter";
    this.script = buildLuaScript();
  }

  /**
   * Allocate a workerId using INCR and modulo wrap-around.
   * When counter exceeds maxWorker, atomically reset to base+1.
   */
  public int allocateOrFail() {
    List<String> keys = Collections.singletonList(counterKey);
    Long res = redisTemplate.execute(script, keys, String.valueOf(maxWorker));
    if (res == null) {
      throw new IllegalStateException("Redis INCR script returned null for key: " + counterKey);
    }
    return res.intValue();
  }

  private DefaultRedisScript<Long> buildLuaScript() {
    String lua = "local key = KEYS[1]\n" + // 获取redis-key
      "local max = tonumber(ARGV[1])\n" + // 获取maxWokerId=65536
      "local c = tonumber(redis.call('INCR', key))\n" + // 执行redis incr命令
      "local base = (c - 1) % max\n" + // 拿到incr值之后，取模。将我们值控制在65536以内。
      "if c > max then\n" + // 当获取到的workerId超过了65536
      "  redis.call('SET', key, base + 1)\n" + // 重置workerId，从1重新开始
      "end\n" +
      "return base\n";
    DefaultRedisScript<Long> script = new DefaultRedisScript<>();
    script.setScriptText(lua);
    script.setResultType(Long.class);
    return script;
  }

}