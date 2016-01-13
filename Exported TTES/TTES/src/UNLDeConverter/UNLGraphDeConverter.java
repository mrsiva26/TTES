package UNLDeConverter;
import simplenlg.features.Feature;
import simplenlg.features.Tense;
import simplenlg.framework.NLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.realiser.english.Realiser;
import UNLWordFormat.*;

public class UNLGraphDeConverter {
	
	Lexicon lexicon;
	NLGFactory nlgFactory;
	Realiser realiser;
	
	public UNLGraphDeConverter()
	{
		lexicon = Lexicon.getDefaultLexicon();
        nlgFactory = new NLGFactory(lexicon);
        realiser = new Realiser(lexicon);
	}
	
	/* -1-unknown , 1- simple , 2 - complex , 3- compound ; conjunction  - to correctly conjunct */
	private String GraphDeconvert(SimpleNode unl,int type,String conjunction)
	{
		String string="";
		if(unl.number_of_childs==0)
		{
			String prefix="";
			if(unl.hasAttribute("def"))
				prefix=prefix+"the ";
			if(unl.hasAttribute("def"))
				prefix=prefix+"a ";
			if(unl.hasAttribute("progressive"))
				return prefix+unl.word+"ing";
	        
			return prefix+unl.word;
		}
		for(int i=0;i<unl.number_of_childs;i++)
		{
			if(unl.relations[i]=="plc")
			{
				string=string+" in "+GraphDeconvert(unl.child[i],-1,"");
			}
			else if(unl.relations[i]=="ben"||unl.relations[i]=="gol")
			{
				string=string+" to "+GraphDeconvert(unl.child[i],-1,"");
			}
			else if(unl.relations[i]=="frm"||unl.relations[i]=="tmi")
			{
				string=string+" from "+GraphDeconvert(unl.child[i],-1,"");
			}
			else if(unl.relations[i]=="to"||unl.relations[i]=="tmf")
			{
				string=string+" to "+GraphDeconvert(unl.child[i],-1,"");
			}
			else if(unl.relations[i]=="to")
			{
				string=string+" to "+GraphDeconvert(unl.child[i],-1,"");
			}
			else if(unl.relations[i]=="via")
			{
				string=string+" via "+GraphDeconvert(unl.child[i],-1,"");
			}
			else if(unl.relations[i]=="and")
			{
				string+=" "+GraphDeconvert(unl.child[i],2,"")+" and ";
			}
			else if(unl.relations[i]=="but")
			{
				string+=" "+GraphDeconvert(unl.child[i],2,"")+"  but ";
			}
			else if(unl.relations[i]=="or")
			{
				string+=" "+GraphDeconvert(unl.child[i],2,"")+" or ";
			}
			else if(unl.relations[i]=="seq")
			{
				string+=" "+GraphDeconvert(unl.child[i],1,"After")+" , ";
			}
			else if(unl.relations[i]=="man")
			{
				string+=" "+GraphDeconvert(unl.child[i],-1,"");
			}
			else if(unl.relations[i]=="cnd")
			{
				string+=GraphDeconvert(unl.child[i],-1,"")+" So,";
			}
			else if(unl.relations[i]=="cnd")
			{
				string+=GraphDeconvert(unl.child[i],-1,"")+" So,";
			}
			else if(unl.relations[i]=="obj")
			{
				string+=" "+GraphDeconvert(unl.child[i],-1,"");
			}
			else if(unl.relations[i]=="agt")
			{
				if(type==1)
					string+=" "+conjunction+" "+GraphDeconvert(unl.child[i],-1,"")+ tenseConvert(unl);
				else
					string+=" "+GraphDeconvert(unl.child[i],-1,"")+" "+tenseConvert(unl);
			}
			else if(unl.relations[i]=="mod")
			{
				unl.word=unl.child[i].word+" "+unl.word;
			}
		}
		
		return string;
	}
	
	private String tenseConvert(SimpleNode unl) {
		
		SPhraseSpec p = nlgFactory.createClause();
		if(unl.word!=null)
		{
		String tense=unl.getTense();
        p.setVerb(unl.word);
        
        if(tense=="past")
        	p.setFeature(Feature.TENSE, Tense.PAST);
        else if(tense=="future")
        	p.setFeature(Feature.TENSE, Tense.FUTURE);
        else
        	p.setFeature(Feature.TENSE, Tense.PRESENT);
        
        String word = realiser.realiseSentence(p);
        
        word=word.substring(0, word.length()-1);
		
        return word.toLowerCase();
		}
		else
			return null;
	}

	public String Deconvert(SimpleNode UNL)
	{
		String output=GraphDeconvert(UNL,-1,"");
		return postprocess(output);
		 
	}

	private String postprocess(String output) {
		// TODO Auto-generated method stub
		return output;
	}

}
