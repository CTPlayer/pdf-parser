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
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.util.PDFTextStripper;



//分析pdf文件
public  class FileUtils {
	public List<String> extractCitations(File pdfFile,String title){
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
        /*******************************提取引用部分和主体部分******************************************/
        String articlereference=article.substring(article.length()/3*2,article.length());   //论文引用部分
        String articleBody=article.substring(0,article.length()/3*2);          //论文主体部分
        if(article.contains("References")){
        	articlereference = article.substring(article.indexOf("References"));
            articleBody = article.substring(0,article.indexOf("References"));
        }
        if(article.contains("REFERENCES")){
        	articlereference = article.substring(article.indexOf("REFERENCES"));
        	articleBody = article.substring(0,article.indexOf("REFERENCES"));
        }
        if(article.contains("Bibliography")){
        	articlereference = article.substring(article.indexOf("Bibliography"));
            articleBody = article.substring(0,article.indexOf("Bibliography"));
        }
        /*********************************处理title****************************************/
        String[] titlewords = title.toLowerCase().split(" ");    
        for(int i=0;i<titlewords.length;i++){
        	titlewords[i] = titlewords[i].replaceAll("\\W", "");
        }
    
        //String[] titlewords = title.replaceAll("\\W", " ").toLowerCase().split(" ");
        /*********************处理articlereferences并输出标号************************/
        String articlereferencesmall = articlereference.toLowerCase();
        String num="没找到";
        //情况1：以[]划分
        String[] references = articlereferencesmall.split("\\[");      //引用部分切割        
        for(String reference:references){        	
        	//System.out.println(reference);
        	if(reference.contains("]")){        		
        	int flag=1;
        	for(int i=0;i<titlewords.length;i++){               //若完全包含则表明找到对应title
        		if(!reference.contains(titlewords[i])){           
        			flag=0;
        		}
        	}		       
        	if(flag==1){
        		String[] referencesplit = reference.split("\\]");                  //title在论文中的标号     
        		num = referencesplit[0];
        		if(!num.matches("^[0-9]*$")){												//情况一的特殊情况特殊处理
        		 	String newTitle="";
                	for(int j=0;j<titlewords.length;j++){               
                		newTitle = newTitle+" "+titlewords[j];
                	}
                	newTitle = newTitle.replaceFirst(" ", "");           
                		for(int i=articlereferencesmall.indexOf(newTitle);i>0;i--){
                			if(articlereferencesmall.substring(i, i+1).matches("^[0-9]*$")){
                				num = articlereferencesmall.substring(i-1, i+1);
                				String regEx="[^0-9]";
                				Pattern p = Pattern.compile(regEx);   
                				Matcher m1 = p.matcher(num);  
                				num =	m1.replaceAll("").trim();
                				break;
                			}
                		}
                		if(!num.matches("^[0-9]*$")){
                			for(int i=articlereferencesmall.indexOf(title.toLowerCase());i>0;i--){
                    			if(articlereferencesmall.substring(i, i+1).matches("^[0-9]*$")){
                    				num = articlereferencesmall.substring(i-1, i+1);
                    				String regEx="[^0-9]";
                    				Pattern p = Pattern.compile(regEx);   
                    				Matcher m1 = p.matcher(num);  
                    				num =	m1.replaceAll("").trim();
                    				break;
                    			}
                    		}
                		}
        		}
        	}
        	}
        }
        //情况二：单纯的是数字
        if(!articlereference.contains("[")&&!articlereference.contains("]")){      
               	String newTitle="";
            	for(int j=0;j<titlewords.length;j++){               
            		newTitle = newTitle+" "+titlewords[j];
            	}
            	newTitle = newTitle.replaceFirst(" ", "");           
            		for(int i=articlereferencesmall.indexOf(newTitle);i>0;i--){
            			if(articlereferencesmall.substring(i, i+1).matches("^[0-9]*$")){
            				num = articlereferencesmall.substring(i-1, i+1);
            				String regEx="[^0-9]";
            				Pattern p = Pattern.compile(regEx);   
            				Matcher m1 = p.matcher(num);  
            				num =	m1.replaceAll("").trim();
            				break;
            			}
            		}
            		if(num=="没找到"){
            			for(int i=articlereferencesmall.indexOf(title.toLowerCase());i>0;i--){
                			if(articlereferencesmall.substring(i, i+1).matches("^[0-9]*$")){
                				num = articlereferencesmall.substring(i-1, i+1);
                				String regEx="[^0-9]";
                				Pattern p = Pattern.compile(regEx);   
                				Matcher m1 = p.matcher(num);  
                				num =	m1.replaceAll("").trim();
                				break;
                			}
                		}
            		}
        	}
        
        if(num=="没找到"){
        	for(int i=articlereferencesmall.indexOf((titlewords[0]+titlewords[1]).toLowerCase());i>0;i--){
        		if(articlereferencesmall.substring(i, i+1).matches("^[0-9]*$")){
    				num = articlereferencesmall.substring(i-1, i+1);
    				String regEx="[^0-9]";
    				Pattern p = Pattern.compile(regEx);   
    				Matcher m1 = p.matcher(num);  
    				num =	m1.replaceAll("").trim();
    				break;
    			}
        	}
        }
               
        if(num=="没找到"){
        	 Random random = new Random();
        	 num = String.valueOf(random.nextInt(10)+1);
        }

        /**************************根据num找引用句********************************/
        articleBody = articleBody.replace("Fig. ", "Fig.");
        articleBody = articleBody.replace("VOL. ", "VOL.");
        articleBody = articleBody.replace("NO. ", "NO.");
        articleBody = articleBody.replace("e.g. ", "e.g.");
        articleBody = articleBody.replace("i.e. ", "i.e.");
        articleBody = articleBody.replace("etc. ", "etc.");
        articleBody = articleBody.replace("et al. ", "et al.");
        String[] sentences = articleBody.split("\\. ");
        for(String sentence:sentences){
        	//情况1：
        	if(sentence.contains("["+num+"]")){
        		list.add(num+"?"+sentence);
        	}else if(sentence.contains("["+num+",")||sentence.contains("["+num+" ,")){
        		list.add(num+"?"+sentence);
        	}else if(sentence.contains(","+num+"]")||sentence.contains(", "+num+"]")){
        		list.add(num+"?"+sentence);
        	}
        	//情况2：获得‘]-[’两边的数字与上面得到的编号比较
        	if(sentence.contains("]-[")){
        		//int number = Integer.parseInt(num);
        		sentence = "  "+sentence + "               ";//防止下标越界
        		String number1 = sentence.substring(sentence.indexOf("]-[")-3, sentence.indexOf("]-["));
        		String number2 = sentence.substring(sentence.indexOf("]-[")+3, sentence.indexOf("]-[")+6);
        		String regEx="[^0-9]"; 
        		Pattern p = Pattern.compile(regEx);   
        		Matcher m1 = p.matcher(number1);  
        		Matcher m2 = p.matcher(number2);  
        		Matcher m3 = p.matcher(num);
        		int number = Integer.parseInt(m3.replaceAll("").trim());
        		int num1 =Integer.parseInt(m1.replaceAll("").trim());       
        		int num2 =Integer.parseInt(m2.replaceAll("").trim());           		
        		if(num2>number&&number>num1){
        			list.add(num+"?"+sentence);
        		}
        	}
        	if(sentence.contains("]–[")){
        		//int number = Integer.parseInt(num);
        		sentence = "  "+sentence + "               ";//防止下标越界       		
        		String number1 = sentence.substring(sentence.indexOf("]–[")-3, sentence.indexOf("]–["));
        		//System.out.println(sentence);
        		String number2 = sentence.substring(sentence.indexOf("]–[")+3,sentence.indexOf("]–[")+6);
        		String regEx="[^0-9]"; 
        		Pattern p = Pattern.compile(regEx);   
        		Matcher m1 = p.matcher(number1);  
        		Matcher m2 = p.matcher(number2); 
        		Matcher m3 = p.matcher(num);
        		int number = Integer.parseInt(m3.replaceAll("").trim());
        		int num1 =Integer.parseInt(m1.replaceAll("").trim());       
        		int num2 =Integer.parseInt(m2.replaceAll("").trim());   
        		if(num2>number&&number>num1){
        			list.add(num+"?"+sentence);
        		}
        	}
        	//情况三：获得[ - ]之间的的数字与上面的num比较
        	if(sentence.contains("["+"^[0-9]*$"+"-"+"^[0-9]*$"+"]")){
        		//int number = Integer.parseInt(num);
        		String number1 = sentence.substring(sentence.indexOf("["),sentence.indexOf("-"));
        		String number2 = sentence.substring(sentence.indexOf("-"),sentence.indexOf("]"));
        		String regEx="[^0-9]"; 
        		Pattern p = Pattern.compile(regEx); 
        		Matcher m1 = p.matcher(number1);  
        		Matcher m2 = p.matcher(number2); 
        		Matcher m3 = p.matcher(num);
        		int number = Integer.parseInt(m3.replaceAll("").trim());
        		int num1 =Integer.parseInt(m1.replaceAll("").trim());       
        		int num2 =Integer.parseInt(m2.replaceAll("").trim());   
        		if(num2>number&&number>num1){
        			list.add(num+"?"+sentence);
        		}
        	}
        	if(sentence.contains("["+"^[0-9]*$"+"–"+"^[0-9]*$"+"]")){
        		//int number = Integer.parseInt(num);
        		String number1 = sentence.substring(sentence.indexOf("["),sentence.indexOf("-"));
        		String number2 = sentence.substring(sentence.indexOf("-"),sentence.indexOf("]"));
        		String regEx="[^0-9]"; 
        		Pattern p = Pattern.compile(regEx); 
        		Matcher m1 = p.matcher(number1);  
        		Matcher m2 = p.matcher(number2); 
        		Matcher m3 = p.matcher(num);
        		int number = Integer.parseInt(m3.replaceAll("").trim());
        		int num1 =Integer.parseInt(m1.replaceAll("").trim());       
        		int num2 =Integer.parseInt(m2.replaceAll("").trim());   
        		if(num2>number&&number>num1){
        			list.add(num+"?"+sentence);
        		}
        	}	
        }
      //情况四：
        for(String sentence:sentences){
    	if(list.size()==0){
    	if(sentence.contains("("+num+")")){
    		list.add(num+"?"+sentence);
    		}
    		}
        }
       // System.out.println("num:"+num);
		return list;
	}
	
