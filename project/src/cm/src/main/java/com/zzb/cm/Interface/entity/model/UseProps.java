package com.zzb.cm.Interface.entity.model;

/**
 * 车辆使用性质
 * Created by austinChen on 2015/9/18.
 */
public enum UseProps {

     NotCase,// "不区分营业非营业")
     Domestic , //"家庭自用")
     Taxi , //"营业出租租赁")
     Bus , //"营业城市公交")
     Passenger , //"营业公路客运")
     Journey ,// "营业旅游")
     Freight , //"营业货运")
     Business , //"营业用")
     NonOperating, //"非营业用")
     Personal , //"非营业个人")
     Enterprise , //"非营业企业")
     Organization , //"非营业机关")
     SelfFreight , //"非营业货运")
     DualPurpose,//"兼用型（拖拉机专用）")
     Transportation , //"运输型（拖拉机专用）")
     Special , //"营业特种车")
     NotOperatingSpecial  //"非营业特种车")
}
