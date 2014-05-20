package FileShare;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JList;
import java.awt.Choice;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class CustomChooserFrame extends JFrame {

	private JPanel contentPane;
	static JTextField ipField;
	private Choice choice;
	private JButton send;
	private FileSender fileSender;
	private CopyDialog copy;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CustomChooserFrame frame = new CustomChooserFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public CustomChooserFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 86);
		copy = new CopyDialog();
		copy.setBounds(WindowProperty.getWidth() - 200,
				WindowProperty.getHeight() - 200, 200, 100);
		fileSender = new FileSender();
		fileSender.copy = this.copy;

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		ipField = new JTextField();
		contentPane.add(ipField, BorderLayout.WEST);
		ipField.setColumns(10);
		ipField.setEnabled(false);
		choice = new Choice();
		choice.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if(arg0.getItem().equals("Local Lan Share")){
					ipField.setEnabled(false);
				}else{
					ipField.setEnabled(true);
				}
			}
		});
		contentPane.add(choice, BorderLayout.CENTER);

		choice.add("Local Lan Share");
		choice.add("FTP");
		choice.add("Cloud");
		choice.add("DataBase");

		send = new JButton("SEND");
		send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println(choice.getSelectedIndex());
				if (choice.getSelectedItem() != null) {
					Test.selectedItem = choice.getSelectedItem();
					setVisible(false);
				}
			}
		});
		contentPane.add(send, BorderLayout.EAST);
	}

}
