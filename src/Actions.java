import java.util.ArrayList;

public class Actions 
{	
	public int mode;
	private TreeBuilder tb;
	
	public Actions(TreeBuilder tb) 
	{
		mode = Constant.Mode_Database_Value;
		this.tb = tb;
	}
	
	public void ShowAnimation(ArrayList<TreeNode> nodePath) 
	{
		new Thread()
		{
            @Override
            public void run()
            {
            	tb.vv.repaint();
            	int counter = 0;
            	for (TreeNode node: nodePath)
            	{
            		if(mode == Constant.Mode_Partition && node.isRepresentive == true) node.color = Constant.Color_Representive;
            		else if(counter == 0) node.color = Constant.Color_Source;
            		else if(counter == nodePath.size()-1) node.color = Constant.Color_Destination;
            		else node.color = Constant.Color_Animation;
                    try 
                    {
						sleep(1000);
						tb.vv.repaint();
					} 
                    catch (InterruptedException e) 
                    {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                    counter++;
                }
            	
            	try 
            	{
					sleep(2000);
				} 
            	catch (InterruptedException e) 
            	{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	for (TreeNode node: nodePath)
            	{
                    node.color=Constant.Color_Default;
                }
                tb.vv.repaint();
            }
        }.start();
	}
}