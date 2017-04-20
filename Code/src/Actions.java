import java.util.ArrayList;

public class Actions {
	
	public int mode;
	private TreeView tv;
	private Ratio ratio;
	
	private static boolean partitionDestFound;
	
	public Actions(TreeView tv) {
		mode = Constant.Mode_Database_Value;
		ratio = new Ratio();
		this.tv = tv;
	}
	
	/*****************/
	//Update Operations
	/*****************/
	public void PerformUpdate(int user, String currentCell, String newCell) {
		TreeNode currentNode = tv.GetLeafNodeByName(currentCell);
		TreeNode newNode = tv.GetLeafNodeByName(newCell);
		tv.database.put(String.valueOf(user), newNode.id);
		
		PerformUpdate_Mode_Database_Value(user, currentNode, newNode);
		PerformUpdate_Mode_Actual_Pointer(user, currentNode, newNode);
		PerformUpdate_Mode_Forwarding_Pointer(user, currentNode, newNode);
		PerformUpdate_Mode_Partition(user, currentNode, newNode);
	}
	
	public void PerformUpdate_Mode_Database_Value(int user, TreeNode currentNode, TreeNode newNode) {
		//find the LCA from destination, add new user from dest to until LCA
		TreeNode dest = newNode;
		while(!dest.database.containsKey(String.valueOf(user))) {
			dest.database.put(String.valueOf(user), newNode.id);
			dest = dest.parent;
			ratio.IncrementUpdateCost(user, Constant.Mode_Database_Value);
		}
		//update the LCA
		TreeNode LCA = dest;
		LCA.database.put(String.valueOf(user), newNode.id);
		ratio.IncrementUpdateCost(user, Constant.Mode_Database_Value);
		//update LCA to parent
		TreeNode parent = LCA.parent;
		while(parent != null) {
			parent.database.put(String.valueOf(user), newNode.id);
			parent = parent.parent;
			ratio.IncrementUpdateCost(user, Constant.Mode_Database_Value);
		}
		//delete source to untill LCA
		TreeNode src = currentNode;
		while(src.id != LCA.id) {
			src.database.remove(String.valueOf(user));
			src = src.parent;
			ratio.IncrementUpdateCost(user, Constant.Mode_Database_Value);
		}
	}
	
	public void PerformUpdate_Mode_Actual_Pointer(int user, TreeNode currentNode, TreeNode newNode) {
		//add pointer from dest until LCA
		newNode.pointer.put(String.valueOf(user), newNode.id);
		ratio.IncrementUpdateCost(user, Constant.Mode_Actual_Pointer);
		TreeNode dest = newNode;
		TreeNode parent = dest.parent;		
		while(!parent.pointer.containsKey(String.valueOf(user))) {
			parent.pointer.put(String.valueOf(user), dest.id);
			dest = parent;
			parent = dest.parent;
			ratio.IncrementUpdateCost(user, Constant.Mode_Actual_Pointer);
		}
		//update the pointer of LCA
		TreeNode LCA = parent;
		LCA.pointer.put(String.valueOf(user), dest.id);
		ratio.IncrementUpdateCost(user, Constant.Mode_Actual_Pointer);
		//delete pointer from src until LCA
		TreeNode src = currentNode;
		while(src.id != LCA.id) {
			src.pointer.remove(String.valueOf(user));
			src = src.parent;
			ratio.IncrementUpdateCost(user, Constant.Mode_Actual_Pointer);
		}
	}

	public void PerformUpdate_Mode_Forwarding_Pointer(int user, TreeNode currentNode, TreeNode newNode) {
		TreeNode cParent = currentNode.parent;
		TreeNode nParent = newNode.parent;
		//first level LCA, ignore simple forwarding pointer
		if(cParent.id == nParent.id) { 
			newNode.fpointer.put(String.valueOf(user), newNode.id);
			ratio.IncrementUpdateCost(user, Constant.Mode_Forwarding_Pointer);
			nParent.fpointer.put(String.valueOf(user), newNode.id);
			ratio.IncrementUpdateCost(user, Constant.Mode_Forwarding_Pointer);
			currentNode.fpointer.remove(String.valueOf(user));
			ratio.IncrementUpdateCost(user, Constant.Mode_Forwarding_Pointer);
			return;
		}
		//get the LCA
		while(cParent.id != nParent.id) {
			cParent = cParent.parent;
			nParent = nParent.parent;
		}
		TreeNode LCA = nParent;
		
		//add pointer from dest until LCA
		newNode.fpointer.put(String.valueOf(user), newNode.id);
		ratio.IncrementUpdateCost(user, Constant.Mode_Forwarding_Pointer);
		TreeNode dest = newNode;
		TreeNode parent = dest.parent;		
		while(parent.id != LCA.id) {
			parent.fpointer.put(String.valueOf(user), dest.id);
			ratio.IncrementUpdateCost(user, Constant.Mode_Forwarding_Pointer);
			dest = parent;
			parent = dest.parent;
		}
		//get the intermidiate source
		TreeNode src = currentNode;
		while(src.parent.id != LCA.id) {
			src = src.parent;
		}
		//now delete all forwarding pointer to that user under src node
		DeleteAllForwardingPointer(user, src);
		//add forwarding pointer between intermidiate nodes
		src.fpointer.put(String.valueOf(user), dest.id);
		//ratio.IncrementUpdateCost(user, Constant.Mode_Forwarding_Pointer);
	}
	
