package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.util.PDFTextStripper;

/*
 * from jpier
 */


public class FileUtils5 {
	public static List<String> FindFromJpier(File pdfFile,String title){
		ArrayList<String> list = new ArrayList<String>();
		/***************************************pdf转换为txt*******************************************/
		 //是否排序
       boolean sort = false;
       //pdf文件名
       String pdfFileName = pdfFile.getAbsolutePath();
       //输入文本文件名称
       String textFile = null;
       //编码方式
       String enconding = "utf-8";
       //开始提取页数
       int startPage = 1;
       //结束提取页数
       int endPage = Integer.MAX_VALUE;
       //文件输入流，生成文本文件
       Writer output = null;
       //内存中存储的PDF Document
       PDDocument document = null;
       try {    	
           document = PDDocument.load(pdfFile);
           if(pdfFileName.length()>4){
               textFile = pdfFileName.substring(0,pdfFileName.length()-4)+".txt";
           }
           // 文件输入流，写入到textFile
           output = new OutputStreamWriter(new FileOutputStream(textFile),enconding);
           //PDFTextStripper来提取文本
           PDFTextStripper stripper = new PDFTextStripper();
           // 设置是否排序
           stripper.setSortByPosition(sort);
           // 设置起始页
           stripper.setStartPage(startPage);
           // 设置结束页
           stripper.setEndPage(endPage);
           // 调用PDFTextStripper的writeText提取并输出文本
           stripper.writeText(document, output);
       
           if (output != null)  
           {  
               // 关闭输出流     
               output.close();  
           }  
           if (document != null)  
           {  
               // 关闭PDF Document     
               document.close();  
           }
           
       }catch (Throwable e){
           e.printStackTrace();
           
           if (output != null)  
           {  
               // 关闭输出流     
           	try {
               output.close();
           	} catch (Exception e2) {
           		
           	}
           }  
           if (document != null)  
           {  
               // 关闭PDF Document     
           	
               try {
					document.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}  
           }
       }
       
       /*********************************将txt文件读取到buffer中***************************************/
       StringBuffer sb = new StringBuffer();
       StringBuffer au = new StringBuffer();
       try {
			InputStream is = new FileInputStream(textFile);
			InputStreamReader read = new InputStreamReader(is,"utf-8");
			String line;                                   //用来保存每行读取的内容
			BufferedReader reader = new BufferedReader(read);
			line = reader.readLine();            //读取第一行
			  while(line != null){                   //如果line为空则表示已读完
				  if(line.contains("*")&&au.length()<2){      //找出pdf中的作者
					  au.append(line);
				  }
				  sb.append(line.replace("-", ""));                   //将读到的内容添加到buffer中
				  if(!line.endsWith("-")){
	                    sb.append(" ");
	                }
				  line = reader.readLine();      //读取下一行
			  }
			  reader.close();
	          read.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		/****************************提取buffer中的文件为字符串**********************************/
       String article = sb.toString(); //原始文章
       String authors = au.toString();//作者
       if(!authors.contains("and")){
    	   authors = authors +"and";
       }

       String articleBody = "";
       try{
       if(article.contains("Abstract")&&authors.length()>0){
    	   articleBody = article.substring(article.indexOf(authors)+authors.length(), article.indexOf("Abstract"));
       }       
       }catch(Exception e){
    	   e.printStackTrace();
       }
       /****************************提取信息**********************************/
       	String num = authors.substring(authors.indexOf("and"));
   		String regEx="[^0-9]";
   		Pattern p = Pattern.compile(regEx);   
   		Matcher m1 = p.matcher(num);  
   		num = m1.replaceAll("").trim();
   		String info;
   		
   		String authors2 = au.toString().replace("*", "").replace(",", "").replace("and ", "");
   		//System.out.println("num:"+num);
   		if(num.length()>0){
   		Integer maxNumber = Integer.parseInt(num);
   		for(int i=1;i<maxNumber;i++){
   			info = articleBody.substring(articleBody.indexOf(" "+String.valueOf(i))+2,articleBody.indexOf(" "+String.valueOf(i+1)));
   			authors2 = authors2.replace(String.valueOf(i), " "+info+"&");
   		}
   			authors2 = authors2.replace(num, " "+articleBody.substring(articleBody.indexOf(" "+String.valueOf(maxNumber))+2));
   			list.add(authors2);
   		}else{
   			list.add(article.substring(article.indexOf(title.toUpperCase())+title.length(),article.indexOf("Abstract")));
   		}
   	   			
       return list;
	}
}
