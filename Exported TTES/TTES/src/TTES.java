import analyser.*;
import UNLDeConverter.*;
import UNLEnCoverter.*;
import UNLWordFormat.*;
import voiceGenerator.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class TTES extends JFrame {

	private static final long serialVersionUID = 1L;
	static voiceGenerator vg;
	static EnConverter ec;
	static DeConverter dc;
	static analyser a;
	static JLabel unlgraph;
	public TTES(){}

	public static void main(String[] args) {
		//  UI instantiation	
		try {
			Runtime.getRuntime().exec("cmd /c start C:\\Users\\mrsiva268\\MARYTTS\\bin\\maryserver.bat");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		vg=new voiceGenerator();
		ec = new EnConverter();
		dc = new DeConverter();
		a = new analyser();
		JFrame user=new JFrame("Tamil text to English Emotional Speech");
		user.setSize(1000, 1000);
		JPanel mainpanel=new JPanel();
		mainpanel.setSize(1000,1000);
		mainpanel.setLayout(new BoxLayout(mainpanel, BoxLayout.Y_AXIS));
		user.setDefaultCloseOperation(EXIT_ON_CLOSE);
		JLabel inputLabel=new JLabel("மொழி மாற்றதுக்கான வரியை பதிவு செய்க");
		System.out.println("மொழி மாற்றதுக்கான வரியை பதிவு செய்க");
		mainpanel.add(inputLabel,BorderLayout.CENTER);
		JTextField input=new JTextField(40);
		mainpanel.add(input,BorderLayout.CENTER);
		JButton translateButton=new JButton("Translate");
		unlgraph=new JLabel("UNL");
		mainpanel.add(unlgraph,BorderLayout.CENTER);
		unlgraph.setText("UNL GRAPH");
		final JTextField output=new JTextField(40);
		mainpanel.add(output,BorderLayout.CENTER);
		output.setText("Output");
		translateButton.setPreferredSize(new Dimension(10,40));
		mainpanel.add(translateButton,BorderLayout.CENTER);
		user.setContentPane(mainpanel);
		user.pack(); 
		user.setVisible(true);

		translateButton.addActionListener( new ActionListener(){
			@Override public void actionPerformed(ActionEvent e) {
				System.out.println("input"+input.getText());

				String englishEquivalent="";
					String allSentence[] = input.getText().toString().split("\\.");
					System.out.println(allSentence.length);
					for(int k=0;k<allSentence.length;k++){
						String eachSentence = allSentence[k];
						System.out.println("count");
						boolean splitSentenceOrNot=false;
						SegmentedWord intermediateOutput[] = a.analyzeText(eachSentence);
						SegmentedWord intermediateOutput1[], intermediateOutput2[];
						for(int i=0;i<intermediateOutput.length;i++){
							if(intermediateOutput[i].POSId==10){
								System.out.println("I came in");
								splitSentenceOrNot=true;
								intermediateOutput1=new SegmentedWord[i];
								intermediateOutput2=new SegmentedWord[intermediateOutput.length-i-1];
								for(int j=0;j<intermediateOutput1.length;j++)
									intermediateOutput1[j]=intermediateOutput[j];
								for(int j=0;j<intermediateOutput2.length;j++)
									intermediateOutput2[j]=intermediateOutput[i+j+1];

								englishEquivalent+=afterAnalyser(intermediateOutput1,false).replace(".", ", ");
								englishEquivalent+=afterAnalyser(intermediateOutput2,true);	
								break;
							}
						}
						if(!splitSentenceOrNot){
							englishEquivalent+=afterAnalyser(intermediateOutput,false);
						}
						englishEquivalent+=" ";
					}
				output.setText(englishEquivalent);
			}
		});
	}

	public static String afterAnalyser(SegmentedWord[] intermediateOutput, boolean iamContinous){
		UNLGraphNode UNLGraph=ec.enconvertToUNL(intermediateOutput,iamContinous);
		unlgraph.setText("<html>"+UNLGraph.getRepresentation().replaceAll("\n", "<br>")+"</html>");
		String englishEquivalent=dc.Deconvert(UNLGraph);
		vg.generateVoice(UNLGraph,englishEquivalent);
		return englishEquivalent;
	}

}
