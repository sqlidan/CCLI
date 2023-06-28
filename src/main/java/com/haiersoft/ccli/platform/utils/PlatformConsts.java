package com.haiersoft.ccli.platform.utils;

public interface PlatformConsts {

	//String PLATFORM_URL = "http://localhost:9098";
	 String PLATFORM_URL =  "http://10.135.252.216:9098";

	String PLATFORM_URL_IN = PLATFORM_URL + "/gate/ccligateIn";

	String PLATFORM_URL_OUT = PLATFORM_URL + "/gate/ccligateOut";

	//月台启用停用
	String PLATFORM_URL_UPDATE_PLATROM_QUEUE = PLATFORM_URL + "/platform/platformQueue/updatePlatromQueue";

	//指派
	String PLATFORM_URL_UPDATE_PLATROM_PRIORQUEUE = PLATFORM_URL + "/platform/platformQueue/updatePlatromPriorQueue";

	//核验
	String PLATFORM_URL_UPDATE_PLATROM_QUEUE_CHECK = PLATFORM_URL + "/platform/platformQueue/updatePlatromQueueOfCheck";

	// 排队  取消作业  取消预约
	String PLATFORM_URL_CANCEL_RESERVATION = PLATFORM_URL + "/platform/platformQueue/cancelReservation";
	// 取消预约
	String PLATFORM_URL_CANCEL_RESERVATION_NO_GATEIN = PLATFORM_URL + "/platform/platformQueue/cancelReservationOfNoGateIn";

	// 排队  重新排队
	String PLATFORM_URL_RESERT_QUEUE = PLATFORM_URL + "/platform/platformQueue/resertQueue";

	// 散货同步信息
	String PLATFORM_URL_BULKCARGO_UPDATE_OUTRESERVATION = PLATFORM_URL + "/platform/platformQueue/updateOutReservation";

}
