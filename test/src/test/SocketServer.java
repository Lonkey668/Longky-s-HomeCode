package test;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * 	socket通讯，通过线程交互在swing窗体上简单通讯
 * @author Administrator
 *	服务端
 */
public class SocketServer implements Runnable {

	ServerSocket server; 
	String serverAddress;//服务器地址
	Socket client;
	Scanner in;       //文本扫描器输入
	PrintWriter out;  //打印字符输出流
	JFrame frame;      //服务器图形界面
	JTextField textField; //信息输入框
	JTextArea messageArea;//信息接收区域
	
	/**
	 * 	主函数，程序入口
	 * @param args
	 */
	public static void main(String[] args) {
		new Thread(new SocketServer(6669)).start();
	}

	/**
	 *	初始化服务器
	 * @param port 连接端口
	 */
	public SocketServer(int port) {
		init(port);
	}
	
	/**
	 * 	服务器初始化
	 * @param port 连接端口
	 */
	public void init(int port) {
		//初始化服务器地址
		try {
			server = new ServerSocket(port);
			server.setSoTimeout(10000);  //十秒超时
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//初始化窗体和窗体内组件
		textField = new JTextField(50);
		messageArea = new JTextArea(16,50);
		messageArea.setText("请等待客户端10秒连接！\n");
		messageArea.setEditable(false);
		frame = new JFrame("佛系少年");
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
					out.println("出走半生的少年：" +  textField.getText());
					textField.setText("");
				}
			}
		});
		//关闭窗体时间默认退出程序
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if(null != out) {
					out.print("服务器关闭！");
				}
				System.exit(0);
			}
		});
	}
	
	/**
	 * 	重写run方法
	 */
	public void run () {
		try {
			client = server.accept();
			in = new Scanner(client.getInputStream(),"UTF-8");
			out = new PrintWriter(client.getOutputStream(),true);
			while (in.hasNextLine()) {
				String line = in.nextLine();
				messageArea.append(line +"\n");
			}
			if(null != in) {
				in.close();
			}
			if(null != out) {
				out.close();
			}
		} catch(UnknownHostException e) {
			e.printStackTrace();
		}catch(IOException e) {
			JOptionPane.showMessageDialog(frame,"客户端连接超时！");
			textField.setEditable(true);
		}
	}
}
