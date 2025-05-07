package com.barlow.app.batch.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class HashUtil {

	private static final String SHA_256 = "SHA-256";

	private HashUtil() {
	}

	public static <T> String generate(T data) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance(SHA_256);
		} catch (NoSuchAlgorithmException e) {
			throw new  IllegalArgumentException("지원하지 않는 digest algorithm 입니다", e);
		}
		byte[] hash = digest.digest(data.toString().getBytes(StandardCharsets.UTF_8));
		return Base64.getEncoder().encodeToString(hash);
	}
}
