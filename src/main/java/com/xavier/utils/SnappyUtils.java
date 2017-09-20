package com.xavier.utils;

import com.xavier.exception.CompressException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xerial.snappy.Snappy;

import java.io.IOException;
import java.util.Base64;

/**
 * Created by qiuwenbin on 2017/9/20.
 */
public class SnappyUtils {

	private static final Logger LOG = LoggerFactory.getLogger("SnappyUtils");

	private static final String CHARSET_NAME = "UTF-8";

	public static String compressString(String inputString) {
		try {
			if (StringUtils.isEmpty(inputString)) {
				return StringUtils.EMPTY;
			}
			byte[] compressBytes = Snappy.compress(inputString.getBytes(CHARSET_NAME));
			return Base64.getEncoder().encodeToString(compressBytes);
		} catch (IOException e) {
			LOG.warn("SnppyUtil compressString error =({}),parameters = ({})", e.getCause(), inputString);
			throw new CompressException("压缩字符串出错");
		}

	}


	public static String decompressString(String inputString) {
		try {
			if (StringUtils.isEmpty(inputString)) {
				return StringUtils.EMPTY;
			}
			return new String(Snappy.uncompress(Base64.getDecoder().decode(inputString)), CHARSET_NAME);
		} catch (IOException e) {
			LOG.warn("SnppyUtil decompressHtml error =({}),parameters = ({})", e.getCause(), inputString);
			throw new CompressException("解压字符串出错");
		}
	}
}
