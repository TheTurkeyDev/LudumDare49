package dev.theturkey.ld49.mainui;

import javax.swing.*;
import java.awt.*;

public class ErrorPopUp extends JFrame
{
	private static ErrorPopUp popUp;
	private static JTextArea textArea;

	public ErrorPopUp()
	{
		popUp = this;

		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setSize(600, 400);

		this.getContentPane().setBackground(MainUI.BG_COLOR);
		this.setLayout(null);

		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setVisible(true);
		textArea.setBackground(MainUI.BG_COLOR);
		textArea.setForeground(MainUI.FG_COLOR);
		textArea.setBounds(0, 0, 600, 400);
		Font f = new Font(textArea.getFont().getName(), Font.PLAIN, 14);
		textArea.setFont(f);

		JScrollPane scroll = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll.setBounds(0, 0, 580, 350);
		this.add(scroll);

		this.setVisible(true);
	}

	public static void addError(String error)
	{
		if(popUp == null || !popUp.isVisible())
			new ErrorPopUp();

		textArea.setText(textArea.getText() + "\n\n" + error);
	}
}
