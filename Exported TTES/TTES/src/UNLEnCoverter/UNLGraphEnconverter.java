package UNLEnCoverter;
import analyser.SegmentedWord;
import UNLWordFormat.*;

public class UNLGraphEnconverter {
	
	static int current_scope=0;
	static wordnetCommunicator wTranslator = new wordnetCommunicator();
	
	public SimpleNode enconvert(SimpleNode root) {
		
		//System.out.println("root childs "+root.number_of_childs+" scope ID "+root.scopeID+ " is root null "+(root==null));
		if(root.temp_seg.length<1)
		{
			return root;
		}
		if(root.temp_seg.length==1||root.inOpLength==1)
		{
			/* Add the attributes here */
			root.word=root.temp_seg[0].rootWord;
			if(root.temp_seg[0].POSId==1 || root.temp_seg[0].POSId==2)
			{	
	    		if(root.temp_seg[0].rootWord=="அவன்")
	    			root.addAttribute("masculine");
	    		else if(root.temp_seg[0].rootWord=="அவள்")
	    			root.addAttribute("feminine");
	    	}
	    	else if(root.temp_seg[0].POSId==4)
	    	{
	    		root.addAttribute("adjective");
	    	}
	    	else if(root.temp_seg[0].POSId==5)
	    	{
	    		root.addAttribute("adverb");
	    	} 
	    	else if(root.temp_seg[0].POSId==3)
	    	{
	    		
	    		/*adding tense forms to UWs */	    			
	    		System.out.println("adding tense attribute");
	    		if(root.temp_seg[0].tenseSuffixId==1 || root.temp_seg[0].tenseSuffixId==2 
	    					|| root.temp_seg[0].tenseSuffixId==3 || root.temp_seg[0].tenseSuffixId==4)
	    				root.addAttribute("past");
	    		
	    			else if(root.temp_seg[0].tenseSuffixId==8 || root.temp_seg[0].tenseSuffixId==9)
	    				root.addAttribute("future");
	    			
	    			else if(root.temp_seg[0].tenseSuffixId==5 || root.temp_seg[0].tenseSuffixId==6 || root.temp_seg[0].tenseSuffixId==7)
	    				root.addAttribute("present");	
			root.IsComplete=1;
	    	}
			root.word=wTranslator.getEnglishWord(root.word,root.temp_seg[0].POSId);
			return root;
		}
		else
		{
			
			int i=0;
			/*Check for the First set of rules that split the sentence */
			while(i<root.temp_seg.length)
			{
				
				SegmentedWord seg[]=root.temp_seg;
				if(root.temp_seg.length-1<i || root.temp_seg[i]==null)
					break;
				/* Rule 01 - matches or */
				if(seg[i].rootWord.matches("அல்லது|அல்லாது") && i!=0)
				{
					System.out.println("matching allathu rule : so splitting at"+root.temp_seg[i].rootWord);
					SimpleNode sub=new SimpleNode(root.temp_seg,0,i);
					root=root.addRelation(i,"or",enconvert(sub));
					root=root.removeSegment(0, i+1);
					i=0;
					continue;
				}
				/*Rule 02- matches but  */
				else if(root.temp_seg[i].rootWord.matches("ஆனால்|ஆயினும்|எனினும்|என்றாலும்") && i!=0)
				{
					System.out.println("matching aanal rule : so splitting at"+root.temp_seg[i].rootWord);
					SimpleNode sub=new SimpleNode(root.temp_seg,0,i);
					root=root.addRelation(i,"but",enconvert(sub));
					root=root.removeSegment(0,i+1);
					i=0;
					continue;
				}
				/*Rule 03- matches condition*/
				else if(root.temp_seg[i].rootWord.matches("ஆகையால்|ஆகவே") && i!=0 )
				{
					System.out.println("matching agaiyaal rule : so splitting at"+root.temp_seg[i].rootWord);
					SimpleNode sub=new SimpleNode(root.temp_seg,0,i);
					root=root.addRelation(i,"cnd",enconvert(sub));
					root=root.removeSegment(0,i+1);
					i=0;continue;
				}
				else if(root.temp_seg[i].rootWord.matches("மற்றும்") && i!=0 )
				{
					System.out.println("matching matrum rule : so splitting at"+root.temp_seg[i].rootWord);
					SimpleNode sub=new SimpleNode(root.temp_seg,0,i);
					root=root.addRelation(i,"and",enconvert(sub));
					root=root.removeSegment(0,i+1);
					i=0;continue;
				}
				else if(root.temp_seg[i].rootWord.matches("என்று") && i!=0 )
				{
					System.out.println("matching endru rule : so splitting at"+root.temp_seg[i].rootWord);
					System.out.println("A match for endru at "+i+" In the phase "+root.temp_seg.length);
					SimpleNode sub=new SimpleNode(root.temp_seg,0,i);
					root=root.addRelation(i,"aoj",enconvert(sub));
					root=root.removeSegment(0,i+1);
					i=0;continue;
				}
				/* Hyper nodes based on punctuation*/
				else if(root.temp_seg[i].rootWord.matches(",") && i!=0)
				{
					System.out.println("matching , rule : so splitting at"+root.temp_seg[i].rootWord);
					SimpleNode sub=new SimpleNode(root.temp_seg,0,i);
					if(seg[i-1].POSId==0)
						root=root.addRelation(i,"agt", enconvert(sub));
					else if(seg[i-1].POSId==3)
						root=root.addRelation(i, "seq", enconvert(sub));
					else if(seg[i-1].POSId==1 || root.temp_seg[i].POSId==6)
						root=root.addRelation(i, "obj", enconvert(sub));		
					root=root.removeSegment(0, i);
					i=0;continue;
				}
				/* matches definition */
				else if(root.temp_seg[i].rootWord.matches("என்பது|எனப்படுவது|எனக்கூரப்படுவது") && i!=0)
				{
					System.out.println("matching enbathu rule : so splitting at"+root.temp_seg[i].rootWord);
					SimpleNode sub=new SimpleNode(root.temp_seg,0,i);
					root=root.addRelation(i,"cnt",enconvert(sub));
					root=root.removeSegment(0,i+1);
					i=0;continue;
				}
				else if(root.temp_seg[i].rootWord.matches("லாயின்") && i>0)
				{
					System.out.println("matching லாயின் rule : so splitting at"+root.temp_seg[i].rootWord);
					SimpleNode sub=new SimpleNode(root.temp_seg,0,i);
					root=root.addRelation(i,"cnd",enconvert(sub));
					root=root.removeSegment(0,i+1);
					i=0;continue;
				}
				i++;
						
			}
			 
			i=0;
			while(i<root.temp_seg.length)
			{
				if(root.temp_seg.length-1<i || root.temp_seg[i]==null)
					break;
				/*Rule - 1 : Check for start time or end time */
				if(root.temp_seg[i].rootWord.matches("இருந்து|தொடங்கி") && i>1)
				{
					System.out.println("matching இருந்து|தொடங்கி rule : for "+root.temp_seg[i-1].rootWord);
					if(isTimeDomain(root.temp_seg[i-1]))
					{
						SimpleNode sub=new SimpleNode(root.temp_seg,i-1,1);
						root=root.addRelation(i,"tmi",enconvert(sub));
						root=root.removeSegment(i-1,2);
						i-=1;
						continue;
					}
					else 
					{
						
						SimpleNode sub=new SimpleNode(root.temp_seg,i-1,1);
						root=root.addRelation(i,"frm",enconvert(sub));
						root=root.removeSegment(i-1,2);
						i-=1;continue;
					}
				}
				 
				if(root.temp_seg[i].rootWord.matches("வரை") && i>1)
				{
					System.out.println("matching வரை rule : for "+root.temp_seg[i-1].rootWord);
					if(isTimeDomain(root.temp_seg[i-1]))
					{
						SimpleNode sub=new SimpleNode(root.temp_seg,i-1,1);
						root=root.addRelation(i,"tmf",enconvert(sub));
						root=root.removeSegment(i-1,2);
						i-=1;continue;
					}
					else
					{
						SimpleNode sub=new SimpleNode(root.temp_seg,i-1,1);
						root=root.addRelation(i,"to",enconvert(sub));
						root=root.removeSegment(i-1,2);
						i-=1;continue;
					}
				}
				 
				if(root.temp_seg[i].rootWord.matches("வழியாக|வழியே|வழியில்") && i>1)
				{
					
					System.out.println("matching வழியாக|வழியே|வழியில் rule : for "+root.temp_seg[i-1].rootWord);
						SimpleNode sub=new SimpleNode(root.temp_seg,i-1,1);
						root=root.addRelation(i,"via",enconvert(sub));
						root=root.removeSegment(i-1,2);
						i-=1;continue;
				}
				if(root.temp_seg[i].POSId==0 && root.temp_seg[i].nounSuffixId==4 && root.temp_seg[i].rootWord.matches("காலை|மாலை|இரவு|பகல்|மதியம்|ஜாமம்|அந்தி"))
				{
					System.out.println("matching காலை : for "+root.temp_seg[i].rootWord);
					SimpleNode sub=new SimpleNode(root.temp_seg,i,1);
					root=root.addRelation(i,"tim",enconvert(sub));
					root=root.removeSegment(i,1);
					continue;
				}
				/*Rule - 2 : Check for place */
				 
				if(root.temp_seg[i].POSId==0 && root.temp_seg[i].nounSuffixId==4)
				{
					SimpleNode sub=new SimpleNode(root.temp_seg,i,1);
					root=root.addRelation(i,"plc",sub);
					root=root.removeSegment(i,1);	
					continue;
				}
				if(root.temp_seg[i].POSId==4 && i+1<root.temp_seg.length)
				{
					SimpleNode sub=new SimpleNode(root.temp_seg,i+1,1);
					SimpleNode sub1=new SimpleNode(root.temp_seg,i,1);
					sub=sub.addRelation(i,"mod",enconvert(sub1));
					root=root.removeSegment(i,2);
					root=root.addRelation(i+1,"agt",enconvert(sub));
					continue;
				}
				else if(root.temp_seg[i].POSId==5)
				{
					SimpleNode sub=new SimpleNode(root.temp_seg,i,1);
					root=root.addRelation(i,"man",enconvert(sub));
					root=root.removeSegment(i,1);
					continue;
				}
				if(root.temp_seg[i].POSId==7 && i!=0)
				{
					SimpleNode sub=new SimpleNode(root.temp_seg,i-1,1);
					root=root.addRelation(i,"obj",enconvert(sub));
					root=root.removeSegment(i-1,1);
					continue;
				}
				/* if there is a verb remaining the noun and object should form SVo format*/
				if(root.temp_seg[i].POSId==1 || root.temp_seg[i].POSId==6)
				{
					SimpleNode sub=new SimpleNode(root.temp_seg,i,1);
					root=root.addRelation(i,"obj",enconvert(sub));
					root=root.removeSegment(i,1);
					continue;
				}
				else if(root.temp_seg[i].POSId==0 || root.temp_seg[i].POSId==2 )
				{
					SimpleNode sub=new SimpleNode(root.temp_seg,i,1);
					root=root.addRelation(i,"agt",enconvert(sub));
					root=root.removeSegment(i,1);
					continue;
				}
				i++;
			}
		}
		if(root.temp_seg.length==1||root.inOpLength==1)
		{
			/* Add the attributes here */
			root.word=root.temp_seg[0].rootWord;
			if(root.temp_seg[0].POSId==1 || root.temp_seg[0].POSId==2)
			{	
	    		if(root.temp_seg[0].rootWord=="அவன்")
	    			root.addAttribute("masculine");
	    		else if(root.temp_seg[0].rootWord=="அவள்")
	    			root.addAttribute("feminine");
	    	}
	    	else if(root.temp_seg[0].POSId==4)
	    	{
	    		root.addAttribute("adjective");
	    	}
	    	else if(root.temp_seg[0].POSId==5)
	    	{
	    		root.addAttribute("adverb");
	    	} 
	    	else if(root.temp_seg[0].POSId==3)
	    	{
	    		
	    		/*adding tense forms to UWs */	    			
	    		System.out.println("adding tense attribute");
	    		if(root.temp_seg[0].tenseSuffixId==1 || root.temp_seg[0].tenseSuffixId==2 
	    					|| root.temp_seg[0].tenseSuffixId==3 || root.temp_seg[0].tenseSuffixId==4)
	    				root.addAttribute("past");
	    		
	    			else if(root.temp_seg[0].tenseSuffixId==8 || root.temp_seg[0].tenseSuffixId==9)
	    				root.addAttribute("future");
	    			
	    			else if(root.temp_seg[0].tenseSuffixId==5 || root.temp_seg[0].tenseSuffixId==6 || root.temp_seg[0].tenseSuffixId==7)
	    				root.addAttribute("present");	
			root.IsComplete=1;
	    	}
			root.word=wTranslator.getEnglishWord(root.word,root.temp_seg[0].POSId);
			return root;
		}
		return root;
			
	}
	
	private boolean isTimeDomain(SegmentedWord segmentedWord) {
		
		if(segmentedWord.rootWord.matches("காலை|மாலை|இரவு|பகல்|மதியம்|ஜாமம்|அந்தி|ஆடி|ஆவனி|ஐப்பசி"))
			return true;
		
		return false;
	}

	public SimpleNode enconverter(SegmentedWord[] intermediateOutput)
	{
		SimpleNode root=new SimpleNode(intermediateOutput);
		return enconvert(root);
	}
}
