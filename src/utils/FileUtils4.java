package utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;



/*
 * 生成Excel表格
 */
public class FileUtils4 {
	public 	static void writeExcel(String information,int i,int j) throws IOException, RowsExceededException, WriteException{
		// 输出的excel的路径   
        String filePath = "D:\\sentence\\info2.xls";
        // 创建Excel工作薄   
        WritableWorkbook wwb; 
        // 新建立一个jxl文件  
        OutputStream os = new FileOutputStream(filePath);   
        wwb=Workbook.createWorkbook(os);   
        // 添加第一个工作表并设置第一个Sheet的名字   
        WritableSheet sheet = wwb.createSheet("sheet1", 0);
        Label label = new Label(i, j, information);  
        sheet.addCell(label);
        wwb.write();
        wwb.close();
	}

	public static void main(String[] args) {
		try {
			FileUtils4.writeExcel("ct", 0, 0);
		} catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
