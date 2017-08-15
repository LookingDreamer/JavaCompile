package com.common;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.cninsure.core.tools.util.ValidateUtil;
import com.cninsure.core.utils.DateUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.core.utils.UUIDUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.zzb.mobile.util.EncodeUtils;

public class ZxingImgUtil extends LuminanceSource {
	private static final int BLACK = 0xFF000000;
	private static final int WHITE = 0xFFFFFFFF;

	public static BufferedImage toBufferedImage(BitMatrix matrix) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
			}
		}
		return image;
	}

	public static void writeToFile(BitMatrix matrix, String format, File file) throws IOException {
		BufferedImage image = toBufferedImage(matrix);
		if (!ImageIO.write(image, format, file)) {
			throw new IOException("Could not write an image of format " + format + " to " + file);
		}
	}

	public static void writeToStream(BitMatrix matrix, String format, OutputStream stream) throws IOException {
		BufferedImage image = toBufferedImage(matrix);
		if (!ImageIO.write(image, format, stream)) {
			throw new IOException("Could not write an image of format " + format);
		}
	}
	
	public static String decodeBinaryBitmapBase64(String path, String file,HttpServletRequest request) throws IOException, NotFoundException{
		String resultStr = null;
		MultiFormatReader formatReader = new MultiFormatReader(); 
		File filetemp = null;
		BufferedImage image = null;
		if(StringUtil.isNotEmpty(path)){
			filetemp = new File(path);
			if(path.contains("http://") || path.contains("https://")){
				image = ImageIO.read(new java.net.URL(path));
			}else{
				image = ImageIO.read(filetemp);
			}
		}else{
			String uuid = UUIDUtils.random();
			String currentDate = DateUtil.getCurrentDate();
			if(StringUtil.isEmpty(ValidateUtil.getConfigValue("uploadimage.path"))){
				filetemp = new File(request.getSession().getServletContext().getRealPath("/") +"/static/upload/"+ currentDate + "/" + uuid +".png" );
			}else{
				filetemp = new File(ValidateUtil.getConfigValue("uploadimage.path") +"/static/upload/"+ currentDate + "/" + uuid +".png" );
			}
			if(!filetemp.getParentFile().exists()){
				filetemp.getParentFile().mkdirs();
			}
			FileCopyUtils.copy(EncodeUtils.decodeBase64(file), filetemp);
			image = ImageIO.read(filetemp);
			
		}
		LuminanceSource source = new ZxingImgUtil(image); 
		Binarizer binarizer = new HybridBinarizer(source); 
		BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer); 
		Map rhints = new HashMap(); 
		rhints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); 
		Result result = formatReader.decode(binaryBitmap,rhints);
		resultStr = result.getText();
		filetemp.delete();
		return resultStr;
	}

	public static String decodeBinaryBitmap(String path, MultipartFile file,HttpServletRequest request) throws IOException, NotFoundException{
		String resultStr = null;
		MultiFormatReader formatReader = new MultiFormatReader(); 
		File filetemp = null;
		BufferedImage image = null;
		if(StringUtil.isNotEmpty(path)){
			filetemp = new File(path);
			if(path.contains("http://")||path.contains("https://")){
				image = ImageIO.read(new java.net.URL(path));
			}else{
				image = ImageIO.read(filetemp);
			}
		}else{
			String uuid = UUIDUtils.random();
			String currentDate = DateUtil.getCurrentDate();
			if(StringUtil.isEmpty(ValidateUtil.getConfigValue("uploadimage.path"))){
				filetemp = new File(request.getSession().getServletContext().getRealPath("/") +"/static/upload/"+ currentDate + "/" + uuid +".png" );
			}else{
				filetemp = new File(ValidateUtil.getConfigValue("uploadimage.path") +"/static/upload/"+ currentDate + "/" + uuid +".png" );
			}
			if(!filetemp.getParentFile().exists()){
				filetemp.getParentFile().mkdirs();
			}
			FileCopyUtils.copy(file.getBytes(), filetemp);
			image = ImageIO.read(filetemp);
			
		}
		LuminanceSource source = new ZxingImgUtil(image); 
		Binarizer binarizer = new HybridBinarizer(source); 
		BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer); 
		Map rhints = new HashMap(); 
		Vector<BarcodeFormat> decodeFormats = null;
		if (decodeFormats == null || decodeFormats.isEmpty()) {
	    	 decodeFormats = new Vector<BarcodeFormat>();
	    	 decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);
	    	 decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
	    	 decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
	    }
		rhints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);
		rhints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); 
		rhints.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
		
		formatReader.setHints(rhints);
	    try {
	    	Result result = formatReader.decodeWithState(binaryBitmap);
	    	resultStr = result.getText();
	    } catch (ReaderException re) {
	    	// continue
	    	//re.printStackTrace();
	    } finally {
	    	formatReader.reset();
	    }
		filetemp.delete();
		return resultStr;
	}
	
	private final BufferedImage image;
	private final int left;
	private final int top;

	public ZxingImgUtil(BufferedImage image) {
		this(image, 0, 0, image.getWidth(), image.getHeight());
	}

	public ZxingImgUtil(BufferedImage image, int left, int top, int width, int height) {
		super(width, height);

		int sourceWidth = image.getWidth();
		int sourceHeight = image.getHeight();
		if (left + width > sourceWidth || top + height > sourceHeight) {
			throw new IllegalArgumentException("Crop rectangle does not fit within image data.");
		}

		for (int y = top; y < top + height; y++) {
			for (int x = left; x < left + width; x++) {
				if ((image.getRGB(x, y) & 0xFF000000) == 0) {
					image.setRGB(x, y, 0xFFFFFFFF); // = white
				}
			}
		}

		this.image = new BufferedImage(sourceWidth, sourceHeight, BufferedImage.TYPE_BYTE_GRAY);
		this.image.getGraphics().drawImage(image, 0, 0, null);
		this.left = left;
		this.top = top;
	}

	@Override
	public byte[] getRow(int y, byte[] row) {
		if (y < 0 || y >= getHeight()) {
			throw new IllegalArgumentException("Requested row is outside the image: " + y);
		}
		int width = getWidth();
		if (row == null || row.length < width) {
			row = new byte[width];
		}
		image.getRaster().getDataElements(left, top + y, width, 1, row);
		return row;
	}

	@Override
	public byte[] getMatrix() {
		int width = getWidth();
		int height = getHeight();
		int area = width * height;
		byte[] matrix = new byte[area];
		image.getRaster().getDataElements(left, top, width, height, matrix);
		return matrix;
	}

	@Override
	public boolean isCropSupported() {
		return true;
	}

	@Override
	public LuminanceSource crop(int left, int top, int width, int height) {
		return new ZxingImgUtil(image, this.left + left, this.top + top, width, height);
	}

	@Override
	public boolean isRotateSupported() {
		return true;
	}

	@Override
	public LuminanceSource rotateCounterClockwise() {
		int sourceWidth = image.getWidth();
		int sourceHeight = image.getHeight();

		AffineTransform transform = new AffineTransform(0.0, -1.0, 1.0, 0.0, 0.0, sourceWidth);

		BufferedImage rotatedImage = new BufferedImage(sourceHeight, sourceWidth, BufferedImage.TYPE_BYTE_GRAY);

		Graphics2D g = rotatedImage.createGraphics();
		g.drawImage(image, transform, null);
		g.dispose();
		int width = getWidth();
		return new ZxingImgUtil(rotatedImage, top, sourceWidth - (left + width), getHeight(), width);
	}
	public static void main(String[] args) throws Exception {
//		String text = "你好";
//		int width = 100;
//		int height = 100;
//		String format = "png";
//		Hashtable hints = new Hashtable();
//		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
//		BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);
//		File outputFile = new File("d://new.png");
//		writeToFile(bitMatrix, format, outputFile);
		
		
//		MultiFormatReader formatReader = new MultiFormatReader(); 
//		String filePath = "d://new.png"; 
//		File file = new File(filePath); 
//		BufferedImage image = ImageIO.read(file);
//		LuminanceSource source = new ZxingImgUtil(image); 
//		Binarizer binarizer = new HybridBinarizer(source); 
//		BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer); 
//		Map rhints = new HashMap(); 
//		rhints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); 
//		Result result = formatReader.decode(binaryBitmap,rhints); 
//
//		System.out.println("result = "+ result.toString()); 
//		System.out.println("resultFormat = "+ result.getBarcodeFormat()); 
//		System.out.println("resultText = "+ result.getText()); 
	/*File filetemp = new File("http://www.wood168.net/adsimg/2014/v340257146.jpg"); 
		System.out.println(filetemp.getName());
		BufferedImage image1 = ImageIO.read(new java.net.URL("https://pipay.pingan.com/epcis_nps/getQRCodeImg.do?qRCode=https://pipay.pingan.com/epcis_nps/publicPay.do?payKey=4C7A4288E1855CC52FE05490E2BA930724////md5=de3ce7a56d2471f8ec2f66f50110b417////systemFlag=3302"));
		System.out.println(image1);*/
		System.out.println(decodeBinaryBitmap("https://pipay.pingan.com/epcis_nps/getQRCodeImg.do?qRCode=https://pipay.pingan.com/epcis_nps/publicPay.do?payKey=4C7A4288E1855CC52FE05490E2BA930724////md5=de3ce7a56d2471f8ec2f66f50110b417////systemFlag=3302", null, null));
	}
}
