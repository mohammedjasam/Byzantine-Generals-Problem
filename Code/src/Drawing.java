import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.util.ArrayDeque;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.ConstantTransformer;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.algorithms.shortestpath.MinimumSpanningForest2;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.DelegateTree;
import edu.uci.ics.jung.graph.DirectedOrderedSparseMultigraph;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer;

public class Drawing {
	
	public static VisualizationViewer<TreeNode,Number> LoadTree(TreeView tv){

        Graph<TreeNode,Number> graph = createGraph(tv);

        VisualizationViewer<TreeNode,Number> vv;
        Dimension dimension = new Dimension(Window.width,Window.height);

        MinimumSpanningForest2<TreeNode,Number> prim = new MinimumSpanningForest2<TreeNode,Number>(graph,
                        new DelegateForest<TreeNode,Number>(), DelegateTree.<TreeNode,Number>getFactory(),
                        new ConstantTransformer(1.0));
        Forest<TreeNode,Number> tree = prim.getForest();

        // create two layouts for the one graph, one layout for each model
        Layout<TreeNode,Number> layout1 = new TreeLayout<TreeNode,Number>(tree,39,120);
        Transformer<TreeNode,Shape> vertexSize = new Transformer<TreeNode,Shape>(){
            public Shape transform(TreeNode i){
                Ellipse2D circle = new Ellipse2D.Double(-8, -6, 16, 12);
                // in this case, the vertex is twice as large
                return AffineTransform.getScaleInstance(2, 2).createTransformedShape(circle);
            }
        };

        Transformer<TreeNode,Paint> vertexColor = new Transformer<TreeNode,Paint>() {
            public Paint transform(TreeNode n) {
            	if(n.color == Constant.Color_Animation){
            		return Color.MAGENTA;
            	}
            	else if(n.color == Constant.Color_Representive){
            		return Color.BLUE;
            	}
            	else if(n.color == Constant.Color_Source){
            		return Color.RED;
            	}
            	else if(n.color == Constant.Color_Destination){
            		return Color.GREEN;
            	}
            	else {
            		return Color.WHITE;
            	}
            }
        };

        vv = new VisualizationViewer<TreeNode,Number>(layout1, dimension);
        vv.setBounds(0, -100, Window.width, Window.height);

        vv.getRenderContext().setVertexFillPaintTransformer(vertexColor);
        vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line());
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vv.getRenderContext().setVertexShapeTransformer(vertexSize);

        vv.setBackground(Color.decode("0xffaa55"));
        vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
        vv.setLayout(new BorderLayout());

        return vv;
    }

    private static Graph<TreeNode, Number> createGraph(TreeView tv) {
        Graph<TreeNode, Number> graph =   new DirectedOrderedSparseMultigraph<TreeNode, Number>();
        String[] vertex=new String[tv.size];
        ArrayDeque<TreeNode> nodeQue=new ArrayDeque<TreeNode>();
        nodeQue.add(tv.root);

        while(!nodeQue.isEmpty()){
            TreeNode dtn=nodeQue.poll();
            TreeNode dtnParent=nodeQue.poll();
            graph.addVertex(dtn);

            if(dtnParent!=null){
                graph.addEdge(Window.edgeName,dtnParent,dtn);
                Window.edgeName++;
            }

            for(TreeNode children:dtn.children){
                nodeQue.add(children);
                nodeQue.add(dtn);
            }
        }
        return graph;
    }

}
