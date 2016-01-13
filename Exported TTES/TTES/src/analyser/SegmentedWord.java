package analyser;
import java.util.LinkedList;

import UNLWordFormat.UNLGraphNode;
/*sample output
 * அவன் புத்தகத்தை படிக்கிறான்
 * 
 * ( You may seperate segmented word to segmentedNounWord / Segmeneted Verb Word - if needed
 * segmentedWord [0]{
 *          root -  அவன்
 *          POS - pronoun
 *          nounSuffix- null
 *          everything else is null
 * }
 * 
 * segmentedWord [1]{
 *          root -  புத்தகம்
 *          POS - noun
 *          nounSuffix- ஐ
 *          nounSuffixId - 0
 *          everything else is null
 * }
 * 
 * SegmentedWord[2]{
 *          root- படி
 *          POS - verb
 *          nounSuffix-null
 *          nounSuffixId-null
 *          tenseSuffix - கிறு
 *          tenseSuffixId- 6
 *          doersuffix - ஆன்
 *          doerSuffixId - 14
 *          }
 */
public class SegmentedWord {
	
	public String POS;
	public int POSId;
	public String rootWord;
	public String inflectedWord;
	public float confidence;
	
	public String nounSuffix;
	public int nounSuffixId;

	public String tenseSuffix;
	public int tenseSuffixId;
	public String doerSuffix;
	public int doerSuffixId;
	
	public LinkedList <String> relations;
	public LinkedList <UNLGraphNode> associated_nodes;
	public LinkedList <String> acquired_attributes;
	
	public SegmentedWord(){
		POS=null;
		POSId=-999;
		rootWord=null;
		nounSuffix=null;
		nounSuffixId=-999;
		tenseSuffix=null;
		tenseSuffixId=-999;
		doerSuffix=null;
		doerSuffixId=-999;
		
		this.relations=new  LinkedList <String>();
		this.acquired_attributes=new  LinkedList <String>();
		this.associated_nodes=new  LinkedList <UNLGraphNode>();
	}

	public void addAttribute(String string) {
		this.acquired_attributes.add(string);
	}

	public void addRelation(String string, UNLGraphNode sub) {
	
		this.relations.add(string);
		this.associated_nodes.add(sub);
		
	}
}
