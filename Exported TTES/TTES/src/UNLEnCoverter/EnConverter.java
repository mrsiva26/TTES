package UNLEnCoverter;
import UNLWordFormat.*;
import analyser.*;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Pattern;

public class EnConverter {
	
	boolean debug;
	private wordnetCommunicator wTranslator;
	
	public EnConverter()
	{
		debug=true;
		wTranslator=new wordnetCommunicator();
	}
	private LinkedList<SegmentedWord> condenseIntermediary(LinkedList<SegmentedWord> segment) {
	
		int count=segment.size();
		int iterator=0;

		while(iterator<segment.size())
		{
			System.out.println("Condensing at "+iterator+" with total size"+segment.size());
			System.out.println("is it null "+segment.get(iterator).rootWord);
			if(segment.get(iterator).rootWord.matches("ஒரு"))
			{
				if(iterator+1<count)
				{	
					segment.get(iterator+1).addAttribute("indef");
					segment.remove(iterator); count--;
					continue;
				}
			}
			
			else if(segment.get(iterator).rootWord.matches("ஒன்றை|ஒன்று"))
			{
				if(iterator-1<count && iterator>0)
				{	
					segment.get(iterator-1).addAttribute("indef");
					segment.remove(iterator); count--;
					continue;
				}
			}
			else if(segment.get(iterator).inflectedWord.matches("பகுதியில்|தொலைவில்|பக்கத்தில்|அருகில்|அருகே|தூரத்தில்"))
			{
				if(iterator-1<count && iterator>0)
				{	
					UNLGraphNode sub=new UNLGraphNode(segment,iterator-1,1);
					sub.rootWord=segment.get(iterator-1).rootWord;
					segment.get(iterator).addRelation("mod",sub);
					segment.remove(iterator); count--;
					continue;
				}
			}
			else if(segment.get(iterator).rootWord.matches("(யும்)"))
			{
				if(iterator+1<count && segment.get(iterator).rootWord.matches("(யும்)"))
				{	
					UNLGraphNode sub=new UNLGraphNode(segment,iterator+1,1);
					sub.rootWord=segment.get(iterator+1).rootWord.replaceAll("யும்","");
					segment.get(iterator).POSId=1;
					segment.get(iterator).addRelation("and",sub);
					segment.remove(iterator+1); count--;
					continue;
				}
			}
			else if(segment.get(iterator).POSId==5 || segment.get(iterator).POS.equals("mod"))
			{
				if((iterator+1<count) && (segment.get(iterator+1).POSId!=3))
				{
					UNLGraphNode sub=new UNLGraphNode(segment,iterator,1);
					sub.rootWord=segment.get(iterator).rootWord;
					segment.get(iterator+1).addRelation("mod",sub);
					segment.remove(iterator); count--;
					if(iterator+2<count && segment.get(iterator+2).rootWord.matches("இல்லை|இல்"))
					{
								segment.get(iterator+1).associated_nodes.getLast().addAttribute("neg");
					}
					continue;
				}
				else
				{
					int i;
					for(i=iterator+1;i<count;i++)
					{
						if(segment.get(iterator+1).POSId!=3)
							break;
					}
					
					UNLGraphNode sub=new UNLGraphNode(segment,iterator,1);
					sub.rootWord=segment.get(iterator).rootWord;
					segment.get(i).addRelation("mod",sub);
					segment.remove(i); count--;
					continue;
				}
				
			}
			else if(segment.get(iterator).POSId==4)
			{
				if((iterator+1<count) && (segment.get(iterator+1).POSId==3))
				{
					UNLGraphNode sub=new UNLGraphNode(segment,iterator,1);
					sub.rootWord=segment.get(iterator).rootWord;
					segment.get(iterator+1).addRelation("man",sub);
					segment.remove(iterator); count--;
					continue;
				}
				
				else if((iterator-1>0) && (segment.get(iterator-1).POSId==3))
				{
					UNLGraphNode sub=new UNLGraphNode(segment,iterator,1);
					sub.rootWord=segment.get(iterator).rootWord;
					segment.get(iterator-1).addRelation("man",sub);
					segment.remove(iterator); count--;
					continue;
				}
				else
				{
					int i;
					for(i=iterator+1;i<count;i++)
					{
						if(segment.get(iterator+1).POSId==3||segment.get(iterator+1).POSId==7)
							break;
					}
					
					UNLGraphNode sub=new UNLGraphNode(segment,iterator,1);
					sub.rootWord=segment.get(iterator).rootWord;
					if(i<count)
					{
						segment.get(i).addRelation("mod",sub);
						segment.remove(i); count--;
						continue;
					}
				}
			}
			iterator++;
		}
		return segment;
	}
	UNLGraphNode graphConstructor(UNLGraphNode root)
	{		int count=0;
		if(root.segments.size()>1)
		{
			while(count<root.segments.size())
			{
				SegmentedWord segment=root.segments.get(count);		
				System.out.println("Now searching the word "+segment.rootWord+" count: "+count+"Size "+root.segments.size());
				if(segment.rootWord.matches("மற்றும்|மற்றுமன்றி"))
				{	
					
					if(debug)
						System.out.println("Adding and relation");
					UNLGraphNode sub=new UNLGraphNode(root.segments,root.segments.indexOf(segment)+1,root.segments.size()-root.segments.indexOf(segment)-1);
					root.addRelation("and", graphConstructor(sub));
					root.removeSegment(count,root.segments.size()-count);
					count=0; 
					continue;
				}
				else if(segment.POSId==7 && count+1<root.segments.size() && root.segments.get(count+1).POSId==3 )
				{	
					
					if(debug)
						System.out.println("Adding and relation");
					UNLGraphNode sub=new UNLGraphNode(root.segments,0,root.segments.indexOf(segment)+1);
					sub.acquire(root.relations,root.attributes,root.children);
					root.resolveAll();
					root.addRelation("and", graphConstructor(sub));
					root.removeSegment(0, count+1);
					count=0; 
					continue;
				}
				else if(segment.rootWord.matches("ஆதலால்|அதனால்|எனவே|ஆகவே|யெனவே"))
				{	
					
					if(debug)
						System.out.println("Adding and relation");
					UNLGraphNode sub=new UNLGraphNode(root.segments,0,root.segments.indexOf(segment));
					sub.acquire(root.relations,root.attributes,root.children);
					root.resolveAll();
					root.addRelation("rsn", graphConstructor(sub));
					root.removeSegment(0, count+1);
					count=0; 
					continue;
				}
				else if(segment.rootWord.matches("அல்லது|அல்லாது"))
				{	
					
					if(debug)
						System.out.println("Adding or relation");
					UNLGraphNode sub=new UNLGraphNode(root.segments,root.segments.indexOf(segment)+1,root.segments.size()-root.segments.indexOf(segment)-1);
					root.addRelation("or", graphConstructor(sub));
					root.removeSegment(count,root.segments.size()-count);
					count=0; 
					continue;
				}
				else if(segment.rootWord.matches("முன்னர்|முன்னே"))
				{	
					
					if(debug)
						System.out.println("Adding seq relation");
					UNLGraphNode sub=new UNLGraphNode(root.segments,root.segments.indexOf(segment)+1,root.segments.size()-root.segments.indexOf(segment)-1);
					root.addRelation("seq", graphConstructor(sub));
					root.removeSegment(count,root.segments.size()-count);
					count=0; 
					continue;
				}
				else if(segment.rootWord.matches("[0-9]*-ல்"))
				{	
					if(debug)
						System.out.println("Adding dur relation");
					int segId=root.segments.indexOf(segment);
					root.segments.get(segId).rootWord=root.segments.get(segId).rootWord.replaceAll("-ல்", "");
					UNLGraphNode sub=new UNLGraphNode(root.segments,segId,1);
					sub.addAttribute("year");
					root.addRelation("dur",graphConstructor(sub));
					root.removeSegment(segId, 1);
					continue;
				}
				else if(segment.rootWord.matches("என்பது|எனப்படுவது|எனச்சொல்லப்படுவது|எனக்கூறப்படுவது|பெயர்"))
				{	
					if(debug)
						System.out.println("Adding concept(cnt) relation");
					
					if(count+1<root.segments.size() && root.segments.get(count+1).rootWord.matches("யாதெனில்|யாதென்பது"))
					{	root.removeSegment(count, 2); count-=1;}
					else
						root.removeSegment(count, 1);
					
					UNLGraphNode sub=new UNLGraphNode(root.segments,count,root.segments.size()-count);
					root.addRelation("cnt", graphConstructor(sub));
					root.removeSegment(count,root.segments.size()-count);  
					continue;
				}
				
				else if(segment.rootWord.matches("போது|அப்போது|இப்போது|நேற்று|இன்று|காலை|மாலை|இரவு|பகல்|மதியம்|ஜாமம்"))
				{
					if(debug)
						System.out.println("Adding time relation");
					UNLGraphNode sub=new UNLGraphNode(root.segments,root.segments.indexOf(segment),1);
					root.addRelation("tim", graphConstructor(sub));
					root.removeSegment(root.segments.indexOf(segment),1);
					count=0; 
					continue;
				}
				else if((segment.rootWord.matches("சமயத்தில்|நேரத்தில்|வேளையில்|மாத்தில்|வருடத்தில்") || ( segment.rootWord.matches("சமயம்|நேரம்|வேளை|வருடம்|மாதம்")) && count>=1 )) 
				{

					if(debug)
						System.out.println("Adding during relation");
					UNLGraphNode sub=new UNLGraphNode(root.segments,root.segments.indexOf(segment)-1,1);
					root.addRelation("dur", graphConstructor(sub));
					root.removeSegment(0, count+1);
					count=0; 
					continue;
				}
				else if(segment.rootWord.matches("பற்றி")) 
				{
					if(debug)
						System.out.println("Adding cnt relation");
					UNLGraphNode sub=new UNLGraphNode(root.segments,0,root.segments.indexOf(segment));
					root.addRelation("cnt", graphConstructor(sub));
					root.removeSegment(0, count+1);
					count=0; 
					continue;
				}
				else if(segment.rootWord.matches("இருந்து|தொடங்கி|ஆரம்பித்து") && count>=1)
				{
					System.out.println("matching இருந்து|தொடங்கி rule : for ");
					
					if(isTimeDomain(root.segments.get(count-1)))
					{
						UNLGraphNode sub=new UNLGraphNode(root.segments,count-1,1);
						root=root.addRelation("tmi",graphConstructor(sub));
						root.removeSegment(count-1, 2);
						count-=1;
						continue;
					}
					else 
					{
						UNLGraphNode sub=new UNLGraphNode(root.segments,count-1,1);
						root=root.addRelation("frm",graphConstructor(sub));
						root.removeSegment(count-1, 2);
						count-=1;
						continue;
					}
				}
				else if(segment.rootWord.matches("முடிந்து|வரை") && count>=1)
				{
					if(debug)
						System.out.println("matching  முடிந்து|வரை rule");
					
					if(isTimeDomain(root.segments.get(count-1)))
					{
						UNLGraphNode sub=new UNLGraphNode(root.segments,count-1,1);
						root=root.addRelation("tmf",graphConstructor(sub));
						root.removeSegment(count-1, 2);
						count-=1;
						continue;
					}
					else 
					{
						UNLGraphNode sub=new UNLGraphNode(root.segments,count-1,1);
						root=root.addRelation("to",graphConstructor(sub));
						root.removeSegment(count-1, 2);
						count-=1;
						continue;
					}
				}
				else if(segment.rootWord.matches("வழியாக|வழியே|வழியில்") && count>=1)
				{
					System.out.println("matching via rule ");
					
					UNLGraphNode sub=new UNLGraphNode(root.segments,root.segments.indexOf(segment)-1,1);
					root.addRelation("via", graphConstructor(sub));
					root.removeSegment(0, count+1); 
					continue;
				}
				/*
				else if(segment.rootWord.matches("யினது|உடையது") && count>=1)
				{
					System.out.println("matching belongs to rule ");	
					UNLGraphNode sub=new UNLGraphNode(root.segments,root.segments.indexOf(segment)-1,1);
					root.addRelation("blg", graphConstructor(sub));
					root.removeSegment(0, count+1); 
					continue;
				}*/
				else if(segment.POSId==8) 
				{
					/*siva -confirmation*/
					if(debug)
						System.out.println("Adding time relation");
					UNLGraphNode sub=new UNLGraphNode(root.segments,root.segments.indexOf(segment),1);
					root.addRelation("tim", graphConstructor(sub));
					root.removeSegment(root.segments.indexOf(segment), 1); 
					continue;
				}
				else if(segment.rootWord.matches("லாயின்"))
				{
					if(debug)
						System.out.println("matching லாயின் rule : so splitting at");
					UNLGraphNode sub=new UNLGraphNode(root.segments,0,root.segments.indexOf(segment)+1);
					root.addRelation("cnd", graphConstructor(sub));
					root.removeSegment(0,root.segments.indexOf(segment)); 
					continue;
				}
				else if(segment.rootWord.matches("எதை|எப்போது|எப்படி|ஏன்|எதற்கு|எவ்வாறு|என்ன|எங்கு"))
				{
					if(debug)
						System.out.println("matching question rule");
					
					//UNLGraphNode sub=new UNLGraphNode(root.segments,root.segments.indexOf(segment),1);
					
					root.addAttribute("interrogative");
					if(segment.rootWord.matches("எப்போது"))
							root.addAttribute("what");
					else if(segment.rootWord.matches("(எப்படி|என்ன|எதை)"))
						root.addAttribute("what");
					else if(segment.rootWord.matches("ஏன்"))
						root.addAttribute("why");
					else if(segment.rootWord.matches("எதற்கு"))
						root.addAttribute("why");
					else if(segment.rootWord.matches("எவ்வாறு"))
						root.addAttribute("how");
					else if(segment.rootWord.matches("எங்கு"))
						root.addAttribute("where");
					
					root.removeSegment(root.segments.indexOf(segment),1);
					continue;
				}
				else if(segment.POSId==1 && segment.nounSuffixId==4 && !( (count+1)<root.segments.size() && root.segments.get(count+1).rootWord.matches("இருந்து|தொடங்கி|ஆரம்பித்து") ))
				{
					if(debug)
						System.out.println("matching இல் rule");
					UNLGraphNode sub=new UNLGraphNode(root.segments,root.segments.indexOf(segment),1);
					root.addRelation("plc", graphConstructor(sub));
					root.removeSegment(root.segments.indexOf(segment),1); 
					continue;
				}
				else if( segment.POSId==1 && segment.nounSuffixId==3)
				{
					if(debug)
						System.out.println("Adding gol relation");
					UNLGraphNode sub=new UNLGraphNode(root.segments,root.segments.indexOf(segment),1);
					root.addRelation("gol", graphConstructor(sub));
					root.removeSegment(root.segments.indexOf(segment),1); 
					continue;
				}
				else if((segment.POSId==2 || segment.POSId==0) && !(root.hasRelation("agt")))
				{
					if(debug)
						System.out.println("Adding Agent relation");
					UNLGraphNode sub=new UNLGraphNode(root.segments,root.segments.indexOf(segment),1);
					root.addRelation("agt", graphConstructor(sub));
					root.removeSegment(root.segments.indexOf(segment),1); 
					continue;
				}
				else if(segment.POSId==1 && segment.nounSuffixId==2)
				{
					if(debug)
						System.out.println("Adding ins relation");
					UNLGraphNode sub=new UNLGraphNode(root.segments,root.segments.indexOf(segment),1);
					root.addRelation("ins", graphConstructor(sub));
					root.removeSegment(root.segments.indexOf(segment),1); 
					continue;
				}
				else if((segment.POSId==1 && segment.nounSuffixId==1))
				{
					if(debug)
						System.out.println("Adding obj relation");
					UNLGraphNode sub=new UNLGraphNode(root.segments,root.segments.indexOf(segment),1);
					root.addRelation("obj", graphConstructor(sub));
					root.removeSegment(root.segments.indexOf(segment),1); 
					continue;
				}
				else if(segment.POSId==0 && segment.nounSuffixId==3)
				{
					if(debug)
						System.out.println("Adding plc relation");
					UNLGraphNode sub=new UNLGraphNode(root.segments,root.segments.indexOf(segment),1);
					root.addRelation("fmt", graphConstructor(sub));
					root.removeSegment(root.segments.indexOf(segment),1); 
					continue;
				}
				
				count++;
				if(debug)
					System.out.println("Now looking at "+segment.rootWord+"with index"+count+" in a total of "+root.segments.size());
			}
		}
		
		/*Adding rootWord*/
		String rootTerm="";
		int found=0;
		if(root.segments.size()>1)
		{
			SegmentedWord temp=null;
			System.out.println("Entered the more than one segment section with "+root.segments.size()+" size");
			for(SegmentedWord segment:root.segments)
			{
				if(segment.POSId==3)
				{
					found=1;
					temp=segment;
					break;
				}
			}
			if(found==1)
			{
				root.segments.removeFirstOccurrence(temp);
				System.out.println("Entered the more than one segment section with "+root.segments.size()+" size temp"+temp.rootWord);
				UNLGraphNode sub=new UNLGraphNode(root.segments,0,root.segments.size());
				for(SegmentedWord segment:sub.segments)
				{
					rootTerm+=segment.rootWord+" ";
				}
				sub.rootWord=rootTerm;
				if(debug)
					System.out.println("The newly added segment has "+sub.segments.size()+"elements and its first value is");
				root.segments.clear();
				root.segments.add(temp);
				if(debug)
					System.out.println("Entered the more than one segment section with "+root.segments.size()+" size with "+root.segments.getLast().rootWord);
				root.addRelation("aoj", sub);
			}
			else
			{
				UNLGraphNode sub=new UNLGraphNode(root.segments,0,root.segments.size());
				for(SegmentedWord segment:sub.segments)
				{
					rootTerm+=segment.rootWord+" ";
				}
				sub.rootWord=rootTerm;
				root.segments.clear();
				//System.out.println("The newly added segment has "+sub.segments.size()+"elements and its first value is"+sub.segments.get(0).rootWord);
				root.addRelation("aoj", sub);
				root.rootWord="";
			}
		}
		rootTerm="";
		for(SegmentedWord segment:root.segments)
		{
			rootTerm+=segment.rootWord+" ";
		}
		
		root.rootWord=rootTerm;
				
		/*Adding attributes */
		for(SegmentedWord segment:root.segments)
		{
			if(segment.POSId==0 || segment.POSId==2)
			{	
	    		if(segment.doerSuffixId==1)
	    			root.addAttribute("self");
	    		if(segment.doerSuffixId==2||segment.doerSuffixId==5||segment.doerSuffixId==9||segment.doerSuffixId==10)
	    			root.addAttribute("plural");
	    		else
	    			root.addAttribute("singular");
	    		if(segment.doerSuffixId==6) 
	    				root.addAttribute("masculine");
	    		else if(segment.doerSuffixId==7)
	    				root.addAttribute("feminine");
	    		
	    	}
	    	else if(segment.POSId==4)
	    	{
	    		root.addAttribute("adjective");
	    	}
	    	else if(segment.POSId==5)
	    	{
	    		root.addAttribute("adverb");
	    	} 
	    	else if(segment.POSId==3)
	    	{	    		
	    		/*adding tense forms to UWs */	    			
	    		System.out.println("adding tense attribute");
	    	
	    		if(segment.tenseSuffixId==1 || segment.tenseSuffixId==2 
	    					|| segment.tenseSuffixId==3 || segment.tenseSuffixId==4)
	    				root.addAttribute("past");
	    		
	    		else if(segment.tenseSuffixId==8 || segment.tenseSuffixId==9)
	    				root.addAttribute("future");
	    			
	    		else if(segment.tenseSuffixId==5 || segment.tenseSuffixId==6 || segment.tenseSuffixId==7)
	    				root.addAttribute("present");	
			
	    	}
		}
		return root;
	}
	
