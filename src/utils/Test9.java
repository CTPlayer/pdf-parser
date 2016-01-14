package utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
/*
 * test of FileUtils5
 */
public class Test9 {

	
	public static void main(String[] args) {
		int i;
		int k;
		Sheet sheet;
		Workbook book;
		Cell cell1,cell2,cell3;
		
		
		try {
			
			// 输出的excel的路径   
	        String filePath = "C:\\Users\\ct\\Downloads\\excel\\infoFromJpier.xls";
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
			
			k=0;
			i=0;
			while(true){
				cell1 = sheet.getCell(0, i);
				cell2 = sheet.getCell(2, i);
				cell3 = sheet.getCell(1,i);
				if("".equals(cell1.getContents())==true)
					break;
				if(cell2.getContents().contains("jpier")){
					System.out.println(cell1.getContents());
					File pdfFile = new File("D:\\pdf2\\"+cell1.getContents()+".pdf");
					List<String> list = FileUtils5.FindFromJpier(pdfFile,cell3.getContents().replace("-", ""));		
					if(list.size()>0){
					String details[] = list.get(0).split("&");
					for(int j=0;j<details.length;j++){
						Label label = new Label(j,k,details[j]);
						sheetname.addCell(label);
						System.out.println(details[j]);
					}
					    k++;
					}
					System.out.println("\n");
				}
				i++;
			}
			wwb.write();
			wwb.close();
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
