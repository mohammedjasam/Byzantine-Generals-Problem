import java.util.*;
public class TreeBuilder {

	TreeNode root;
	
	public TreeBuilder(){
		root = new TreeNode("G");
		root.id = 0;
	}
	
	public void BuildTree(int lieutenants, int traitors, TreeNode t){
		
		for(int i = 0; i<lieutenants; i++){
			TreeNode c = new TreeNode("L"+(i+1));
			c.id=i+1;
			t.children.add(c);
			
		}
	}
	
}
