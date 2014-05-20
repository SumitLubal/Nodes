package FileShare;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;


public class CopyDialog extends JFrame{
	JProgressBar status;
	JLabel info;
	public CopyDialog() {
		status = new JProgressBar();
		info = new JLabel();
		setResizable(false);
		setSize(200, 100);
		info.setBounds(WindowProperty.getWidth()-200,WindowProperty.getHeight()-100,200,100);
		add(info);
		add(status);
		setLayout(new FlowLayout());;
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
}
