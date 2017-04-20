import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.GridLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import java.awt.Color;

import javax.swing.SwingConstants;

import javax.swing.JButton;
import javax.swing.DefaultComboBoxModel;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

public class Window {

	public static final int height=718;
	public static final int width=1366;
	public static int edgeName=0;
	
	private JFrame frame;
	private JPanel panel_up;
	private JPanel panel_down;
	private JPanel panel_mode;
	private JPanel panel_move;
	private JPanel panel_call;
	private JTextField txtLrmmm;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Window window = new Window();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Window() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		//initial file and tree load
		TreeView tv = new TreeView();
        tv.BuildTree(Constant.TREE_FILE);
        tv.BuildDatabase(Constant.USER_FILE);
        tv.DrawTree();
        
        //to fill the leaf combobox
        String[] leafNames = tv.GetAllLeafNodesName();
        //to do work
        Actions actions = new Actions(tv);
        
		
		frame = new JFrame();
		frame.setBounds(0, 0, width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.getContentPane().setLayout(null);
		
		
		panel_up = new JPanel();
		panel_up.setBackground(Color.WHITE);
		panel_up.setBounds(0, 0, width, height/5);
		frame.getContentPane().add(panel_up);
		panel_up.setLayout(new GridLayout(0, 3, 0, 0));
		
		panel_mode = new JPanel();
		panel_mode.setBackground(new Color(240, 255, 240));
		panel_up.add(panel_mode);
		
		JComboBox comboBox_mode = new JComboBox();
		comboBox_mode.setFont(new Font("Tahoma", Font.PLAIN, 14));
		comboBox_mode.setModel(new DefaultComboBoxModel(new String[] {"Database Value", "Actual Pointer", "Forwarding Pointer", "Partition"}));
		comboBox_mode.setSelectedIndex(0);
		comboBox_mode.setBounds(45, 41, 368, 23);
		comboBox_mode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				actions.mode = comboBox_mode.getSelectedIndex();
			}
		});
		panel_mode.setLayout(null);
		panel_mode.add(comboBox_mode);
		
		JLabel lblChangeMode = new JLabel("Change Mode");
		lblChangeMode.setHorizontalAlignment(SwingConstants.CENTER);
		lblChangeMode.setBounds(45, 11, 368, 23);
		panel_mode.add(lblChangeMode);
		
		txtLrmmm = new JTextField();
		txtLrmmm.setText("L,R,M1,M2,M3");
		txtLrmmm.setToolTipText("Enter Representive Nodes");
		txtLrmmm.setBounds(45, 109, 368, 20);
		panel_mode.add(txtLrmmm);
		txtLrmmm.setColumns(10);
		
		JButton btnMakePartition = new JButton("Make Partition");
		btnMakePartition.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actions.MakePartition(txtLrmmm.getText());
			}
		});
		btnMakePartition.setBounds(166, 75, 132, 23);
		panel_mode.add(btnMakePartition);
		actions.MakePartition(txtLrmmm.getText());//for the first time
		
		panel_move = new JPanel();
		panel_up.add(panel_move);
		panel_move.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("Current Cell");
		lblNewLabel_1.setBounds(71, 48, 94, 20);
		panel_move.add(lblNewLabel_1);

		
		JComboBox comboBox_move_current_user = new JComboBox();
		comboBox_move_current_user.setBounds(291, 48, 94, 20);
		panel_move.add(comboBox_move_current_user);
		
		JComboBox comboBox_move_current = new JComboBox();
		comboBox_move_current.setModel(new DefaultComboBoxModel(leafNames));
		comboBox_move_current.setSelectedIndex(-1);
		comboBox_move_current.setBounds(175, 48, 94, 20);
		comboBox_move_current.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TreeNode leaf = tv.GetLeafNodeByName(comboBox_move_current.getSelectedItem().toString());
				String[] users = tv.GetAllUsersName(leaf);
				comboBox_move_current_user.setModel(new DefaultComboBoxModel(users));
				comboBox_move_current_user.setSelectedIndex(0);
			}
		});
		panel_move.add(comboBox_move_current);
		
		JComboBox comboBox_move_new = new JComboBox();
		comboBox_move_new.setModel(new DefaultComboBoxModel(leafNames));
		comboBox_move_new.setSelectedIndex(-1);
		comboBox_move_new.setBounds(175, 79, 94, 20);
		panel_move.add(comboBox_move_new);
		
		JLabel lblNewLabel = new JLabel("New Cell");
		lblNewLabel.setBounds(71, 79, 94, 20);
		panel_move.add(lblNewLabel);
		
		JLabel lblChangeCell = new JLabel("Move User");
		lblChangeCell.setHorizontalAlignment(SwingConstants.CENTER);
		lblChangeCell.setBounds(71, 11, 314, 26);
		panel_move.add(lblChangeCell);
		
		JButton btnMove = new JButton("Move");
		btnMove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				actions.PerformUpdate(Integer.valueOf(comboBox_move_current_user.getSelectedItem().toString()),
						comboBox_move_current.getSelectedItem().toString(),
						comboBox_move_new.getSelectedItem().toString());
			}
		});
		btnMove.setBounds(175, 109, 94, 23);
		panel_move.add(btnMove);
		
		panel_call = new JPanel();
		panel_call.setBackground(new Color(245, 255, 250));
		panel_up.add(panel_call);
		panel_call.setLayout(null);
		
		JLabel lblSourceCell = new JLabel("Source User");
		lblSourceCell.setBounds(77, 48, 94, 20);
		panel_call.add(lblSourceCell);
		
		JLabel lblCall = new JLabel("Make a Call");
		lblCall.setHorizontalAlignment(SwingConstants.CENTER);
		lblCall.setBounds(77, 11, 314, 26);
		panel_call.add(lblCall);

		
		JComboBox comboBox_call_source_user = new JComboBox();
		comboBox_call_source_user.setBounds(297, 48, 94, 20);
		panel_call.add(comboBox_call_source_user);
		
		JComboBox comboBox_call_source = new JComboBox();
		comboBox_call_source.setModel(new DefaultComboBoxModel(leafNames));
		comboBox_call_source.setSelectedIndex(-1);
		comboBox_call_source.setBounds(181, 48, 94, 20);
		comboBox_call_source.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TreeNode leaf = tv.GetLeafNodeByName(comboBox_call_source.getSelectedItem().toString());
				String[] users = tv.GetAllUsersName(leaf);
				comboBox_call_source_user.setModel(new DefaultComboBoxModel(users));
				comboBox_call_source_user.setSelectedIndex(0);
			}
		});
		panel_call.add(comboBox_call_source);

		
		JComboBox comboBox_call_dest_user = new JComboBox();
		comboBox_call_dest_user.setBounds(297, 79, 94, 20);
		panel_call.add(comboBox_call_dest_user);
		
		JComboBox comboBox_call_dest = new JComboBox();
		comboBox_call_dest.setModel(new DefaultComboBoxModel(leafNames));
		comboBox_call_dest.setSelectedIndex(-1);
		comboBox_call_dest.setBounds(181, 79, 94, 20);
		comboBox_call_dest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TreeNode leaf = tv.GetLeafNodeByName(comboBox_call_dest.getSelectedItem().toString());
				String[] users = tv.GetAllUsersName(leaf);
				comboBox_call_dest_user.setModel(new DefaultComboBoxModel(users));
				comboBox_call_dest_user.setSelectedIndex(0);
			}
		});
		panel_call.add(comboBox_call_dest);
		
		JLabel lblDestinationCell = new JLabel("Destination User");
		lblDestinationCell.setBounds(77, 79, 94, 20);
		panel_call.add(lblDestinationCell);
		
		JButton btnCall = new JButton("Call");
		btnCall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actions.PerformCall(Integer.valueOf(comboBox_call_source_user.getSelectedItem().toString()), 
						Integer.valueOf(comboBox_call_dest_user.getSelectedItem().toString()));
			}
		});
		btnCall.setBounds(181, 109, 94, 23);
		panel_call.add(btnCall);
		
		panel_down = new JPanel();
		panel_down.setBackground(Color.RED);
		panel_down.setBounds(0, height/5, width, 4*(height/5));
		frame.getContentPane().add(panel_down);
		panel_down.setLayout(null);
		panel_down.add(tv.vv);
	}
}
