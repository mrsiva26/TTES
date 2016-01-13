package UNLEnCoverter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Iterator;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.helpers.collection.IteratorUtil;

public class wordnetCommunicator {

	GraphDatabaseFactory dbFactory;
	GraphDatabaseService db;
	ExecutionEngine engine;
	static int islastTermTransliterated;
	Connection conn;
	Statement st;

	wordnetCommunicator()
	{
		try{
			dbFactory = new GraphDatabaseFactory();
			db= dbFactory.newEmbeddedDatabase("C:/Users/mrsiva268/git/ttes/TamilWordnet.graphdb");
			engine = new ExecutionEngine(db);
			String url = "jdbc:mysql://localhost:3306/"; 
			String dbName = "tamilwordnet"; 
			String driver = "com.mysql.jdbc.Driver"; 
			String userName = "root"; 
			String password = "";
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(url+dbName+"?characterEncoding=UTF-8",userName,password);
			st = conn.createStatement();
			//dbFactory = new GraphDatabaseFactory();
			islastTermTransliterated=0;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public int IslastTermTransliterated()
	{
		return islastTermTransliterated;
	}
	public static String transliterate(String word)
	{
		islastTermTransliterated=1;
		word=word.replaceAll("கா", "kaa");
		word=word.replaceAll("கீ", "kii");
		word=word.replaceAll("கூ", "koo");
		word=word.replaceAll("கே", "kae");
		word=word.replaceAll("கை", "kai");
		word=word.replaceAll("கோ","ko" );
		word=word.replaceAll("கௌ", "kau");
		word=word.replaceAll("கி","ki");
		word=word.replaceAll("கு","ku");
		word=word.replaceAll("கெ","ke");
		word=word.replaceAll("கொ","ko");
		word=word.replaceAll("க்", "k");	
		word=word.replaceAll("க", "ka");

		word=word.replaceAll("ஙா", "ngaa");
		word=word.replaceAll("ஙீ", "ngii");
		word=word.replaceAll("ஙூ", "ngoo");
		word=word.replaceAll("ஙே", "ngae");
		word=word.replaceAll("ஙை", "ngai");
		word=word.replaceAll("ஙோ","ngo" );
		word=word.replaceAll("ஙௌ", "ngau");
		word=word.replaceAll("ஙி","ngi");
		word=word.replaceAll("ஙு","ngu");
		word=word.replaceAll("ஙெ","nge");
		word=word.replaceAll("ஙொ","ngo");
		word=word.replaceAll("ங்", "ng");
		word=word.replaceAll("ங", "nga");

		word=word.replaceAll("சா", "saa");
		word=word.replaceAll("சீ", "sii");
		word=word.replaceAll("சூ", "soo");
		word=word.replaceAll("சே", "sae");
		word=word.replaceAll("சை", "sai");
		word=word.replaceAll("சோ","so" );
		word=word.replaceAll("சௌ", "sau");
		word=word.replaceAll("சி","si");
		word=word.replaceAll("சு","su");
		word=word.replaceAll("செ","se");
		word=word.replaceAll("சொ","so");
		word=word.replaceAll("ச்", "s");		
		word=word.replaceAll("ச", "sa");

		word=word.replaceAll("ஞா", "gnaa");
		word=word.replaceAll("ஞீ", "gnii");
		word=word.replaceAll("ஞூ", "gnoo");
		word=word.replaceAll("ஞே", "gnae");
		word=word.replaceAll("ஞை", "gnai");
		word=word.replaceAll("ஞோ", "gno" );
		word=word.replaceAll("ஞௌ", "gnau");
		word=word.replaceAll("ஞி","gni");
		word=word.replaceAll("ஞு","gnu");
		word=word.replaceAll("ஞெ","gne");
		word=word.replaceAll("ஞொ","gno");
		word=word.replaceAll("ஞ்", "gn");
		word=word.replaceAll("ஞ", "gna");

		word=word.replaceAll("டா", "taa");
		word=word.replaceAll("டீ", "tii");
		word=word.replaceAll("டூ", "too");
		word=word.replaceAll("டே", "tae");
		word=word.replaceAll("டை", "tai");
		word=word.replaceAll("டோ","to" );
		word=word.replaceAll("டௌ", "tau");
		word=word.replaceAll("டி","ti");
		word=word.replaceAll("டு","tu");
		word=word.replaceAll("டெ","te");
		word=word.replaceAll("டொ","to");
		word=word.replaceAll("ட்", "t");	
		word=word.replaceAll("ட", "ta");

		word=word.replaceAll("ணா", "naa");
		word=word.replaceAll("ணீ", "nii");
		word=word.replaceAll("ணூ", "noo");
		word=word.replaceAll("ணே", "nae");
		word=word.replaceAll("ணை", "nai");
		word=word.replaceAll("ணோ","no" );
		word=word.replaceAll("ணௌ", "nau");
		word=word.replaceAll("ணி","ni");
		word=word.replaceAll("ணு","nu");
		word=word.replaceAll("ணெ","ne");
		word=word.replaceAll("ணொ","no");
		word=word.replaceAll("ண்", "n");	
		word=word.replaceAll("ண", "na");

		word=word.replaceAll("தா","thaa");
		word=word.replaceAll("தீ","thii");
		word=word.replaceAll("தூ","thuu");
		word=word.replaceAll("தே","thee");
		word=word.replaceAll("தை","thai");
		word=word.replaceAll("தோ","thoo");
		word=word.replaceAll("தௌ","thau");
		word=word.replaceAll("தி","thi");
		word=word.replaceAll("து","thu");
		word=word.replaceAll("தெ","the");
		word=word.replaceAll("தொ","tho");
		word=word.replaceAll("த்","th");	
		word=word.replaceAll("த","tha");

		word=word.replaceAll("நா","nhaa");
		word=word.replaceAll("நீ","nhii");
		word=word.replaceAll("நூ","nhuu");
		word=word.replaceAll("நே","nhee");
		word=word.replaceAll("நை","nhai");
		word=word.replaceAll("நோ","nhoo");
		word=word.replaceAll("நை","nhau");
		word=word.replaceAll("நி","nhi");
		word=word.replaceAll("நு","nhu");
		word=word.replaceAll("நெ","nde");
		word=word.replaceAll("நொ","nho");
		word=word.replaceAll("ந்","nh");	
		word=word.replaceAll("ந","nha");

		word=word.replaceAll("பா","paa");
		word=word.replaceAll("பீ","pii");
		word=word.replaceAll("பூ","puu");
		word=word.replaceAll("பே","pee");
		word=word.replaceAll("பை","pai");
		word=word.replaceAll("போ","poo");
		word=word.replaceAll("பௌ","pau");
		word=word.replaceAll("பி","pi");
		word=word.replaceAll("பு","pu");
		word=word.replaceAll("பெ","pe");
		word=word.replaceAll("பொ","po");
		word=word.replaceAll("ப்","p");
		word=word.replaceAll("ப","pa");	


		word=word.replaceAll("மா","maa");
		word=word.replaceAll("மீ","mii");
		word=word.replaceAll("மூ","muu");
		word=word.replaceAll("மே","mee");
		word=word.replaceAll("மை","mai");
		word=word.replaceAll("மோ","moo");
		word=word.replaceAll("மௌ","mau");
		word=word.replaceAll("மி","mi");
		word=word.replaceAll("மு","mu");
		word=word.replaceAll("மெ","me");
		word=word.replaceAll("மொ","mo");
		word=word.replaceAll("ம்","m");	
		word=word.replaceAll("ம","ma");


		word=word.replaceAll("யா","yaa");
		word=word.replaceAll("யீ","yii");
		word=word.replaceAll("யூ","yuu");
		word=word.replaceAll("யே","yee");
		word=word.replaceAll("யை","yai");
		word=word.replaceAll("யோ","yoo");
		word=word.replaceAll("யௌ","yau");	
		word=word.replaceAll("யி","yi");
		word=word.replaceAll("யு","yu");
		word=word.replaceAll("யெ","ye");
		word=word.replaceAll("யொ","yo");
		word=word.replaceAll("ய்","y");
		word=word.replaceAll("ய","ya");


		word=word.replaceAll("ரா","raa");
		word=word.replaceAll("ரீ","rii");
		word=word.replaceAll("ரூ","ruu");
		word=word.replaceAll("ரே","ree");
		word=word.replaceAll("ரை","rai");
		word=word.replaceAll("ரோ","roo");
		word=word.replaceAll("ரௌ","rau");
		word=word.replaceAll("ரி","ri");
		word=word.replaceAll("ரு","ru");
		word=word.replaceAll("ரெ","re");
		word=word.replaceAll("ரொ","ro");
		word=word.replaceAll("ர்","r");
		word=word.replaceAll("ர","ra");


		word=word.replaceAll("லா","laa");
		word=word.replaceAll("லீ","lii");
		word=word.replaceAll("லூ","luu");
		word=word.replaceAll("லே","lee");
		word=word.replaceAll("லை","lai");
		word=word.replaceAll("லோ","loo");
		word=word.replaceAll("லௌ","lau");
		word=word.replaceAll("லி","li");
		word=word.replaceAll("லு","lu");
		word=word.replaceAll("லெ","le");
		word=word.replaceAll("லொ","lo");
		word=word.replaceAll("ல்","l");
		word=word.replaceAll("ல","la");

		word=word.replaceAll("வா","vaa");
		word=word.replaceAll("வீ","vii");
		word=word.replaceAll("வூ","vuu");
		word=word.replaceAll("வே","vee");
		word=word.replaceAll("வை","vai");
		word=word.replaceAll("வோ","voo");
		word=word.replaceAll("வௌ","vau");
		word=word.replaceAll("வி","vi");
		word=word.replaceAll("வு","vu");
		word=word.replaceAll("வெ","ve");
		word=word.replaceAll("வொ","vo");
		word=word.replaceAll("வ்","v");
		word=word.replaceAll("வ","va");

		word=word.replaceAll("ழா","zhaa");
		word=word.replaceAll("ழீ","zhii");
		word=word.replaceAll("ழூ","zhuu");
		word=word.replaceAll("ழே","zhee");
		word=word.replaceAll("ழை","zhai");
		word=word.replaceAll("ழோ","zhoo");
		word=word.replaceAll("ழௌ","zhau");
		word=word.replaceAll("ழி","zhi");
		word=word.replaceAll("ழு","zhu");
		word=word.replaceAll("ழெ","zhe");
		word=word.replaceAll("ழொ","zho");
		word=word.replaceAll("ழ்","zh");
		word=word.replaceAll("ழ","zha");

		word=word.replaceAll("ளா","laa");
		word=word.replaceAll("ளீ","lii");
		word=word.replaceAll("ளூ","luu");
		word=word.replaceAll("ளே","lee");
		word=word.replaceAll("ளை","lai");
		word=word.replaceAll("ளோ","loo");
		word=word.replaceAll("ளௌ","lau");
		word=word.replaceAll("ளி","li");
		word=word.replaceAll("ளு","lu");
		word=word.replaceAll("ளெ","le");
		word=word.replaceAll("ளொ","lo");
		word=word.replaceAll("ள்","l");
		word=word.replaceAll("ள","la");

		word=word.replaceAll("றா","raa");
		word=word.replaceAll("றீ","rii");
		word=word.replaceAll("றூ","ruu");
		word=word.replaceAll("றே","ree");
		word=word.replaceAll("றை","rai");
		word=word.replaceAll("றோ","roo");
		word=word.replaceAll("றௌ","rau");
		word=word.replaceAll("றி","ri");
		word=word.replaceAll("று","ru");
		word=word.replaceAll("றெ","re");
		word=word.replaceAll("றொ","ro");
		word=word.replaceAll("ற்","r");
		word=word.replaceAll("ற","ra");

		word=word.replaceAll("னா","naa");
		word=word.replaceAll("னீ","nii");
		word=word.replaceAll("னூ","nuu");
		word=word.replaceAll("னே","nee");
		word=word.replaceAll("னை","nai");
		word=word.replaceAll("னோ","noo");
		word=word.replaceAll("னௌ","nau");
		word=word.replaceAll("னி","ni");
		word=word.replaceAll("னு","nu");
		word=word.replaceAll("னெ","ne");
		word=word.replaceAll("னொ","no");
		word=word.replaceAll("ன்","n");
		word=word.replaceAll("ன","na");

		word=word.replaceAll("ஷா","shaa");
		word=word.replaceAll("ஷீ","shii");
		word=word.replaceAll("ஷூ","shuu");
		word=word.replaceAll("ஷே","shee");
		word=word.replaceAll("ஷை","shai");
		word=word.replaceAll("ஷோ","shoo");
		word=word.replaceAll("ஷௌ","shau");
		word=word.replaceAll("ஷி","shi");
		word=word.replaceAll("ஷு","shu");
		word=word.replaceAll("ஷெ","she");
		word=word.replaceAll("ஷொ","sho");
		word=word.replaceAll("ஷ்","sh");
		word=word.replaceAll("ஷ","sha");

		word=word.replaceAll("ஸா","saa");
		word=word.replaceAll("ஸீ","sii");
		word=word.replaceAll("ஸூ","suu");
		word=word.replaceAll("ஸே","see");
		word=word.replaceAll("ஸை","sai");
		word=word.replaceAll("ஸோ","soo");
		word=word.replaceAll("ஸௌ","sau");
		word=word.replaceAll("ஸி","si");
		word=word.replaceAll("ஸு","su");
		word=word.replaceAll("ஸெ","se");
		word=word.replaceAll("ஸொ","so");
		word=word.replaceAll("ஸ்","s");
		word=word.replaceAll("ஸ","sa");

		word=word.replaceAll("ஜா","jaa");
		word=word.replaceAll("ஜீ","jii");
		word=word.replaceAll("ஜூ","juu");
		word=word.replaceAll("ஜே","jee");
		word=word.replaceAll("ஜை","jai");
		word=word.replaceAll("ஜோ","joo");
		word=word.replaceAll("ஜௌ","jau");
		word=word.replaceAll("ஜி","ji");
		word=word.replaceAll("ஜு","ju");
		word=word.replaceAll("ஜெ","je");
		word=word.replaceAll("ஜொ","jo");
		word=word.replaceAll("ஜ்","j");
		word=word.replaceAll("ஜ","ja");

		word=word.replaceAll("க்ஷா","xaa");
		word=word.replaceAll("க்ஷீ","xii");
		word=word.replaceAll("க்ஷூ","xuu");
		word=word.replaceAll("க்ஷே","xee");
		word=word.replaceAll("க்ஷை","xai");
		word=word.replaceAll("க்ஷோ","xoo");
		word=word.replaceAll("க்ஷௌ","xau");
		word=word.replaceAll("க்ஷி","xi");
		word=word.replaceAll("க்ஷு","xu");
		word=word.replaceAll("க்ஷெ","xe");
		word=word.replaceAll("க்ஷொ","xo");
		word=word.replaceAll("க்ஷ்","x");
		word=word.replaceAll("க்ஷ","xa");

		word=word.replaceAll("ஸா","saa");
		word=word.replaceAll("ஸீ","sii");
		word=word.replaceAll("ஸூ","suu");
		word=word.replaceAll("ஸே","see");
		word=word.replaceAll("ஸை","sai");
		word=word.replaceAll("ஸோ","soo");
		word=word.replaceAll("ஸௌ","sau");
		word=word.replaceAll("ஸி","si");
		word=word.replaceAll("ஸு","su");
		word=word.replaceAll("ஸெ","se");
		word=word.replaceAll("ஸொ","so");
		word=word.replaceAll("ஸ்","s");
		word=word.replaceAll("ஸ","sa");

		word=word.replaceAll("ஹா","haa");
		word=word.replaceAll("ஹீ","hii");
		word=word.replaceAll("ஹூ","huu");
		word=word.replaceAll("ஹே","hee");
		word=word.replaceAll("ஹை","hai");
		word=word.replaceAll("ஹோ","hoo");
		word=word.replaceAll("ஹௌ","hau");
		word=word.replaceAll("ஹி","hi");
		word=word.replaceAll("ஹு","hu");
		word=word.replaceAll("ஹெ","he");
		word=word.replaceAll("ஹொ","ho");
		word=word.replaceAll("ஹ்","h");
		word=word.replaceAll("ஹ","ha");

		word=word.replaceAll("ஆ","aa");
		word=word.replaceAll("ஈ","ii");
		word=word.replaceAll("ஊ","uu");
		word=word.replaceAll("ஏ","ee");
		word=word.replaceAll("ஐ","ai");
		word=word.replaceAll("ஓ","oo");
		word=word.replaceAll("ஔ","au");
		word=word.replaceAll("அ","a");
		word=word.replaceAll("இ","i");
		word=word.replaceAll("உ","u");
		word=word.replaceAll("எ","e");
		word=word.replaceAll("ஒ","o");
		word=word.replaceAll("ஃ","f");

		return word;
	}

	
	public  String getEnglishWord(String tamilWord,int POS)
	{
		tamilWord=tamilWord.trim();
		String tamilWords[]=tamilWord.split(" ");
		String tamilword,englishWord,output="";
		for(int i=0;i<tamilWords.length;i++)
		{
			englishWord="";
			tamilword=tamilWords[i];
			if(!tamilword.equals("நான்"))
			englishWord=pingGraphWordnet(tamilword,POS);
			if(englishWord=="")
				englishWord=pingMysqlDB(tamilword);
			if(englishWord=="")
				englishWord=transliterate(tamilword);
			output+=englishWord+" ";
		}
		return output.trim();
	}
	
	
	private String pingGraphWordnet(String tamilWord, int POS) {
		
		String result="";
		/*Searching the Wordnet */
		String POS_word="noun";

		if(POS==3)
			POS_word="verb";			
		
		try (Transaction tx = db.beginTx()) 
		{
			ExecutionResult result1=engine.execute("MATCH (n:TamilWord)-[:"+POS_word+"]->(k:EnglishWord) where n.word='"+tamilWord+"' return k.word");

			Iterator<String> n_column = result1.columnAs( "k.word" );
			for ( String str : IteratorUtil.asIterable( n_column ) )
			{
				System.out.println("In GraphDb  found for "+tamilWord+"is "+str);
			    result=str;
			}
			tx.success();
		}
		catch(Exception e)
		{
			System.out.println(e);

		}
		return result.trim();
	}
	
	private String pingMysqlDB(String tamilWord) {
	
		String result="";
		
		try{	
			String query,englishWord;
				query = "SELECT distinct * FROM testing"+" where"+" tamil like "+"'"+tamilWord+"';";
				System.out.println(query);
				ResultSet res = st.executeQuery(query);
				while(res.next()){
					englishWord = res.getString("english");
					result+=englishWord+" ";
					System.out.println("From db: "+tamilWord+" --> " +englishWord);
					break;
				}
				
		}catch(Exception e){
			e.printStackTrace();
		}
		System.err.println(result);
		return result.trim();		
	}

	public static void main(String[] args)
	{
		wordnetCommunicator we  = new wordnetCommunicator();
		we.getEnglishWord("காகம்",0);
		we.getEnglishWord("கூகுள் சென்னை",0);

	}
}

