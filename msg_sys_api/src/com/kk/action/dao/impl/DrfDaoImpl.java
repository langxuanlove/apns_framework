package com.kk.action.dao.impl;

import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.kk.action.dao.IDrfDao;
import com.kk.utils.PropertiesUtil;
import com.kk.utils.PropertyUtil;

import drf.IDRFService;
import explain.publicFunction.DRFService;

@Repository(value = "drfDao")
public class DrfDaoImpl implements IDrfDao {

	public String getNickName(String uniqueId) throws Exception {
		String result="";
		String DRF_ADDRESS = PropertiesUtil.getKeyValue("DRF_ADDRESS");
		String GNET_IOT_SYS_DBID=PropertiesUtil.getKeyValue("GNET_IOT_SEC_BUS_DBID");
		String GNET_IOT_SYS_UserId=PropertiesUtil.getKeyValue("GNET_IOT_SEC_BUS_UserId");
		String GNET_IOT_SYS_GroupId=PropertiesUtil.getKeyValue("GNET_IOT_SEC_BUS_GroupId");
		IDRFService drf = new DRFService().goDrfInterfaceService(DRF_ADDRESS);
		String drfResult = drf.executeFunction("{\"DBID\":\""+ GNET_IOT_SYS_DBID + "\"," +
				"\"UserId\":\"" + GNET_IOT_SYS_UserId+ "\",\"GroupId\":\"" + GNET_IOT_SYS_GroupId + "\"}",
				"getUserDeviceByUniqueId", "[\"" + uniqueId+ "\"]", null);
		JSONObject json=JSONObject.parseObject(drfResult);
		JSONArray  jsonData=json.getJSONObject("Data").getJSONArray("table0");
		JSONObject object=JSONObject.parseObject(jsonData.getString(0));
		result=object.getString("nick_name");
		return result;
	};
	public static void main(String[] args) throws Exception {
//		DrfDaoImpl daoImpl=new DrfDaoImpl();
//		String drfResult="{\"ErrorDataIndex\":-1,\"ErrorTableIndex\":-1," +
//		"\"IsSuccess\":true,\"Result\":1,\"RowEffect\":0,\"TotalCount\":1," +
//		"\"DataType\":\"DataSet\",\"ErrorMessage\":\"\",\"ErrorFieldList\":null," +
//		"\"ErrorTableList\":null,\"Data\":{\"table0\":" +
//		"[{\"user_dev_id\":\"b1d4946bcca442d992080d28ee9359a0\"," +
//		"\"user_name\":\"18614065381\",\"devices_id\":\"dfddfff\",\"nick_name\":\"3\",\"permission_sign\":\"1\",\"update_time\":\"2015-08-18 13:40:28\",\"time\":\"2015-08-18 13:40:30\",\"access_type\":\"AD\",\"device_img\":null," +
//		"\"user_settings\":\"\"}]},\"ExceptionMessage\":\"\",\"ErrorSystemId\":null,\"JsonResult\":null,\"ProcedureOutParameterInfo\":null,\"IdentityValueList\":[],\"ErrorCode\":0}";
//		JSONObject  json=JSONObject.parseObject(drfResult);
//		JSONArray  jsonData=json.getJSONObject("Data").getJSONArray("table0");
//		JSONObject object=JSONObject.parseObject(jsonData.getString(0));
//		System.out.println(object.getString("nick_name"));
//		System.out.println(daoImpl.getNickName("8978655544442"));
	}
}
