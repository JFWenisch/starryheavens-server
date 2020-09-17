package system;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.PrintStream;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class SHDLFrame extends JFrame
{
	public static JLabel oUserCount = new JLabel("User:");
	public static JLabel oPuzzleCount = new JLabel("Puzzle:");
	public static JLabel oUpdated = new JLabel("Last update:");
	public SHDLFrame()
	{
		super("SHDL-Server");
		setSize(500,500);
		setResizable(false);
		//

		//TEXT AREA
		JTextArea textArea = new JTextArea("Server gestartet");
		textArea.setSize(400,400);    

		textArea.setLineWrap(true);
		textArea.setEditable(false);
		textArea.setVisible(true);

		JScrollPane scroll = new JScrollPane (textArea);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		PrintStream printStream = new PrintStream(new CustomOutputStream(textArea));
		System.setOut(printStream);
		System.setErr(printStream);
		add(scroll,BorderLayout.CENTER);
		JPanel oBottomPanel = new JPanel();
		add(oBottomPanel,BorderLayout.SOUTH);
		oBottomPanel.setLayout(new FlowLayout());
		oBottomPanel.add(oUserCount);
		oBottomPanel.add(oPuzzleCount);
		oBottomPanel.add(oUpdated);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
}