	public void DeleteAllForwardingPointer(int user, TreeNode currentNode) {
		for(TreeNode child: currentNode.children) {
			DeleteAllForwardingPointer(user, child);
		}
		if(currentNode.fpointer.containsKey(String.valueOf(user))){
			currentNode.fpointer.remove(String.valueOf(user));
			ratio.IncrementUpdateCost(user, Constant.Mode_Forwarding_Pointer);
		}
	}

	public void PerformUpdate_Mode_Partition(int user, TreeNode currentNode, TreeNode newNode) {
		//add cost for source delete
		ratio.IncrementUpdateCost(user, Constant.Mode_Partition);
		//add cost for dest insert
		ratio.IncrementUpdateCost(user, Constant.Mode_Partition);
		//find the LCA
		TreeNode cParent = currentNode.parent;
		TreeNode nParent = newNode.parent;
		while(cParent.id != nParent.id) {
			cParent = cParent.parent;
			nParent = nParent.parent;
		}
		TreeNode LCA = nParent;
		//if LCA is the representive then no update
		if(LCA.isRepresentive) return;
		//if parent of LCA is the representive then no update
		TreeNode lcaParent = LCA.parent;
		while(lcaParent != null) {
			if(lcaParent.isRepresentive) return;
			lcaParent = lcaParent.parent;
		}
		//now as they are in different partition, find the cost
		boolean srcR = false;
		TreeNode src = currentNode;
		do {
			if(src.isRepresentive) srcR = true;
			if(srcR == true) ratio.IncrementUpdateCost(user, Constant.Mode_Partition);
			src = src.parent;
		}while (src.id != LCA.id);
		
		boolean destR = false;
		TreeNode dest = newNode;
		do {
			if(dest.isRepresentive) destR = true;
			if(destR == true) ratio.IncrementUpdateCost(user, Constant.Mode_Partition);
			dest = dest.parent;
		}while (dest.id != LCA.id);
		//now increment cost for LCA
		ratio.IncrementUpdateCost(user, Constant.Mode_Partition);
	}

	public void MakePartition(String partitionString) {
		//first, remove all representive nodes
		for(TreeNode node: tv.nodes) {
			node.isRepresentive = false;
		}
		
		String[] partitionList = partitionString.split(",");
		for(String partition: partitionList) {
			TreeNode representive = tv.GetNodeByName(partition);
			representive.isRepresentive = true;
		}
	}
	
	
	/***************/
	//Call Operations
	/***************/
	public void PerformCall(int caller, int callee) {
	
		switch(mode) {
			case Constant.Mode_Database_Value:
				PerformCall_Mode_Database_Value(caller, callee);
				break;
			case Constant.Mode_Actual_Pointer:
				PerformCall_Mode_Actual_Pointer(caller, callee);
				break;
			case Constant.Mode_Forwarding_Pointer:
				PerformCall_Mode_Forwarding_Pointer(caller, callee);
				break;
			case Constant.Mode_Partition:
				PerformCall_Mode_Partition(caller, callee);
				break;
		}
	}

	public void PerformCall_Mode_Database_Value(int caller, int callee) {
		ArrayList<TreeNode> nodePath  = new ArrayList<TreeNode>();
		
		TreeNode callerNode = this.tv.nodes.get(this.tv.database.get(String.valueOf(caller)));
		nodePath.add(callerNode);
		ratio.IncrementSearchCost(callee, mode);
		
		TreeNode parent = callerNode.parent;
		nodePath.add(parent);
		ratio.IncrementSearchCost(callee, mode);
		
		while(!parent.database.containsKey(String.valueOf(callee))) {
			parent = parent.parent;
			nodePath.add(parent);
			ratio.IncrementSearchCost(callee, mode);
		}
		TreeNode calleeNode = tv.nodes.get(parent.database.get(String.valueOf(callee)));
		nodePath.add(calleeNode);
		ratio.IncrementSearchCost(callee, mode);
		
		ShowAnimation(nodePath);
		ratio.PrintUpdateVsSearchRatio(callee, mode);
	}
	
