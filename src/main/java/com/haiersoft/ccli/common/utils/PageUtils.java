package com.haiersoft.ccli.common.utils;
public class PageUtils {
	/**
	 * 获得分页总数
	 * @param totalCount 总记录数
	 * @param pageSize 分页大小
	 * @return 总分页数
	 */
	public static Integer calPageCount(Integer totalCount, Integer pageSize) {
		Integer result = null;
		if ( null == totalCount ) {
			totalCount = Integer.valueOf(0);
		}
		if ( null == pageSize ) {
			pageSize = 20;
		}
		if ( totalCount < 0 ) {
			totalCount = Integer.valueOf(0);
		}
		if ( pageSize < 0 ) {
			pageSize =20;
		}
		if ( totalCount % pageSize > 0 ) {
			result = totalCount / pageSize + 1;
		} else {
			result = totalCount / pageSize;
		}
		return result; 
	}
	
	/**
	 * 获得分页结果集首记录开始地址(由0开始)
	 * @param pageIndex Integer 分页数(由1开始)
	 * @param pageSize Integer 分页大小(小于0时为Page.DEFAULT_PAGESIZE大小)
	 * @param totalCount Integer 总记录数
	 * @return 开始记录地址
	 */
	public static Integer calBeginIndex(Integer pageIndex, Integer pageSize, Integer totalCount) {
		Integer _totalCount = totalCount;
		if ( null == pageIndex ) {
			pageIndex =1;
		}
		if ( null == pageSize ) {
			pageSize = 20;
		}
		if ( pageIndex.intValue() <= 0 ) {
			pageIndex = 1;
		}
		if ( pageSize.intValue() <= 0 ) {
			pageSize = 20;
		}
		int pageCount = calPageCount(_totalCount, pageSize);
		if ( pageIndex > pageCount ) {
			pageIndex = pageCount;
		}
		return Integer.valueOf((pageIndex.intValue()-1) * pageSize.intValue());
	}
}
