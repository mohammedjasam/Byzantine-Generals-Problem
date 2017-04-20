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
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

import java.awt.FlowLayout;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import net.miginfocom.swing.MigLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

public class Window {

	public static final int height=718;
	public static final int width=1366;
	public static int edgeName=0;
	
	private JFrame frame;
	private JPanel panel_up;
	private JPanel panel_down;
	private JPanel panel1;
	private JPanel panel2;
	private JPanel panel3;
	private JTextField tf_Lieutenants;
	private JTextField tf_Traitors;
	private JTextField tf_FinalOutput;
	private JScrollPane scrollPane;

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
		TreeBuilder tb = new TreeBuilder();
        tb.BuildTree(10,3,tb.root);
        tb.DrawTree();
        

        //to do work
        Actions actions = new Actions(tb);
        
		
		frame = new JFrame();
		frame.setBounds(0, 0, width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.getContentPane().setLayout(null);
		
		
		panel_up = new JPanel();
		panel_up.setBackground(Color.WHITE);
		panel_up.setBounds(0, 0, 1366, 112);
		frame.getContentPane().add(panel_up);
		panel_up.setLayout(new GridLayout(0, 3, 0, 0));
		
		panel1 = new JPanel();
		panel1.setBackground(Color.LIGHT_GRAY);
		panel_up.add(panel1);
		
		JCheckBox cb_FaultyGeneral = new JCheckBox("Faulty General");
		cb_FaultyGeneral.setBackground(Color.LIGHT_GRAY);
		cb_FaultyGeneral.setBounds(109, 24, 153, 31);
		cb_FaultyGeneral.setHorizontalAlignment(SwingConstants.CENTER);
		
		JCheckBox cb_ShowIData = new JCheckBox("Show Intermediate Data");
		cb_ShowIData.setBackground(Color.LIGHT_GRAY);
		cb_ShowIData.setBounds(107, 66, 237, 31);
		cb_ShowIData.setHorizontalAlignment(SwingConstants.CENTER);
		panel1.setLayout(null);
		panel1.add(cb_FaultyGeneral);
		panel1.add(cb_ShowIData);
		
		panel2 = new JPanel();
		panel2.setBackground(Color.LIGHT_GRAY);
		panel_up.add(panel2);
		panel2.setLayout(new GridLayout(0, 2, 0, 0));
		
		JLabel lb_Lieutenants = new JLabel("Number of Lieutenant: ");
		lb_Lieutenants.setHorizontalAlignment(SwingConstants.CENTER);
		panel2.add(lb_Lieutenants);
		
		tf_Lieutenants = new JTextField();
		tf_Lieutenants.setHorizontalAlignment(SwingConstants.CENTER);
		panel2.add(tf_Lieutenants);
		tf_Lieutenants.setColumns(10);
		
		JLabel lb_Traitors = new JLabel("Number of Traitors: ");
		lb_Traitors.setHorizontalAlignment(SwingConstants.CENTER);
		panel2.add(lb_Traitors);
		
		tf_Traitors = new JTextField();
		tf_Traitors.setHorizontalAlignment(SwingConstants.CENTER);
		tf_Traitors.setColumns(10);
		panel2.add(tf_Traitors);
		
		JRadioButton rb_Retreat = new JRadioButton("Retreat");
		rb_Retreat.setBackground(Color.LIGHT_GRAY);
		rb_Retreat.setHorizontalAlignment(SwingConstants.CENTER);
		panel2.add(rb_Retreat);
		
		JRadioButton rb_Attack = new JRadioButton("Attack");
		rb_Attack.setBackground(Color.LIGHT_GRAY);
		rb_Attack.setHorizontalAlignment(SwingConstants.CENTER);
		panel2.add(rb_Attack);
		
		panel3 = new JPanel();
		panel3.setBackground(Color.LIGHT_GRAY);
		panel_up.add(panel3);
		panel3.setLayout(new GridLayout(0, 1, 0, 0));
		
		JLabel lb_FinalOutput = new JLabel("Final Output");
		lb_FinalOutput.setHorizontalAlignment(SwingConstants.CENTER);
		panel3.add(lb_FinalOutput);
		
		tf_FinalOutput = new JTextField();
		tf_FinalOutput.setBackground(Color.LIGHT_GRAY);
		tf_FinalOutput.setHorizontalAlignment(SwingConstants.CENTER);
		tf_FinalOutput.setEditable(false);
		panel3.add(tf_FinalOutput);
		tf_FinalOutput.setColumns(10);
		
		panel_down = new JPanel();
		panel_down.setBackground(Color.RED);
		panel_down.setBounds(0, 111, 1366, 604);
		frame.getContentPane().add(panel_down);
		panel_down.setLayout(null);
		panel_down.add(tb.vv);
		
//		scrollPane = new JScrollPane();
//		scrollPane.setBounds(0, 0, 2, 2);
//		panel_down.add(scrollPane);
		JScrollPane scrollPane = new JScrollPane(panel_down);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(1000,1000,1000,1000);
	}
}
