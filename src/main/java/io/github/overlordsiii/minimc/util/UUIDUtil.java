package io.github.overlordsiii.minimc.util;

import java.math.BigInteger;
import java.util.UUID;

public class UUIDUtil {
	public static UUID stringToUuid(String uuid) {
		String s2 = uuid.replace("-", "");

		return new UUID(
			new BigInteger(s2.substring(0, 16), 16).longValue(),
			new BigInteger(s2.substring(16), 16).longValue());
	}
}
