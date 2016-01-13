package UNLWordFormat;
import analyser.SegmentedWord;

import java.util.*;
public class UNLGraphNode
{
	public String rootWord;
	public LinkedList <String> attributes;
	String tense;
	public int id;
	public int type;
	public int scopeID;
	public int number_of_attributes;
	public int IsComplete;
	public LinkedList <String> relations;
	public LinkedList <UNLGraphNode> children;
	public int number_of_childs;
	public LinkedList <SegmentedWord> segments;
	public SegmentedWord temp_seg[];
	public int inOpLength;
	public int assignedPOS;
	public String parentRelation;
	private static final boolean debug = false;

	public UNLGraphNode(String root)
	{
		this.rootWord=root;
		this.type=0;
		this.tense=null;
		this.scopeID=0;
		this.number_of_attributes=0;
		this.IsComplete=0;
		this.number_of_childs=0;
		this.attributes=new  LinkedList <String>();
		this.relations=new  LinkedList <String>();
		this.children=new  LinkedList <UNLGraphNode>();
		this.segments=new LinkedList<SegmentedWord>();
	}
	
	public UNLGraphNode(SegmentedWord temp[])
	{
		this.type=0;
		this.tense=null;
		this.scopeID=4;
		this.number_of_attributes=0;
		this.IsComplete=0;
		this.number_of_childs=0;
		this.temp_seg=temp;
		this.inOpLength=temp.length;
		this.attributes=new  LinkedList <String>();
		this.relations=new  LinkedList <String>();
		this.children=new  LinkedList <UNLGraphNode>();
		this.segments=new LinkedList<SegmentedWord>(Arrays.asList(temp));
	}
	
	public UNLGraphNode(LinkedList<SegmentedWord> seg,int fromIndex,int numberOfNodes)
	{
		this.type=0;
		this.tense=null;
		this.scopeID=0;
		this.number_of_attributes=0;
		this.IsComplete=0;
		this.number_of_childs=0;
		this.attributes=new  LinkedList <String>();
		this.relations=new  LinkedList <String>();
		this.children=new  LinkedList <UNLGraphNode>();
		this.segments=new LinkedList<SegmentedWord>(seg.subList(fromIndex, fromIndex+numberOfNodes));
	}
	
	public UNLGraphNode(LinkedList<SegmentedWord> segments) {
		this.type=0;
		this.tense=null;
		this.scopeID=4;
		this.number_of_attributes=0;
		this.IsComplete=0;
		this.number_of_childs=0;
		this.attributes=new LinkedList <String>();
		this.relations = new  LinkedList <String>();
		this.children  =  new   LinkedList <UNLGraphNode>();
		this.segments=segments;
	}

	public void AddScopeID(int id)
	{
		this.scopeID=id;
	}
	
	public void addAttribute(String attribute)
	{
		attribute=attribute.toLowerCase();
		
		if(attribute=="present")
			this.tense="present";
		else if(attribute=="past")
			this.tense="past";
		else if(attribute=="future")
			this.tense="future";
		
		this.attributes.add(attribute);
	}
	
	public int getAttributeCount()
	{
		return this.attributes.size();
	}
	
	public boolean hasAttribute(String attribute)
	{
		return this.attributes.contains(attribute);
	}
	
	public String getRoot()
	{
		return this.rootWord.toLowerCase();
	}
	
	public String printAttributes()
	{
		String str="";
		
		for(String attribute:this.attributes)
		{
			str=str+" @"+attribute;
		}
		return str;
	}
	
	public String printNode()
	{
		return this.rootWord+this.printAttributes();
	}
	
	public String getTense()
	{
		return this.tense;
	}
	
	public UNLGraphNode addRelation(String string, UNLGraphNode sub) {		
		this.number_of_childs++;
		this.relations.add(string);
		this.children.add(sub);
		return this;
	}
	
