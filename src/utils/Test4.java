package utils;

import java.io.File;
import java.util.List;

public class Test4 {
	public static void main(String[] args) {
		File pdfFile = new File("D:\\pdf2\\Emz2utPQ8ycJ.pdf");
		List<String> list = FileUtils5.FindFromJpier(pdfFile,"Diversity techniques with parallel dipole antennas: Radiation pattern analysis");					
		String details[] = list.get(0).split("&");
		for(int j=0;j<details.length;j++){
			System.out.println(details[j]);
		}
	}
}