	private boolean isTimeDomain(SegmentedWord segmentedWord) {
		
		if(segmentedWord.rootWord.matches("காலை|மாலை|இரவு|பகல்|மதியம்|ஜாமம்"))
			return true;
		
		return false;
	}
	UNLGraphNode addVerbifNotFound(UNLGraphNode root)
	{
		System.out.println("Root word is"+root.rootWord+"--");
		
		if(root.rootWord==null || root.rootWord=="" || root.rootWord.equals(" "))	
			root.rootWord="be";
		
		if( root.segments.size()>0 && ! ( root.segments.get(0).POSId==3 ||  root.segments.get(0).POSId==7) ) 
		{
			UNLGraphNode sub= new UNLGraphNode(root.segments,0,root.segments.size());
			sub.rootWord=root.rootWord;
			sub.acquireAttributes(root.attributes);
			root.attributes.clear();
			root.segments.clear();
			root.rootWord="be";
			root.addRelation("aoj", sub);
		}
		return root;
	}
	UNLGraphNode addHiddenSubject(UNLGraphNode root)
	{
		String str="";
		if(!root.hasRelation("agt"))
		{
			if(root.segments.size()>0)
			{
				for(SegmentedWord segment:root.segments)
				{
					if(segment.POSId==3)
					{
						switch(segment.doerSuffixId)
						{
						case 2:
							str="I"; break;
						case 3:
							str="You"; break;
						case 4:
							str="You"; break;
						case 5:
							str="You"; break;
						case 6:
							str="He"; break;
						case 7:
							str="She"; break;
						case 8:
							str="He"; break;
						case 9:
							str="They"; break;
						case 10:
							str="It"; break;
						case 11:
							str="They"; break;
						}
					}
				}
			}
			
			if(str!="")
			{	UNLGraphNode sub=new UNLGraphNode(str);
				root.addRelation("agt", sub);
			}	
		}
		return root;
	}
	
