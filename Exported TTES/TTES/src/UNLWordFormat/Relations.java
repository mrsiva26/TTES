package UNLWordFormat;

public class Relations{
	
	public UNLNode Node1,Node2;
	public int node1id;
	public int node2id;
	public String relation;
	public int relation_id;
	
	
	public Relations(UNLNode node1,int node1id,UNLNode node2,int node2id,String relation)
	{
		this.Node1=node1;
		this.Node2=node2;
		this.node1id=node1id;
		this.node2id=node2id;
		this.relation=relation;
	}
	
	public String printRelation()
	{
		return this.node1id+" "+this.relation+" "+this.node2id;
	}
}

