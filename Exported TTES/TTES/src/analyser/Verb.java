package analyser;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Verb {
	static String inflectedverb;
	public static String root;
	static SegmentedWord sw;

	public static SegmentedWord analyse(String s){
		sw = new SegmentedWord();
		inflectedverb=s;
		sw.POS = "verb";
		sw.POSId = 3;
		Pattern pat;
		Matcher matcher;
		//Nedil ned = new Nedil();

		pat = Pattern.compile("(தேன்|தாய்|தீர்|தீர்கள்|தான்|தாள்|தார்|தார்கள்|தனர்|தோம்|தது|தன)$");
		matcher = pat.matcher(inflectedverb);        
		if(matcher.find())
		{
			//			System.out.println("\nஇற்ந்தகாலம் த் இடைநிலை");
			String[] splitString = inflectedverb.split("(தேன்|தாய்|தீர்|தீர்கள்|தான்|தாள்|தார்|தார்கள்|தனர்|தோம்|தது|தன)$");
			root = splitString[0];
			if(splitString[0].endsWith("த்")||splitString[0].endsWith("ந்"))
			{
				splitString = (splitString[0].split("\\p{L}\\p{M}$"));
				root = splitString[0];
			}
			if(root.equals("த"))
				root = "தா";
			if(root.equals("வ"))
				root = "வா";
			sw.tenseSuffix="த்";
			sw.tenseSuffixId=1;
			sw.rootWord=root;
		}

		pat = Pattern.compile("(டேன்|டாய்|டீர்|டீர்கள்|டான்|டாள்|டார்|டார்கள்|டனர்|டோம்|டது|டன)$");
		matcher = pat.matcher(inflectedverb);        
		if(matcher.find())
		{
			//			System.out.println("\nஇற்ந்தகாலம் ட் இடைநிலை");
			String[] splitString = inflectedverb.split("(டேன்|டாய்|டீர்|டீர்கள்|டான்|டாள்|டார்|டார்கள்|டனர்|டோம்|டது|டன)$");
			root = splitString[0];
			if(root.endsWith("ட்"))
			{splitString = (splitString[0].split("\\p{L}\\p{M}$"));
			root = splitString[0]+"டு";}
			else if(root.equals("கண்"))
				root="காண்";
			sw.tenseSuffix="ட்";
			sw.tenseSuffixId=1;
			sw.rootWord=root;
		}
		
		pat = Pattern.compile("(றேன்|றாய்|றீர்|றீர்கள்|றான்|றாள்|றார்|றார்கள்|றனர்|றோம்|றது|றன)$");
		matcher = pat.matcher(inflectedverb);        
		if(matcher.find())
		{
			//			System.out.println("\nஇற்ந்தகாலம் ற் இடைநிலை");
			String[] splitString = inflectedverb.split("(றேன்|றாய்|றீர்|றீர்கள்|றான்|றாள்|றார்|றார்கள்|றனர்|றோம்|றது|றன)$");
			root = splitString[0];
			if(root.endsWith("ற்")||root.endsWith("ன்"))
			{splitString = (splitString[0].split("\\p{L}\\p{M}$"));
			root = splitString[0]+"ல்";}
			sw.tenseSuffix="ற்";
			sw.tenseSuffixId=1;
			sw.rootWord=root;
		}

		pat = Pattern.compile("(னேன்|னாய்|னீர்|னீர்கள்|னான்|னாள்|னார்|னார்கள்|டினர்|னோம்|யது|டின)$");
		matcher = pat.matcher(inflectedverb);        
		if(matcher.find())
		{
			//			System.out.println("\nஇற்ந்தகாலம் இன் இடைநிலை");
			String[] splitString = inflectedverb.split("(னேன்|னாய்|னீர்|னீர்கள்|னான்|னாள்|னார்|னார்கள்|னர்|னோம்|யது|ன)$");
			root = splitString[0];
			
			if(root.endsWith("ி"))
			{splitString = (splitString[0].split("\\p{M}$"));
			root = splitString[0]+"ு";}
			sw.tenseSuffix="இன்";
			sw.tenseSuffixId=1;
			sw.rootWord=root;
		}
		
		pat = Pattern.compile("(கிறேன்|கிறோம்|கிறாய்|கிறீர்கள்|கிறான்|கிறாள்|கிறார்கள்|கிறார்|கிறன|கிறது)$");
		matcher = pat.matcher(inflectedverb);        
		if(matcher.find())        
		{
			//System.out.println("\nநிகழ்காலம் கிறு இடைநிலை");
			String[] splitString = inflectedverb.split("(கிறேன்|கிறோம்|கிறாய்|கிறீர்கள்|கிறான்|கிறாள்|கிறார்கள்|கிறார்|கிறன|கிறது)");
			root = splitString[0];
			if(root.endsWith("க்")||root.endsWith("ங்"))
			{
				//நடக்க்கிறான்
				splitString = (splitString[0].split("\\p{L}\\p{M}$"));
				root = splitString[0];
				//root.charAt(root.length()-1)
			}
			else if(splitString[0].endsWith("ற்"))
			{
				//கற்கிறான்
				root = splitString[0].replace("ற்", "ல்");
			}
			else if(splitString[0].endsWith("ட்"))
			{
				//கேட்கிறான் 
				root = splitString[0].replace("ட்", "ள்");
			}
			if(root.endsWith("ரு")&&!root.equals("இரு"))
			{
				//வருகிறான்
				splitString = (splitString[0].split("\\p{L}\\p{M}$"));
				root = splitString[0]+"ா";             
			}
			sw.tenseSuffix="கிறு";
			sw.tenseSuffixId=5;
			sw.rootWord=root;
		}        

		pat = Pattern.compile("(கின்றேன்|கின்றோம்|கின்றாய்|கின்றீர்கள்|கின்றான்|கின்றாள்|கின்றார்கள்|கின்றார்|கின்றன|கின்றது)$");
		matcher = pat.matcher(inflectedverb);        
		if(matcher.find())        
		{
			//System.out.println("\nநிகழ்காலம் கிறு இடைநிலை");
			String[] splitString = inflectedverb.split("(கின்றேன்|கின்றோம்|கின்றாய்|கின்றீர்கள்|கின்றான்|கின்றாள்|கின்றார்கள்|கின்றார்|கின்றன|கின்றது)");
			root = splitString[0];
			if(root.endsWith("க்")||root.endsWith("ங்"))
			{
				//நடக்க்கிறான்
				splitString = (splitString[0].split("\\p{L}\\p{M}$"));
				root = splitString[0];
				//root.charAt(root.length()-1)
			}
			else if(splitString[0].endsWith("ற்"))
			{
				//கற்கிறான்
				root = splitString[0].replace("ற்", "ல்");
			}
			else if(splitString[0].endsWith("ட்"))
			{
				//கேட்கிறான் 
				root = splitString[0].replace("ட்", "ள்");
			}
			if(root.endsWith("ரு")&& !root.equals("இரு"))
			{
				//வருகிறான்
				splitString = (splitString[0].split("\\p{L}\\p{M}$"));
				root = splitString[0]+"ா";             
			}
			sw.tenseSuffix="கிறு";
			sw.tenseSuffixId=5;
			sw.rootWord=root;
		}        
		
		pat = Pattern.compile("(ப்பர்|ப்பேன்|ப்பாய்|ப்பீர்|ப்பீர்கள்|ப்பான்|ப்பாள்|ப்பார்|ப்பார்கள்|ப்பனர்|ப்போம்|ப்பது|ப்பன)");
		matcher = pat.matcher(inflectedverb);        
		if(matcher.find())
		{
			//System.out.println("\nஎதிர்காலம் ப் இடைநிலை");
			String[] splitString = inflectedverb.split("(ப்பர்|ப்பேன்|ப்பாய்|ப்பீர்|ப்பீர்கள்|ப்பான்|ப்பாள்|ப்பார்|ப்பார்கள்|ப்பனர்|ப்போம்|ப்பது|ப்பன)");
			root = splitString[0];
			sw.tenseSuffix="ப்";
			sw.tenseSuffixId=8;
			sw.rootWord=root;
		}
		
		pat = Pattern.compile("(வர்|வேன்|வாய்|வீர்|வீர்கள்|வான்|வாள்|வார்|வார்கள்|வனர்|வோம்|வது|வன)");
		matcher = pat.matcher(inflectedverb);        
		if(matcher.find())
		{
			//System.out.println("\nஎதிர்காலம் வ் இடைநிலை");
			String[] splitString = inflectedverb.split("(வர்|வேன்|வாய்|வீர்|வீர்கள்|வான்|வாள்|வார்|வார்கள்|வனர்|வோம்|வது|வன)");
			root = splitString[0];
			if(root.endsWith("ரு"))
				root = root.substring(0, root.length()-2) +"ா";
			sw.tenseSuffix="வ்";
			sw.tenseSuffixId=9;
			sw.rootWord=root;
		}
		
		if(inflectedverb.endsWith("ேன்")){
			sw.doerSuffix = "ஏன்";
			sw.doerSuffixId = 1;
			sw.acquired_attributes.add("singular");
			//			System.out.println("தன்மை - ஒருமை");			
		}
		else if(inflectedverb.endsWith("ோம்")){
			sw.doerSuffix = "ஓம்";
			sw.doerSuffixId = 2;
			//			System.out.println("தன்மை - பன்மை");
		}
		else if(inflectedverb.endsWith("ாய்")){
			sw.doerSuffix = "ஆய்";
			sw.doerSuffixId = 3;
			sw.acquired_attributes.add("singular");
			//			System.out.println("முன்னிலலை - ஒருமை");
		}
		else if(inflectedverb.endsWith("ீர்")){
			sw.doerSuffix = "ஈர் ";
			sw.doerSuffixId = 4;
			sw.acquired_attributes.add("singular");
			//			System.out.println("முன்னிலலை - பன்மை");
		}
		else if(inflectedverb.endsWith("ீர்கள்")){
			sw.doerSuffix = "ஈர்கள் ";
			sw.doerSuffixId = 5;
			sw.acquired_attributes.add("plural");
			//			System.out.println("முன்னிலலை - பன்மை");
		}
		else if(inflectedverb.endsWith("ான்")){
			sw.doerSuffix="ஆன்";
			sw.doerSuffixId=6;
			sw.acquired_attributes.add("singular");
			//			System.out.println("படர்கை - ஒருமை - ஆண்பால்");
		}
		else if(inflectedverb.endsWith("ாள்")){
			sw.doerSuffix = "ஆள்  ";
			sw.doerSuffixId = 7;
			sw.acquired_attributes.add("singular");
			//			System.out.println("படர்கை - ஒருமை - பெண்பால்");
		}
		else if(inflectedverb.endsWith("ார்")){
			sw.doerSuffix = "ஆர்  ";
			sw.doerSuffixId = 8;
			sw.acquired_attributes.add("singular");
			//			System.out.println("முன்னிலலை - ஒருமை ");
		}
		else if(inflectedverb.endsWith("ார்கள்")||inflectedverb.endsWith("வர்")||inflectedverb.endsWith("ப்பர்")){
			sw.doerSuffix = "ஆர்கள் ";
			sw.doerSuffixId = 9;
			sw.acquired_attributes.add("plural");
			//			System.out.println("முன்னிலலை - பன்மை ");
		}
		else if(inflectedverb.endsWith("னர்")){
			sw.doerSuffix = "னர் ";
			sw.doerSuffixId = 10;
			sw.acquired_attributes.add("plural");
			//			System.out.println("அஃறினை - பன்மை ");
		}
		else if(inflectedverb.endsWith("து")){
			sw.doerSuffix = "அது ";
			sw.doerSuffixId = 11;
			sw.acquired_attributes.add("singular");
			//			System.out.println("அஃறினை - ஒருமை");
		}		
		else if(inflectedverb.endsWith("ன")){
			sw.doerSuffix = "அன ";
			sw.doerSuffixId = 12;
			sw.acquired_attributes.add("plural");
			//			System.out.println("அஃறினை - பன்மை ");
		}
		return sw;
	}

	public static void main(String args[]){
		String test[] = {"இருக்கிறது","கிறுக்கினான்","பாடினார்கள்","நட்டான்","கற்றான்","கற்கிறேன்","கேட்கிறது","வருகிறான்","வாங்குவான்","பெறுவான்","அழிப்பான்","எரித்தான்"};
		for(String s:test){
			Verb.analyse(s);
			System.out.println("\n"+ inflectedverb +"\n"+ "POS :"+sw.POS +"\n"+ "POSid :"+sw.POSId +"\n"+ "rootword: "+sw.rootWord +"\n"+ "TenseSuffix :"+sw.tenseSuffix +"\n"+ "TenseSuffixid :"+sw.tenseSuffixId +"\n"+ "DoerSuffix :"+sw.doerSuffix +"\n"+ "DoerSuffixid :"+sw.doerSuffixId);
		}
	}

}
