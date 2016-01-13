package UNLWordFormat;
import analyser.SegmentedWord;

import java.util.LinkedList;
import java.util.Queue;

public class SimpleNode{
	
	public String word;
	String attributes[];
	String tense;
	public int id;
	Integer attributes_id[];
	public int type,scopeID,number_of_attributes;
	public int IsComplete;
	public String relations[];
	public SimpleNode child[];
	public int number_of_childs;
	public SegmentedWord temp_seg[];
	public int inOpLength;
	
	public SimpleNode(String root)
	{
		this.word=root;
		this.type=0;
		this.tense=null;
		this.scopeID=4;
		this.number_of_attributes=0;
		this.IsComplete=0;
		this.child=new SimpleNode[10];
		this.number_of_childs=0;
		attributes=new String[10];
	}
	public SimpleNode(SegmentedWord temp[])
	{
		this.type=0;
		this.tense=null;
		this.scopeID=4;
		this.number_of_attributes=0;
		this.IsComplete=0;
		this.relations=new String[10];
		this.child=new SimpleNode[10];
		this.number_of_childs=0;
		this.temp_seg=temp;
		this.inOpLength=temp.length;
		attributes=new String[10];
	}
	
	public SimpleNode(String root,SegmentedWord temp[])
	{
		this.word=root;
		this.type=0;
		this.tense=null;
		this.scopeID=0;
		this.number_of_attributes=0;
		this.IsComplete=0;
		this.child=new SimpleNode[10];
		this.number_of_childs=0;
		this.temp_seg=temp;
		attributes=new String[10];
	}
	
	public SimpleNode(String root, int node_count) {
		this.word=root;
		this.type=0;
		this.tense=null;
		this.scopeID=0;
		this.number_of_attributes=0;
		attributes=new String[10];
	}

	public SimpleNode(SegmentedWord[] seg, int i, int j) {
		this.type=0;
		this.tense=null;
		this.scopeID=0;
		this.number_of_attributes=0;
		this.IsComplete=0;
		this.child=new SimpleNode[10];
		this.number_of_childs=0;
		this.temp_seg=new SegmentedWord[j];
		for(int k=0;k<j;k++)
			temp_seg[k]=seg[i++];
		attributes=new String[10];
	}
	
	public void AddScopeID(int id)
	{
		this.scopeID=id;
	}
	
	
	public void addAttribute(String att)
	{
		attributes[number_of_attributes++]=(att);
	
		if(att=="present")
			this.tense="present";
		else if(att=="past")
			this.tense="past";
		else if(att=="future")
			this.tense="future";
		
		//System.out.println("Adding tense"+this.tense);
		//this.attributes_id.add(ID_MAP(attribute));
	}
	
	public boolean hasAttribute(String attr)
	{
		for(int i=0;i<this.number_of_attributes;i++)
			if(this.attributes[i]==attr)
				return true;
				
		return false;
		
	}
	
	public String getRoot()
	{
		return word;
	}
	
	public String[] getAttributes()
	{
		return attributes;
	}
	
	public String printAttributes()
	{
		String str="";
		for(int i=0;i<this.number_of_attributes;i++)
		{
			str=str+" @"+attributes[i];
		}
		return str;
	}
	
	public String printNode()
	{
		return word+this.printAttributes();
	}
	
	public String getTense()
	{
	//	System.out.println("getting tense"+this.tense);
		return this.tense;
	}
	public SimpleNode addRelation(int i, String string, SimpleNode sub) {
		
		this.number_of_childs++;
		this.relations[this.number_of_childs-1]=string;
		this.child[this.number_of_childs-1]=sub;
		return this;
	}
	public SimpleNode removeSegment(int i, int j) {
		
		SegmentedWord temp[]=new SegmentedWord[this.temp_seg.length-j];
		for(int k=0;k<i;k++)
		{
			temp[k]=this.temp_seg[k];
		}
		for(int k=i;k<this.temp_seg.length-j-i;k++)
		{
			temp[k]=this.temp_seg[k+j];
		}
		this.temp_seg=temp;
		System.out.println("Removing from "+i+"th , count "+j+" "+"Head is "+temp_seg[0].rootWord);
		this.inOpLength-=j;
		return this;
	}
	public void printGraphBFS() {
		
		 System.out.println("[W]");
		 Queue<SimpleNode> queue=new LinkedList<SimpleNode>();
		 queue.add(this);
		 this.id=0;
		 SimpleNode i,element;
		 int count=0;
		 while (!queue.isEmpty())
	     {
	            element = queue.remove();
	            System.out.println(element.word+element.printAttributes());
	            element.id=++count;
	            for(int k=0;k<element.number_of_childs;k++)
	            {
	            	queue.add(element.child[k]);
	            }
	    }
		 System.out.println("[/W]");
		 queue.add(this);
		 this.id=1;
		 while (!queue.isEmpty())
	     {
	            element = queue.remove();
	           // element.id=count++;
	            for(int k=0;k<element.number_of_childs;k++)
	            {
	            	System.out.println(element.id+" "+element.relations[k]+" "+element.child[k].id);
	            	queue.add(element.child[k]);
	            }
	    }		
	}
	public String getRepresentation() {
		 
		String UNLrep="([W])"+"\n";
		
		Queue<SimpleNode> queue=new LinkedList<SimpleNode>();
		 queue.add(this);
		 this.id=0;
		 SimpleNode i,element;
		 int count=0;
		 while (!queue.isEmpty())
	     {
	            element = queue.remove();
	            UNLrep+=element.word+" "+element.printAttributes()+"\n";
	            element.id=++count;
	            for(int k=0;k<element.number_of_childs;k++)
	            {
	            	queue.add(element.child[k]);
	            }
	    }
		 UNLrep+="([/W])"+"\n";
		 queue.add(this);
		 this.id=1;
		 while (!queue.isEmpty())
	     {
	            element = queue.remove();
	           // element.id=count++;
	            for(int k=0;k<element.number_of_childs;k++)
	            {
	            	UNLrep+=element.id+" "+element.relations[k]+" "+element.child[k].id+"\n";
	            	queue.add(element.child[k]);
	            }
	    }
		return UNLrep;
	}
	
}