	public void addParentRelation(String string){
		this.parentRelation=string;
	}
	public UNLGraphNode removeSegment(int i, int j) {
		
		/*SegmentedWord temp[]=new SegmentedWord[this.temp_seg.length-j];
		
		for(int k=0;k<i;k++)
		{
			temp[k]=this.temp_seg[k];
		}
		for(int k=i;k<this.temp_seg.length-j-i;k++)
		{
			temp[k]=this.temp_seg[k+j];
		}
		this.temp_seg=temp;*/
		
		while(j-->0)
			this.segments.remove(i);
		
		if(debug)
			System.out.println("Removing from "+i+" th , count "+j+" "+"Head is "+this.segments.get(0).rootWord);
		
		this.inOpLength-=j;
		return this;
	}
	
	public void printGraphBFS() {
		
		 System.out.println("[W]");
		 Queue<UNLGraphNode> queue=new LinkedList<UNLGraphNode>();
		 queue.add(this);
		 this.id=0;
		 UNLGraphNode i,element;
		 int count=0;
		 while (!queue.isEmpty())
	     {
	            element = queue.remove();
	            
	            System.out.println(element.rootWord+element.printAttributes());
	            
	            element.id=++count;
	            
	            for(UNLGraphNode child:element.children)
	            {
	            	queue.add(child);
	            }
	    }
		 
		 System.out.println("[/W]");
		 queue.add(this);
		 this.id=1;
		 while (!queue.isEmpty())
	     {
	            element = queue.remove();
	           // element.id=count++
	            int index=0;
	            for(UNLGraphNode child:element.children)
	            {
	            	child.parentRelation=element.relations.get(index);
	            	System.out.println(element.id+" "+child.parentRelation+" "+child.id);
	            	index++;
	            	queue.add(child);
	            }
	    }		
	}
	
	public String getRepresentation() {
		
		 String UNLrep="([W])"+"\n";
		 Queue<UNLGraphNode> queue=new LinkedList<UNLGraphNode>();
		 queue.add(this);
		 this.id=0;
		 UNLGraphNode element;
		 int count=0;
		 while (!queue.isEmpty())
	     {
	            element = queue.remove();
	            
	            UNLrep+=element.rootWord+element.printAttributes()+"\n";
	            
	            element.id=++count;
	            
	            for(UNLGraphNode child:element.children)
	            {
	            	queue.add(child);
	            }
	    }
		 
		 UNLrep+="[/W]"+"\n";
		 queue.add(this);
		 this.id=1;
		 while (!queue.isEmpty())
	     {
	            element = queue.remove();
	           // element.id=count++
	            int index=0;
	            for(UNLGraphNode child:element.children)
	            {
	            	child.parentRelation=element.relations.get(index);
	            	UNLrep+=element.id+" "+child.parentRelation+" "+child.id+"\n";
	            	index++;
	            	queue.add(child);
	            }
	    }		
		 
		return UNLrep;
	}

	public boolean hasRelation(String string) {
		
		for(String relation:this.relations)
		{
			if(relation.equals(string))
				return true;
		}
		return false;
	}
	
	class RelationSorter implements Comparator<UNLGraphNode> {

		public int getPriorityOrder(String relation)
		{

			if(relation.equals("seq")) return 0;
			else if(relation.equals("cnd")) return 1;
			else if(relation.equals("mod")) return 2;
			else if(relation.equals("tmi")) return 3;
			else if(relation.equals("dur")) return 4;
			else if(relation.equals("agt")) return 5;
			else if(relation.equals("unl")) return 6;
			else if(relation.equals("aoj")) return 7;
			else if(relation.equals("obj")) return 8;
			else if(relation.equals("cnt")) return 9;
			else if(relation.equals("man")) return 10;
			else if(relation.equals("frm")) return 11;
			else if(relation.equals("to")) return 12;
			else if(relation.equals("gol")) return 13;
			else if(relation.equals("plc")) return 14;
			else if(relation.equals("via")) return 15;
			else if(relation.equals("and")) return 16;
			else if(relation.equals("or")) return  17;
			else if(relation.equals("rsn")) return 19;
			else return 20;
		}
		
		public int compare(UNLGraphNode arg0, UNLGraphNode arg1) {
			return getPriorityOrder(arg0.parentRelation)-getPriorityOrder(arg1.parentRelation);
		}
	}

	class StringRelationSorter implements Comparator<String> {
		
