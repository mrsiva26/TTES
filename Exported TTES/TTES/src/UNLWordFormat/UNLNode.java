package UNLWordFormat;

public abstract class  UNLNode
{
	public int type;
	/* 0-SimpleNode
	 * 1-HyperNode
	 */
	public void addAttribute(String s){
	 System.out.println("calling mother class");	
	}
	
	public String printAttributes()
	{
		return null;
	}
	
	public String printNode()
	{
		return null;
	}
	
	public String[] getAttributes()
	{
		return null;
	}
	
	public String getRoot()
	{
		return null;
	}
	
	public String getTense()
	{
		return null;
	}
}


/*
public class HyperNode extends UNLNode {
	UNLNode origin;
	UNLNode destination;
	String Relation;
	int RelationID;
	
}
*/


