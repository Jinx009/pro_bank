package com.rongdu.p2psys.nb.util;

/***
 * 银行卡支行信息辅助实体类
 * @author cgw
 *	2015-09-21
 */
public class CityCodeModel {
	
	private String Province;	//省份
	private String City;			//市
	private String CityCode;	//县
	
	public CityCodeModel(String Province,String City,String CityCode) {
		this.Province = Province;
		this.City = City;
		this.CityCode = CityCode;
	}
	
	public String getProvince() {
		return Province;
	}
	public void setProvince(String province) {
		Province = province;
	}
	public String getCity() {
		return City;
	}
	public void setCity(String city) {
		City = city;
	}
	public String getCityCode() {
		return CityCode;
	}
	public void setCityCode(String cityCode) {
		CityCode = cityCode;
	}
}
