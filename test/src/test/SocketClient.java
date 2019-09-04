package test;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * 	socket通讯，通过线程交互在swing窗体上简单通讯
 * @author Administrator
 *	客户端
 */
public class SocketClient implements Runnable {

	Socket client;
	String serverAddress;  //服务器地址
	Scanner in;            //文本扫描输入
	PrintWriter out;       //打印字符输出流
	JFrame frame;          //客户端图形界面
	JTextField textField;  //信息输入框
	JTextArea messageArea; //信息接收区域
	
	public static void main(String[] args) throws UnknownHostException {
		//服务器地址（这里服务器和客户端在同一电脑上）
		String serverAddress = InetAddress.getLocalHost().getHostAddress();
        new Thread(new SocketClient(serverAddress)).start();
		
	}
	
	/**
	 * 	初始化
	 * @param serverAddress 连接地址
	 */
	public SocketClient(String serverAddress) {
		init(serverAddress);
	}

	/**
	 * 	窗体内容
	 * @param serverAddress 连接地址
	 */
	public void init(String serverAddress) {
		//初始化服务器地址
		this.serverAddress = serverAddress;
		//初始化窗体和窗体内组件
		textField = new JTextField(50);
		messageArea = new JTextArea(16,50);
		messageArea.setText("请等待客户端10秒连接！");
		messageArea.setEditable(false);
		frame = new JFrame("出走半生的少年");
		frame.getContentPane().add(textField,BorderLayout.SOUTH);
		frame.getContentPane().add(new JScrollPane(messageArea),BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
		
		//添加监听事件
		myEvent();
	}
	
	/**
	 * 	事件监听
	 */
	public void myEvent() {
		//发送信息事件
		textField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					messageArea.append("我：" + textField.getText() + "\n");
					out.println("佛系少年：" +  textField.getText());
					textField.setText("");
				}
			}
		});
		//关闭窗体时间默认退出程序
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if(null != out) {
					out.print("客户端断开连接！");
				}
				System.exit(0);
			}
		});
	}

	/**
	 * 	重写run方法
	 */
	public void run() {
		//接收线程体
		try {
			client = new Socket(serverAddress,6669);
			messageArea.setText("连接服务器成功！\n");
			in = new Scanner(client.getInputStream()/*,"utf-8"*/);//默认系统字符集utf-8
			out = new PrintWriter(client.getOutputStream(), true);
			//out.println("连接服务器成功！");
			while (in.hasNextLine()) {
				String line = in.nextLine();
				if(line.startsWith("服务器关闭！")) {
					textField.setEditable(false);
					messageArea.append(line.substring(1) + "\n");
				}else {
					messageArea.append(line + "\n");
				}
			}	
		}catch (UnknownHostException e) {
			e.printStackTrace();
		}catch (IOException e) {
			messageArea.append("服务器连接失败！\n");
			textField.setEditable(false);
		}
	}
}
