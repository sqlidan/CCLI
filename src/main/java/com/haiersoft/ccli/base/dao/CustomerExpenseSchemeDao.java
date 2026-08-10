package com.haiersoft.ccli.base.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.haiersoft.ccli.base.entity.CustomerExpenseSchemeInfoView;
import com.haiersoft.ccli.base.entity.CustomerExpenseSchemeQuery;
import com.haiersoft.ccli.base.entity.CustomerExpenseSchemeView;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.utils.StringUtils;

/**
 * 客户费用方案明细查询DAO。
 */
@Repository
public class CustomerExpenseSchemeDao extends HibernateDao<CustomerExpenseSchemeView, String> {

    /**
     * 分页查询费用方案及关联的合同主表数据。
     */
    public Page<CustomerExpenseSchemeView> findPage(Page<CustomerExpenseSchemeView> page,
            CustomerExpenseSchemeQuery query) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = buildSchemeSql(query, params) + " order by scheme.operate_time desc nulls last, scheme.scheme_num desc";
        return findPageSql(page, sql, buildSchemeParamTypes(), params);
    }

    /**
     * 查询符合筛选条件的费用方案及合同主表数据，用于主表导出。
     */
    public List<CustomerExpenseSchemeView> findList(CustomerExpenseSchemeQuery query) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = buildSchemeSql(query, params) + " order by scheme.operate_time desc nulls last, scheme.scheme_num desc";
        return findSql(sql, buildSchemeParamTypes(), params);
    }

    /**
     * 根据方案编号查询详情页的费用方案及合同主表数据。
     */
    public CustomerExpenseSchemeView findBySchemeNum(String schemeNum) {
        CustomerExpenseSchemeQuery query = new CustomerExpenseSchemeQuery();
        query.setSchemeNum(schemeNum);
        List<CustomerExpenseSchemeView> list = findList(query);
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * 查询符合筛选条件的费用方案子表数据，用于明细导出。
     */
    public List<CustomerExpenseSchemeInfoView> findInfoList(CustomerExpenseSchemeQuery query) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select scheme.scheme_num as schemeNum, ");
        sql.append(" scheme.scheme_name as schemeName, ");
        sql.append(" scheme.customs_name as customsName, ");
        sql.append(" contract.contract_num as contractNum, ");
        sql.append(" contract.client_name as contractClientName, ");
        sql.append(" info.fee_code as feeCode, ");
        sql.append(" info.fee_name as feeName, ");
        sql.append(" info.currency as currency, ");
        sql.append(" info.billing as billing, ");
        sql.append(" info.unit as unit, ");
        sql.append(" info.fee_type as feeType, ");
        sql.append(" info.term_attribute as termAttribute, ");
        sql.append(" info.min_price as minPrice, ");
        sql.append(" info.max_price as maxPrice, ");
        sql.append(" info.gear_code as gearCode, ");
        sql.append(" info.gear_exp as gearExp, ");
        sql.append(" info.if_pay as ifPay, ");
        sql.append(" info.remark as remark ");
        sql.append(" from base_expense_scheme scheme ");
        sql.append(" left join base_expense_contract contract on contract.contract_num = scheme.contract_id ");
        sql.append(" inner join base_expense_scheme_info info on info.scheme_num = scheme.scheme_num ");
        appendWhere(sql, query, params);
        sql.append(" order by scheme.operate_time desc nulls last, scheme.scheme_num desc, info.id ");
        return findInfoSql(sql.toString(), buildInfoParamTypes(), params);
    }

    /**
     * 组装费用方案主表和合同主表的查询语句。
     */
    private String buildSchemeSql(CustomerExpenseSchemeQuery query, Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select scheme.scheme_num as schemeNum, ");
        sql.append(" scheme.scheme_name as schemeName, ");
        sql.append(" scheme.customs_name as customsName, ");
        sql.append(" scheme.contract_id as contractNum, ");
        sql.append(" contract.client_name as contractClientName, ");
        sql.append(" contract.contract_state as contractState, ");
        sql.append(" contract.sign_time as signTime, ");
        sql.append(" contract.expiration_time as expirationTime, ");
        sql.append(" contract.canvassion_person as canvassionPerson, ");
        sql.append(" scheme.if_get as ifGet, ");
        sql.append(" scheme.program_state as programState, ");
        sql.append(" scheme.operator_person as operatorPerson, ");
        sql.append(" scheme.operate_time as operateTime, ");
        sql.append(" scheme.remark as schemeRemark, ");
        sql.append(" contract.remark as contractRemark ");
        sql.append(" from base_expense_scheme scheme ");
        sql.append(" left join base_expense_contract contract on contract.contract_num = scheme.contract_id ");
        appendWhere(sql, query, params);
        return sql.toString();
    }

    /**
     * 追加客户费用方案明细页面的筛选条件。
     */
    private void appendWhere(StringBuilder sql, CustomerExpenseSchemeQuery query, Map<String, Object> params) {
        sql.append(" where scheme.is_del = '0' ");
        if (query == null) {
            return;
        }
        if (!StringUtils.isNull(query.getSchemeNum())) {
            sql.append(" and scheme.scheme_num like :schemeNum ");
            params.put("schemeNum", "%" + query.getSchemeNum() + "%");
        }
        if (!StringUtils.isNull(query.getSchemeName())) {
            sql.append(" and scheme.scheme_name like :schemeName ");
            params.put("schemeName", "%" + query.getSchemeName() + "%");
        }
        if (!StringUtils.isNull(query.getContractNum())) {
            sql.append(" and scheme.contract_id like :contractNum ");
            params.put("contractNum", "%" + query.getContractNum() + "%");
        }
        if (!StringUtils.isNull(query.getCustomerName())) {
            sql.append(" and (scheme.customs_name like :customerName or contract.client_name like :customerName) ");
            params.put("customerName", "%" + query.getCustomerName() + "%");
        }
        if (!StringUtils.isNull(query.getIfGet())) {
            sql.append(" and scheme.if_get = :ifGet ");
            params.put("ifGet", query.getIfGet());
        }
        if (!StringUtils.isNull(query.getProgramState())) {
            sql.append(" and scheme.program_state = :programState ");
            params.put("programState", query.getProgramState());
        }
        if (!StringUtils.isNull(query.getContractState())) {
            sql.append(" and contract.contract_state = :contractState ");
            params.put("contractState", query.getContractState());
        }
    }

    /**
     * 费用方案及合同主表字段类型。
     */
    private Map<String, Object> buildSchemeParamTypes() {
        Map<String, Object> paramTypes = new HashMap<String, Object>();
        paramTypes.put("schemeNum", String.class);
        paramTypes.put("schemeName", String.class);
        paramTypes.put("customsName", String.class);
        paramTypes.put("contractNum", String.class);
        paramTypes.put("contractClientName", String.class);
        paramTypes.put("contractState", String.class);
        paramTypes.put("signTime", Date.class);
        paramTypes.put("expirationTime", Date.class);
        paramTypes.put("canvassionPerson", String.class);
        paramTypes.put("ifGet", String.class);
        paramTypes.put("programState", String.class);
        paramTypes.put("operatorPerson", String.class);
        paramTypes.put("operateTime", Date.class);
        paramTypes.put("schemeRemark", String.class);
        paramTypes.put("contractRemark", String.class);
        return paramTypes;
    }

    /**
     * 费用方案子表字段类型。
     */
    private Map<String, Object> buildInfoParamTypes() {
        Map<String, Object> paramTypes = new HashMap<String, Object>();
        paramTypes.put("schemeNum", String.class);
        paramTypes.put("schemeName", String.class);
        paramTypes.put("customsName", String.class);
        paramTypes.put("contractNum", String.class);
        paramTypes.put("contractClientName", String.class);
        paramTypes.put("feeCode", String.class);
        paramTypes.put("feeName", String.class);
        paramTypes.put("currency", String.class);
        paramTypes.put("billing", String.class);
        paramTypes.put("unit", Double.class);
        paramTypes.put("feeType", String.class);
        paramTypes.put("termAttribute", String.class);
        paramTypes.put("minPrice", Double.class);
        paramTypes.put("maxPrice", Double.class);
        paramTypes.put("gearCode", String.class);
        paramTypes.put("gearExp", String.class);
        paramTypes.put("ifPay", Integer.class);
        paramTypes.put("remark", String.class);
        return paramTypes;
    }

    /**
     * 将费用方案子表查询结果转换为导出对象。
     */
    @SuppressWarnings("unchecked")
    private List<CustomerExpenseSchemeInfoView> findInfoSql(String sql,
            Map<String, Object> paramTypes, Map<String, Object> params) {
        List<CustomerExpenseSchemeInfoView> list = new ArrayList<CustomerExpenseSchemeInfoView>();
        org.hibernate.SQLQuery query = createSQLQuery(sql, params);
        for (Map.Entry<String, Object> entry : paramTypes.entrySet()) {
            if (String.class == entry.getValue()) {
                query.addScalar(entry.getKey(), org.hibernate.type.StandardBasicTypes.STRING);
            } else if (Double.class == entry.getValue()) {
                query.addScalar(entry.getKey(), org.hibernate.type.StandardBasicTypes.DOUBLE);
            } else if (Integer.class == entry.getValue()) {
                query.addScalar(entry.getKey(), org.hibernate.type.StandardBasicTypes.INTEGER);
            }
        }
        query.setResultTransformer(org.hibernate.transform.Transformers.aliasToBean(CustomerExpenseSchemeInfoView.class));
        list.addAll(query.list());
        return list;
    }
}
