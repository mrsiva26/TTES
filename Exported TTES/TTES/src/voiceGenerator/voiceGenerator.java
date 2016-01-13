package voiceGenerator;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.UnsupportedAudioFileException;

import marytts.client.MaryClient;
import marytts.client.http.Address;
import marytts.util.data.audio.AudioPlayer;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;
import UNLWordFormat.*;

/* Emotion Constants
 * 0- Happy 
 * 1 - Sad
 * 2 - cry
 * 3 - laughter 
 */

public class voiceGenerator {
	static MatlabProxyFactory factory;
	static MatlabProxy proxy;
	public voiceGenerator(){
		try{
		factory = new MatlabProxyFactory();
	    proxy = factory.getProxy();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public int IdentifyEmotions(UNLGraphNode UNL)
	{
		int k=0;
		Queue<UNLGraphNode> queue=new LinkedList<UNLGraphNode>();
		ArrayList <String> emotions=new ArrayList <String>();
		ArrayList <String> neg=new ArrayList <String>();
		ArrayList <String> otherWords=new ArrayList <String>();
		queue.add(UNL);
		UNLGraphNode element;
		int count=0;
		while (!queue.isEmpty())
	    {
	            element = queue.remove();
	            if(element.parentRelation=="mod"||element.parentRelation=="man")
	            {
	            	if(element.hasAttribute("neg"))
	            		neg.add(element.rootWord);
	            	else
	            		emotions.add(element.rootWord);
	            }
	            else 
	            		otherWords.add(element.rootWord);
	            for(UNLGraphNode child:element.children)
	            {
	            	queue.add(child);
	            }
	    }
		System.out.println(neg+"\n"+emotions+"\n"+otherWords);
		/* 0- No emotion 
		 * 1 - Sad
		 * 2 - angry
		 * 3-  Happy
		 *first check negative emotions*/
		int sad=0, happy=0, angry=0;
		Pattern pat1 = Pattern.compile("(happy|happily|joy|joyous|joyfully|bliss|blissfully|celebrate|laugh)");
		Matcher matcher;
		Pattern pat2 = Pattern.compile("(sorrow|sad|grief|grieve|grievance|tear|depressed|moan|cried|cry)");
		Pattern pat3 = Pattern.compile("(angrily|angry|vengence|anger)");
		for(String s:neg)
		{
			matcher = pat1.matcher(s); 
			if(matcher.find())
				happy--;
			matcher = pat2.matcher(s);
			if(matcher.find())
				sad--;
			matcher = pat3.matcher(s);
			if(matcher.find())
				angry--;
		}
		
		/* next check mod adverbs */
		for(String s:emotions)
		{
			matcher = pat1.matcher(s);
			if(matcher.find())
				happy++;
			matcher = pat2.matcher(s);
			if(matcher.find())
				sad++;
			matcher = pat3.matcher(s);
			if(matcher.find())
				angry++;
		}
		
		/*then check the remaining words */
		for(String s:otherWords)
		{
			matcher = pat1.matcher(s);
			if(matcher.find())
				happy++;
			matcher = pat2.matcher(s);
			if(matcher.find())
				sad++;
			matcher = pat3.matcher(s);
			if(matcher.find())
				angry++;
		}
		
		if(sad!=0 || happy!=0 || angry!=0){
			return max(sad,happy,angry);
		}
		return 0;
	}

	private int max(int sad, int happy, int angry) {
		if(sad>happy && sad>angry)
			return 1;
		else if(happy>sad && happy>angry)
			return 3;
		else
			return 2;
	}

	public void addEmotionsToVoice(String filePath,int Emotion,String modifiedFilePath)
	{

	}

	public void TTS(String text,int emotion)
	{
		try {
			//Runtime.getRuntime().exec("cmd /c start C:\\Users\\mrsiva268\\MARYTTS\\bin\\maryserver.bat");
			//Thread.sleep(9000);
			String serverHost = System.getProperty("server.host", "localhost");
			int serverPort = Integer.getInteger("server.port", 59125).intValue();
			MaryClient mary;

			mary = MaryClient.getMaryClient(new Address(serverHost, serverPort));
			String locale = "en_US";
			String inputType = "TEXT";
			String outputType = "AUDIO";
			String audioType = "WAVE";
			String defaultVoiceName = null;
			FileOutputStream fos = new FileOutputStream("C:\\Users\\mrsiva268\\git\\ttes\\mary_outputs\\op1.wav");
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			mary.process(text, inputType, outputType, locale, audioType, defaultVoiceName, fos);
			System.out.println("Unmodified file saved");
			mary.process(text, inputType, outputType, locale, audioType, defaultVoiceName, baos);
			fos.close();
			
		  //Display 'hello world' just like when using the demo
		    proxy.eval("addpath('C:\\Users\\mrsiva268\\git\\ttes\\mary_outputs')");
		    proxy.eval("addpath('C:\\Users\\mrsiva268\\git\\ttes\\mary_outputs\\STRAIGHTV40_007d')");
	        switch(emotion){
	        case 1: proxy.feval("voice_modify",'s'); break;
	        case 2: proxy.feval("voice_modify",'a'); break;
	        case 3: proxy.feval("voice_modify",'h'); break;
	        default: proxy.feval("voice_modify",'s'); break;
	        }
		    
	        proxy.eval("rmpath('C:\\Users\\mrsiva268\\git\\ttes\\mary_outputs')");

//		    //Disconnect the proxy from MATLAB
//		    proxy.disconnect();
		    
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void generateVoice(UNLGraphNode uNLGraph,String Input) {
		int identifiedEmotion=IdentifyEmotions(uNLGraph);
		System.out.println("I identified " + identifiedEmotion);
		TTS(Input,identifiedEmotion);
		//addEmotionsToVoice(TTSStoragePath,identifiedEmotion,filePathToStore);

	}
	public static void main(String[] args){
		voiceGenerator vG = new voiceGenerator();
		vG.TTS("He cried in sorrow",1);
		vG.TTS("I will kill you",2);
		vG.TTS("I am happy",3);
	}
}
