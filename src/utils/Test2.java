package utils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class Test2 {
	public static void main(String[] args) {
		FileUtils utils = new FileUtils();
		int i;
		Sheet sheet;
		Workbook book;
		Cell cell1,cell2,cell3;
		
		
		try {
			File file = new File("C:\\Users\\ct\\Desktop\\note\\test2.xls");
			book = Workbook.getWorkbook(file);
			sheet = book.getSheet(0);
			
			i=0;
			while(true){
				cell1=sheet.getCell(0, i);
				cell2=sheet.getCell(1, i);
				cell3=sheet.getCell(2, i);
				if("".equals(cell1.getContents())==true)
					break;
				int score = utils.rankSentence(cell1.getContents(), cell2.getContents());
				System.out.println(cell1.getContents()+"\n"+cell2.getContents()+"\n"+cell3.getContents()+"\n"+score);
				i++;
			}
			book.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		  
//		FileUtils utils = new FileUtils();
//	        int score = utils.rankSentence("Other studies have examined the effect of mutual coupling on pattern characteristics for a variety of com-munications applications [8]�C[12]", "8");
//        System.out.println(score);

	}
}
