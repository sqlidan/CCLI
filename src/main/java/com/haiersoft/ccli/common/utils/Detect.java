package com.haiersoft.ccli.common.utils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class Detect {

	public static boolean notNull(Object object){
		return object != null;
	}

	public static boolean positiveAndEquals(long x, long y) {
		return isPositive(x) && Detect.isPositive(y) && x == y;
	}

	public static boolean notEmpty(String string) {
		return StringUtils.isNotEmpty(string);
	}

	public static boolean notEmpty(byte[] bytes) {
		return ArrayUtils.isNotEmpty(bytes);
	}

	public static boolean notEmpty(List<?> list) {
		return CollectionUtils.isNotEmpty(list);
	}

	public static boolean notEmpty(Map<?, ?> map) {
		return MapUtils.isNotEmpty(map);
	}

	public static boolean notEmpty(Collection<?> collection) {
		return CollectionUtils.isNotEmpty(collection);
	}

	public static boolean notEmpty(short[] array) {
		return ArrayUtils.isNotEmpty(array);
	}

	public static boolean notEmpty(int[] array) {
		return ArrayUtils.isNotEmpty(array);
	}

	public static boolean notEmpty(long[] array) {
		return ArrayUtils.isNotEmpty(array);
	}

	public static boolean notEmpty(String[] array) {
		return ArrayUtils.isNotEmpty(array);
	}

	public static <T extends Object> boolean notEmpty(T[] array) {
		return ArrayUtils.isNotEmpty(array);
	}

	public static boolean isEmpty(String string) {
		return StringUtils.isEmpty(string);
	}

	public static boolean isEmpty(byte[] bytes) {
		return ArrayUtils.isEmpty(bytes);
	}

	public static boolean isEmpty(List<?> list) {
		return CollectionUtils.isEmpty(list);
	}

	public static boolean isEmpty(Map<?, ?> map) {
		return MapUtils.isEmpty(map);
	}

	public static boolean isEmpty(Collection<?> collection) {
		return CollectionUtils.isEmpty(collection);
	}

	public static boolean isEmpty(short[] array) {
		return ArrayUtils.isEmpty(array);
	}

	public static boolean isEmpty(int[] array) {
		return ArrayUtils.isEmpty(array);
	}

	public static boolean isEmpty(long[] array) {
		return ArrayUtils.isEmpty(array);
	}

	public static boolean isEmpty(String[] array) {
		return ArrayUtils.isEmpty(array);
	}

	public static <T extends Object> boolean isEmpty(T[] array) {
		return ArrayUtils.isEmpty(array);
	}

	public static boolean isNegative(Short value) {
		return value == null || value < 0;
	}

	public static boolean isPositive(Short value) {
		return value != null && value > 0;
	}

	public static boolean isPositive(Byte value) {
		return value != null && value > 0;
	}

	public static boolean isNegative(Integer value) {
		return value == null || value < 0;
	}

	public static boolean isNegative(Byte value) {
		return value != null && value < 0;
	}

	public static boolean isPositive(Integer value) {
		return value != null && value > 0;
	}

	public static boolean isNegative(Long value) {
		return value == null || value < 0;
	}

	public static boolean isPositive(Long value) {
		return value != null && value > 0;
	}

	public static boolean isNegative(Float value) {
		return value == null || value < 0;
	}

	public static boolean isPositive(Float value) {
		return value != null && value > 0;
	}

	public static boolean isNegative(Double value) {
		return value == null || value < 0;
	}

	public static boolean isPositive(Double value) {
		return value != null && value > 0;
	}

	public static boolean isTrue(Boolean value) {
		return Objects.equals(Boolean.TRUE, value);
	}

	public static boolean isFalse(Boolean value) {
		return Objects.equals(Boolean.FALSE, value);
	}

	public static boolean contains(Long[] values, Long value) {
		return ArrayUtils.contains(values, value);
	}

	public static boolean containsAll(Long[] values, Long[] subValues) {
		if (ArrayUtils.isEmpty(values) && ArrayUtils.isEmpty(subValues)) {
			return true;
		}

		if (ArrayUtils.isEmpty(values)) {
			return false;
		}

		if (ArrayUtils.isEmpty(subValues)) {
			return true;
		}

		for (Long subValue : subValues) {
			if (!contains(values, subValue)) {
				return false;
			}
		}

		return true;
	}

	public static <E> boolean contains(List<E> list, E one) {
		return list.indexOf(one) != -1;
	}

	public static boolean onlyOne(List<?> list) {
		if (Detect.notEmpty(list) && list.size() == 1) {
			return true;
		}
		return false;
	}

	public static boolean between(long value, long floor, long ceil) {
		return value >= floor && value <= ceil;
	}

	public static boolean bizIdEquals(long bizId1, long bizId2) {
		return bizId1 > 0 && bizId2 > 0 && Objects.equals(bizId1, bizId2);
	}

}
