package com.common;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import com.cninsure.core.utils.DateUtil;
import com.cninsure.core.utils.UUIDUtils;

public class AffineTransImageUtil {
	
	public static String path = "";
	
	static {
		ResourceBundle resourceBundle = ResourceBundle
				.getBundle("config/config");
		path = resourceBundle.getString("uploadimage.path");
	}
	
	public static File getAffineTransImage(File fi){
		int nw = 500;
		String imgType="";
		String fileName = fi.getName();
		if(fileName.contains(".")){
			imgType = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
		}
		String currentDate = DateUtil.getCurrentDate();
		File fo = new File(path+"/img/"+currentDate+"/"+UUIDUtils.create()+"."+imgType);
		if(!fo.getParentFile().exists()){
			fo.getParentFile().mkdirs();
		}
		try {
		 AffineTransform transform = new AffineTransform();  
         BufferedImage bis;
			bis = ImageIO.read(fi);
			int w = bis.getWidth();  
			int h = bis.getHeight();  
			//double scale = (double)w/h;  
			int nh = (nw*h)/w ;  
			double sx = (double)nw/w;  
			double sy = (double)nh/h;  
			transform.setToScale(sx,sy); //setToScale(double sx, double sy) 将此变换设置为缩放变换。
			System.out.println(w + ";zdsuofang;" +h);  
			AffineTransformOp ato = new AffineTransformOp(transform,AffineTransformOp.TYPE_BILINEAR);
	        BufferedImage bid = new BufferedImage(nw,nh,BufferedImage.TYPE_BYTE_INDEXED);
	        ato.filter(bis,bid);

			ato.filter(bid, null).getGraphics().drawImage(
					bis.getScaledInstance((int) nw, (int) nh,
							Image.SCALE_SMOOTH), 0, 0, null);
            ImageIO.write(bid,imgType,fo);  
            return fo;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("zdsuofang;" + e.getMessage());  
			return null;
		} 
	}
//	public static void main(String[] args){
//		File file = new File("C:/123.png");
//		File ff = AffineTransImageUtil.getAffineTransImage(file);
//
//		File file1 = new File("C:/2.jpg");
//		AffineTransImageUtil.getAffineTransImage(file1);
//		System.out.println(ff.getPath());
//	}
}

