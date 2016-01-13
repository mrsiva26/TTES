package analyser;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Noun {
	static String inflectednoun;
	public static String root;
	static SegmentedWord sw;

	private static int getnoofஎழுத்து(String str){
		ArrayList<String> characters=new ArrayList<String>();
		Pattern pat=Pattern.compile("\\p{L}\\p{M}*");
		Matcher matcher=pat.matcher(str);
		while(matcher.find())
		{
			characters.add(matcher.group());
		}
		return characters.size();
	}

	public static SegmentedWord analyse(String s){
		inflectednoun=s;
		sw = new SegmentedWord();
		sw.POS = "object";
		sw.POSId = 1;
		Pattern pat;
		Matcher matcher;
		Nedil ned = new Nedil();

		//type=1;
		pat = Pattern.compile("(லை|லால்|லிற்கு|லிற்குள்|லுக்கு|லுக்குள்|லில்|லின்|லினது|ல்கள்|ல்களால்|ற்கள்|ற்களால்|லின்கண்)$");
		matcher = pat.matcher(inflectednoun); 
		if(matcher.find()){
			String[] splitString = inflectednoun.split("(லை|லால்|லிற்கு|லிற்குள்|லுக்கு|லுக்குள்|லில்|லின்|லினது|ல்கள்|ல்களால்|ற்கள்|ற்களால்|லின்கண்)$");
			root = splitString[0];
			String tempSuffix = inflectednoun.replace(splitString[0], "");
			System.out.println("\n"+root + "\t"+getnoofஎழுத்து(root)+"\t" + tempSuffix);

			if(ned.isNedil(root)==true){
				//{"காலை","காலால்","காலுக்கு","காலில்","காலின்","காலினது","காலின்கண்"};
				System.out.println("IamNedil");
				root = root+"ல்";
				switch(tempSuffix){
				case "லை": sw.nounSuffix="ஐ"; sw.nounSuffixId=1;
				break;
				case "லால்": sw.nounSuffix="ஆல்"; sw.nounSuffixId=2;
				break;
				case "லிற்குள்":
				case "லுக்குள்": sw.nounSuffix="உள்"; sw.nounSuffixId=10;
				break;
				case "லிற்கு":
				case "லுக்கு": sw.nounSuffix="கு"; sw.nounSuffixId=3;
				break;
				case "லில்": sw.nounSuffix="இல்"; sw.nounSuffixId=4;
				break;
				case "லின்": sw.nounSuffix="இன்"; sw.nounSuffixId=5;
				break;
				case "லினது": sw.nounSuffix="அது"; sw.nounSuffixId=6;
				break;
				case "ற்கள்": sw.nounSuffix="கள்"; sw.nounSuffixId=7;
				break;
				case "ற்களால்": sw.nounSuffix="களால்"; sw.nounSuffixId=8;
				break;
				case "லின்கண்": sw.nounSuffix="கண்"; sw.nounSuffixId=9;
				break;
				default: System.out.println("Exception from காலை, காலால், காலுக்கு, காலில், காலின், காலினது, காலின்கண்");
				}
				sw.rootWord=root;
				System.out.println("Print from காலை, காலால், காலுக்கு, காலில், காலின், காலினது, காலின்கண்" +"\n"+ inflectednoun +"\n"+ "POS :"+sw.POS +"\n"+ "POSid :"+sw.POSId +"\n"+ "rootword: "+sw.rootWord +"\n"+ "NounSuffix :"+sw.nounSuffix +"\n"+ "NounSuffixid :"+sw.nounSuffixId);
				return sw;	
			}else if(root.endsWith("ல்")){
				//{"சொல்லை","சொல்லால்","சொல்லுக்கு","சொல்லின்","சொல்லில்","சொல்லினது","சொல்ல்ங்கண்","சொற்கள்"}
				System.out.println("IamNotNedil");
				switch(tempSuffix){
				case "லை": sw.nounSuffix="ஐ"; sw.nounSuffixId=1;
				break;
				case "லால்": sw.nounSuffix="ஆல்"; sw.nounSuffixId=2;
				break;
				case "லிற்குள்":
				case "லுக்குள்": sw.nounSuffix="உள்"; sw.nounSuffixId=10;
				break;
				case "லிற்கு":
				case "லுக்கு": sw.nounSuffix="கு"; sw.nounSuffixId=3;
				break;
				case "லில்": sw.nounSuffix="இல்"; sw.nounSuffixId=4;
				break;
				case "லின்": sw.nounSuffix="இன்"; sw.nounSuffixId=5;
				break;
				case "லினது": sw.nounSuffix="அது"; sw.nounSuffixId=6;
				break;
				case "ற்கள்": root = root + "ல்"; sw.nounSuffix="கள்"; sw.nounSuffixId=7;
				break;
				case "ற்களால்": root = root + "ல்"; sw.nounSuffix="களால்"; sw.nounSuffixId=8;
				break;
				case "லின்கண்": sw.nounSuffix="கண்"; sw.nounSuffixId=9;
				break;
				default: System.out.println("Exception from சொல்லை, சொல்லால், சொல்லுக்கு, சொல்லின், சொல்லில், சொல்லினது, சொல்ல்ங்கண், சொற்கள்");
				}
				sw.rootWord=root;
				System.out.println("Print from சொல்லை, சொல்லால், சொல்லுக்கு, சொல்லின், சொல்லில், சொல்லினது, சொல்ல்ங்கண், சொற்கள்" +"\n"+ inflectednoun +"\n"+ "POS :"+sw.POS +"\n"+ "POSid :"+sw.POSId +"\n"+ "rootword: "+sw.rootWord +"\n"+ "NounSuffix :"+sw.nounSuffix +"\n"+ "NounSuffixid :"+sw.nounSuffixId);
				return sw;	
			}else{
				//{"கப்பலை","கப்பலால்","கப்பலுக்கு","கப்பலின்","கப்பலில்","கப்பலினது","கப்பலிங்கண்"};
				root = root + "ல்";
				switch(tempSuffix){
				case "லை": sw.nounSuffix="ஐ"; sw.nounSuffixId=1;
				break;
				case "லால்": sw.nounSuffix="ஆல்"; sw.nounSuffixId=2;
				break;
				case "லிற்குள்":
				case "லுக்குள்": sw.nounSuffix="உள்"; sw.nounSuffixId=10;
				break;
				case "லிற்கு":
				case "லுக்கு": sw.nounSuffix="கு"; sw.nounSuffixId=3;
				break;
				case "லில்": sw.nounSuffix="இல்"; sw.nounSuffixId=4;
				break;
				case "லின்": sw.nounSuffix="இன்"; sw.nounSuffixId=5;
				break;
				case "லினது": sw.nounSuffix="அது"; sw.nounSuffixId=6;
				break;
				case "ல்கள்": sw.nounSuffix="கள்"; sw.nounSuffixId=7;
				break;
				case "ல்களால்": sw.nounSuffix="களால்"; sw.nounSuffixId=8;
				break;
				case "லின்கண்": sw.nounSuffix="கண்"; sw.nounSuffixId=9;
				break;
				default: System.out.println("Exception from கப்பலை, கப்பலால், கப்பலுக்கு, கப்பலின், கப்பலில், கப்பலினது, கப்பலிங்கண்");
				}
				sw.rootWord=root;
				System.out.println("Print from கப்பலை, கப்பலால், கப்பலுக்கு, கப்பலின், கப்பலில், கப்பலினது, கப்பலிங்கண்" +"\n"+ inflectednoun +"\n"+ "POS :"+sw.POS +"\n"+ "POSid :"+sw.POSId +"\n"+ "rootword: "+sw.rootWord +"\n"+ "NounSuffix :"+sw.nounSuffix +"\n"+ "NounSuffixid :"+sw.nounSuffixId);
				return sw;				
			}
		}

		//type=2;
		pat = Pattern.compile("(கை|கால்|கிற்குள்|குக்குள்|கிற்கு|குக்கு|கில்|கின்|கினது|குகள்|குகளாள்|கின்கண்)$");
		matcher = pat.matcher(inflectednoun); 
		if(matcher.find()){
			String[] splitString = inflectednoun.split("(கை|கால்|கிற்குள்|குக்குள்|கிற்கு|குக்கு|கில்|கின்|கினது|குகள்|குகளாள்|கின்கண்)$");

			root = splitString[0];

			String tempSuffix = inflectednoun.replace(splitString[0], "");
			System.out.println("\n"+root + "\t"+getnoofஎழுத்து(root)+"\t" + tempSuffix);
			switch(tempSuffix){
			case "கை": sw.nounSuffix="ஐ"; sw.nounSuffixId=1; root+="கு";
			break;
			case "கால்": sw.nounSuffix="ஆல்"; sw.nounSuffixId=2; root+="கு";
			break;
			case "கிற்குள்":
			case "குக்குள்": sw.nounSuffix="உள்"; sw.nounSuffixId=10; root+="கு";
			break;
			case "குக்கு":
			case "கிற்கு": sw.nounSuffix="கு"; sw.nounSuffixId=3; root+="கு";
			break;
			case "கில்": sw.nounSuffix="இல்"; sw.nounSuffixId=4; root+="கு";
			break;
			case "கின்": sw.nounSuffix="இன்"; sw.nounSuffixId=5; root+="கு";
			break;
			case "கினது": sw.nounSuffix="அது"; sw.nounSuffixId=6; root+="கு";
			break;
			case "குகள்": sw.nounSuffix="கள்"; sw.nounSuffixId=7;root+="கு";
			break;
			case "குகளால்": sw.nounSuffix="களால்"; sw.nounSuffixId=8;root+="கு";
			break;
			case "கின்கண்": sw.nounSuffix="கண்"; sw.nounSuffixId=9; root+="கு";
			break;
			default: System.out.println("Exception from கை|கால்|க்கு|கில்|கின்|கினது|கள்|களால்|கின்கண் switch");
			}
			sw.rootWord=root;
			System.out.println(inflectednoun +"\n"+ "POS :"+sw.POS +"\n"+ "POSid :"+sw.POSId +"\n"+ "rootword: "+sw.rootWord +"\n"+ "NounSuffix :"+sw.nounSuffix +"\n"+ "NounSuffixid :"+sw.nounSuffixId);
			return sw;
		}

		//type=3;
		pat = Pattern.compile("(த்தை|த்தால்|த்துக்குள்|த்திற்குள்|த்துக்கு|த்திற்கு|த்தில்|த்தின்|த்தினது|ங்கள்|ங்களால்|த்தின்கண்)$");
		matcher = pat.matcher(inflectednoun); 
		if(matcher.find()){
			String[] splitString = inflectednoun.split("(த்தை|த்தால்|த்துக்குள்|த்திற்குள்|த்துக்கு|த்திற்கு|த்தில்|த்தின்|த்தினது|ங்கள்|ங்களால்|த்தின்கண்)$");
			if(splitString[0].endsWith("ங்")){
				root = splitString[0].substring(0, splitString[0].length()-2) + "ம்";
			}else{
				root = splitString[0] + "ம்";
			}
			String tempSuffix = inflectednoun.replace(splitString[0], "");
			System.out.println("\n"+root + "\t"+getnoofஎழுத்து(root)+"\t" + tempSuffix);
			sw.rootWord=root;
			switch(tempSuffix){
			case "த்தை": sw.nounSuffix="ஐ"; sw.nounSuffixId=1;
			break;
			case "த்தால்": sw.nounSuffix="ஆல்"; sw.nounSuffixId=2;
			break;
			case "த்துக்குள்":
			case "த்திற்குள்": sw.nounSuffix="உள்"; sw.nounSuffixId=10;
			break;
			case "த்திற்கு":
			case "த்துக்கு": sw.nounSuffix="கு"; sw.nounSuffixId=3;
			break;
			case "த்தில்": sw.nounSuffix="இல்"; sw.nounSuffixId=4;
			break;
			case "த்தின்": sw.nounSuffix="இன்"; sw.nounSuffixId=5;
			break;
			case "த்தினது": sw.nounSuffix="அது"; sw.nounSuffixId=6;
			break;
			case "ங்கள்": sw.nounSuffix="கள்"; sw.nounSuffixId=7;
			break;
			case "ங்களால்": sw.nounSuffix="களால்"; sw.nounSuffixId=8;
			break;
			case "த்தின்கண்": sw.nounSuffix="கண்"; sw.nounSuffixId=9;
			break;
			default: System.out.println("Exception from த்தை|த்தால்|த்துக்கு|த்தில்|த்தின்|த்தினது|கள்|களால்|த்தின்கண் switch");
			}
			System.out.println(inflectednoun +"\n"+ "POS :"+sw.POS +"\n"+ "POSid :"+sw.POSId +"\n"+ "rootword: "+sw.rootWord +"\n"+ "NounSuffix :"+sw.nounSuffix +"\n"+ "NounSuffixid :"+sw.nounSuffixId);
			return sw;
		}

		//type=4;
		pat = Pattern.compile("(பை|பால்|புக்குள்|பிற்குள்|புக்கு|பிற்கு|பில்|பின்|பினது|புகள்|புகளால்|பின்கண்)$");
		matcher = pat.matcher(inflectednoun); 
		if(matcher.find()){
			String[] splitString = inflectednoun.split("(பை|பால்|புக்குள்|பிற்குள்|புக்கு|பிற்கு|பில்|பின்|பினது|புகள்|புகளால்|பின்கண்)$");

			root = splitString[0];

			String tempSuffix = inflectednoun.replace(splitString[0], "");
			System.out.println("\n"+root + "\t"+getnoofஎழுத்து(root)+"\t" + tempSuffix);
			switch(tempSuffix){
			case "பை": sw.nounSuffix="ஐ"; sw.nounSuffixId=1; root+="பு";
			break;
			case "பால்": sw.nounSuffix="ஆல்"; sw.nounSuffixId=2; root+="பு";
			break;
			case "புக்குள்":
			case "பிற்குள்": sw.nounSuffix="உள்"; sw.nounSuffixId=10;
			break;
			case "புக்கு":
			case "பிற்கு": sw.nounSuffix="கு"; sw.nounSuffixId=3; root+="பு";
			break;
			case "பில்": sw.nounSuffix="இல்"; sw.nounSuffixId=4; root+="பு";
			break;
			case "பின்": sw.nounSuffix="இன்"; sw.nounSuffixId=5; root+="பு";
			break;
			case "பினது": sw.nounSuffix="அது"; sw.nounSuffixId=6; root+="பு";
			break;
			case "புகள்": sw.nounSuffix="கள்"; sw.nounSuffixId=7;root+="பு";
			break;
			case "புகளால்": sw.nounSuffix="களால்"; sw.nounSuffixId=8;root+="பு";
			break;
			case "பின்கண்": sw.nounSuffix="கண்"; sw.nounSuffixId=9; root+="பு";
			break;
			default: System.out.println("Exception from பை|பால்|புக்கு|பிற்கு|பில்|பின்|பினது|புகள்|புகளால்|பின்கண் switch");
			}
			sw.rootWord=root;
			System.out.println(inflectednoun +"\n"+ "POS :"+sw.POS +"\n"+ "POSid :"+sw.POSId +"\n"+ "rootword: "+sw.rootWord +"\n"+ "NounSuffix :"+sw.nounSuffix +"\n"+ "NounSuffixid :"+sw.nounSuffixId);
			return sw;
		}

		//type=5;
		pat = Pattern.compile("(ட்டை|ட்டால்|ட்டுக்குள்|ட்டிற்குள்|ட்டுக்கு|ட்டிற்கு|ட்டில்|ட்டின்|ட்டினது|டுகள்|டுகளால்|ட்டின்கண்)$");
		matcher = pat.matcher(inflectednoun); 
		if(matcher.find()){
			String[] splitString = inflectednoun.split("(ல்|ட்டுக்குள்|ட்டிற்குள்|ட்டுக்கு|ட்டிற்கு|ட்டில்|ட்டின்|ட்டினது|டுகள்|டுகளால்|ட்டின்கண்)$");
			root = splitString[0];

			String tempSuffix = inflectednoun.replace(splitString[0], "");
			System.out.println("\n"+root + "\t"+getnoofஎழுத்து(root)+"\t" + tempSuffix);
			switch(tempSuffix){
			case "ட்டை": sw.nounSuffix="ஐ"; sw.nounSuffixId=1; root+="டு";
			break;
			case "ட்டால்": sw.nounSuffix="ஆல்"; sw.nounSuffixId=2; root+="டு";
			break;
			case "ட்டிற்குள்":
			case "ட்டுக்குள்": sw.nounSuffix="உள்"; sw.nounSuffixId=10;
			break;
			case "ட்டுக்கு": 
			case "ட்டிற்கு": sw.nounSuffix="கு"; sw.nounSuffixId=3; root+="டு";
			break;
			case "ட்டில்": sw.nounSuffix="இல்"; sw.nounSuffixId=4; root+="டு";
			break;
			case "ட்டின்": sw.nounSuffix="இன்"; sw.nounSuffixId=5; root+="டு";
			break;
			case "ட்டினது": sw.nounSuffix="அது"; sw.nounSuffixId=6; root+="டு";
			break;
			case "கள்": sw.nounSuffix="கள்"; sw.nounSuffixId=7;
			break;
			case "களால்": sw.nounSuffix="களால்"; sw.nounSuffixId=8;
			break;
			case "ட்டின்கண்": sw.nounSuffix="கண்"; sw.nounSuffixId=9; root+="டு";
			break;
			default: System.out.println("Exception from ட்டை|ட்டால்|ட்டுக்கு|ட்டிற்கு|ட்டில்|ட்டின்|ட்டினது|கள்|களால்|ட்டின்கண் switch");
			}
			sw.rootWord=root;
			System.out.println(inflectednoun +"\n"+ "POS :"+sw.POS +"\n"+ "POSid :"+sw.POSId +"\n"+ "rootword: "+sw.rootWord +"\n"+ "NounSuffix :"+sw.nounSuffix +"\n"+ "NounSuffixid :"+sw.nounSuffixId);
			return sw;
		}

		//type=6,14;
		pat = Pattern.compile("(றை|றால்|றுக்கு|றில்|றின்|றினது|றின்கண்)$");
		matcher = pat.matcher(inflectednoun);        
		if(matcher.find()){
			String[] splitString = inflectednoun.split("(றை|றால்|றுக்கு|றில்|றின்|றினது|றுகள்|றுகளால்|றின்கண்)$");
			root = splitString[0]+"று";
			String tempSuffix = inflectednoun.replace(splitString[0], "");
			System.out.println("\n"+root + "\t"+getnoofஎழுத்து(root)+"\t" + tempSuffix);
			sw.rootWord=root;
			switch(tempSuffix){
			case "றை": sw.nounSuffix="ஐ"; sw.nounSuffixId=1;
			break;
			case "றால்": sw.nounSuffix="ஆல்"; sw.nounSuffixId=2;
			break;
			case "றுக்கு": sw.nounSuffix="கு"; sw.nounSuffixId=3;
			break;
			case "றில்": sw.nounSuffix="இல்"; sw.nounSuffixId=4;
			break;
			case "றின்": sw.nounSuffix="இன்"; sw.nounSuffixId=5;
			break;
			case "றினது": sw.nounSuffix="அது"; sw.nounSuffixId=6;
			break;
			case "றுகள்": sw.nounSuffix="கள்"; sw.nounSuffixId=7;
			break;
			case "றுகளால்": sw.nounSuffix="களால்"; sw.nounSuffixId=8;
			break;
			case "றின்கண்": sw.nounSuffix="கண்"; sw.nounSuffixId=9;
			break;
			default: System.out.println("Exception from றை|றால்|றுக்கு|றில்|றின்|றினது|றுகள்|றுகளால்|றின்கண் switch");
			}
			System.out.println(inflectednoun +"\n"+ "POS :"+sw.POS +"\n"+ "POSid :"+sw.POSId +"\n"+ "rootword: "+sw.rootWord +"\n"+ "NounSuffix :"+sw.nounSuffix +"\n"+ "NounSuffixid :"+sw.nounSuffixId);
			return sw;
		}

		//type=7;
		pat = Pattern.compile("(ளை|ளால்|ளுக்குள்|ளிற்குள்|ளுக்கு|ளிற்கு|ளில்|ளின்|ளினது|ள்கள்|ள்களால்|ளின்கண்)$");
		matcher = pat.matcher(inflectednoun);        
		if(matcher.find()){
			String[] splitString = inflectednoun.split("(ளை|ளால்|ளுக்குள்|ளிற்குள்|ளுக்கு|ளிற்கு|ளில்|ளின்|ளினது|ள்கள்|ள்களால்|ளின்கண்)$");
			root = splitString[0];
			if(!root.endsWith("ள்"))root=root+"ள்";
			String tempSuffix = inflectednoun.replace(splitString[0], "");
			System.out.println("\n"+root + "\t"+getnoofஎழுத்து(root)+"\t" + tempSuffix);
			sw.rootWord=root;
			switch(tempSuffix){
			case "ளை": sw.nounSuffix="ஐ"; sw.nounSuffixId=1;
			break;
			case "ளால்": sw.nounSuffix="ஆல்"; sw.nounSuffixId=2;
			break;
			case "ளுக்குள்":
			case "ளிற்குள்": sw.nounSuffix="உள்"; sw.nounSuffixId=10;
			break;
			case "ளுக்கு": 
			case "ளிற்கு":	sw.nounSuffix="கு"; sw.nounSuffixId=3;
			break;
			case "ளில்": sw.nounSuffix="இல்"; sw.nounSuffixId=4;
			break;
			case "ளின்": sw.nounSuffix="இன்"; sw.nounSuffixId=5;
			break;
			case "ளினது": sw.nounSuffix="அது"; sw.nounSuffixId=6;
			break;
			case "ள்கள்": sw.nounSuffix="கள்"; sw.nounSuffixId=7;
			break;
			case "ள்களால்": sw.nounSuffix="களால்"; sw.nounSuffixId=8;
			break;
			case "ளின்கண்": sw.nounSuffix="கண்"; sw.nounSuffixId=9;
			break;
			default: System.out.println("Exception from ளை|ளால்|ளுக்கு|ளிற்கு|ளில்|ளின்|ளினது|கள்|களால்|ளின்கண் switch");
			}
			System.out.println(inflectednoun +"\n"+ "POS :"+sw.POS +"\n"+ "POSid :"+sw.POSId +"\n"+ "rootword: "+sw.rootWord +"\n"+ "NounSuffix :"+sw.nounSuffix +"\n"+ "NounSuffixid :"+sw.nounSuffixId);
			return sw;
		}


		//type=8;
		pat = Pattern.compile("(தை|தால்|திற்குள்|துக்குள்|திற்கு|துக்கு|தில்|தின்|தினது|க்கள்|க்களால்|தின்கண்)$");
		matcher = pat.matcher(inflectednoun); 
		if(matcher.find()){
			String[] splitString = inflectednoun.split("(தை|தால்|திற்குள்|துக்குள்|திற்கு|துக்கு|தில்|தின்|தினது|க்கள்|க்களால்|தின்கண்)$");
			root = splitString[0] + "து";

			String tempSuffix = inflectednoun.replace(splitString[0], "");
			System.out.println("\n"+root + "\t"+getnoofஎழுத்து(root)+"\t" + tempSuffix);
			sw.rootWord=root;
			switch(tempSuffix){
			case "தை": sw.nounSuffix="ஐ"; sw.nounSuffixId=1;
			break;
			case "தால்": sw.nounSuffix="ஆல்"; sw.nounSuffixId=2;
			break;
			case "திற்குள்":
			case "துக்குள்": sw.nounSuffix="உள்"; sw.nounSuffixId=10;
			break;
			case "திற்கு" :
			case "துக்கு": sw.nounSuffix="கு"; sw.nounSuffixId=3;
			break;
			case "தில்": sw.nounSuffix="இல்"; sw.nounSuffixId=4;
			break;
			case "தின்": sw.nounSuffix="இன்"; sw.nounSuffixId=5;
			break;
			case "தினது": sw.nounSuffix="அது"; sw.nounSuffixId=6;
			break;
			case "க்கள்": sw.nounSuffix="கள்"; sw.nounSuffixId=7;
			break;
			case "க்களால்": sw.nounSuffix="களால்"; sw.nounSuffixId=8;
			break;
			case "தின்கண்": sw.nounSuffix="கண்"; sw.nounSuffixId=9;
			break;
			default: System.out.println("Exception from தை|தால்|திற்கு|துக்கு|தில்|தின்|தினது|தின்கண்|கள்|களால் switch");
			}
			System.out.println(inflectednoun +"\n"+ "POS :"+sw.POS +"\n"+ "POSid :"+sw.POSId +"\n"+ "rootword: "+sw.rootWord +"\n"+ "NounSuffix :"+sw.nounSuffix +"\n"+ "NounSuffixid :"+sw.nounSuffixId);
			return sw;
		}

		//type=9;
		pat = Pattern.compile("(ணை|ணால்|ணுக்குள்|ணிற்குள்|ணுக்கு|ணிற்கு|ணில்|ணின்|ணினது|ண்கள்|ண்களால்|ணின்கண்)$");
		matcher = pat.matcher(inflectednoun); 
		if(matcher.find()){
			String[] splitString = inflectednoun.split("(ணை|ணால்|ணுக்குள்|ணிற்குள்|ணுக்கு|ணிற்கு|ணில்|ணின்|ணினது|ண்கள்|ண்களால்|ணின்கண்)$");
			root = splitString[0];
			String tempSuffix = inflectednoun.replace(splitString[0], "");
			System.out.println("\n"+root + "\t"+getnoofஎழுத்து(root)+"\t" + tempSuffix);

			if(!root.endsWith("ண்")) root=root+"ண்";
			{
				//தூண்
				switch(tempSuffix){
				case "ணை": sw.nounSuffix="ஐ"; sw.nounSuffixId=1;
				break;
				case "ணால்": sw.nounSuffix="ஆல்"; sw.nounSuffixId=2;
				break;
				case "ணுக்குள்":
				case "ணிற்குள்": sw.nounSuffix="உள்"; sw.nounSuffixId=10;
				break;
				case "ணிற்கு":
				case "ணுக்கு": sw.nounSuffix="கு"; sw.nounSuffixId=3;
				break;
				case "ணில்": sw.nounSuffix="இல்"; sw.nounSuffixId=4;
				break;
				case "ணின்": sw.nounSuffix="இன்"; sw.nounSuffixId=5;
				break;
				case "ணினது": sw.nounSuffix="அது"; sw.nounSuffixId=6;
				break;
				case "ண்கள்": sw.nounSuffix="கள்"; sw.nounSuffixId=7;
				break;
				case "ண்களால்": sw.nounSuffix="களால்"; sw.nounSuffixId=8;
				break;
				case "ணின்கண்": sw.nounSuffix="கண்"; sw.nounSuffixId=9;
				break;
				default: System.out.println("Exception from ணை|ணால்|ணுக்கு|ணிற்கு|ணில்|ணின்|ணினது|ணின்கண்");
				}
				sw.rootWord=root;
				System.out.println("\n"+ inflectednoun +"\n"+ "POS :"+sw.POS +"\n"+ "POSid :"+sw.POSId +"\n"+ "rootword: "+sw.rootWord +"\n"+ "NounSuffix :"+sw.nounSuffix +"\n"+ "NounSuffixid :"+sw.nounSuffixId);
				return sw;	
			}
		}

		//type=10;
		pat = Pattern.compile("(ரை|ரால்|ருக்குள்|ரிற்குள்|ருக்கு|ரிற்கு|ரில்|ரின்|ரினது|ர்கள்|ர்களால்|ரின்கண்)$");
		matcher = pat.matcher(inflectednoun); 
		if(matcher.find()){
			String[] splitString = inflectednoun.split("(ரை|ரால்|ருக்குள்|ரிற்குள்|ருக்கு|ரிற்கு|ரில்|ரின்|ரினது|ர்கள்|ர்களால்|ரின்கண்)$");
			root = splitString[0]+"ர்";
			String tempSuffix = inflectednoun.replace(splitString[0], "");
			System.out.println("\n"+root + "\t"+getnoofஎழுத்து(root)+"\t" + tempSuffix);
			sw.rootWord=root;
			switch(tempSuffix){
			case "ரை": sw.nounSuffix="ஐ"; sw.nounSuffixId=1;
			break;
			case "ரால்": sw.nounSuffix="ஆல்"; sw.nounSuffixId=2;
			break;
			case "ரிற்குள்":
			case "ருக்குள்": sw.nounSuffix="உள்"; sw.nounSuffixId=10;
			break;
			case "ரிற்கு" :
			case "ருக்கு": sw.nounSuffix="கு"; sw.nounSuffixId=3;
			break;
			case "ரில்": sw.nounSuffix="இல்"; sw.nounSuffixId=4;
			break;
			case "ரின்": sw.nounSuffix="இன்"; sw.nounSuffixId=5;
			break;
			case "ரினது": sw.nounSuffix="அது"; sw.nounSuffixId=6;
			break;
			case "ர்கள்": sw.nounSuffix="கள்"; sw.nounSuffixId=7;
			break;
			case "ர்களால்": sw.nounSuffix="களால்"; sw.nounSuffixId=8;
			break;
			case "ரின்கண்": sw.nounSuffix="கண்"; sw.nounSuffixId=9;
			break;
			default: System.out.println("Exception from ரை|ரால்|ருக்கு|ரிற்கு|ரில்|ரின்|ரினது|ர்கள்|ர்களால்|ரின்கண் switch");
			}
			System.out.println(inflectednoun +"\n"+ "POS :"+sw.POS +"\n"+ "POSid :"+sw.POSId +"\n"+ "rootword: "+sw.rootWord +"\n"+ "NounSuffix :"+sw.nounSuffix +"\n"+ "NounSuffixid :"+sw.nounSuffixId);
			return sw;
		}

		//type=12;				
		pat = Pattern.compile("(னை|னால்|னுக்குள்|னிற்குள்|னுக்கு|னிற்கு|னில்|னின்|னினது|ன்கள்|ன்களாள்|னின்கண்)$");
		matcher = pat.matcher(inflectednoun); 
		if(matcher.find()){
			String[] splitString = inflectednoun.split("(னை|னால்|னுக்குள்|னிற்குள்|னுக்கு|னிற்கு|னில்|னின்|னினது|ன்கள்|ன்களாள்|னின்கண்)$");
			root = splitString[0];
			String tempSuffix = inflectednoun.replace(splitString[0], "");
			System.out.println("\n"+root + "\t"+getnoofஎழுத்து(root)+"\t" + tempSuffix);

			if(!root.endsWith("ன்")) root=root+"ன்";
			{
				//தேன்
				switch(tempSuffix){
				case "னை": sw.nounSuffix="ஐ"; sw.nounSuffixId=1;
				break;
				case "னால்": sw.nounSuffix="ஆல்"; sw.nounSuffixId=2;
				break;
				case "னுக்குள்":
				case "னிற்குள்": sw.nounSuffix="உள்"; sw.nounSuffixId=10;
				break;
				case "னுக்கு":
				case "னிற்கு": sw.nounSuffix="கு"; sw.nounSuffixId=3;
				break;
				case "னில்": sw.nounSuffix="இல்"; sw.nounSuffixId=4;
				break;
				case "னின்": sw.nounSuffix="இன்"; sw.nounSuffixId=5;
				break;
				case "னினது": sw.nounSuffix="அது"; sw.nounSuffixId=6;
				break;
				case "ன்கள்": sw.nounSuffix="கள்"; sw.nounSuffixId=7;
				break;
				case "ன்களாள்": sw.nounSuffix="களால்"; sw.nounSuffixId=8;
				break;
				case "னின்கண்": sw.nounSuffix="கண்"; sw.nounSuffixId=9;
				break;
				default: System.out.println("Exception from னை|னால்|னுக்குள்|னிற்குள்|னுக்கு|னிற்கு|னில்|னின்|னினது|ன்கள்|ன்களாள்|னின்கண்");
				}
				sw.rootWord=root;
				System.out.println("\n"+ inflectednoun +"\n"+ "POS :"+sw.POS +"\n"+ "POSid :"+sw.POSId +"\n"+ "rootword: "+sw.rootWord +"\n"+ "NounSuffix :"+sw.nounSuffix +"\n"+ "NounSuffixid :"+sw.nounSuffixId);
				return sw;	
			}
		}

		//type=0;
		pat = Pattern.compile("(யை|யால்|க்கு|யில்|யின்|யினது|யின்கண்)$");
		matcher = pat.matcher(inflectednoun);        
		if(matcher.find()){
			String[] splitString = inflectednoun.split("யை|யால்|க்கு|யில்|யின்|யினது|கள்|களால்|யின்கண்");
			root = splitString[0];
			String tempSuffix = inflectednoun.replace(splitString[0], "");
			System.out.println("\n"+root + "\t"+getnoofஎழுத்து(root)+"\t" + tempSuffix);
			if(ned.isNedil(root)==true){
				System.out.println("\tIamNedil\t");
				switch(tempSuffix){
				case "யை": root = root + "ய்"; sw.nounSuffix="ஐ"; sw.nounSuffixId=1;
				break;
				case "யால்": root = root + "ய்"; sw.nounSuffix="ஆல்"; sw.nounSuffixId=2;
				break;
				//பேய் + க்கு - SO root is no longer a nedil. It will be handled by next Switch case
				/*case "க்கு": sw.nounSuffix="கு"; sw.nounSuffixId=3;
				break;*/
				case "யில்": root = root + "ய்"; sw.nounSuffix="இல்"; sw.nounSuffixId=4;
				break;
				case "யின்": root = root + "ய்"; sw.nounSuffix="இன்"; sw.nounSuffixId=5;
				break;
				case "யினது": root = root + "ய்"; sw.nounSuffix="அது"; sw.nounSuffixId=6;
				break;
				//YETTOHANDLE
				/*
				case "கள்": sw.nounSuffix="கள்"; sw.nounSuffixId=7;
				break;
				case "களால்": sw.nounSuffix="களால்"; sw.nounSuffixId=8;
				break;
				 */
				case "யின்கண்": root = root + "ய்"; sw.nounSuffix="கண்"; sw.nounSuffixId=9;
				break;
				default: System.out.println("Exception from யை|யால்|க்கு|யில்|யின்|யினது|கள்|களால்|யின்கண்  Nedil");
				}
				sw.rootWord=root;
				System.out.println(inflectednoun +"\n"+ "POS :"+sw.POS +"\n"+ "POSid :"+sw.POSId +"\n"+ "rootword: "+sw.rootWord +"\n"+ "NounSuffix :"+sw.nounSuffix +"\n"+ "NounSuffixid :"+sw.nounSuffixId);
				return sw;
			}
			sw.rootWord=root;
			switch(tempSuffix){
			case "யை": sw.nounSuffix="ஐ"; sw.nounSuffixId=1;
			break;
			case "யால்": sw.nounSuffix="ஆல்"; sw.nounSuffixId=2;
			break;
			case "க்கு": sw.nounSuffix="கு"; sw.nounSuffixId=3;
			break;
			case "யில்": sw.nounSuffix="இல்"; sw.nounSuffixId=4;
			break;
			case "யின்": sw.nounSuffix="இன்"; sw.nounSuffixId=5;
			break;
			case "யினது": sw.nounSuffix="அது"; sw.nounSuffixId=6;
			break;
			case "கள்": sw.nounSuffix="கள்"; sw.nounSuffixId=7;
			break;
			case "களால்": sw.nounSuffix="களால்"; sw.nounSuffixId=8;
			break;
			case "யின்கண்": sw.nounSuffix="கண்"; sw.nounSuffixId=9;
			break;
			default: System.out.println("Exception from யை|யால்|க்கு|யில்|யின்|யினது|கள்|களால்|யின்கண்  switch");
			}
			System.out.println(inflectednoun +"\n"+ "POS :"+sw.POS +"\n"+ "POSid :"+sw.POSId +"\n"+ "rootword: "+sw.rootWord +"\n"+ "NounSuffix :"+sw.nounSuffix +"\n"+ "NounSuffixid :"+sw.nounSuffixId);
			return sw;
		}


		//type=11;
		pat = Pattern.compile("(வை|வால்|வுக்குள்|விற்குள்|வுக்கு|விற்கு|வில்|வின்|வினது|வின்கண்)$");
		matcher = pat.matcher(inflectednoun); 
		if(matcher.find()){
			String[] splitString = inflectednoun.split("(வை|வால்|வுக்கு|விற்கு|வில்|வின்|வினது|வுகள்|வுகளால்|வின்கண்)$");
			root = splitString[0];
			String tempSuffix = inflectednoun.replace(splitString[0], "");
			System.out.println("\n"+root + "\t"+getnoofஎழுத்து(root)+"\t" + tempSuffix);
			sw.rootWord=root;
			switch(tempSuffix){
			case "வை": sw.nounSuffix="ஐ"; sw.nounSuffixId=1;
			break;
			case "வால்": sw.nounSuffix="ஆல்"; sw.nounSuffixId=2;
			break;
			case "வுக்குள்":
			case "விற்குள்": sw.nounSuffix="உள்"; sw.nounSuffixId=10;
			break;
			case "விற்கு" :
			case "வுக்கு": sw.nounSuffix="கு"; sw.nounSuffixId=3;
			break;
			case "வில்": sw.nounSuffix="இல்"; sw.nounSuffixId=4;
			break;
			case "வின்": sw.nounSuffix="இன்"; sw.nounSuffixId=5;
			break;
			case "வினது": sw.nounSuffix="அது"; sw.nounSuffixId=6;
			break;
			case "வுகள்": sw.nounSuffix="கள்"; sw.nounSuffixId=7;
			break;
			case "வுகளால்": sw.nounSuffix="களால்"; sw.nounSuffixId=8;
			break;
			case "வின்கண்": sw.nounSuffix="கண்"; sw.nounSuffixId=9;
			break;
			default: System.out.println("Exception from வை|வால்|வுக்கு|வில்|வின்|வினது|வுகள்|வுகளால்|வின்கண் switch");
			}
			System.out.println(inflectednoun +"\n"+ "POS :"+sw.POS +"\n"+ "POSid :"+sw.POSId +"\n"+ "rootword: "+sw.rootWord +"\n"+ "NounSuffix :"+sw.nounSuffix +"\n"+ "NounSuffixid :"+sw.nounSuffixId);
			return sw;
		}


		return sw;
	}

	public static void main(String args[]){
		String test[] = {"தேனை","காற்றில்","பசுவால்"};
		//{"எள்ளை","தேளை","சதங்கள்","சதத்தை","மணிக்கு"}; 
		//{"மோரை","தேரால்","போருக்கு","போரில்","போரின்","போர்கள்","போரின்கண்"};
		//{"கண்ணை","தூணை","தூணால்","தூணிற்கு","கண்ணில்","தூணினது"}; 
		//{"நல்லதை","தீதால்","நல்லதுக்கு","தீதில்","தீதின்","ஒன்றினது","நன்றுகள்","நன்றுகளால்","கீற்றின்கண்"};
		//{"ஒன்றை","ஒன்றால்","ஒன்றுக்கு","ஒன்றில்","ஒன்றின்","ஒன்றினது","நன்றுகள்","நன்றுகளால்","கீற்றின்கண்"}; 
		//{"காற்றை","காற்றால்","காற்றுக்கு","காற்றில்","காற்றின்","காற்றினது","கீற்றுகள்","கீற்றுகளால்","கீற்றிங்கண்"};
		//{"ஓட்டை","ஓட்டால்","ஓட்டுக்கு","ஓட்டிற்கு","சாப்பாட்டில்","வீட்டின்","வீட்டினது","வீடுகள்","மேடுகளால்","மேட்டின்கண்"};				
		//{"தோப்பை","சீப்பால்","தோப்புக்கு","தோப்பிற்கு","தோப்பில்","சீப்பின்","சீப்பினது","தோப்புகள்","தோப்புகளால்","தோப்பின்கண்"};
		//{"நாக்கை","நாக்கால்","நாக்குக்கு","நாக்கிற்கு","நாக்கில்","நாக்கின்","நாக்கினது","சாக்குகள்","சாக்குகளால்","நாக்கின்கண்"}; 
		//{"கோவிலை","காலை","காலால்","காலுக்கு","காலில்","காலின்","காலினது","காலின்கண்"};
		//{"சொல்லை","சொல்லால்","சொல்லுக்கு","சொல்லின்","சொல்லில்","சொல்லினது","சொல்ல்ங்கண்","சொற்கள்"};
		//{"கப்பலை","கப்பலால்","கப்பலுக்கு","கப்பலின்","கப்பலில்","கப்பலினது","கப்பலிங்கண்"};
		//{"வடையை","வடையால்","வடைக்கு","வடையின்","வடையில்","வடையினது","வடையின்கண்"};
		//{"பேயை","பேயால்","பேய்க்கு","பேயின்","பேயில்","பேயினது","பேயிங்கண்"};
		for(String s:test){
			Noun.analyse(s);
		}
	}
}
