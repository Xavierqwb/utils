package com.xavier.utils;

import com.xavier.exception.JsonParseException;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.PropertyNamingStrategy;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * json编解码工具类
 *
 * @author xavier
 */
@SuppressWarnings("deprecation")
public class JsonUtils {

	private static final ObjectMapper MAPPER = new ObjectMapper();

	private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtils.class);

	static {
		// 为保持对象版本兼容性,忽略未知的属性
		MAPPER.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		// 序列化的时候，跳过null值
		MAPPER.getSerializationConfig().setSerializationInclusion(Inclusion.NON_NULL);
		// date类型转化
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		MAPPER.setDateFormat(fmt);
		MAPPER.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
	}

	/**
	 * 将一个对象编码为json字符串
	 *
	 * @param obj ,if null return "null" 要编码的字符串
	 * @return json字符串
	 * @throws JsonParseException 若对象不能被编码为json串
	 */
	public static String toJson(Object obj) {
		if (obj == null) {
			return null;
		}
		try {
			return MAPPER.writeValueAsString(obj);
		} catch (Exception e) {
            LOGGER.error("[JsonUtils# toJson] error obj={}", obj, e);
			throw new JsonParseException("error encode json for " + obj, e);
		}
	}

	/**
	 * 将一个对象编码成字节
	 */
	public static byte[] toBytes(Object obj) {

		try {
			return MAPPER.writeValueAsBytes(obj);
		} catch (Exception e) {
            LOGGER.error("[JsonUtils# toBytes] error obj={}", obj, e);
			throw new JsonParseException("error encode json for " + obj, e);
		}
	}

	/**
	 * 将一个json字符串解码为java对象
	 * <p>
	 * 注意：如果传入的字符串为null，那么返回的对象也为null
	 *
	 * @param json json字符串
	 * @param cls  对象类型
	 * @return 解析后的java对象
	 * @throws JsonParseException 若解析json过程中发生了异常
	 */
	public static <T> T toObject(String json, Class<T> cls) {
		if (json == null) {
			return null;
		}
		try {
			return MAPPER.readValue(json, cls);
		} catch (Exception e) {
            LOGGER.error("[JsonUtils# toBytes] error obj={}", e);
			throw new JsonParseException("error decode json to " + cls, e);
		}
	}

	/**
	 * 将json字节解码为java对象
	 *
	 * @param jsonBytes json字节
	 * @param cls       对象类型
	 * @return 解码后的对象
	 */
	public static <T> T toObject(byte[] jsonBytes, Class<T> cls) {
		if (jsonBytes == null) {
			return null;
		}
		try {
			return MAPPER.readValue(jsonBytes, cls);
		} catch (Exception e) {
            LOGGER.error("[JsonUtils# toBytes] error obj={}", e);
			throw new JsonParseException("error decode json to " + cls);
		}
	}

	/**
	 * 将json字节解码为java对象
	 */
	@SuppressWarnings("rawtypes")
	public static <T> T toObject(String json, TypeReference typeReference) {
		try {
			return MAPPER.readValue(json, typeReference);
		} catch (Exception e) {
            LOGGER.error("[JsonUtils# toBytes] error obj={}", e);
			throw new JsonParseException("error decode json", e);
		}
	}

	/**
	 * 读取JSON字符串为MAP
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> readMap(String json) {
		try {
			return MAPPER.readValue(json, HashMap.class);
		} catch (Exception e) {
            LOGGER.error("[JsonUtils# readMap] error json={}", json, e);
			throw new JsonParseException("error readMap from json: " + json);
		}
	}

	/**
	 * 获取泛型的Collection Type
	 *
	 * @param collectionClass 泛型的Collection
	 * @param elementClasses  元素类
	 * @return JavaType Java类型
	 * @since 1.0
	 */
	public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
		return MAPPER.getTypeFactory().constructParametricType(collectionClass, elementClasses);
	}

	public static <T> List<T> readList(String json, Class<T> clazz) {
		try {
			if (json == null || "".equals(json.trim())) {
				return null;
			}
			JavaType javaType = getCollectionType(ArrayList.class, clazz);
			return MAPPER.readValue(json, javaType);
		} catch (Exception e) {
            LOGGER.error("[JsonUtils# readList] error json=" + json, e);
			throw new JsonParseException("error readList from json: " + json);
		}
	}

}

