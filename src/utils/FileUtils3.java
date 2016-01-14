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

public class FileUtils3 {
	public List<String> findIntroduce(File pdfFile,String authors){
		ArrayList<String> list = new ArrayList<String>();
		/***************************************pdfת��Ϊtxt*******************************************/
		 //�Ƿ�����
      boolean sort = false;
      //pdf�ļ���
      String pdfFileName = pdfFile.getAbsolutePath();
      //�����ı��ļ����
      String textFile = null;
      //���뷽ʽ
      String enconding = "utf-8";
      //��ʼ��ȡҳ��
      int startPage = 1;
      //������ȡҳ��
      int endPage = Integer.MAX_VALUE;
      //�ļ�������������ı��ļ�
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
				  if(!line.endsWith("-")){
	                    sb.append(" ");
	                }
				  line = reader.readLine();      //��ȡ��һ��
			  }
			  reader.close();
	          read.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		/****************************��ȡbuffer�е��ļ�Ϊ�ַ�**********************************/
      String article = sb.toString(); //ԭʼ����
      String articlefoot = "";
      if(article.contains("REFERENCES")){
    	  articlefoot = article.substring(article.indexOf("REFERENCES"));
      }
      if(article.contains("References")){
    	  articlefoot = article.substring(article.indexOf("References"));
      }
      /****************************��������**********************************/
      String[] author = authors.split(" and ");
      for(int i=0;i<author.length;i++){
    	  String[] temp = author[i].split(", ");
    	  if(temp.length>1){
//    		  String[] mid = temp[1].split(" ");
//    		  if(mid.length>1){
//    			  author[i]=temp[1]+". "+temp[0];
//    		  }else{
    			  author[i] = temp[1]+" "+temp[0];
//    		  }
    	  }
      }
      /*********************��ȡ������Ϣ***************************/
     int flag=0;
     for(String a:author){
    	 if(!articlefoot.toLowerCase().contains(a.toLowerCase())){
    		 flag=1;
    	 }
     }    
     if(flag==0){
    	 articlefoot = articlefoot.toLowerCase();
    	 for(int i=0;i<author.length-1;i++){
       		 author[i]=author[i].toLowerCase();
    		 if(articlefoot.indexOf(author[i+1].toLowerCase())>articlefoot.indexOf(author[i])){
    			 list.add(articlefoot.substring(articlefoot.indexOf(author[i]), articlefoot.indexOf(author[i+1].toLowerCase())));
    		 }
    	 }
    	 	list.add(articlefoot.substring(articlefoot.indexOf(author[author.length-1].toLowerCase())));
     }
     
     if(list.size()==0){
    	 String[] author2 = authors.split(" and ");
         for(int i=0;i<author2.length;i++){
       	  String[] temp = author2[i].split(", ");

       	  if(temp.length>1){
       		  author2[i] = temp[1]+" "+temp[0];
       		  String[] mid = temp[1].split(" ");
       		  if(mid.length>1){
       			  author2[i]=temp[1]+". "+temp[0];
       			 // System.out.println(author2[i]);
       		  }
       	  }
         }
         int tag=0;
         for(String a:author2){
        	 if(!articlefoot.toLowerCase().contains(a.toLowerCase())){
        		 tag=1;
        	 }
         }
   //System.out.println(tag);
         if(tag==0){
    		 articlefoot = articlefoot.toLowerCase();
        	 for(int i=0;i<author2.length-1;i++){
        		 author2[i]=author2[i].toLowerCase();
        		 if(articlefoot.indexOf(author2[i+1].toLowerCase())>articlefoot.indexOf(author2[i])){
        			 list.add(articlefoot.substring(articlefoot.indexOf(author2[i]), articlefoot.indexOf(author2[i+1].toLowerCase())));
        		 }
        	 }
        	 	list.add(articlefoot.substring(articlefoot.indexOf(author2[author2.length-1].toLowerCase())));
         }
     }
		return list;
	}
}
