package com.zzb.cm.Interface.entity.model;

/**
 * 车身颜色
 * Created by austinChen on 2015/9/18.
 */
public enum CarBodyColor {

	White(1,"白色"),
    Gray(2,"灰"),
    Yellow(3,"黄") ,
    Powder(4,"粉"),
    Red (5,"红"),
    Purple(6,"紫") ,
    Green (7,"绿"),
    Blue (8,"蓝"),
    Palm (9,"棕"),
    Black (10,"黑"),
    Other(11,"其他"),
    Argent (12,"银白"),
    SilverGray(13,"银灰") ,
    DarkGreen (14,"墨绿"),
    Azure (15,"天蓝"),
    DarkBrown(16,"深棕") ,
    Milky(17,"乳白");
   CarBodyColor(int value,String name){
       this.value = value;
       this.name = name;
   }
   private Integer value;
   private String name;

   public int getValue() {
       return value;
   }

   public String getName() {
       return name;
   }
}
