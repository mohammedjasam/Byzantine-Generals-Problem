import java.util.*;

import edu.uci.ics.jung.visualization.VisualizationViewer;
public class TreeBuilder {

	TreeNode root;
	int size;
    public VisualizationViewer<TreeNode,Number> vv;

	
	public void Initialize(){
		root = new TreeNode("G");
		root.id = 0;
		size = 1;
	}
	
	public void BuildTree(int lieutenants, int traitors, TreeNode t)
	{
		if(traitors<1)
		{
			return;
		}
		for(int i = 0; i<lieutenants; i++)
		{
			if(t.path.contains(String.valueOf(i+1))==false)
			{
				TreeNode c = new TreeNode("L"+(i+1));
				c.id=i+1;
				c.path += c.id;		
				t.addChild(c);
				size++;
				BuildTree(lieutenants, traitors-1, c);
			}			
		}
	}
	
    public void DrawTree(){
    	this.vv = Drawing.LoadTree(this);
    }
	
}