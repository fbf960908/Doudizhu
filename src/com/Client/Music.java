package com.Client;

import java.applet.Applet;
import java.io.FileInputStream;
import java.io.IOException;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class Music extends Applet implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	AudioStream as;

	public Music() {
		System.out.println("音频初始化中……");
		as = null;
	}

	public void run() {
		while (true) {
			try {
				FileInputStream fileau = new FileInputStream("music/背景.wav");
				as = new AudioStream(fileau);
			} catch (IOException e) {
				e.printStackTrace();
			}
			AudioPlayer.player.start(as);
			try {
				Thread.sleep(94000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void shengli() {
		System.out.println("音频初始化中……");
		try {
			FileInputStream fileau = new FileInputStream("music/胜利.wav");
			AudioStream c = new AudioStream(fileau);
			AudioPlayer.player.start(c);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void jiaodizhu() {
		System.out.println("音频初始化中……");
		try {
			FileInputStream fileau = new FileInputStream("music/叫地主.wav");
			AudioStream b = new AudioStream(fileau);
			AudioPlayer.player.start(b);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void yaobuqi() {
		System.out.println("音频初始化中……");
		try {
			FileInputStream fileau = new FileInputStream("music/要不起.wav");
			AudioStream a = new AudioStream(fileau);
			AudioPlayer.player.start(a);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void feiji() {
		System.out.println("音频初始化中……");
		try {
			FileInputStream fileau = new FileInputStream("music/飞机.wav");
			AudioStream b = new AudioStream(fileau);
			AudioPlayer.player.start(b);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void wangzha() {
		System.out.println("音频初始化中……");
		try {
			FileInputStream fileau = new FileInputStream("music/王炸.wav");
			AudioStream b = new AudioStream(fileau);
			AudioPlayer.player.start(b);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
