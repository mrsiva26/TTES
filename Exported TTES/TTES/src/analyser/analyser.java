package analyser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class analyser {
	Connection conn;
	Statement st;
	List<String> misc,connector;

	public analyser(){
		try{
			misc=Arrays.asList("ஆகும்","இது","அது","அன்று","இன்று","நேற்று","நாளை","அதை","இதை","எதை","அப்படி","இப்படி","காலை","மாலை","இரவு","பகல்","மதியம்","ஜாமம்","அந்தி","ஆடி","ஆவனி","ஐப்பசி","ஒரு","ஒன்றை","ஒன்று","பகுதியில்","அருகில்","தூரத்தில்","பக்கத்தில்","தொலைவில்","இல்லை","மற்றும்","மற்றுமன்றி","ஆதலால்","அதனால்","எனவே","ஆகவே","யெனவே","அல்லது","அல்லாது","முன்னர்","முன்னே","என்பது","அப்போது","தற்போது","இப்போது","சமயத்தில்","நேரத்தில்","வேளையில்","சமயம்","நேரம்","வேளை","பற்றி","இருந்து","தொடங்கி","ஆரம்பித்து","முடிந்து","வரை","வழியாக","வழியே","வழியில்","எப்போது","எப்படி","ஏன்","எதற்கு","எவ்வாறு","என்ன","போது","பொழுது","உடன்","கொண்டிருந்தபோது");
			connector=Arrays.asList("போது","பொழுது","உடன்","கொண்டிருந்தபோது");
			String url = "jdbc:mysql://localhost:3306/"; 
			String dbName = "tamilwordnet"; 
			String driver = "com.mysql.jdbc.Driver"; 
			String userName = "root"; 
			String password = "";
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(url+dbName+"?characterEncoding=UTF-8",userName,password);
			st = conn.createStatement();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static String[] removeElements(String[] input, String deleteMe) {
		List<String> result = new LinkedList<String>();
		for(String item : input)
			if(!deleteMe.equals(item))
				result.add(item);
		return (String[]) result.toArray(input);
	}

	public static String[] removeElementsByIndex(String[] input, int deleteMe) {
		List<String> result = new LinkedList<String>();
		for(int i=0;i<input.length;i++){
			if(i!=deleteMe){
				result.add(input[i]);
				System.out.println("I am added to list" + input[i]);
			}
		}
		return (String[]) result.toArray(input);
	}

	public static SegmentedWord[] removeSegmentedWords( SegmentedWord[] input, int unwanted) {
		SegmentedWord temp[] = new  SegmentedWord[input.length-unwanted];
		System.out.println("I am temp size "+temp.length);
		for(int i=0;i<temp.length;i++)
			temp[i]=input[i];
		return temp;
	}

	private static ArrayList<String> getஎழுத்து(String str){
		ArrayList<String> characters=new ArrayList<String>();
		Pattern pat=Pattern.compile("\\p{L}\\p{M}*");
		Matcher matcher=pat.matcher(str);
		while(matcher.find())
		{
			characters.add(matcher.group());
		}
		return characters;
	}

	private static String deleteஒற்று(String str){
		ArrayList<String> characters=new ArrayList<String>();
		Pattern pat=Pattern.compile("\\p{L}\\p{M}*");
		Matcher matcher=pat.matcher(str);
		while(matcher.find())
		{
			characters.add(matcher.group());
		}
		String s="";
		for(int k=0;k<characters.size()-1;k++)
			s+=characters.get(k);
		return s;
	}

	public SegmentedWord[] analyzeText(String input)
	{
		SegmentedWord IntermediateOutput[]=null;
		int unwanted=0;
		try{
			Pattern pat;
			Matcher matcher;
			String tempwords[] = input.replaceAll("\\s+", " ").split(" ");
			System.out.println(tempwords.length);
			List<String> result = new LinkedList<String>();
			
			for(int i=0;i<tempwords.length;i++){
				if(tempwords[i]!=null){
					tempwords[i]=tempwords[i].replaceAll("\\s+", "");
					if(tempwords[i].length()<=1){
					}else{
						System.out.println("I am added"+ tempwords[i]);
						result.add(tempwords[i]);
					}
				}else{
				}
			}
			String words[] = (String[]) result.toArray(new String[result.size()]);			
			
			IntermediateOutput = new SegmentedWord[words.length];
			for(int i=0;i<words.length;i++){
				System.out.println("I am a word"+words[i]);
				//இயல்பினும் விதியினும் நின்ற உயிர் முன் கசதப மிகும்
				ArrayList<String> characters = getஎழுத்து(words[i]);
				if(characters.get(characters.size()-1).equals("க்")||
						characters.get(characters.size()-1).equals("ச்")||
						characters.get(characters.size()-1).equals("த்")||
						characters.get(characters.size()-1).equals("ப்")){
					switch(characters.get(characters.size()-1)){
					case "க்": if(i+1<words.length && words[i+1].startsWith("க"))words[i]=deleteஒற்று(words[i]);
					break; 
					case "ச்": if(i+1<words.length && words[i+1].startsWith("ச"))words[i]=deleteஒற்று(words[i]);
					break;
					case "த்": if(i+1<words.length && words[i+1].startsWith("த"))words[i]=deleteஒற்று(words[i]);
					break;
					case "ப்": if(i+1<words.length && words[i+1].startsWith("ப"))words[i]=deleteஒற்று(words[i]);
					break;
					default:
					}
				}
			}

			for(int i=0;i<words.length-unwanted;i++){
				IntermediateOutput[i] = new SegmentedWord();
				IntermediateOutput[i].inflectedWord=words[i];

				if(words[i].contains("?")){
					System.err.println("Came in");
					IntermediateOutput[i].addAttribute("interrogative");
					words[i]=words[i].replace("?", "");
				}

				//misc words
				if(misc.contains(words[i])){
					IntermediateOutput[i].rootWord=words[i];
					IntermediateOutput[i].POS="misc";
					IntermediateOutput[i].POSId=777;
					System.out.println("\n"+ words[i] +"\n"+ "POS :"+IntermediateOutput[i].POS +"\n"+ "POSid :"+IntermediateOutput[i].POSId  +"\n"+ "rootword: "+IntermediateOutput[i].rootWord);
					continue;
				}

				//pronoun
				boolean pronoun=false;
				switch(words[i]){
				case "நான்": 
				case "என்" :
					pronoun=true;
					IntermediateOutput[i].rootWord=words[i];
					IntermediateOutput[i].POS="pronoun";
					IntermediateOutput[i].POSId=2;
					IntermediateOutput[i].doerSuffix="ஏன்";
					IntermediateOutput[i].doerSuffixId=1;
					System.out.println("\n"+ words[i] +"\n"+ "POS :"+IntermediateOutput[i].POS +"\n"+ "POSid :"+IntermediateOutput[i].POSId  +"\n"+ "rootword: "+IntermediateOutput[i].rootWord +"\n"+ "DoerSuffix :"+IntermediateOutput[i].doerSuffix +"\n"+ "DoerSuffixid :"+IntermediateOutput[i].doerSuffixId);
					break;
				case "நாம்":
					pronoun=true;
					IntermediateOutput[i].rootWord="நாம்";
					IntermediateOutput[i].POS="pronoun";
					IntermediateOutput[i].POSId=2;
					IntermediateOutput[i].doerSuffix="ஓம்";
					IntermediateOutput[i].doerSuffixId=2;
					System.out.println("\n"+ words[i] +"\n"+ "POS :"+IntermediateOutput[i].POS +"\n"+ "POSid :"+IntermediateOutput[i].POSId  +"\n"+ "rootword: "+IntermediateOutput[i].rootWord +"\n"+ "DoerSuffix :"+IntermediateOutput[i].doerSuffix +"\n"+ "DoerSuffixid :"+IntermediateOutput[i].doerSuffixId);
					break;
				case "நாங்கள்":
				case "எங்கள்" :
					pronoun=true;
					IntermediateOutput[i].rootWord=words[i];
					IntermediateOutput[i].POS="pronoun";
					IntermediateOutput[i].POSId=2;
					IntermediateOutput[i].doerSuffix="ஓம்";
					IntermediateOutput[i].doerSuffixId=2;
					System.out.println("\n"+ words[i] +"\n"+ "POS :"+IntermediateOutput[i].POS +"\n"+ "POSid :"+IntermediateOutput[i].POSId  +"\n"+ "rootword: "+IntermediateOutput[i].rootWord +"\n"+ "DoerSuffix :"+IntermediateOutput[i].doerSuffix +"\n"+ "DoerSuffixid :"+IntermediateOutput[i].doerSuffixId);
					break;
				case "நீ":
				case "உன்" :
					pronoun=true;
					IntermediateOutput[i].rootWord=words[i];
					IntermediateOutput[i].POS="pronoun";
					IntermediateOutput[i].POSId=2;
					IntermediateOutput[i].doerSuffix="ஆய்";
					IntermediateOutput[i].doerSuffixId=3;
					System.out.println("\n"+ words[i] +"\n"+ "POS :"+IntermediateOutput[i].POS +"\n"+ "POSid :"+IntermediateOutput[i].POSId  +"\n"+ "rootword: "+IntermediateOutput[i].rootWord +"\n"+ "DoerSuffix :"+IntermediateOutput[i].doerSuffix +"\n"+ "DoerSuffixid :"+IntermediateOutput[i].doerSuffixId);
					break;
				case "நீர்":
					pronoun=true;
					IntermediateOutput[i].rootWord="நீர்";
					IntermediateOutput[i].POS="pronoun";
					IntermediateOutput[i].POSId=2;
					IntermediateOutput[i].doerSuffix="ஈர்";
					IntermediateOutput[i].doerSuffixId=4;
					System.out.println("\n"+ words[i] +"\n"+ "POS :"+IntermediateOutput[i].POS +"\n"+ "POSid :"+IntermediateOutput[i].POSId  +"\n"+ "rootword: "+IntermediateOutput[i].rootWord +"\n"+ "DoerSuffix :"+IntermediateOutput[i].doerSuffix +"\n"+ "DoerSuffixid :"+IntermediateOutput[i].doerSuffixId);
					break;
				case "நீங்கள்":
				case "உங்கள்" :
					pronoun=true;
					IntermediateOutput[i].rootWord=words[i];
					IntermediateOutput[i].POS="pronoun";
					IntermediateOutput[i].POSId=2;
					IntermediateOutput[i].doerSuffix="ஈர்கள்";
					IntermediateOutput[i].doerSuffixId=5;
					System.out.println("\n"+ words[i] +"\n"+ "POS :"+IntermediateOutput[i].POS +"\n"+ "POSid :"+IntermediateOutput[i].POSId  +"\n"+ "rootword: "+IntermediateOutput[i].rootWord +"\n"+ "DoerSuffix :"+IntermediateOutput[i].doerSuffix +"\n"+ "DoerSuffixid :"+IntermediateOutput[i].doerSuffixId);
					break;
				case "அவன்":
					pronoun=true;
					IntermediateOutput[i].rootWord="அவன்";
					IntermediateOutput[i].POS="pronoun";
					IntermediateOutput[i].POSId=2;
					IntermediateOutput[i].doerSuffix="ஆன்";
					IntermediateOutput[i].doerSuffixId=6;
					System.out.println("\n"+ words[i] +"\n"+ "POS :"+IntermediateOutput[i].POS +"\n"+ "POSid :"+IntermediateOutput[i].POSId  +"\n"+ "rootword: "+IntermediateOutput[i].rootWord +"\n"+ "DoerSuffix :"+IntermediateOutput[i].doerSuffix +"\n"+ "DoerSuffixid :"+IntermediateOutput[i].doerSuffixId);
					break;
				case "அவள்":
					pronoun=true;
					IntermediateOutput[i].rootWord="அவள்";
					IntermediateOutput[i].POS="pronoun";
					IntermediateOutput[i].POSId=2;
					IntermediateOutput[i].doerSuffix="ஆள்";
					IntermediateOutput[i].doerSuffixId=7;
					System.out.println("\n"+ words[i] +"\n"+ "POS :"+IntermediateOutput[i].POS +"\n"+ "POSid :"+IntermediateOutput[i].POSId  +"\n"+ "rootword: "+IntermediateOutput[i].rootWord +"\n"+ "DoerSuffix :"+IntermediateOutput[i].doerSuffix +"\n"+ "DoerSuffixid :"+IntermediateOutput[i].doerSuffixId);
					break;
				case "அவர்":
					pronoun=true;
					IntermediateOutput[i].rootWord="அவர்";
					IntermediateOutput[i].POS="pronoun";
					IntermediateOutput[i].POSId=2;
					IntermediateOutput[i].doerSuffix="ஆர்";
					IntermediateOutput[i].doerSuffixId=8;
					System.out.println("\n"+ words[i] +"\n"+ "POS :"+IntermediateOutput[i].POS +"\n"+ "POSid :"+IntermediateOutput[i].POSId  +"\n"+ "rootword: "+IntermediateOutput[i].rootWord +"\n"+ "DoerSuffix :"+IntermediateOutput[i].doerSuffix +"\n"+ "DoerSuffixid :"+IntermediateOutput[i].doerSuffixId);
					break;
				case "அவர்கள்":
					pronoun=true;
					IntermediateOutput[i].rootWord=words[i];
					IntermediateOutput[i].POS="pronoun";
					IntermediateOutput[i].POSId=2;
					IntermediateOutput[i].doerSuffix="ஆர்கள்";
					IntermediateOutput[i].doerSuffixId=9;
					System.out.println("\n"+ words[i] +"\n"+ "POS :"+IntermediateOutput[i].POS +"\n"+ "POSid :"+IntermediateOutput[i].POSId  +"\n"+ "rootword: "+IntermediateOutput[i].rootWord +"\n"+ "DoerSuffix :"+IntermediateOutput[i].doerSuffix +"\n"+ "DoerSuffixid :"+IntermediateOutput[i].doerSuffixId);
					break;
				case "அது":
					pronoun=true;
					IntermediateOutput[i].rootWord="அது";
					IntermediateOutput[i].POS="pronoun";
					IntermediateOutput[i].POSId=2;
					IntermediateOutput[i].doerSuffix="அது";
					IntermediateOutput[i].doerSuffixId=11;
					System.out.println("\n"+ words[i] +"\n"+ "POS :"+IntermediateOutput[i].POS +"\n"+ "POSid :"+IntermediateOutput[i].POSId  +"\n"+ "rootword: "+IntermediateOutput[i].rootWord +"\n"+ "DoerSuffix :"+IntermediateOutput[i].doerSuffix +"\n"+ "DoerSuffixid :"+IntermediateOutput[i].doerSuffixId);
					break;
				case "அவை":
					pronoun=true;
					IntermediateOutput[i].rootWord="அவை";
					IntermediateOutput[i].POS="pronoun";
					IntermediateOutput[i].POSId=2;
					IntermediateOutput[i].doerSuffix="அன";
					IntermediateOutput[i].doerSuffixId=12;
					System.out.println("\n"+ words[i] +"\n"+ "POS :"+IntermediateOutput[i].POS +"\n"+ "POSid :"+IntermediateOutput[i].POSId  +"\n"+ "rootword: "+IntermediateOutput[i].rootWord +"\n"+ "DoerSuffix :"+IntermediateOutput[i].doerSuffix +"\n"+ "DoerSuffixid :"+IntermediateOutput[i].doerSuffixId);
					break;
				case "அவைகள்":
					pronoun=true;
					IntermediateOutput[i].rootWord="அவைகள்";
					IntermediateOutput[i].POS="pronoun";
					IntermediateOutput[i].POSId=2;
					IntermediateOutput[i].doerSuffix="அன";
					IntermediateOutput[i].doerSuffixId=12;
					System.out.println("\n"+ words[i] +"\n"+ "POS :"+IntermediateOutput[i].POS +"\n"+ "POSid :"+IntermediateOutput[i].POSId  +"\n"+ "rootword: "+IntermediateOutput[i].rootWord +"\n"+ "DoerSuffix :"+IntermediateOutput[i].doerSuffix +"\n"+ "DoerSuffixid :"+IntermediateOutput[i].doerSuffixId);
					break;
				case "தன்":
				case "அங்கு":
				case "இங்கு":/*
				case "அவனை":
				case "அவளை":
				case "என்னை":
				case "தன்னை":
				case "உன்ன்ன ":
				case "உங்களை":
				case "எங்களை":
				case "அவரை":
				case "அவர்களை":
				case "அதை":
				case "அவைகளை":
				case "உங்களுக்கு":*/
					pronoun=true;
					IntermediateOutput[i].rootWord=words[i];
					IntermediateOutput[i].POS="pronoun";
					IntermediateOutput[i].POSId=2;
					System.out.println("\n"+ words[i] +"\n"+ "POS :"+IntermediateOutput[i].POS +"\n"+ "POSid :"+IntermediateOutput[i].POSId  +"\n"+ "rootword: "+IntermediateOutput[i].rootWord);
					break;
				case "அந்த" :
				case "இந்த" :
					pronoun=true;
					IntermediateOutput[i].rootWord=words[i];
					IntermediateOutput[i].POS="mod";
					IntermediateOutput[i].POSId=2;
					System.out.println("\n"+ words[i] +"\n"+ "POS :"+IntermediateOutput[i].POS +"\n"+ "POSid :"+IntermediateOutput[i].POSId  +"\n"+ "rootword: "+IntermediateOutput[i].rootWord);
					break;
				case "அவனுடைய":
				case "அவளுடைய":
					pronoun=true;
					IntermediateOutput[i].rootWord=words[i]+" " +words[i+1];
					IntermediateOutput[i].POS="pronoun";
					IntermediateOutput[i].POSId=2;
					words = removeElements(words, words[i+1]);
					unwanted++;
					System.out.println("\n"+ words[i] +"\n"+ "POS :"+IntermediateOutput[i].POS +"\n"+ "POSid :"+IntermediateOutput[i].POSId  +"\n"+ "rootword: "+IntermediateOutput[i].rootWord);
					break;
				}
				if(pronoun)
				{	//checkDB(IntermediateOutput[i]);
					continue;}

				//வினைமுற்று விகுதிகள்
				pat = Pattern.compile(".(ப்பர்|வர்|தேன்|தாய்|தீர்|தீர்கள்|தான்|தாள்|தார்|தார்கள்|தனர்|தோம்|தது|தன|டேன்|டாய்|டீர்|டீர்கள்|டான்|டாள்|டார்|டார்கள்|டனர்|டோம்|டது|டன|றேன்|றாய்|றீர்|றீர்கள்|றான்|றாள்|றார்|றார்கள்|றனர்|றோம்|றது|றன|னேன்|னாய்|னீர்|னீர்கள்|னான்|னாள்|னார்|னார்கள்|டினர்|னோம்|யது|டின|கிறேன்|கிறோம்|கிறாய்|கிறீர்கள்|கிறான்|கிறாள்|கிறார்கள்|கிறார்|கிறன|கிறது|கின்றேன்|கின்றோம்|கின்றாய்|கின்றீர்கள்|கின்றான்|கின்றாள்|கின்றார்கள்|கின்றார்|கின்றன|கின்றது|ப்பேன்|ப்பாய்|ப்பீர்|ப்பீர்கள்|ப்பான்|ப்பாள்|ப்பார்|ப்பார்கள்|ப்பனர்|ப்போம்|ப்பது|ப்பன|வேன்|வாய்|வீர்|வீர்கள்|வான்|வாள்|வார்|வார்கள்|வனர்|வோம்|வது|வன)$");
				matcher = pat.matcher(words[i]);    
				if(matcher.find()){
					IntermediateOutput[i] = Verb.analyse(words[i]);
					IntermediateOutput[i].inflectedWord=words[i];
					System.out.println("\n"+ words[i] +"\n"+ "POS :"+IntermediateOutput[i].POS +"\n"+ "POSid :"+IntermediateOutput[i].POSId  +"\n"+ "rootword: "+IntermediateOutput[i].rootWord);
					//checkDB(IntermediateOutput[i]);
					continue;
				}

				//Inflected பெயர்சொல் 
				pat = Pattern.compile("(யை|யால்|க்கு|யில்|யின்|யினது|யின்கண்|லை|லால்|லிற்கு|லிற்குள்|லுக்கு|லுக்குள்|லில்|லின்|லினது|ல்கள்|ல்களால்|ற்கள்|ற்களால்|லின்கண்|கை|கால்|கிற்குள்|குக்குள்|கிற்கு|குக்கு|கில்|கின்|கினது|குகள்|குகளாள்|கின்கண்|த்தை|த்தால்|த்துக்குள்|த்திற்குள்|த்துக்கு|த்திற்கு|த்தில்|த்தின்|த்தினது|ங்கள்|ங்களால்|த்தின்கண்|பை|பால்|புக்குள்|பிற்குள்|புக்கு|பிற்கு|பில்|பின்|பினது|புகள்|புகளால்|பின்கண்|ட்டை|ட்டால்|ட்டுக்குள்|ட்டிற்குள்|ட்டுக்கு|ட்டிற்கு|ட்டில்|ட்டின்|ட்டினது|டுகள்|டுகளால்|ட்டின்கண்|ளை|ளால்|ளுக்குள்|ளிற்குள்|ளுக்கு|ளிற்கு|ளில்|ளின்|ளினது|ள்கள்|ள்களால்|ளின்கண்|தை|தால்|திற்குள்|துக்குள்|திற்கு|துக்கு|தில்|தின்|தினது|க்கள்|க்களால்|தின்கண்|ணை|ணால்|ணுக்குள்|ணிற்குள்|ணுக்கு|ணிற்கு|ணில்|ணின்|ணினது|ண்கள்|ண்களால்|ணின்கண்|ணை|ணால்|ணுக்குள்|ணிற்குள்|ணுக்கு|ணிற்கு|ணில்|ணின்|ணினது|ண்கள்|ண்களால்|ணின்கண்|ரை|ரால்|ருக்குள்|ரிற்குள்|ருக்கு|ரிற்கு|ரில்|ரின்|ரினது|ர்கள்|ர்களால்|ரின்கண்|வை|வால்|வுக்குள்|விற்குள்|வுக்கு|விற்கு|வில்|வின்|வினது|வின்கண்|னை|னால்|னுக்குள்|னிற்குள்|னுக்கு|னிற்கு|னில்|னின்|னினது|ன்கள்|ன்களாள்|னின்கண்)$");
				matcher = pat.matcher(words[i]);    
				if(matcher.find()){
					IntermediateOutput[i] = Noun.analyse(words[i]);
					IntermediateOutput[i].inflectedWord=words[i];
					//System.out.println("\n"+ words[i] +"\n"+ "POS :"+IntermediateOutput[i].POS +"\n"+ "POSid :"+IntermediateOutput[i].POSId  +"\n"+ "rootword: "+IntermediateOutput[i].rootWord);
					//checkDB(IntermediateOutput[i]);
					continue;
				}

				//Adverb
				pat = Pattern.compile("ாக$");
				matcher = pat.matcher(words[i]);    
				if(matcher.find()){
					IntermediateOutput[i].rootWord=words[i];
					IntermediateOutput[i].POS="adverb";
					IntermediateOutput[i].POSId=4;
					System.out.println("\n"+ words[i] +"\n"+ "POS :"+IntermediateOutput[i].POS +"\n"+ "POSid :"+IntermediateOutput[i].POSId  +"\n"+ "rootword: "+IntermediateOutput[i].rootWord);
					//checkDB(IntermediateOutput[i]);
					continue;
				}

				//Adjective
				pat = Pattern.compile("(ான|ய|நீண்ட)$");
				matcher = pat.matcher(words[i]);    
				if(matcher.find()){
					IntermediateOutput[i].rootWord=words[i];
					IntermediateOutput[i].POS="adjective";
					IntermediateOutput[i].POSId=5;
					System.out.println("\n"+ words[i] +"\n"+ "POS :"+IntermediateOutput[i].POS +"\n"+ "POSid :"+IntermediateOutput[i].POSId  +"\n"+ "rootword: "+IntermediateOutput[i].rootWord);
					//checkDB(IntermediateOutput[i]);
					continue;
				}	

				//CHECK IF ITS AN NOT INFLECTED OBJECT
				String query = "SELECT distinct * FROM wordnetpingtable"+" where"+" word like "+"'"+words[i]+"' and " +" pos like "+"'noun';";
				System.out.println(query);
				ResultSet res = st.executeQuery(query);
				boolean flag=false;
				while(res.next()){
					flag=true;
					//					String dbPOS = res.getString("pos");
					IntermediateOutput[i].rootWord=words[i];
					//					if(dbPOS.equals("noun")){
					IntermediateOutput[i].POS="object";
					IntermediateOutput[i].POSId=1;
					//					}
					//					else{
					//						IntermediateOutput[i].POS=dbPOS;
					//						IntermediateOutput[i].POSId=22;
					//					}
					System.out.println("\n"+ words[i] +"\n"+ "POS :"+IntermediateOutput[i].POS +"\n"+ "POSid :"+IntermediateOutput[i].POSId  +"\n"+ "rootword: "+IntermediateOutput[i].rootWord);
					break;
				}
				if(flag)continue;

				//எச்சம்
				pat = Pattern.compile("(த்த|த்து|ந்த|ந்து|ப்பு|ட|டு|ன்ற|ன்று|ற்ற|ற்று|கிற|கின்ற|ரும்|லும்|ளும்|க்கும்|கும்|வும்|யும்)$");
				matcher = pat.matcher(words[i]);        
				if(matcher.find())
				{
					IntermediateOutput[i] = Echam.analyse(words[i]);
					IntermediateOutput[i].inflectedWord=words[i];
					//checkDB(IntermediateOutput[i]);
					System.out.println("\n"+ words[i] +"\n"+ "POS :"+IntermediateOutput[i].POS +"\n"+ "POSid :"+IntermediateOutput[i].POSId  +"\n"+ "rootword: "+IntermediateOutput[i].rootWord);
					continue;
				}


				//Mostly a named entity
				IntermediateOutput[i].rootWord=words[i];
				IntermediateOutput[i].POS="subject";
				IntermediateOutput[i].POSId=0;
				System.out.println("\n"+ words[i] +"\n"+ "POS :"+IntermediateOutput[i].POS +"\n"+ "POSid :"+IntermediateOutput[i].POSId  +"\n"+ "rootword: "+IntermediateOutput[i].rootWord);
				//checkDB(IntermediateOutput[i]);
				continue;
			}

		}catch(Exception e){
			e.printStackTrace();
		}

		//SPLIT PEYARECHAM AND GENERATE TWO SENTENCES
		SegmentedWord containerForEcham = new SegmentedWord();
		int echamPosition=99,connectorPosition=99;
		boolean pronounexists=false,echamexists=false,connectorexists=false;
		SegmentedWord IntermediateOutput2[] = removeSegmentedWords(IntermediateOutput,unwanted);
		for(int j=0;j<IntermediateOutput2.length;j++){
			if(IntermediateOutput2[j].POSId==2){
				pronounexists=true;
				System.err.println("Pronoun found");
				containerForEcham.doerSuffixId=IntermediateOutput2[j].doerSuffixId;
				containerForEcham.doerSuffix=IntermediateOutput2[j].doerSuffix;
			}else if(IntermediateOutput2[j].POSId==7){
				System.err.println("Echam found");
				echamexists=true;
				echamPosition=j;
			}else if(IntermediateOutput2[j].POSId==777){
				if(connector.contains(IntermediateOutput2[j].rootWord))
				{System.err.println("Connector found");connectorexists=true;connectorPosition=j;break;}
			}else
				continue;
		}
		if(pronounexists && echamexists && connectorexists && echamPosition!=99 && connectorPosition!=99){
			System.err.println("qualified for splitting");
			IntermediateOutput2[echamPosition].POS="verb";
			IntermediateOutput2[echamPosition].POSId=3;
			IntermediateOutput2[echamPosition].doerSuffixId=containerForEcham.doerSuffixId;
			IntermediateOutput2[echamPosition].doerSuffix=containerForEcham.doerSuffix;
			SegmentedWord SlicedSentences[]=addDelimiter(IntermediateOutput2,connectorPosition);
			//			for(SegmentedWord swtemp:SlicedSentences){
			//				System.err.println("\nPOS :"+swtemp.POS +"\n"+ "POSid :"+swtemp.POSId  +"\n"+ "rootword: "+swtemp.rootWord);
			//			}
			return SlicedSentences;
		}

		//		for(SegmentedWord swtemp:IntermediateOutput2){
		//			System.err.println("\nPOS :"+swtemp.POS +"\n"+ "POSid :"+swtemp.POSId  +"\n"+ "rootword: "+swtemp.rootWord);
		//		}
		return IntermediateOutput2;
	}
	private SegmentedWord[] addDelimiter(SegmentedWord[] input, int connectorPosition) {
		SegmentedWord temp[] = new  SegmentedWord[input.length+1];
		SegmentedWord dummy = new SegmentedWord();
		dummy.POS="delimiter";
		dummy.POSId=10;
		for(int i=0;i<temp.length;i++){
			if(i<connectorPosition){
				temp[i]=input[i];
			}else if(i>connectorPosition){
				temp[i]=input[i-1];
			}else{
				temp[i]=dummy;
			}
		}
		return temp;
	}

	private void checkDB(SegmentedWord segmentedWord) {
		try{ 
			String url = "jdbc:mysql://localhost:3306/"; 
			String dbName = "tamilwordnet"; 
			String driver = "com.mysql.jdbc.Driver"; 
			String userName = "root"; 
			String password = "";
			Class.forName(driver).newInstance(); 
			Connection conn = DriverManager.getConnection(url+dbName+"?characterEncoding=UTF-8",userName,password);
			Statement st = conn.createStatement();
			System.out.println(segmentedWord.POS);
			String query = "SELECT distinct * FROM wordnetpingtable"+" where"+" word like "+"'"+segmentedWord.inflectedWord+"';";
			System.out.println(query);
			ResultSet res = st.executeQuery(query); 
			while(res.next()){
				String dbPOS = res.getString("pos");
				System.err.println(segmentedWord.inflectedWord+"\t"+dbPOS);
			}
			query = "SELECT distinct * FROM wordnetpingtable"+" where"+" word like "+"'"+segmentedWord.rootWord+"';";
			System.out.println(query);
			res = st.executeQuery(query); 
			while(res.next()){
				String dbPOS = res.getString("pos");
				System.err.println(segmentedWord.rootWord+"\t"+dbPOS);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void main(String args[]) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		analyser a = new analyser();
		a.analyzeText("     சந்திரனை மணி அடித்தது  ");
		//a.analyzeText("கன்னியாகுமரியில் இருந்து சென்னை வரை நான் நடந்தேன்");
		//a.analyzeText("இந்தக் கடிதம் எங்களுக்கு காலை வந்தது");
		//a.analyzeText("அவன் மழையில்  சென்ற போது மரம் விழுந்தது");
		//a.analyzeText("அவர்கள் கேசட் கடைக்குச் செல்கிறார்கள்");
		//a.analyzeText("காட்டுக்குள் சென்று ராஜ்குமாரை விடுவிக்கும்படி சந்தன வீரப்பனை கேட்டுக் கொள்வேன்");	
		//a.analyzeText("பிடியில் இருந்து நக்கீரன் கோபால் மீட்டு வருவார் என்று நம்புகிறோம்");
		//a.analyzeText("கண்ணகி மதுரையை எரித்தாள்");
		//a.analyzeText("பாரக் கீழே விழுந்தான்");
		//a.analyzeText("இன்று மழை பெய்யும் அல்லது பலமான காற்று அடிக்கும்");
		System.out.println("Completed");
	}
}