	public void PerformCall_Mode_Actual_Pointer(int caller, int callee) {
		ArrayList<TreeNode> nodePath  = new ArrayList<TreeNode>();
		
		TreeNode callerNode = this.tv.nodes.get(this.tv.database.get(String.valueOf(caller)));
		nodePath.add(callerNode);
		ratio.IncrementSearchCost(callee, mode);
		
		TreeNode parent = callerNode.parent;
		nodePath.add(parent);
		ratio.IncrementSearchCost(callee, mode);
		
		while(!parent.pointer.containsKey(String.valueOf(callee))) {
			parent = parent.parent;
			nodePath.add(parent);
			ratio.IncrementSearchCost(callee, mode);
		}
		
		TreeNode child = this.tv.nodes.get(parent.pointer.get(String.valueOf(callee)));
		nodePath.add(child);
		ratio.IncrementSearchCost(callee, mode);
		
		while(child.id != this.tv.database.get(String.valueOf(callee))) {
			child = this.tv.nodes.get(child.pointer.get(String.valueOf(callee)));
			nodePath.add(child);
			ratio.IncrementSearchCost(callee, mode);
		}
		
		ShowAnimation(nodePath);
		ratio.PrintUpdateVsSearchRatio(callee, mode);
	}
	
	public void PerformCall_Mode_Forwarding_Pointer(int caller, int callee) {
		ArrayList<TreeNode> nodePath  = new ArrayList<TreeNode>();
		
		TreeNode callerNode = this.tv.nodes.get(this.tv.database.get(String.valueOf(caller)));
		nodePath.add(callerNode);
		ratio.IncrementSearchCost(callee, mode);
		
		TreeNode parent = callerNode.parent;
		nodePath.add(parent);
		ratio.IncrementSearchCost(callee, mode);
		
		while(!parent.fpointer.containsKey(String.valueOf(callee))) {
			parent = parent.parent;
			nodePath.add(parent);
			ratio.IncrementSearchCost(callee, mode);
		}
		
		TreeNode child = this.tv.nodes.get(parent.fpointer.get(String.valueOf(callee)));
		nodePath.add(child);
		ratio.IncrementSearchCost(callee, mode);
		
		while(child.id != this.tv.database.get(String.valueOf(callee))) {
			child = this.tv.nodes.get(child.fpointer.get(String.valueOf(callee)));
			nodePath.add(child);
			ratio.IncrementSearchCost(callee, mode);
		}
		
		ShowAnimation(nodePath);
		ratio.PrintUpdateVsSearchRatio(callee, mode);
	}
	
	public void PerformCall_Mode_Partition(int caller, int callee) {
		ArrayList<TreeNode> nodePath  = new ArrayList<TreeNode>();
		partitionDestFound = false;
		
		TreeNode callerNode = this.tv.nodes.get(this.tv.database.get(String.valueOf(caller)));
		nodePath.add(callerNode);
		ratio.IncrementSearchCost(callee, mode);
		
		TreeNode parent = callerNode.parent;		
		while(parent.isRepresentive == false) {
			parent = parent.parent;
		}
		nodePath.add(parent);
		ratio.IncrementSearchCost(callee, mode);
		
		//check if this representive contains callee
		if(parent.pointer.containsKey(String.valueOf(callee))) {
			RecurciveTraverseLeafNodes(parent, callee, callerNode, nodePath);
		}
		//go to upper nodes
		else {
			do {
				parent = parent.parent;
				nodePath.add(parent);
				ratio.IncrementSearchCost(callee, mode);
			}
			while(!parent.pointer.containsKey(String.valueOf(callee)));
			//go down to find the representattive
			TreeNode child = this.tv.nodes.get(parent.pointer.get(String.valueOf(callee)));
			nodePath.add(child);	
			ratio.IncrementSearchCost(callee, mode);
			while(child.isRepresentive == false) {
				child = this.tv.nodes.get(child.pointer.get(String.valueOf(callee)));
				nodePath.add(child);
				ratio.IncrementSearchCost(callee, mode);
			}
			RecurciveTraverseLeafNodes(child, callee, callerNode, nodePath);
		}			
		ShowAnimation(nodePath);
		ratio.PrintUpdateVsSearchRatio(callee, mode);
	}
	
	public void RecurciveTraverseLeafNodes(TreeNode node, int callee, TreeNode callerNode, ArrayList<TreeNode> nodePath) {
		if(node.children.size() > 0){
			for(TreeNode child: node.children) {
				RecurciveTraverseLeafNodes(child, callee, callerNode, nodePath);
			}
		}
		else {
			if(partitionDestFound == false &&  callerNode.id != node.id) {
				nodePath.add(node);
				ratio.IncrementSearchCost(callee, mode);
			}
			if(node.pointer.containsKey(String.valueOf(callee))) {
				partitionDestFound = true;
			}
		}
	}
	
	public void ShowAnimation(ArrayList<TreeNode> nodePath) {
		new Thread(){
            @Override
            public void run(){
            	tv.vv.repaint();
            	int counter = 0;
            	for (TreeNode node: nodePath){
            		if(mode == Constant.Mode_Partition && node.isRepresentive == true) node.color = Constant.Color_Representive;
            		else if(counter == 0) node.color = Constant.Color_Source;
            		else if(counter == nodePath.size()-1) node.color = Constant.Color_Destination;
            		else node.color = Constant.Color_Animation;
                    try {
						sleep(1000);
						tv.vv.repaint();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                    counter++;
                }
            	
            	try {
					sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	for (TreeNode node: nodePath){
                    node.color=Constant.Color_Default;
                }
                tv.vv.repaint();
            }
        }.start();
	}
}
