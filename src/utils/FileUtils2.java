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

import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.util.PDFTextStripper;

public class FileUtils2 {
	public List<String> getInfo(File pdfFile,String title,String authors){
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
       try {
			InputStream is = new FileInputStream(textFile);
			InputStreamReader read = new InputStreamReader(is,"utf-8");
			String line;                                   //用来保存每行读取的内容
			BufferedReader reader = new BufferedReader(read);
			line = reader.readLine();            //读取第一行
			  while(line != null){                   //如果line为空则表示已读完
				  sb.append(line);                   //将读到的内容添加到buffer中
				  sb.append("!");
				  line = reader.readLine();      //读取下一行
			  }
			  reader.close();
	          read.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		/****************************提取buffer中的文件为字符串**********************************/
       String article = sb.toString(); //原始文章
      //System.out.println(article);
       /**********************处理作者************************/
       	  String[] author = authors.split(" and ");
       	  //String info="";
       	// String[] info = article.split("Abstract");
       	  
       	 // System.out.println(info[0]);
//     
       /************************提取信息**************************/
       //情况1:
//       if((info[0].length()-title.length()-authors.length())>130){
//    	   String[] infodetails = info[0].split("\\!");
//    	   //list.add(authors);
//    	   for(String s:infodetails){
//    		   list.add(s);
//    	   }
//      }
       //else{
       
       //情况2：
    	String p ="";
    	if(article.contains("Manuscript")&&article.contains("Identifier")){
    	   p = article.substring(article.indexOf("Manuscript"), article.indexOf("Identifier"));
    	}
       String[] s = p.split("\\.!");

       for(int i=1;i<s.length;i++){
    	   s[i] = s[i].replace("!", "");
    	  // System.out.println(s[i]);
    	   for(String singleauthor:author){     //每一个作者
    		   String[] word = singleauthor.split(", ");
    		   //int num2 = word.length-1;
    		  // System.out.println(word[num2]+"//"+word[0]);
    	   if(s[i].contains(word[0])&&s[i].indexOf(word[0])<70){      //判断包含该作者的姓，和名字的首字母缩写
    		   if(s[i].contains("e-mail")){
    			   String information =  s[i].substring(0, s[i].indexOf("e-mail")).replace("(", "");  	//拆分信息									   
    			   String[] details = information.split(",");     																									 
    			   int num3 = details.length-1;																														 
    			   list.add(singleauthor);																															
    			   list.add("unversity:"+details[num3-2]);
    			   list.add("address:"+details[num3-1]+details[num3]);
    			   if(s[i].endsWith(")")){
    				   list.add(s[i].substring(s[i].indexOf("(")+1,s[i].indexOf(")")));
    			   }else{
    				  if(s[i].contains("(")&&s[i].contains(")")){
    			   	  list.add((s[i].substring(s[i].indexOf("(")+1,s[i].length())+s[i+1]).replace(")", ""));	
    				  }else{
    					  list.add(s[i].substring(s[i].indexOf("e-mail")+1,s[i].length()));	
    				  }
    			   }
    		   }else{
       			   String[] details = s[i].split(",");     		   	//拆分信息																								 
    			   int num3 = details.length-1;																														 
    			   list.add(singleauthor);																															
    			   list.add("unversity:"+details[num3-2]);
    			   list.add("address:"+details[num3-1]+details[num3]);
    		   }
    	   }
       }
       }
       
       for(int i=0;i<s.length;i++){
    	  // System.out.println(s[i]);
    	   if(s[i].contains("The author")){
    		   if(s[i].contains("e-mail")){
    			   String information =  s[i].substring(0, s[i].indexOf("(e-mail")).replace("!", "");  	//拆分信息									   
    			   String[] details = information.split(",");     																									 
    			   int num3 = details.length-1;																														 
    			   list.add(authors);																															
    			   list.add("unversity:"+details[num3-2]);
    			   list.add("address:"+details[num3-1]+details[num3]);
    			   if(s[i].replace("!", "").contains(")")){
    				   list.add((s[i].substring(s[i].indexOf("e-mail"),s[i].length())).replace(")", ""));
    			   }else{
    			   	  list.add((s[i].substring(s[i].indexOf("e-mail"),s[i].length())+s[i+1]).replace(")", ""));			
    			   }
    		   }else{
    			   String[] details = s[i].split(",");     		   	//拆分信息																								 
    			   int num3 = details.length-1;																														 
    			   list.add(authors);																															
    			   list.add("unversity:"+details[num3-2]);
    			   list.add("address:"+details[num3-1]+details[num3]);
    		   }
    	   }
       }       
		return list;
	}
}
