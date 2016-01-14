package utils;

import java.io.File;
import java.util.List;

public class Test {
	public static void main(String[] args) {
		File folder = new File("C:\\Users\\ct\\Downloads\\pdf");
        File[] files = folder.listFiles();

         for (File file : files) {
            if (file.isFile()) {
                String filename = file.getName();
                if (filename.length() > 3 && filename.substring(filename.length() - 3).equals("pdf")) {
                    try {
                        testExtractCitations(file);
                    } catch(Throwable e) {
                        System.out.println(file.getName() + ": " + "Error");
                        e.printStackTrace();
                    }
                }
            }
        }
	}
	
    public static void testExtractCitations(File file) {
    	FileUtils utils = new FileUtils();
        List<String> list = utils.extractCitations(file, "Antenna theory: analysis and design");        
        	System.out.println(file.getName() + ": " + list.size());
        	for(String sentence:list){
        		String num = sentence.substring(0, sentence.indexOf("?"));
        		sentence = sentence.substring(num.length()+1);
        		System.out.println(num+":"+sentence);
        	}
    }
}
