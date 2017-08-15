package com.Client;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Card extends JLabel implements MouseListener {

	public String name;// 图片url名字
	public int color;//花色
	public int points;//点数
	public boolean up;// 是否正反面
	public boolean canClick = false;// 是否可被点击
	public boolean clicked = false;// 是否点击过

	public Card(String name, boolean up) {
		this.name = name;
		String [] s = name.split("\\-");
		this.color = Integer.parseInt(s[0]);
		this.points = Integer.parseInt(s[1]);
		this.up = up;
		if (this.up)
			this.turnFront();
		else {
			this.turnRear();
		}
		this.setSize(71, 96);
		this.setVisible(true);
		this.addMouseListener(this);
	}

	public String getName() {
		return name;
	}

	// 正面
	public void turnFront() {
		this.setIcon(new ImageIcon("images/" + name + ".gif"));
		this.up = true;
	}

	// 反面
	public void turnRear() {
		this.setIcon(new ImageIcon("images/rear.gif"));
		this.up = false;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (canClick) {
			Point from = this.getLocation();
			int step; // 移动的距离
			if (clicked)
				step = -20;
			else {
				step = 20;
			}
			clicked = !clicked; // 反向
			this.setLocation(new Point(from.x,from.y-step));
		}
	}

	public int getColor() {
		return color;
	}

	public int getPoints() {
		return points;
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}