		public int getPriorityOrder(String relation)
		{
			if(relation.equals("seq")) return 0;
			else if(relation.equals("cnd")) return 1;
			else if(relation.equals("mod")) return 2;
			else if(relation.equals("tmi")) return 3;
			else if(relation.equals("dur")) return 4;
			else if(relation.equals("agt")) return 5;
			else if(relation.equals("unl")) return 6;
			else if(relation.equals("aoj")) return 7;
			else if(relation.equals("obj")) return 8;
			else if(relation.equals("cnt")) return 9;
			else if(relation.equals("man")) return 10;
			else if(relation.equals("frm")) return 11;
			else if(relation.equals("to")) return 12;
			else if(relation.equals("gol")) return 13;
			else if(relation.equals("plc")) return 14;
			else if(relation.equals("via")) return 15;
			else if(relation.equals("and")) return 16;
			else if(relation.equals("or")) return  17;
			else if(relation.equals("rsn")) return 19;
			else return 20;
		}
		
		public int compare(String arg0, String arg1) {
			return getPriorityOrder(arg0)-getPriorityOrder(arg1);
		}
	}
	public void reOrder() {

		 Queue<UNLGraphNode> queue=new LinkedList<UNLGraphNode>();
		 queue.add(this);
		 this.id=0;
		 UNLGraphNode element;
		 int count=0;
		 while (!queue.isEmpty())
	     {
	            element = queue.remove();	
	            Collections.sort(element.children,new RelationSorter());
	            Collections.sort(element.relations,new StringRelationSorter());
	            for(UNLGraphNode child:element.children)
	            {
	            	queue.add(child);
	            }
	    }
		
	}
	
	public void reCondense()
	{
		Queue<UNLGraphNode> queue=new LinkedList<UNLGraphNode>();
		 queue.add(this);
		 this.id=0;
		 UNLGraphNode element;
		 int count=0;
		 while (!queue.isEmpty())
	     {
	            element = queue.remove();
	            for(SegmentedWord segment:element.segments)
	            {
	            	for(String attribute:segment.acquired_attributes)
	            		element.addAttribute(attribute);
	            	for(UNLGraphNode node:segment.associated_nodes)
	            	{
	            		element.addRelation(segment.relations.get(segment.associated_nodes.indexOf(node)),node);
	            	}
	            }
	            for(UNLGraphNode child:element.children)
	            {
	            	queue.add(child);
	            }
	    }
		
	}
	
	public void addDummyForDeconversion()
	{
		Queue<UNLGraphNode> queue=new LinkedList<UNLGraphNode>();
		 queue.add(this);
		 this.id=0;
		 UNLGraphNode element;
		 int count=0;
		 while (!queue.isEmpty())
	     {
	            element = queue.remove();
	            if(element.children.size()>0)
	            {
	            	UNLGraphNode sub=new UNLGraphNode("");
	        		sub.parentRelation="unl";
	        		element.addRelation("unl", sub);
	            }
	            for(UNLGraphNode child:element.children)
	            {
	            	queue.add(child);
	            }
	    }
		
		this.reOrder();
	}
	
	public void acquire(LinkedList<String> relations2,
			LinkedList<String> attributes2, LinkedList<UNLGraphNode> children2) 
	{
		this.relations=new LinkedList<String> (relations2.subList(0, relations2.size()));
		this.attributes=new LinkedList<String>  (attributes2.subList(0,attributes2.size()));
		this.children=new LinkedList<UNLGraphNode> (children2.subList(0, children2.size()));
		
	}
	public void acquireAttributes(LinkedList<String> attributes2) 
	{
		this.attributes=new LinkedList<String>  (attributes2.subList(0,attributes2.size()));	
		
	}
	

	public void resolveAll() {
			this.relations.clear();
			this.attributes.clear();
			this.children.clear();
	}

	public String getInterrogativeType() {
		
		if(this.hasAttribute("what"))
			return "what";
		if(this.hasAttribute("when"))
			return "when";
		if(this.hasAttribute("how"))
			return "how";
		if(this.hasAttribute("where"))
			return "where";
		if(this.hasAttribute("who"))
			return "who";
		return "";
	}

	
}