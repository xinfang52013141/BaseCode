package com.sz.xxx.modules.xxx.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sz.xxx.common.service.BaseService;
import com.sz.xxx.modules.xxx.dao.LgorgmapDao;
import com.sz.xxx.modules.xxx.dao.GetMaxDateDao;
import com.sz.xxx.modules.xxx.entity.LgorgInfo;

@Service
@Transactional(readOnly = true)
public class LgorgmapService extends BaseService {

	@Autowired
	private LgorgmapDao LgorgmapDao;
	@Autowired
	private GetMaxDateDao maxDateDao;

	/***
	 * xxxxxx
	 * 
	 * @param begindate
	 * @param datetype
	 * @return
	 */
	public Map<String, Object> getlgorgmap(String begindate, String datetype,String orgtype) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<HashMap<String, Object>> maxdate = new ArrayList<HashMap<String, Object>>();
		List<String> mList = new ArrayList<String>();
		maxdate = maxDateDao.getlgmaxdate();// xxxx
		if (maxdate != null && maxdate.size() > 0) {
			for (HashMap<String, Object> hashMap : maxdate) {
				String dname = hashMap.get("VDATE") + "";
				mList.add(dname);
			}
		}
		begindate = mList.get(0).toString();
		List<LgorgInfo> list = new ArrayList<LgorgInfo>();
		List<String> timeLineData = new ArrayList<String>();
		if (datetype.indexOf("最近一周") != -1) {
			timeLineData = this.LgorgmapDao.getlgorgBydaywdate(begindate);
			list = this.LgorgmapDao.getlgorgBydayw(begindate,orgtype);
		} else if (datetype.indexOf("最近一月") != -1) {
			timeLineData = this.LgorgmapDao.getlgorgBydaymdate(begindate);
			list = this.LgorgmapDao.getlgorgBydaym(begindate,orgtype);
		} else if (datetype.indexOf("按周进行") != -1) {
			timeLineData = this.LgorgmapDao.getlgorgByweekdate(begindate);
			list = this.LgorgmapDao.getlgorgByweek(begindate,orgtype);
		} else if (datetype.indexOf("按月进行") != -1) {
			timeLineData = this.LgorgmapDao.getlgorgBymondate(begindate);
			list = this.LgorgmapDao.getlgorgBymon(begindate,orgtype);
		}
		map = handleInfo(timeLineData, list, map);
		return map;
	}

	/***
	 * xxxx
	 * 
	 * @param timeLineData
	 * @param list
	 * @param map
	 * @return
	 */
	private Map<String, Object> handleInfo(List<String> timeLineData, List<LgorgInfo> list, Map<String, Object> map) {
		List<Object> optionsList = new ArrayList<Object>();
		for (int i = 0; i < timeLineData.size(); i++) {
			String vdate = (String) timeLineData.get(i);
			Map<String, Object> seriesMap = new HashMap<String, Object>();
			List<Object> dataList = new ArrayList<Object>();
			Map<String, Object> dataMap = new HashMap<String, Object>();
			List<Object> dataAllList = new ArrayList<Object>();
			for (int j = 0; j < list.size(); j++) {
				Map<String, Object> seriesDataMap = new HashMap<String, Object>();
				List<String> valueList = new ArrayList<String>();
				if (vdate.equals(((LgorgInfo) list.get(j)).getTmw())) {
						valueList.add(((LgorgInfo) list.get(j)).getOrganlongitude());
						valueList.add(((LgorgInfo) list.get(j)).getOrganlatitude());
						valueList.add(((LgorgInfo) list.get(j)).getOrigin());
						seriesDataMap.put("name", ((LgorgInfo) list.get(j)).getOrgname());
						seriesDataMap.put("value", valueList);
						dataList.add(seriesDataMap);
						if (list.size() > 1) {
							list.remove(j);
							j -= 1;
						} 				
				}
			}
			dataMap.put("data", dataList);
			dataAllList.add(dataMap);
			seriesMap.put("series", dataAllList);
			optionsList.add(seriesMap);
		}
		map.put("optionsList", optionsList);
		map.put("timeLineData", timeLineData);
		System.out.println(map);
		return map;
	}
	public Map<String, Object> getlgorgdata(String begindate,String datetype,String orgname){
		Map<String, Object> map=new HashMap<String, Object>();
		List<HashMap<String, Object>> maxdate=new ArrayList<HashMap<String, Object>>();
		List<String> mList=new ArrayList<String>();
		maxdate=maxDateDao.getlgmaxdate();//xxx
		if(maxdate!=null&&maxdate.size()>0){
			for (HashMap<String, Object> hashMap : maxdate) {
				String dname=hashMap.get("VDATE")+"";
				mList.add(dname);	
			}
		}
		begindate=mList.get(0).toString();
		List<Object> vdata=new ArrayList<Object>();
		List<Object> originData=new ArrayList<Object>();
		List<String> aList=new ArrayList<String>();
		List<HashMap<String, Object>> ls=new ArrayList<HashMap<String, Object>>();		
		if(datetype.indexOf("最近一周")!=-1){
		ls=LgorgmapDao.getlgorgdataBydayw(orgname,begindate);//xxx
		}
		else if(datetype.indexOf("最近一月")!=-1){
				ls=LgorgmapDao.getlgorgdataBydaym(orgname,begindate);//xxx
		}
		else if(datetype.indexOf("按周进行")!=-1){
				ls=LgorgmapDao.getlgorgdataByweek(orgname,begindate);//xxx
		}
		else if(datetype.indexOf("按月进行")!=-1){
				ls=LgorgmapDao.getlgorgdataBymon(orgname,begindate);//xxx
		}
		if(ls!=null&&ls.size()>0){
			for (HashMap<String, Object> hashMap : ls) {
				String oname=hashMap.get("ORGANAME")+"";
				Object vdate= hashMap.get("TMW");
				Object origin=hashMap.get("ORIGIN");
				aList.add(oname);
				vdata.add(vdate);
				originData.add(origin);	
			}
		}
		//以map方式输出
		map.put("datashow", originData);
		map.put("dateshow", vdata);
		System.out.print(map);
		return map;
}
}
