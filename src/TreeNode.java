import java.util.*;

public class TreeNode {
	
	public boolean input;
	public boolean output;
	public String path;
		
	
	public boolean isTraitor;
	public String name;
	public int id;
	public int color;
	public boolean isLeaf;
    public TreeNode parent;
    public List<TreeNode> children;
    public HashMap<String, Integer> database;
    public HashMap<String, Integer> pointer;
    public HashMap<String, Integer> fpointer;
    public boolean isRepresentive;

    public TreeNode(String name){
    	this.path = "0";   	
        this.name = name;
    	this.isTraitor = false;
        this.id = -1;
        this.parent = null;
        this.color = Constant.Color_Default;
        this.children = new ArrayList<TreeNode>();
        this.database = new HashMap<String, Integer>();
        this.pointer = new HashMap<String, Integer>();
        this.fpointer = new HashMap<String, Integer>();
    }

    public void addChild(TreeNode child){
        this.children.add(child);
        child.parent = this;
    }
   
    @Override
    public String toString(){
        return name;
    }
}