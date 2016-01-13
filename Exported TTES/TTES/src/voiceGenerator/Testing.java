package voiceGenerator;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.UnsupportedAudioFileException;
import marytts.client.MaryClient;
import marytts.client.http.Address;
import marytts.util.data.audio.AudioPlayer;
import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;
import java.net.HttpURLConnection;
import java.util.Locale;
import java.util.Vector;
import java.lang.Object;
public class Testing {

	public static void main(String[] args)
			throws IOException, UnknownHostException, UnsupportedAudioFileException, MatlabConnectionException, MatlabInvocationException
			
	{
		String serverHost = "localhost";
		
		int serverPort = Integer.getInteger("server.port", 59125).intValue();
		MaryClient mary = MaryClient.getMaryClient(new Address(serverHost, serverPort),false,true);
		/*public int getResponseCode()
                throws IOException*/
		String text = "He is very happy";
		String locale = "en_US";
		String inputType = "TEXT";
		String outputType = "AUDIO";
		String audioType = "WAVE";
		String defaultVoiceName = "cmu-slt-hsmm";
		
		//Vector<MaryClient.Voice> v= mary.getVoices(new Locale( locale));
        //System.out.println("The size is "+v.size());	
        FileOutputStream fos = new FileOutputStream("C:\\Users\\mrsiva268\\git\\ttes\\mary_outputs\\op1.wav");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		mary.process(text, inputType, outputType, locale, audioType, defaultVoiceName, fos);
		//String eff=mary.getDefaultAudioEffects();
                
		System.out.println("File saved");
		/*mary.process(text, inputType, outputType, locale, audioType, defaultVoiceName, baos);
		// The byte array constitutes a full wave file, including the headers.
		// And now, play the audio data:
		AudioInputStream ais = AudioSystem.getAudioInputStream(
				new ByteArrayInputStream(baos.toByteArray()));
		LineListener lineListener = new LineListener() {
			public void update(LineEvent event) {
				if (event.getType() == LineEvent.Type.START) {
					System.err.println("Audio started playing.");
			8	} else if (event.getType() == LineEvent.Type.STOP) {
					System.err.println("Audio stopped playing.");
				} else if (event.getType() == LineEvent.Type.OPEN) {
					System.err.println("Audio line opened.");
				} else if (event.getType() == LineEvent.Type.CLOSE) {
					System.err.println("Audio line closed.");
				}
			}
		};

		AudioPlayer ap = new AudioPlayer(ais, lineListener);
		ap.start();*/
		
		
		MatlabProxyFactory factory = new MatlabProxyFactory();
	    MatlabProxy proxy = factory.getProxy();

	    //Display 'hello world' just like when using the demo
	    proxy.eval("addpath('C:\\Users\\mrsiva268\\git\\ttes\\mary_outputs')");
        proxy.feval("voice_modify",'a');
        proxy.eval("rmpath('C:\\Users\\mrsiva268\\git\\ttes\\mary_outputs')");

	    //Disconnect the proxy from MATLAB
	    proxy.disconnect();
	}
}