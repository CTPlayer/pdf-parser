package utils;

import java.io.File;
import java.util.List;

public class Test7 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FileUtils3 utils3 = new FileUtils3();
		File file = new File("D:\\pdf2\\lMfIwGhqoHEJ.pdf");
		List<String> list = utils3.findIntroduce(file,"Warnock, James D and Keaty, John M and Petrovick, John and Clabes, Joachim G and Kircher, Charles J and Krauter, Byron L and Restle, Phillip J and Zoric, Brian A and Anderson, Carl J");
		for(String introduce:list){
			System.out.println(introduce);
		}
	}

}
