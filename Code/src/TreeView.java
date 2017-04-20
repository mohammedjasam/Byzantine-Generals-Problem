/**
 * Created by shudip on 2/8/2017.
 */
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.uci.ics.jung.visualization.VisualizationViewer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class TreeView {

    public TreeNode root;
    public int size;
    public ArrayList<TreeNode> nodes;
    public HashMap<String, Integer> database;
    public HashMap<String, Integer> nodeNameIdMap;
    public VisualizationViewer<TreeNode,Number> vv;
    public void DrawTree(){
    	this.vv = Drawing.LoadTree(this);
    }

    public TreeView () {
    	this.size = 0;
        this.nodes = new ArrayList<TreeNode>();
        this.database = new HashMap<String, Integer>();
        this.nodeNameIdMap = new HashMap<String, Integer>();
    }

    public void BuildTree(File file) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);
            document.getDocumentElement().normalize();
            Node root = document.getDocumentElement();
            this.root = new TreeNode(root.getNodeName());
            this.size++;
            BuildTreeRecursively(this.root, root);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void BuildTreeRecursively(TreeNode tn, Node n) {
        if (n.hasChildNodes()) {
        	tn.isLeaf = false;
        	tn.id = this.nodes.size();
            this.nodes.add(tn);
            this.nodeNameIdMap.put(tn.name, tn.id);
            NodeList nl = n.getChildNodes();
            int num = nl.getLength();
            for (int i = 0; i < num; i++) {
                Node node = nl.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    TreeNode cn = new TreeNode(node.getNodeName());
                    tn.addChild(cn);
                    this.size++;
                    BuildTreeRecursively(cn, node);
                }
            }
        } 
        else {
        	tn.isLeaf = true;
        	tn.id = this.nodes.size();
        	this.nodes.add(tn);
        	this.nodeNameIdMap.put(tn.name, tn.id);
        }
    }

    public void DrawTree(){
    	this.vv = Drawing.LoadTree(this);
    }

    public void BuildDatabase(File file) {
    	try {
	    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        Document document = builder.parse(file);
	        document.getDocumentElement().normalize();
	        
	        Node leafs = document.getDocumentElement();
	        NodeList nodeList = leafs.getChildNodes();
	        
	        for (int i=0; i<nodeList.getLength(); i++) {
	    		
	        	Node leaf = nodeList.item(i);
	    		if (leaf.getNodeType() == Node.ELEMENT_NODE) {
	
		    		NodeList userList = leaf.getChildNodes();
		    		for(int j=0; j<userList.getLength(); j++) {
		    			
		    			Node user = userList.item(j);
		    			if (user.getNodeType() == Node.ELEMENT_NODE) {
		    				
		    				//find the leaf nodes
		    				TreeNode currentLeaf = this.GetLeafNodeByName(leaf.getNodeName());
		    				//fill the global database
    						this.database.put(user.getTextContent(), currentLeaf.id);
    						//fill the cuurent leaf
    						currentLeaf.database.put(user.getTextContent(), currentLeaf.id);
    						currentLeaf.pointer.put(user.getTextContent(), currentLeaf.id);
    						currentLeaf.fpointer.put(user.getTextContent(), currentLeaf.id);
    						//fill in all its parent
    						TreeNode child = currentLeaf;
    						TreeNode parent = child.parent;
    						while(parent != null) {
    							parent.database.put(user.getTextContent(), currentLeaf.id);
    							parent.pointer.put(user.getTextContent(), child.id);
    							parent.fpointer.put(user.getTextContent(), child.id);
    							
    							child = parent;
    							parent = child.parent;
    						}
		    			}
		    		}
	    		}
	    	}
    	}
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public String[] GetAllLeafNodesName() {
    	ArrayList<String> leafs = new ArrayList<String>();
    	for(int k=0; k<this.nodes.size(); k++){
			TreeNode currentLeaf = nodes.get(k);
			if(currentLeaf.isLeaf == true){
				leafs.add(currentLeaf.name);
			}
    	}
    	Collections.sort(leafs);
    	return leafs.stream().toArray(String[]::new);
    }

    public String[] GetAllUsersName(TreeNode leaf) {
    	String[] users = new String[leaf.database.size()];
		int i=0;
		for (String key: leaf.database.keySet()) {
			users[i] = key;
			i++;
		}
		return users;
    }
    
    public TreeNode GetLeafNodeByName(String nodeName) {
    	/*
    	for(int k=0; k<this.nodes.size(); k++){
			TreeNode node = nodes.get(k);
			if(node.isLeaf == true && node.name.equals(nodeName)) {
				return node;
			}
    	}
    	return null;
    	*/
    	return this.nodes.get(this.nodeNameIdMap.get(nodeName));
    }
    
    public TreeNode GetNodeByName(String nodeName) {
    	/*
    	for(int k=0; k<this.nodes.size(); k++){
			TreeNode node = nodes.get(k);
			if(node.name.equals(nodeName)) {
				return node;
			}
    	}
    	return null;
    	*/
    	return this.nodes.get(this.nodeNameIdMap.get(nodeName));
    }
}
