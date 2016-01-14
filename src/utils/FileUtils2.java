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
		/***************************************pdfת��Ϊtxt*******************************************/
		 //�Ƿ�����
       boolean sort = false;
       //pdf�ļ���
       String pdfFileName = pdfFile.getAbsolutePath();
       //�����ı��ļ�����
       String textFile = null;
       //���뷽ʽ
       String enconding = "utf-8";
       //��ʼ��ȡҳ��
       int startPage = 1;
       //������ȡҳ��
       int endPage = Integer.MAX_VALUE;
       //�ļ��������������ı��ļ�
       Writer output = null;
       //�ڴ��д洢��PDF Document
       PDDocument document = null;
       try {    	
           document = PDDocument.load(pdfFile);
           if(pdfFileName.length()>4){
               textFile = pdfFileName.substring(0,pdfFileName.length()-4)+".txt";
           }
           // �ļ���������д�뵽textFile
           output = new OutputStreamWriter(new FileOutputStream(textFile),enconding);
           //PDFTextStripper����ȡ�ı�
           PDFTextStripper stripper = new PDFTextStripper();
           // �����Ƿ�����
           stripper.setSortByPosition(sort);
           // ������ʼҳ
           stripper.setStartPage(startPage);
           // ���ý���ҳ
           stripper.setEndPage(endPage);
           // ����PDFTextStripper��writeText��ȡ������ı�
           stripper.writeText(document, output);
       
           if (output != null)  
           {  
               // �ر������     
               output.close();  
           }  
           if (document != null)  
           {  
               // �ر�PDF Document     
               document.close();  
           }
           
       }catch (Throwable e){
           e.printStackTrace();
           
           if (output != null)  
           {  
               // �ر������     
           	try {
               output.close();
           	} catch (Exception e2) {
           		
           	}
           }  
           if (document != null)  
           {  
               // �ر�PDF Document     
           	
               try {
					document.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}  
           }
       }
       /*********************************��txt�ļ���ȡ��buffer��***************************************/
       StringBuffer sb = new StringBuffer();
       try {
			InputStream is = new FileInputStream(textFile);
			InputStreamReader read = new InputStreamReader(is,"utf-8");
			String line;                                   //��������ÿ�ж�ȡ������
			BufferedReader reader = new BufferedReader(read);
			line = reader.readLine();            //��ȡ��һ��
			  while(line != null){                   //���lineΪ�����ʾ�Ѷ���
				  sb.append(line);                   //��������������ӵ�buffer��
				  sb.append("!");
				  line = reader.readLine();      //��ȡ��һ��
			  }
			  reader.close();
	          read.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		/****************************��ȡbuffer�е��ļ�Ϊ�ַ���**********************************/
       String article = sb.toString(); //ԭʼ����
      //System.out.println(article);
       /**********************��������************************/
       	  String[] author = authors.split(" and ");
       	  //String info="";
       	// String[] info = article.split("Abstract");
       	  
       	 // System.out.println(info[0]);
//     
       /************************��ȡ��Ϣ**************************/
       //���1:
//       if((info[0].length()-title.length()-authors.length())>130){
//    	   String[] infodetails = info[0].split("\\!");
//    	   //list.add(authors);
//    	   for(String s:infodetails){
//    		   list.add(s);
//    	   }
//      }
       //else{
       
       //���2��
    	String p ="";
    	if(article.contains("Manuscript")&&article.contains("Identifier")){
    	   p = article.substring(article.indexOf("Manuscript"), article.indexOf("Identifier"));
    	}
       String[] s = p.split("\\.!");

       for(int i=1;i<s.length;i++){
    	   s[i] = s[i].replace("!", "");
    	  // System.out.println(s[i]);
    	   for(String singleauthor:author){     //ÿһ������
    		   String[] word = singleauthor.split(", ");
    		   //int num2 = word.length-1;
    		  // System.out.println(word[num2]+"//"+word[0]);
    	   if(s[i].contains(word[0])&&s[i].indexOf(word[0])<70){      //�жϰ��������ߵ��գ������ֵ�����ĸ��д
    		   if(s[i].contains("e-mail")){
    			   String information =  s[i].substring(0, s[i].indexOf("e-mail")).replace("(", "");  	//�����Ϣ									   
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
       			   String[] details = s[i].split(",");     		   	//�����Ϣ																								 
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
    			   String information =  s[i].substring(0, s[i].indexOf("(e-mail")).replace("!", "");  	//�����Ϣ									   
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
    			   String[] details = s[i].split(",");     		   	//�����Ϣ																								 
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
