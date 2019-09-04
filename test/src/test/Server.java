package test;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 	通过socket和线程在控制台上实现简单通信
 * @author Administrator
 *	服务端
 */
public class Server implements Runnable{

	ServerSocket server; 
	Socket client; 
	Thread send; 
	BufferedReader br; 
	BufferedOutputStream bos;
	
	public static void main(String args[]) { 
		new Server().ServerStart(); 
	}
	
	/**
	 * 	开启服务，等待连接
	 */
	public void ServerStart() { 
		try { 
			server=new ServerSocket(5678); 
			System.out.println ("本机端口号:"+server.getLocalPort());
			server.setSoTimeout(10000*6); //6秒超时
			client=server.accept(); 
			System.out.println("已建立连接!"); 
			br=new BufferedReader(new InputStreamReader(client.getInputStream()));
			bos=new BufferedOutputStream(client.getOutputStream());
			send=new Thread(this);
			send.start();
			//以下为接收 
			String str_in;
			while((str_in=br.readLine())!=null) { 
				System.out.println("客户端说：" + str_in); 
				if(str_in.equals("bye")) break;
			} 
			send.interrupt(); //中止发送线程 
			br.close();
			server.close(); 
			bos.close();
			client.close(); 
			System.exit(0);
		}catch(Exception e) { 
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
		} catch(Exception e) { 
			System.out.println(e);
		}
	}

}
