import java.util.*;

import edu.uci.ics.jung.visualization.VisualizationViewer;
public class TreeBuilder {

	public TreeNode root;
	public int size;
	public int lieutenants;
	public int traitors;
	public boolean faultyGeneral;
	public boolean command;
    public VisualizationViewer<TreeNode,Number> vv;

	
	public void Initialize(){
		root = new TreeNode("G");
		root.id = 0;
		root.isTraitor = faultyGeneral;
		root.input = command;
		size = 1;
	}
	
	public void BuildTree(int traitorNum, TreeNode t)
	{
		
		if(traitorNum<1)
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

				if (t.isTraitor == true){
					c.input = new Random().nextBoolean();
				}
				else
				{
					c.input = t.input;
				}
				
				t.addChild(c);
				size++;
				c.color = (c.input == Constant.Attack)? Constant.Color_Attack : Constant.Color_Retreat;
				BuildTree(traitorNum-1, c);
			}			
		}
	}
	
	public void runAlgorithm(TreeNode t)
	{
		if(t.children.size() > 0) {
			for(TreeNode c: t.children) {
				runAlgorithm(c);
				if(c.output == Constant.Attack) {
					t.noOfAttack++;
				}
				else {
					t.noOfRetreat++;
				}
			}
			if(t.noOfAttack > t.noOfRetreat) {
				t.output = Constant.Attack;
				//t.color = Constant.Color_Attack;
			}
			else {
				t.output = Constant.Retreat;
				//t.color = Constant.Color_Retreat;
			}
		}
		else {
			if(t.isTraitor) {
				t.output = new Random().nextBoolean();
			}
			else {
				t.output = t.input;
			}
			//t.color = t.output? Constant.Color_Attack : Constant.Color_Retreat; 
		}
	}
	
    public void DrawTree(){
    	this.vv = Drawing.LoadTree(this);
    }
	
}