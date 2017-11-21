package com.kk.action.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSON;
import com.kk.action.dao.IBaseLocationDao;
import com.kk.utils.gps.BSUtils;
import com.kk.utils.gps.BaseStationModel;
import com.kk.utils.iot.BaiduAddress;
import com.kk.utils.iot.GetPosition;

@Repository(value="baseLocationDao")
public class BaseLocationDaoImpl implements IBaseLocationDao {
	@Resource(name="jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	@Override
	public BaseStationModel queryBaseStation(List<BaseStationModel> list)
			throws Exception {
		String lac="";
		String cellid="";
		BaseStationModel model=new BaseStationModel();
		List<Map<String,Object>> arraylist=new ArrayList<Map<String, Object>>();
		StringBuffer baseLocalSql=new StringBuffer();
	    for (int i = 0; i < list.size(); i++) {
	    	lac=list.get(i).getLac();
			cellid=list.get(i).getCell();
			if(i==0){
				if (lac=="0000"||cellid=="0000"||"0000".equals(lac)||"0000".equals(cellid)||lac=="0"||cellid=="0"||"0".equals(lac)||"0".equals(cellid)||lac==null||cellid==null||lac==""||cellid==""||"".equals(lac)||"".equals(cellid)) {
					cellid="";
					lac="";
				}
				else {
					cellid=Integer.parseInt(cellid, 16)+"".trim();
					lac=Integer.parseInt(lac, 16)+"".trim();
				}
				baseLocalSql.append("(lac='"+lac+"' and ci='"+cellid+"')");
			}else{
				if (lac=="0000"||cellid=="0000"||"0000".equals(lac)||"0000".equals(cellid)||lac=="0"||cellid=="0"||"0".equals(lac)||"0".equals(cellid)||lac==null||cellid==null||lac==""||cellid==""||"".equals(lac)||"".equals(cellid)) {
					cellid="";
					lac="";
				}
				else {
					cellid=Integer.parseInt(cellid, 16)+"".trim();
					lac=Integer.parseInt(lac, 16)+"".trim();
				}
				baseLocalSql.append(" or (lac='"+lac+"' and ci='"+cellid+"')");
			}
		}
		String sql = "select lac,ci,lat,lon,addr from cellinfo_addr where "+baseLocalSql;
		arraylist=jdbcTemplate.queryForList(sql);
		List<String> main = new ArrayList<String>();
		BSUtils bsUtils = new BSUtils();
		String[] bsStrings = new String[100];
		if(arraylist!=null){
			if(list.size()>1){
				//存放map键值对便于获取为重新获取rxlevel值设置lac+cellid为键值
				Map<String, Object> map=new HashMap<String, Object>();
				for (int i = 0; i < list.size(); i++) {
					map.put(list.get(i).getLac()+list.get(i).getCell(), list.get(i).getRxlevel());
				}
				int j=0;
				for (int i = 0; i < arraylist.size(); i++) {
					 String thisPostion = GetPosition.transgpshx(Double.parseDouble(arraylist.get(i).get("lon").toString()),Double.parseDouble(arraylist.get(i).get("lat").toString()));
					 //重新装备list
					 list.get(i).setLng(thisPostion.split(",")[0]);
					 list.get(i).setLat(thisPostion.split(",")[1]);
					 list.get(i).setAddress(arraylist.get(i).get("addr").toString());
					 System.out.println(arraylist.get(i).get("lac").toString()+arraylist.get(i).get("ci").toString());
					 //返回的是具体数字，要转化成hex形式
					 String lactmp = Integer.toHexString(Integer.parseInt(arraylist.get(i).get("lac").toString())) + "";
					 String cellidtmp = Integer.toHexString(Integer.parseInt(arraylist.get(i).get("ci").toString())) + "";
					 list.get(i).setRxlevel(map.get(lactmp+cellidtmp).toString());
					 bsStrings[j]=list.get(i).getLat();
					 bsStrings[j+1]=list.get(i).getLng(); 
					 bsStrings[j+2]=list.get(i).getRxlevel();
					 bsStrings[j+3]=1000+"";
					 j=j+4;
				}
				try {
					main = bsUtils.main(arraylist.size()*4,bsStrings);
					model.setLng(main.get(1));// 经度
					model.setLat(main.get(0));// 纬度
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}else if(list.size()==1){
				model.setLat(arraylist.get(0).get("lat").toString());
				model.setLng(arraylist.get(0).get("lon").toString());
			}else{
				model.setLat("0");
				model.setLng("0");
			}
		}else{
				model.setLat("0");
				model.setLng("0");
		}
		//百度地图纠偏
		if ("0".equals(model.getLng()) || "0".equals(model.getLat())) {
			model.setAddress("暂无法获取位置");
		} else {
			Double longitude2 = Double.parseDouble(model.getLng());// 经度
			Double latitude2 = Double.parseDouble(model.getLat());// 纬度
			// 百度地图纠偏 重新获得
			String thisPostion1 = GetPosition.transgpsbd(longitude2, latitude2);
			String longitudebd = thisPostion1.split(",")[0];
			String latitudebd = thisPostion1.split(",")[1];
			model.setLng(longitude2.toString());
			model.setLat(latitude2.toString());
			try {
				String  address = BaiduAddress.getAddress(latitudebd,longitudebd);
				model.setAddress(address);
				Object jsonObject=JSON.toJSON(list);
				Object jsonObjectls=JSON.toJSON(model);
				System.out.println("返回的整体数据jsonObject:"+jsonObjectls+"组装后的数据:"+jsonObject+"数据库返回的数据集合:"+arraylist);
			} catch (Exception e) {
				model.setAddress("暂无法获取位置");
			}// 地址
		}
		//根据纬度和经度通过百度接口获取地理位置
		return model;
	}

}
