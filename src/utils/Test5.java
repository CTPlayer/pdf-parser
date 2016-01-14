package utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class Test5 {
	public static void main(String[] args){
		FileUtils2 utils2 = new FileUtils2();
		int i;
		int k;
		Sheet sheet;
		Workbook book;
		Cell cell1,cell2,cell3,cell4;
		
		
	
		try {
			
			// 输出的excel的路径   
	        String filePath = "C:\\Users\\ct\\Downloads\\excel\\info.xls";
	        // 创建Excel工作薄   
	        WritableWorkbook wwb; 
	        // 新建立一个jxl文件  
	        OutputStream os = new FileOutputStream(filePath);   
	        wwb=Workbook.createWorkbook(os);   
	        // 添加第一个工作表并设置第一个Sheet的名字   
	        WritableSheet sheetname = wwb.createSheet("sheet1", 0);
			
			File file = new File("D:\\authors.xls");
			book = Workbook.getWorkbook(file);
			sheet = book.getSheet(0);
			
			i=0;
			k=0;
			while(true){
				cell1=sheet.getCell(0, i); //�ļ���
				cell2=sheet.getCell(1, i); //���ı���
				cell3=sheet.getCell(2, i); //��ַ
				cell4=sheet.getCell(4, i); //����
				if("".equals(cell1.getContents())==true)
					break;
				if(cell3.getContents().contains("ieee")){
					System.out.println(cell1.getContents());
					File pdfFile = new File("D:\\pdf2\\"+cell1.getContents()+".pdf");
					List<String> list = utils2.getInfo(pdfFile, cell2.getContents(),cell4.getContents());						
					
					if(list.size()>0){
					for(int j=0;j<list.size();j++){
						   Label label = new Label(j, k, list.get(j));  
					       sheetname.addCell(label);
					       
						System.out.println(list.get(j).replace("!", ""));
					}					
						k++;
					}
					System.out.println("\n");
				}
				i++;
			}
			wwb.write();
			wwb.close();
			book.close();
		} catch (Exception e){
			e.printStackTrace();
		}

	}
}
