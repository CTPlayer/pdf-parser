package utils;

import java.io.File;
import java.util.List;

public class test3 {
	public static void main(String[] args) {
		FileUtils utils = new FileUtils();
		File file= new File("C:\\Users\\ct\\Downloads\\pdf\\_1u4nlUBf1kJ.pdf");
        List<String> list = utils.extractCitations(file, "Antenna theory: analysis and design");
        System.out.println(file.getName() + ": " + list.size());
	}
}
