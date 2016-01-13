package UNLWordFormat;

public class UNLGraph {
	
	public UNLNode nodes[];
    public Relations relations[];   
    public int relation_count;
    public int node_count;
    
    public UNLGraph(int n)
    {
    	relation_count=0;
    	node_count=0;
    	nodes= new UNLNode[n];
    	relations=new Relations[n];
    }
    
    public UNLGraph(UNLNode nodes[],Relations relations[],int node_count,int relation_count)
    {
    	this.node_count=node_count;
    	this.relation_count=relation_count;
    	this.nodes=nodes;
    	this.relations=relations;
    }
    public Relations[] getRelations()
    {
    	return this.relations;
    }
    
    
    public UNLNode[] getNodes()
    {
    	return this.nodes;
    }
    
    public void printUNL()
    {
    	System.out.println("[UNL]\n[W]");
    	for(int i=0;i<node_count;i++)
    	{
    		System.out.println(nodes[i].printNode());
    	}
    	System.out.println("\n[\\W]");
    	
    	for(int i=0;i<relation_count;i++)
    		System.out.println(relations[i].printRelation());
    	
    	System.out.println("\n[\\UNL]");
   }
    
   public String getUNL()
   {
	   String output="[UNL]\n[W]";
	   for(int i=0;i<node_count;i++)
   		{
   			output+=nodes[i].printNode()+"\n";
   		}
   	  output+="\n[\\W]";
   	
   	  for(int i=0;i<relation_count;i++)
   		  output+=relations[i].printRelation();
   	
   		output+="\n[\\UNL]";
   		return output;
	   
   }
}


