package UNLDeConverter;

import UNLWordFormat.*;
import simplenlg.features.Feature;
import simplenlg.features.Tense;
import simplenlg.framework.NLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.realiser.english.Realiser;

public class DeConverter {

	Lexicon lexicon;
	NLGFactory nlgFactory;
	Realiser realiser;
	private String questionToAdd;
	private int numberIndicator;

	public DeConverter()
	{
		lexicon = Lexicon.getDefaultLexicon();
		nlgFactory = new NLGFactory(lexicon);
		realiser = new Realiser(lexicon);
	}

	private String GraphDeconvert(UNLGraphNode unl,int type,String conjunction)
	{
		String string="";
		if(unl.relations.size()==0)
		{
			String prefix="";
			if(unl.hasAttribute("plural"))
				numberIndicator=1;

			if(unl.hasAttribute("def"))
				prefix=prefix+"the ";
			if(unl.hasAttribute("indef")) 
			{		
				if(unl.rootWord.startsWith("a")||unl.rootWord.startsWith("e")||unl.rootWord.startsWith("i")||unl.rootWord.startsWith("o")||unl.rootWord.startsWith("u"))
					prefix=prefix+"an ";
				else
					prefix=prefix+"a ";
			}
			if(unl.hasAttribute("progressive")||type==2)
				return prefix+unl.rootWord+"ing";

			return prefix+unl.rootWord;
		}
		for(String relation:unl.relations)
		{
			if(relation=="seq")
			{
				string+=" "+GraphDeconvert(unl.children.get(unl.relations.indexOf(relation)),1,"After")+" , ";
			}
			else if(relation=="cnd")
			{
				string+=" If "+GraphDeconvert(unl.children.get(unl.relations.indexOf(relation)),-1,"")+",";
			}
			else if(relation=="mod")
			{
				string+=" "+GraphDeconvert(unl.children.get(unl.relations.indexOf(relation)),-1,"");
			}
			else if(relation=="plc")
			{
				string=string+" in "+GraphDeconvert(unl.children.get(unl.relations.indexOf(relation)),-1,"");
			}
			else if(relation=="dur"||relation=="tim")
			{
				string=string+" during "+GraphDeconvert(unl.children.get(unl.relations.indexOf(relation)),-1,"");
			}
			else if(relation=="ben"||relation=="gol")
			{
				string=string+" to "+GraphDeconvert(unl.children.get(unl.relations.indexOf(relation)),-1,"");
			}
			else if(relation=="agt")
			{
				if(type==1)
					string+=" "+conjunction+" "+GraphDeconvert(unl.children.get(unl.relations.indexOf(relation)),-1,"");
				else
					string+=" "+GraphDeconvert(unl.children.get(unl.relations.indexOf(relation)),-1,"");
			}
			else if(relation=="aoj")
			{
				String prefix="";
				if(unl.children.get(unl.relations.indexOf(relation)).getTense()!=null)
					prefix="after ";
				string+=" "+prefix+GraphDeconvert(unl.children.get(unl.relations.indexOf(relation)),2,"");
			}
			else if(relation=="obj")
			{
				string+=" "+GraphDeconvert(unl.children.get(unl.relations.indexOf(relation)),-1,"");
			}
			else if(relation=="unl")
			{
				System.out.println(" "+unl.rootWord);
				string+=tenseConvert(unl);
			}
			else if(relation=="ins")
			{
				string+=" with a "+GraphDeconvert(unl.children.get(unl.relations.indexOf(relation)),-1,"");
			}
			else if(relation=="cnt")
			{
				string+=" "+GraphDeconvert(unl.children.get(unl.relations.indexOf(relation)),-1,"");
			}

			else if(relation=="man")
			{
				string+=" "+GraphDeconvert(unl.children.get(unl.relations.indexOf(relation)),-1,"");
			}
			else if(relation=="frm"||relation=="tmi")
			{
				string=string+" from "+GraphDeconvert(unl.children.get(unl.relations.indexOf(relation)),-1,"");
			}
			else if(relation=="to")
			{
				string=string+" to "+GraphDeconvert(unl.children.get(unl.relations.indexOf(relation)),-1,"");
			}
			else if(relation=="tmf")
			{
				string=string+" till "+GraphDeconvert(unl.children.get(unl.relations.indexOf(relation)),-1,"");
			}
			else if(relation=="via")
			{
				string=string+" via "+GraphDeconvert(unl.children.get(unl.relations.indexOf(relation)),-1,"");
			}
			else if(relation=="and")
			{
				string=GraphDeconvert(unl.children.get(unl.relations.indexOf(relation)),2,"")+" and "+string;
			}
			else if(relation=="but")
			{
				string+=" but "+GraphDeconvert(unl.children.get(unl.relations.indexOf(relation)),2,"");
			}
			else if(relation=="or")
			{
				string=GraphDeconvert(unl.children.get(unl.relations.indexOf(relation)),2,"")+"  or  "+string;
			}


		}

		return string;
	}

	private String tenseConvert(UNLGraphNode unl) {
		try{

			SPhraseSpec p = nlgFactory.createClause();

			if(unl.rootWord!=null && !unl.hasAttribute("interrogative"))
			{
				String tense=unl.getTense();
				if(unl.rootWord==""||unl.rootWord==" ") unl.rootWord="be";
				p.setVerb(unl.rootWord);


				if(tense=="past")
					p.setFeature(Feature.TENSE, Tense.PAST);
				else if(tense=="future")
					p.setFeature(Feature.TENSE, Tense.FUTURE);
				else
				{
					if(this.numberIndicator==1)
						return unl.rootWord;
					p.setFeature(Feature.TENSE, Tense.PRESENT);
				}
				String word = realiser.realiseSentence(p);



				word=word.substring(0, word.length()-1);

				return " "+word.toLowerCase();
			}
			else if(unl.rootWord!=null)
			{
				String tense=unl.getTense();
				if(unl.rootWord==""||unl.rootWord==" ") unl.rootWord="be";
				if(tense=="past")
					questionToAdd=unl.getInterrogativeType()+" did ";

				else if(tense=="future")
					questionToAdd=unl.getInterrogativeType()+" will ";

				else if(tense=="present" && unl.hasAttribute("plural"))
					questionToAdd=unl.getInterrogativeType()+" do ";
				else
					questionToAdd=unl.getInterrogativeType()+" does ";

				return " "+unl.rootWord;

			}
		}catch(Exception e){
		return "";
		}
		return "";
	}

	public String Deconvert(UNLGraphNode UNL)
	{
		this.questionToAdd=null;
		this.numberIndicator=0;
		UNL.addDummyForDeconversion();
		UNL.printGraphBFS();
		String output=GraphDeconvert(UNL,-1,"");
		if(this.questionToAdd!=null)
			output=this.questionToAdd+output+"?";
		else 
			output=output+".";
		output=output.trim();
		return Character.toString(output.charAt(0)).toUpperCase()+output.substring(1);

	}

}
