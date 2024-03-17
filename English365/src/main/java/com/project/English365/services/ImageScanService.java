package com.project.English365.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

@Service
public class ImageScanService {

	public String ocr(MultipartFile img){
		File convFile = new File(img.getOriginalFilename());
	    try {
	    	FileOutputStream fos = new FileOutputStream(convFile);
			fos.write(img.getBytes());
			fos.close();
			
			ITesseract image = new Tesseract();
			String words = image.doOCR(convFile);
			return words;
			
//			Pattern p = Pattern.compile("[a-zA-Z]{2}[0-9]{2}[a-zA-Z]{1,2}[0-9]{4}");
//			Matcher m = p.matcher(str);
//			while(m.find()) {
//	            number = m.group();
//	        }
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TesseractException e) {
			e.printStackTrace();
		}
	    return "No words found!";
	}
}
