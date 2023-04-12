package com.haiersoft.ccli.common.utils;

import java.math.BigDecimal;

/**
 * 大数据加减乘除
 * 
 * @ClassName: BigDecimalUtil
 * @Description: BigDecimalUtil
 * @author Connor
 * 
 */
public class BigDecimalUtil {

	private static final int DEF_DIV_SCALE =2;
	
	
	/**
	 * 获取税额
	 * @param v1
	 * @return
	 */
	public static Double getAmt(String v1){
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal("1.06");
		return b1.divide(b2,DEF_DIV_SCALE,BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	/**
	 * 应交的税额
	 * @param v1
	 * @return
	 */
	public static Double getRate(Double v1){
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal("0.06");
		return b1.multiply(b2).setScale(DEF_DIV_SCALE,BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	/**
	 * 提供精确的加法运算。
	 * 
	 * @param v1
	 *            被加数
	 * @param v2
	 *            加数
	 * @return 两个参数的和
	 */
	public static double add(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).doubleValue();
	}

	/**
	 * 提供精确的减法运算。
	 * 
	 * @param v1
	 *            被减数
	 * @param v2
	 *            减数
	 * @return 两个参数的差
	 */
	public static double sub(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).doubleValue();
	}

	/**
	 * 提供精确的乘法运算。
	 * 
	 * @param v1
	 *            被乘数
	 * @param v2
	 *            乘数
	 * @return 两个参数的积
	 */
	public static double mul(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).doubleValue();
	}

	/**
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后多少位，以后的数字四舍五入。
	 * 
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @return 两个参数的商
	 */
	public static double div(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, DEF_DIV_SCALE, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 确定小数点位数
	 * 
	 * @param v
	 * @param scale
	 * @return
	 */
	public static String round(double v, int scale) {
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = BigDecimal.ONE;
		String s = Double.toString(b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue());
		String[] str = s.split("\\.");
		if (str[1].length() != scale) {
			int i = scale - str[1].length();
			StringBuffer sb = new StringBuffer();
			for (int j = 0; j < i; j++) {
				sb.append("0");
			}
			s = s + sb.toString();
		}
		return s;
	}

}