	 public int rankSentence(String citationSentence,String num){
		 String[] rebuildSentence = new String[2];
		 String[] rebuildwords = {"Nevertheless","in other words","Nonetheless","on the contrary","otherwise","still","incidentally",
				 "meanwhile","hence","as a result","after all","as far as","as if","besides","futhermore","moreover","indeed","then",
		 		"thus","Therefore","next","finally","Fig","that","which","however","yet","rather than","not only","but also","whether",
		 		"either","because","since","what","infact","although","as though","even if","before","after","in case","no Matter How",
		 		"provided","supposing","till","untill","when","whenever","where","wherever","while","why"};
		 //-1
		 String[] keywords1 = {"on the other hand","instead of","instead","for example","like","likewise","in addition","but","such as","], ["};
		 //5
		 String[] keywords2 = {"prep","Deusterwald et al.","due to","like","unlike","since","Due to","In","in","from","By","by","From","Reference",
				 "are defined as","detector","similar to that of","counterpart of","used"};
		 //+1
		 String[] keywords3 = {"verb","compare","compares","compared","measure","measured","measures","etch","etched","etches",
				 "fabricate","fabricates","fabricated","develop","develops","developed","and earlier  researches recommend",
				 "investigate","investigates","investigated","improve","improves","improved","report","reports","reported","design","designs",
				 "designed","excellent","accurate","novel","successful","excellently","accurately","successfully","experimental","experimental results",
				 "other types","the first work","propose","proposed by","proposed","proposes","function","functions","equation",
				 "equations","as follows","estimated as","defined by","expressed by","formula","according to","derived as","fig","Fig",
				 "Dotted line","dotted line","Dashed line","ashed line","Solid line","solid line","formulation","data","value","values","] to be used"};
		 //+2
		 String[] keywords4 = {"produced by","presented by", "obtained by", "proposed by", "generated by","created by","provide",
				 "provides","provided","present","presents","presented","by using","given by","method of","The papers","model [","avatars of [","methods [",
		 			"the original work of Schoenberg","published","fig.","Fig.","showed","made by"};
		 //-2
		 String[] keywords5 = {"widely studied","demonstrated","^[0-9]*$"+"-"+"^[0-9]*$"};
		 for(int i=0;i<rebuildwords.length;i++){
			 if(citationSentence.contains(rebuildwords[i])){
				  rebuildSentence = citationSentence.split(rebuildwords[i]);
				 break;
			 }
		 }
		 int score = 3;
		 
		 if(rebuildSentence[1]==null){
				for(int i=0;i<keywords1.length;i++){
					if(citationSentence.contains(keywords1[i])&&score>1){
						score = score -1;
					}
				}
				for(int i=0;i<keywords2.length;i++){
					if(citationSentence.contains(keywords2[i]+" [")){
						score = 5;
					}
				}
				for(int i=0;i<keywords3.length;i++){
					if(citationSentence.contains(keywords3[i])&&score<5){
						score = score + 1;
						break;
					}
				}
				for(int i=0;i<keywords4.length;i++){
					if(citationSentence.contains(keywords4[i])&&score<4){
						score = score + 2;
						break;
					}
				}
				for(int i=0;i<keywords5.length;i++){
					if(citationSentence.contains(keywords5[i])&&score>2){
						score = score-2;
						break;
					}
					if(citationSentence.contains(num+"]-[")&&score>2||(citationSentence.contains("]-["+num)&&score>2)){
						score = score-2;
						break;
					}
				}
			} 
		 
		 if(rebuildSentence[1]!=null){
			 if(rebuildSentence[0].contains(num)){				 
					for(int i=0;i<keywords1.length;i++){
						if(rebuildSentence[0].contains(keywords1[i])&&score>1){
							score = score -1;
						}
					}
					for(int i=0;i<keywords2.length;i++){
						if(rebuildSentence[0].contains(keywords2[i]+" [")){
							 score = 5;
						}
					}
					for(int i=0;i<keywords3.length;i++){
						if(rebuildSentence[0].contains(keywords3[i])&&score<5){
							score = score + 1;
							break;
						}
					}
					for(int i=0;i<keywords4.length;i++){
						if(rebuildSentence[0].contains(keywords4[i])&&score<4){
							score = score +2;
							break;
						}
					}
					for(int i=0;i<keywords5.length;i++){
						if(citationSentence.contains(keywords5[i])&&score>2){
							score = score-2;
							break;
						}
						if(citationSentence.contains(num+"]-[")&&score>2||(citationSentence.contains("]-["+num)&&score>2)){
							score = score-2;
							break;
						}
					}
				}
			 if(rebuildSentence[0].contains("]-[")){
	        		int number = Integer.parseInt(num);
	        		rebuildSentence[0] = "  "+rebuildSentence[0] + "               ";//防止下标越界
	        		String number1 = rebuildSentence[0].substring(rebuildSentence[0].indexOf("]-[")-3, rebuildSentence[0].indexOf("]-["));
	        		String number2 = rebuildSentence[0].substring(rebuildSentence[0].indexOf("]-[")+3, rebuildSentence[0].indexOf("]-[")+6);
	        		String regEx="[^0-9]"; 
	        		Pattern p = Pattern.compile(regEx);   
	        		Matcher m1 = p.matcher(number1);  
	        		Matcher m2 = p.matcher(number2);  
	        		int num1 =Integer.parseInt(m1.replaceAll("").trim());       
	        		int num2 =Integer.parseInt(m2.replaceAll("").trim());   
	        		if(num2>number&&number>num1){
	        			for(int i=0;i<keywords1.length;i++){
							if(rebuildSentence[0].contains(keywords1[i])&&score>1){
								score = score -1;
							}
						}
						for(int i=0;i<keywords2.length;i++){
							if(rebuildSentence[0].contains(keywords2[i]+" [")){
								score = 5;
							}
						}
						for(int i=0;i<keywords3.length;i++){
							if(rebuildSentence[0].contains(keywords3[i])&&score<5){
								score = score + 1;
								break;
							}
						}
						for(int i=0;i<keywords4.length;i++){
							if(rebuildSentence[0].contains(keywords4[i])&&score<4){
								score = score +2;
								break;
							}
						}
						for(int i=0;i<keywords5.length;i++){
							if(citationSentence.contains(keywords5[i])&&score>2){
								score = score-2;
								break;
							}
							if(citationSentence.contains(num+"]-[")&&score>2||(citationSentence.contains("]-["+num)&&score>2)){
								score = score-2;
								break;
							}
						}
	        		}
	        	}			 
			 if(rebuildSentence[0].contains("["+"[^0-9]"+"-"+"[^0-9]"+"]")){
	        		int number = Integer.parseInt(num);
	        		String number1 = rebuildSentence[0].substring(rebuildSentence[0].indexOf("["),rebuildSentence[0].indexOf("-"));
	        		String number2 = rebuildSentence[0].substring(rebuildSentence[0].indexOf("-"),rebuildSentence[0].indexOf("]"));
	        		String regEx="[^0-9]"; 
	        		Pattern p = Pattern.compile(regEx); 
	        		Matcher m1 = p.matcher(number1);  
	        		Matcher m2 = p.matcher(number2); 
	        		int num1 =Integer.parseInt(m1.replaceAll("").trim());       
	        		int num2 =Integer.parseInt(m2.replaceAll("").trim());   
	        		if(num2>number&&number>num1){
	        			for(int i=0;i<keywords1.length;i++){
							if(rebuildSentence[0].contains(keywords1[i])&&score>1){
								score = score -1;
							}
						}
						for(int i=0;i<keywords2.length;i++){
							if(rebuildSentence[0].contains(keywords2[i]+" [")){
								score = 5;
							}
						}
						for(int i=0;i<keywords3.length;i++){
							if(rebuildSentence[0].contains(keywords3[i])&&score<5){
								score = score + 1;
								break;
							}
						}
						for(int i=0;i<keywords4.length;i++){
							if(rebuildSentence[0].contains(keywords4[i])&&score<4){
								score = score +2;
								break;
							}
						}
						for(int i=0;i<keywords5.length;i++){
							if(citationSentence.contains(keywords5[i])&&score>2){
								score = score-2;
								break;
							}
							if((citationSentence.contains(num+"]-[")&&score>2)||(citationSentence.contains("]-["+num)&&score>2)){
								score = score-2;
								break;
							}
						}
	        		}
	        	}
			 //....
			 
			 if(rebuildSentence[1].contains(num)){
					for(int i=0;i<keywords1.length;i++){
						if(rebuildSentence[1].contains(keywords1[i])&&score>1){
							score = score -1;
						}
					}
					for(int i=0;i<keywords2.length;i++){
						if(rebuildSentence[1].contains(keywords2[i]+" [")){
							score = 5;
						}
					}
					for(int i=0;i<keywords3.length;i++){
						if(rebuildSentence[1].contains(keywords3[i])&&score<5){
							score = score + 1;
							break;
						}
					}
					for(int i=0;i<keywords4.length;i++){
						if(rebuildSentence[1].contains(keywords4[i])&&score<4){
							score = score + 2;
							break;
						}
					}
					for(int i=0;i<keywords5.length;i++){
						if(citationSentence.contains(keywords5[i])&&score>2){
							score = score-2;
							break;
						}
						if(citationSentence.contains(num+"]-[")&&score>2||(citationSentence.contains("]-["+num)&&score>2)){
							score = score-2;
							break;
						}
					}
				}
			 if(rebuildSentence[1].contains("]-[")){
				 System.out.println(rebuildSentence[1]);
	        		int number = Integer.parseInt(num);
	        		rebuildSentence[1] = "  "+rebuildSentence[1] + "               ";//防止下标越界
	        		String number1 = rebuildSentence[1].substring(rebuildSentence[1].indexOf("]-[")-3, rebuildSentence[1].indexOf("]-["));
	        		String number2 = rebuildSentence[1].substring(rebuildSentence[1].indexOf("]-[")+3, rebuildSentence[1].indexOf("]-[")+6);
	        		String regEx="[^0-9]"; 
	        		Pattern p = Pattern.compile(regEx);   
	        		Matcher m1 = p.matcher(number1);  
	        		Matcher m2 = p.matcher(number2);  
	        		int num1 =Integer.parseInt(m1.replaceAll("").trim());       
	        		int num2 =Integer.parseInt(m2.replaceAll("").trim());   
	        		if(num2>number&&number>num1){
	        			for(int i=0;i<keywords1.length;i++){
							if(rebuildSentence[0].contains(keywords1[i])&&score>1){
								score = score -1;
							}
						}
						for(int i=0;i<keywords2.length;i++){
							if(rebuildSentence[0].contains(keywords2[i]+" [")){
								score = 5;
							}
						}
						for(int i=0;i<keywords3.length;i++){
							if(rebuildSentence[0].contains(keywords3[i])&&score<5){
								score = score + 1;
								break;
							}
						}
						for(int i=0;i<keywords4.length;i++){
							if(rebuildSentence[0].contains(keywords4[i])&&score<4){
								score = score +2;
								break;
							}
						}
						for(int i=0;i<keywords5.length;i++){
							if(citationSentence.contains(keywords5[i])&&score>2){
								score = score-2;
								break;
							}
							if(citationSentence.contains(num+"]-[")&&score>2||(citationSentence.contains("]-["+num)&&score>2)){
								score = score-2;
								break;
							}
						}
	        		}
	        	}
			 if(rebuildSentence[1].contains("["+"[^0-9]"+"-"+"[^0-9]"+"]")){
	        		int number = Integer.parseInt(num);
	        		String number1 = rebuildSentence[1].substring(rebuildSentence[1].indexOf("["),rebuildSentence[1].indexOf("-"));
	        		String number2 = rebuildSentence[1].substring(rebuildSentence[1].indexOf("-"),rebuildSentence[1].indexOf("]"));
	        		String regEx="[^0-9]"; 
	        		Pattern p = Pattern.compile(regEx); 
	        		Matcher m1 = p.matcher(number1);  
	        		Matcher m2 = p.matcher(number2); 
	        		int num1 =Integer.parseInt(m1.replaceAll("").trim());       
	        		int num2 =Integer.parseInt(m2.replaceAll("").trim());   
	        		if(num2>number&&number>num1){
	        			for(int i=0;i<keywords1.length;i++){
							if(rebuildSentence[1].contains(keywords1[i])&&score>1){
								score = score -1;
							}
						}
						for(int i=0;i<keywords2.length;i++){
							if(rebuildSentence[1].contains(keywords2[i]+" [")){
								score = 5;
							}
						}
						for(int i=0;i<keywords3.length;i++){
							if(rebuildSentence[1].contains(keywords3[i])&&score<5){
								score = score + 1;
								break;
							}
						}
						for(int i=0;i<keywords4.length;i++){
							if(rebuildSentence[1].contains(keywords4[i])&&score<4){
								score = score +2;
								break;
							}
						}
						for(int i=0;i<keywords5.length;i++){
							if(citationSentence.contains(keywords5[i])&&score>2){
								score = score-2;
								break;
							}
							if(citationSentence.contains(num+"]-[")&&score>2||(citationSentence.contains("]-["+num)&&score>2)){
								score = score-2;
								break;
							}
						}
	        		}
	        	}
		 }
	 
	 return score;
	 	}
	}