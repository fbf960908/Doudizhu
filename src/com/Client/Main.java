package com.Client;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Main {
	
	public static void main(String[] args) throws UnknownHostException {
		Box baseBox, box1 = null, box2 = null;

		JFrame LoginUI = new JFrame();

		LoginUI.setLayout(new FlowLayout());
		LoginUI.setBounds(0, 0, 300, 110);
		LoginUI.setLocationRelativeTo(LoginUI.getOwner());
		LoginUI.setTitle("秒天秒地斗地主-登录");
		LoginUI.setResizable(false);

		box1 = box1.createVerticalBox();
		box1.add(new JLabel("服务器地址"));
		box1.add(box1.createVerticalStrut(10));
		box1.add(new JLabel("姓名"));
		box1.add(box1.createVerticalStrut(10));
		box2 = box2.createVerticalBox();

		final JTextField textField1 = new JTextField(10);

		box2.add(textField1);
		box2.add(box1.createVerticalStrut(10));

		final JTextField textField2 = new JTextField(10);

		box2.add(textField2);
		box2.add(box1.createVerticalStrut(10));
		baseBox = Box.createHorizontalBox();
		baseBox.add(box1);
		baseBox.add(box1.createHorizontalStrut(10));
		baseBox.add(box2);
		LoginUI.add(baseBox);
		JButton button = new JButton("登陆");
		button.setLocation(50, 50);
		LoginUI.add(button);
		LoginUI.setLocationRelativeTo(LoginUI.getOwner());
		LoginUI.setVisible(true);
		LoginUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//获取本地IP
		InetAddress ip = InetAddress.getLocalHost();
		String IPs = ip.toString();
		String []IP = IPs.split("\\/");
		
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (isIP(IP[1].trim())) {
					try {
						UI ui = new UI(textField1.getText(), textField2.getText());
						new Thread(ui).start();
						LoginUI.setVisible(false);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} else {
					Error.show("IP格式不正确"); 
				}
			}
		});
	}
	public static boolean isIP(String str) {
		String[] ss = str.split("\\.");
		for (String string : ss) {
			try {
				int a = Integer.parseInt(string);
				if (a <= 0 || a > 255)
					return false;
			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}
}

class Error {
	public static void show(String text) {
		JDialog dialog = new JDialog();
		JLabel label = new JLabel(text);
		dialog.add(label);
		dialog.setSize(100, 100);
		dialog.setLocationRelativeTo(null);
		dialog.setModal(true);
		dialog.setVisible(true);
		dialog.setResizable(true);
	}
}
