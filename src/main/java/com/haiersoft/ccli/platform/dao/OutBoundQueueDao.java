package com.haiersoft.ccli.platform.dao;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.utils.FrameworkUtil;
import com.haiersoft.ccli.cost.entity.PlatformWorkTicket;
import com.haiersoft.ccli.platform.entity.OutBoundQueueVO;
import com.haiersoft.ccli.platform.entity.VehicleQueue;
import com.haiersoft.ccli.wms.entity.CountTemplete;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * @author A0047794
 * @日期 2021/11/29
 * @描述
 */
@Repository
public class OutBoundQueueDao extends HibernateDao<OutBoundQueueVO, String> {

	public Page<OutBoundQueueVO> searchOutBondQueue(Page<OutBoundQueueVO> page, OutBoundQueueVO outBoundQueueVO) {
		StringBuffer sb = new StringBuffer();

		Map<String, Object> parme = new HashMap<>();

		sb.append("select outbound.yyid, outbound.consume_Company consumeCompany,outbound.bill_No billNo,outbound.container_No containerNo," +
				"outbound.product_Name productName,outbound.product_Type productType,outbound.num num,outbound.weight weight," +
				"outbound.car_Number carNumber,outbound.origin_Country originCountry,outbound.appoint_Date appointDate,");
		sb.append("queue.warehouse_No warehouseNo,outbound.room_Num roomNum,outbound.location_No locationNo,queue.platform_No platformNo," +
				"queue.platform_Name platformName,queue.auto_Manual_Flag autoManualFlag,queue.queue_Time queueTime,queue.status_Flag statusFlag," +
                "log.start_Time startTime,log.end_Time endTime,log.leave_Time leaveTime");
        sb.append(" from PLATFORM_RESERVATION_OUTBOUND outbound \n" +
                " LEFT JOIN  PLATFORM_QUEUE queue ON outbound.YYID=queue.YYID\n" +
                " LEFT JOIN PLATFORM_OPERATION_LOG log ON outbound.YYID=log.YYID and log.DELETED_FLAG = '0'  where outbound.DELETED_FLAG = '0' ");

		if((null==outBoundQueueVO.getAppointDateStart() ||"".equals(outBoundQueueVO.getAppointDateStart())) &&
				(null==outBoundQueueVO.getAppointDateEnd() ||"".equals(outBoundQueueVO.getAppointDateEnd()))){


			Calendar start = Calendar.getInstance();
			//结束时间
			Calendar end = Calendar.getInstance();
			start.add(Calendar.DATE, 0);
			// 时
			start.set(Calendar.HOUR_OF_DAY, 0);
			// 分
			start.set(Calendar.MINUTE, 0);
			// 秒
			start.set(Calendar.SECOND, 0);
			start.set(Calendar.MILLISECOND, 0);

			end.add(Calendar.DATE, 1);
			// 时
			end.set(Calendar.HOUR_OF_DAY, 0);
			// 分
			end.set(Calendar.MINUTE, 0);
			// 秒
			end.set(Calendar.SECOND, 0);
			end.set(Calendar.MILLISECOND, 0);

			sb.append(" and outbound.appoint_Date >= :appointDataStart ");
			parme.put("appointDataStart", start);

			sb.append(" and outbound.appoint_Date < :appointDataEnd ");
			parme.put("appointDataEnd", end);

		}else {

			if(null!=outBoundQueueVO.getAppointDateStart() && !"".equals(outBoundQueueVO.getAppointDateStart())){
				sb.append(" and outbound.appoint_Date >= :appointDataStart ");
				parme.put("appointDataStart", outBoundQueueVO.getAppointDateStart());
			}

			if(null!=outBoundQueueVO.getAppointDateEnd() && !"".equals(outBoundQueueVO.getAppointDateEnd())){
				sb.append(" and outbound.appoint_Date < :appointDataEnd ");
				parme.put("appointDataEnd", outBoundQueueVO.getAppointDateEnd());
			}
		}

		if(null!=outBoundQueueVO.getCarNumber() && !"".equals(outBoundQueueVO.getCarNumber())){
			sb.append(" and outbound.car_Number like :carNumber ");
			parme.put("carNumber", "%"+outBoundQueueVO.getCarNumber()+"%");
		}

		if(null!=outBoundQueueVO.getContainerNo() && !"".equals(outBoundQueueVO.getContainerNo())){
			sb.append(" and outbound.container_No like :containerNo ");
			parme.put("containerNo", "%"+outBoundQueueVO.getContainerNo()+"%");
		}

		if(null!=outBoundQueueVO.getBillNo() && !"".equals(outBoundQueueVO.getBillNo())){
			sb.append(" and outbound.bill_No like :billNo ");
			parme.put("billNo", "%"+outBoundQueueVO.getBillNo()+"%");
		}

		if(null!=outBoundQueueVO.getStatusFlag() && !"".equals(outBoundQueueVO.getStatusFlag())){
			if("0".equals(outBoundQueueVO.getStatusFlag())){

				sb.append(" and queue.status_Flag = '0' ");

			}

			if("1".equals(outBoundQueueVO.getStatusFlag())){

				sb.append(" and queue.status_Flag in('1','2') ");

			}
			if("2".equals(outBoundQueueVO.getStatusFlag())){

				sb.append(" and queue.status_Flag in('3','4','5') ");

			}
		}


		sb.append(" order by queue.status_Flag asc,queue.queue_Time asc");

		//查询对象属性转换
		Map<String, Object> parm = new HashMap<String, Object>();
		parm.put("consumeCompany", String.class);
		parm.put("billNo", String.class);
		parm.put("containerNo", String.class);
		parm.put("productName", String.class);
		parm.put("productType", String.class);
		parm.put("num", String.class);
		parm.put("weight", String.class);
		parm.put("carNumber", String.class);
		parm.put("originCountry", String.class);
		parm.put("appointDate", Date.class);
		parm.put("warehouseNo", String.class);
		parm.put("roomNum", String.class);
		parm.put("locationNo", String.class);
		parm.put("platformNo", String.class);
		parm.put("platformName", String.class);
		parm.put("autoManualFlag", String.class);
		parm.put("queueTime", Date.class);
		parm.put("statusFlag", String.class);
		parm.put("startTime", Date.class);
		parm.put("endTime", Date.class);
		parm.put("leaveTime", Date.class);
		parm.put("yyid", String.class);

	//	Map<String, Object> parm= FrameworkUtil.getPropertiesTypeMap(OutBoundQueueVO.class);


		return findPageSql(page, sb.toString(), parm, parme);

		//return findPageSql(page, sb.toString(), queryParams);
	}


}
