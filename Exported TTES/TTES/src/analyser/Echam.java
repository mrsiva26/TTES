package analyser;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Echam {
	static String inflectedverb;
	public static String root;
	static SegmentedWord sw;

	public static SegmentedWord analyse(String s){
		sw = new SegmentedWord();
		inflectedverb=s;
		sw.POS = "echam";
		sw.POSId = 7;
		Pattern pat;
		Matcher matcher;
		
		//Nedil ned = new Nedil();
		//த், ட், ற், இன்
		
		pat = Pattern.compile("(த்த|த்து|ந்த|ந்து|ப்பு)$");
		matcher = pat.matcher(inflectedverb);        
		if(matcher.find())
		{
			//System.out.println("\nஇறந்தகாலம் த் இடைநிலை");
			String[] splitString = inflectedverb.split("(த்து|ந்து)");
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
			if(root.equals("சென்"))
				root = "செல்";
			sw.rootWord=root;
			sw.tenseSuffix="த்";
			sw.tenseSuffixId=1;
			sw.acquired_attributes.add("past");
		}
		
		pat = Pattern.compile("(ட|டு)$");
		matcher = pat.matcher(inflectedverb);        
		if(matcher.find())
		{
			//			System.out.println("\nஇற்ந்தகாலம் ட் இடைநிலை");
			String[] splitString = inflectedverb.split("(ட|டு)$");
			root = splitString[0];
			if(root.equals("கண்"))
				root="காண்";
			if(root.endsWith("ட்"))
			root = splitString[0]+"டு";
			sw.rootWord=root;
			sw.tenseSuffix="ட்";
			sw.tenseSuffixId=1;
			sw.acquired_attributes.add("past");
		}
		
		pat = Pattern.compile("(ன்ற|ன்று|ற்ற|ற்று)$");
		matcher = pat.matcher(inflectedverb);        
		if(matcher.find())
		{
			//			System.out.println("\nஇற்ந்தகாலம் ற் இடைநிலை");
			String[] splitString = inflectedverb.split("(ன்ற|ன்று|ற்ற|ற்று)$");
			root = splitString[0]+"ல்";
			sw.rootWord=root;
			sw.tenseSuffix="ற்";
			sw.tenseSuffixId=1;
			sw.acquired_attributes.add("past");
		}
		
		pat = Pattern.compile("(ி)$");
		matcher = pat.matcher(inflectedverb);        
		if(matcher.find())
		{
			//			System.out.println("\nஇற்ந்தகாலம் இன் இடைநிலை");
			root = inflectedverb;
			if(root.endsWith("ி"))
			{root = root.split("\\p{M}$")[0];
			root = root+"ு";}
			sw.rootWord=root;
			sw.tenseSuffix="இன்";
			sw.tenseSuffixId=1;
			sw.acquired_attributes.add("past");
		}

		pat = Pattern.compile("(கிற|கின்ற)$");
		matcher = pat.matcher(inflectedverb);        
		if(matcher.find())        
		{
			System.out.println("\nநிகழ்காலம் கிறு இடைநிலை");
			String[] splitString = inflectedverb.split("(கிற|கின்ற)$");
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
			sw.rootWord=root;
			sw.tenseSuffix="கிறு";
			sw.tenseSuffixId=5;
			sw.acquired_attributes.add("present");
		}        

		pat = Pattern.compile("(க்கும்|கும்)$");
		matcher = pat.matcher(inflectedverb);        
		if(matcher.find())
		{
			//System.out.println("\nஎதிர்காலம் ப் இடைநிலை");
			String[] splitString = inflectedverb.split("(க்கும்|கும்)$");
			root = splitString[0];
			sw.tenseSuffixId=8;
			sw.acquired_attributes.add("future");
			sw.rootWord=root;
		}
		
		//க சட த ப ர
		//வரும்|தரும்|படிக்கும்|போகும்|சுடும்|க்கும்|கும்
		pat = Pattern.compile("(ரும்|லும்|ளும்|க்கும்|கும்)$");
		matcher = pat.matcher(inflectedverb);        
		if(matcher.find())
		{
			//System.out.println("\nஎதிர்காலம் வ் இடைநிலை");
			String[] splitString = inflectedverb.split("(ரும்|லும்|ளும்|க்கும்|கும்)$");
			root = splitString[0];
			String tempSuffix = inflectedverb.replace(splitString[0], "");
			if(tempSuffix.equalsIgnoreCase("ரும்"))
				root = root +"ா";
			sw.tenseSuffixId=8;
			sw.acquired_attributes.add("future");
			sw.rootWord=root;
		}
		
		//உம்
		pat = Pattern.compile("(வும்|யும்)$");
		matcher = pat.matcher(inflectedverb);        
		if(matcher.find())
		{
			//System.out.println("\nஎதிர்காலம் வ் இடைநிலை");
			String[] splitString = inflectedverb.split("(வும்|யும்)$");
			root = splitString[0];
			String tempSuffix = inflectedverb.replace(splitString[0], "");
			if(tempSuffix.equalsIgnoreCase("ரும்"))
				root = root +"ா";
			sw.tenseSuffixId=8;
			sw.acquired_attributes.add("future");
			sw.rootWord=root;
		}
		System.out.println("\n"+ inflectedverb +"\n"+ "POS :"+sw.POS +"\n"+ "POSid :"+sw.POSId +"\n"+ "rootword: "+sw.rootWord +"\n"+ "TenseSuffix :"+sw.tenseSuffix +"\n"+ "TenseSuffixid :"+sw.tenseSuffixId +"\n"+ "DoerSuffix :"+sw.doerSuffix +"\n"+ "DoerSuffixid :"+sw.doerSuffixId);
		return sw;
	}

	public static void main(String args[]){
		String test[] = {"அடிக்கும்","பெய்யும்","குத்தி","நடந்து","கடித்து","போகும்","படிக்கும்","வரும்","கண்ட"};
		for(String s:test){
			Echam.analyse(s);
		}
	}

}