	UNLGraphNode convertToEnglish(UNLGraphNode root)
	{
		 Queue<UNLGraphNode> queue=new LinkedList<UNLGraphNode>();
		 queue.add(root);
		 UNLGraphNode element;
		 while (!queue.isEmpty())
	     {
	            element = queue.remove();
	            if(element.segments.size()>0)
	            	element.rootWord=wTranslator.getEnglishWord(element.rootWord,element.segments.get(0).POSId);
	            else
	            	element.rootWord=wTranslator.getEnglishWord(element.rootWord,0);
	            for(UNLGraphNode child:element.children)
	            {
	            	queue.add(child);
	            }
	    }
		 
		return root;
	}
	
	UNLGraphNode addAttributes(UNLGraphNode root)
	{
		return root;
	}
	
	public UNLGraphNode enconvertToUNL(SegmentedWord intermediateOutput[],boolean iamContinous)
	{
		LinkedList<SegmentedWord> segments=new LinkedList<SegmentedWord>(Arrays.asList(intermediateOutput));
		segments=condenseIntermediary(segments);
		UNLGraphNode UNL=new UNLGraphNode(segments);
		UNL=graphConstructor(UNL);
		UNL.reCondense();
		System.out.println("After recondensing");
		UNL.printGraphBFS();
		UNL=convertToEnglish(UNL);
		if(!iamContinous)
		UNL=addHiddenSubject(UNL);
		UNL=addVerbifNotFound(UNL);
		UNL.printGraphBFS();
		UNL.reOrder();
		UNL.printGraphBFS();
		return UNL;
	}
	
	

	public static void main(String[] args) {
		
	}

}
