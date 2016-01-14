package utils;

import java.io.File;
import java.util.List;

public class Test6 {
	public static void main(String[] args) {
		FileUtils2 utils2 = new FileUtils2();
		File file = new File("D:\\pdf2\\UoSTiEWoNbwJ.pdf");
		
		try{
		List<String> list = utils2.getInfo(file, "A survey of indoor positioning systems for wireless personal networks",
				"Gu, Yanying and Lo, Anthony and Niemegeers, Ignas");
		for(String info:list){
			System.out.println(info);
		}
		}catch(Exception e){
			System.out.println("≥ˆœ÷“Ï≥£¿≤");
			e.printStackTrace();
		}
	}
}
