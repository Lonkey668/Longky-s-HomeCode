package test;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 *	 通过socket和线程在控制台上实现简单通信
 * @author Administrator
 *	客户端
 */
public class Client implements Runnable{

	Socket client;         
	Thread send; 
	BufferedReader br; 
	BufferedOutputStream bos; 
	
	public static void main(String args[]) {
		Client c=new Client(); 
		c.ClientStart();
	} 
	
	/**
	 *	客户端开启，连接服务端地址
	 */
	public void ClientStart() { 
		try { 
			client=new Socket("127.0.0.1",5678);
			System.out.println("已建立连接!"); 
			br=new BufferedReader(new InputStreamReader(client.getInputStream())); 
			bos=new BufferedOutputStream(client.getOutputStream()); 
			send=new Thread(this);
			send.start(); 
			//以下为接收 
			String str_in;
			while((str_in=br.readLine())!=null) { 
				System.out.println("服务端说：" + str_in);
				if(str_in.equals("bye")) 
					break; 
			}
			send.interrupt();//中止发送线程 
			br.close(); bos.close();
			client.close(); 
			System.exit(0); 
		} catch(Exception e)  {
			System.out.println(e);
		} 
	} 
	
	/**
	 * 	重写run方法，发送线程体
	 */
	public void run() {
		try { 
			int ch;
			while((ch=System.in.read())!=-1) { 
				bos.write(ch);
				if(ch=='\n') 
					bos.flush();
			} 
		}catch(Exception e) { 
			System.out.println(e);
		} 
	}
}
