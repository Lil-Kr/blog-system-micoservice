package org.cy.micoservice.app.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: Lil-K
 * @Date: 2025/11/29
 * @Description:
 */
@Slf4j
public class JsonUtil {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  static {
    // 设置日期格式
    OBJECT_MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    // 注册 JavaTimeModule 支持 Java 8 时间类型
    OBJECT_MAPPER.registerModule(new JavaTimeModule());

    // 序列化配置
    // 禁用将日期序列化为时间戳
    OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    // 禁用空对象转换失败
    OBJECT_MAPPER.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    // 美化输出(可选, 生产环境建议关闭)
    // OBJECT_MAPPER.enable(SerializationFeature.INDENT_OUTPUT);

    // 反序列化配置
    // 忽略未知属性
    OBJECT_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    // 允许空字符串转换为 null
    OBJECT_MAPPER.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

    // 序列化时忽略 null 值(可选)
    OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
  }

  /**
   * 获取 ObjectMapper 实例
   * @return ObjectMapper
   */
  public static ObjectMapper getObjectMapper() {
    return OBJECT_MAPPER;
  }

  /**
   * 对象序列化为 JSON 字符串
   * @param obj 对象
   * @return JSON 字符串
   */
  public static String toJsonString(Object obj) {
    if (Objects.isNull(obj)) {
      return null;
    }
    try {
      return OBJECT_MAPPER.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      log.error("Failed to serialize object to JSON string, obj: {}", obj, e);
      throw new RuntimeException("Object serialization failed", e);
    }
  }

  /**
   * 对象序列化为 JSON 字节数组
   * @param obj 对象
   * @return JSON 字节数组
   */
  public static byte[] toJsonBytes(Object obj) {
    if (Objects.isNull(obj)) {
      return null;
    }
    try {
      return OBJECT_MAPPER.writeValueAsBytes(obj);
    } catch (JsonProcessingException e) {
      log.error("Failed to serialize object to JSON byte array, obj: {}", obj, e);
      throw new RuntimeException("Object serialization failed", e);
    }
  }

  /**
   * 对象序列化为格式化的 JSON 字符串
   * @param obj 对象
   * @return 格式化的 JSON 字符串
   */
  public static String toJsonPrettyString(Object obj) {
    if (Objects.isNull(obj)) {
      return null;
    }
    try {
      return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      log.error("Failed to serialize object to pretty JSON string, obj: {}", obj, e);
      throw new RuntimeException("Object serialization failed", e);
    }
  }

  /**
   * JSON 字符串反序列化为对象
   * @param json JSON 字符串
   * @param clazz 目标类型
   * @param <T> 泛型
   * @return 对象
   */
  public static <T> T parseObject(String json, Class<T> clazz) {
    if (Objects.isNull(json) || json.trim().isEmpty()) {
      return null;
    }
    try {
      return OBJECT_MAPPER.readValue(json, clazz);
    } catch (JsonProcessingException e) {
      log.error("Failed to deserialize JSON string to object, json: {}, clazz: {}", json, clazz.getName(), e);
      throw new RuntimeException("JSON deserialization failed", e);
    }
  }

  /**
   * JSON 字节数组反序列化为对象
   * @param jsonBytes JSON 字节数组
   * @param clazz 目标类型
   * @param <T> 泛型
   * @return 对象
   */
  public static <T> T parseObject(byte[] jsonBytes, Class<T> clazz) {
    if (Objects.isNull(jsonBytes) || jsonBytes.length == 0) {
      return null;
    }
    try {
      return OBJECT_MAPPER.readValue(jsonBytes, clazz);
    } catch (Exception e) {
      log.error("Failed to deserialize JSON byte array to object, clazz: {}", clazz.getName(), e);
      throw new RuntimeException("JSON deserialization failed", e);
    }
  }

  /**
   * JSON 字符串反序列化为对象(支持复杂泛型)
   * @param json JSON 字符串
   * @param typeReference 类型引用
   * @param <T> 泛型
   * @return 对象
   */
  public static <T> T parseObject(String json, TypeReference<T> typeReference) {
    if (Objects.isNull(json) || json.trim().isEmpty()) {
      return null;
    }
    try {
      return OBJECT_MAPPER.readValue(json, typeReference);
    } catch (JsonProcessingException e) {
      log.error("Failed to deserialize JSON string to object with TypeReference, json: {}, typeReference: {}", json, typeReference.getType(), e);
      throw new RuntimeException("JSON deserialization failed", e);
    }
  }

