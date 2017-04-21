import java.io.File;

public class Constant {
    public static final File TREE_FILE = new File("Files/Tree.xml");
    public static final File USER_FILE = new File("Files/User.xml");
    
    public static final int Mode_Database_Value = 0;
    public static final int Mode_Actual_Pointer = 1;
    public static final int Mode_Forwarding_Pointer = 2;
    public static final int Mode_Partition = 3;
    
//    public static final int Color_Default = 0;
    public static final int Color_Animation = 1;
    public static final int Color_Representive = 2;
    public static final int Color_Source = 3;
    public static final int Color_Destination = 4;
    
    
    public static final int Color_Default = 0;
    public static final int Color_Attack = 1;
    public static final int Color_Retreat = 2;
    
    public static final boolean Attack = true;
    public static final boolean Retreat = false;
}