  /**
   * JSON 字符串反序列化为 List
   * @param json JSON 字符串
   * @param clazz 列表元素类型
   * @param <T> 泛型
   * @return List 对象
   */
  public static <T> List<T> parseList(String json, Class<T> clazz) {
    if (Objects.isNull(json) || json.trim().isEmpty()) {
      return null;
    }
    try {
      JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructParametricType(List.class, clazz);
      return OBJECT_MAPPER.readValue(json, javaType);
    } catch (JsonProcessingException e) {
      log.error("Failed to deserialize JSON string to List, json: {}, clazz: {}", json, clazz.getName(), e);
      throw new RuntimeException("JSON deserialization failed", e);
    }
  }

  /**
   * JSON 字符串反序列化为 Map
   * @param json JSON 字符串
   * @param keyClass Map 的 key 类型
   * @param valueClass Map 的 value 类型
   * @param <K> Key 泛型
   * @param <V> Value 泛型
   * @return Map 对象
   */
  public static <K, V> Map<K, V> parseMap(String json, Class<K> keyClass, Class<V> valueClass) {
    if (Objects.isNull(json) || json.trim().isEmpty()) {
      return null;
    }
    try {
      JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructMapType(Map.class, keyClass, valueClass);
      return OBJECT_MAPPER.readValue(json, javaType);
    } catch (JsonProcessingException e) {
      log.error("Failed to deserialize JSON string to Map, json: {}, keyClass: {}, valueClass: {}",
        json, keyClass.getName(), valueClass.getName(), e);
      throw new RuntimeException("JSON deserialization failed", e);
    }
  }

  /**
   * JSON 字符串反序列化为 Map<String, Object>
   * @param json JSON 字符串
   * @return Map 对象
   */
  public static Map<String, Object> parseMap(String json) {
    return parseMap(json, String.class, Object.class);
  }

  /**
   * 对象转换(先序列化再反序列化)
   * @param obj 源对象
   * @param clazz 目标类型
   * @param <T> 泛型
   * @return 目标对象
   */
  public static <T> T convertObject(Object obj, Class<T> clazz) {
    if (Objects.isNull(obj)) {
      return null;
    }
    try {
      if (obj instanceof String) {
        String str = (String) obj;
        // 如果目标类型是 JsonNode/ObjectNode → 使用 readTree
        if (JsonNode.class.isAssignableFrom(clazz)) {
          return OBJECT_MAPPER.readValue(str, clazz);
        }
        // 普通 bean → readValue
        return OBJECT_MAPPER.readValue(str, clazz);
      }
      // 非字符串：对象转换
      return OBJECT_MAPPER.convertValue(obj, clazz);
    } catch (Exception e) {
      log.error("Failed to convert object, obj: {}, clazz: {}", obj, clazz.getName(), e);
      throw new RuntimeException("Object conversion failed", e);
    }
  }

  /**
   * 对象转换为 Map
   * @param obj 对象
   * @return Map 对象
   */
  @SuppressWarnings("unchecked")
  public static Map<String, Object> objectToMap(Object obj) {
    if (Objects.isNull(obj)) {
      return null;
    }
    try {
      if (obj instanceof String) {
        String json = (String) obj;
        Map<String, Object> param = OBJECT_MAPPER.readValue(json, Map.class);
        return param;
      }
      return OBJECT_MAPPER.convertValue(obj, Map.class);
    }catch (Exception e) {
      log.error("Failed to convert object to Map, obj: {}, clazz: {}", obj, obj.getClass().getName(), e);
      throw new RuntimeException("Object conversion failed", e);
    }
  }

  /**
   * Map 转换为对象
   * @param map Map 对象
   * @param clazz 目标类型
   * @param <T> 泛型
   * @return 对象
   */
  public static <T> T mapToObject(Map<String, Object> map, Class<T> clazz) {
    if (Objects.isNull(map) || map.isEmpty()) {
      return null;
    }
    return OBJECT_MAPPER.convertValue(map, clazz);
  }

  /**
   * 深度拷贝对象
   * @param obj 源对象
   * @param clazz 目标类型
   * @param <T> 泛型
   * @return 拷贝后的对象
   */
  public static <T> T deepCopy(T obj, Class<T> clazz) {
    if (Objects.isNull(obj)) {
      return null;
    }
    try {
      String json = OBJECT_MAPPER.writeValueAsString(obj);
      return OBJECT_MAPPER.readValue(json, clazz);
    } catch (JsonProcessingException e) {
      log.error("Failed to deep copy object, obj: {}, clazz: {}", obj, clazz.getName(), e);
      throw new RuntimeException("Object deep copy failed", e);
    }
  }

  /**
   * 判断字符串是否为合法的 JSON 格式
   * @param json JSON 字符串
   * @return true 合法, false 不合法
   */
  public static boolean isValidJson(String json) {
    if (Objects.isNull(json) || json.trim().isEmpty()) {
      return false;
    }
    try {
      OBJECT_MAPPER.readTree(json);
      return true;
    } catch (JsonProcessingException e) {
      return false;
    }
  }
}