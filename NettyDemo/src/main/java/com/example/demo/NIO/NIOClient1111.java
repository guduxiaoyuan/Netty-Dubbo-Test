package com.example.demo.NIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
 
 
 
public class NIOClient1111 {
 
	public static void main(String[] args) throws IOException, InterruptedException {
 
		// 1.在客户端定义ScoketChannel 连接指定的服务端
		InetSocketAddress crunchifyAddr = new InetSocketAddress("localhost", 8889);
		SocketChannel sc = SocketChannel.open(crunchifyAddr);
	
		log("Connecting to Server on port 8889...");
		//客户端信道选择器,轮询读取服务端返回数据
	    Selector selector;
		// 2.定义发送的数据
		ArrayList<String> companyDetails = new ArrayList<String>();
		companyDetails.add("Lenovo1111");
//		companyDetails.add("Samsung1111");
//		companyDetails.add("Huawei1111");


		sc.configureBlocking(false);//设置非阻塞
		
		
		for (String companyName : companyDetails) {
			// 3.1 将字符转转换为字节
			byte[] message = new String(companyName).getBytes("UTF-8");
			
			// 3.2 定义Buffer 并将字节数组数据封装在Buffer中
			ByteBuffer buffer = ByteBuffer.wrap(message);
			
			// 3.3 Channel读取Buffer中的数据
			sc.write(buffer);

			log("sending: " + companyName);
			// 清空Buffer
			buffer.clear();
		}
		
		
		 
		 
		 
         selector = Selector.open();//必须打开
         //将当前客户端注册到多路复用器上,并设置为可读状态
         sc.register(selector, SelectionKey.OP_READ);
         //开启线程,一直轮询
         new Thread(()->{
             while(true){//一直循环
                 try {
                	 selector.select();//多路复用器开始监听
                     //获取已经注册在多了复用器上的key通道集
                     Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                     //遍历
                     while (keys.hasNext()) {
                         SelectionKey key = keys.next();//获取key
                         //如果是有效的
                         if(key.isValid()){
                             // 如果为可读状态,读取服务端返回的数据
                             if(key.isReadable()){
                                 read(key);
                             }
                         }
                         //从容器中移除处理过的key
                         keys.remove();
                     }
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             }
         }).start();
     
		
		
 
//		// 3.循环发送
//		for (String companyName : companyDetails) {
// 
//			// 3.1 将字符转转换为字节
//			byte[] message = new String(companyName).getBytes("UTF-8");
//			
//			// 3.2 定义Buffer 并将字节数组数据封装在Buffer中
//			ByteBuffer buffer = ByteBuffer.wrap(message);
//			
//			// 3.3 Channel读取Buffer中的数据
//			crunchifyClient.write(buffer);
// 
//			log("sending: " + companyName);
//			// 清空Buffer
//			buffer.clear();
// 
//			// 等待2000毫秒
////			Thread.sleep(2000);
//		}
//		// 关闭Channel
//		crunchifyClient.close();
	}
	
	
	 //客户端获取服务端返回的数据
    private static  void read(SelectionKey key) {
        try {
            //建立写缓冲区
            ByteBuffer readBuf = ByteBuffer.allocate(1024);
            //2 获取之前注册的socket通道对象
            SocketChannel sc = (SocketChannel) key.channel();
            //3 读取数据
            int count = sc.read(readBuf);
            //4 如果没有数据
            if(count == -1){
                key.channel().close();
                key.cancel();
                return;
            }
            //5 有数据则进行读取 读取之前需要进行复位方法(把position 和limit进行复位)
            readBuf.flip();
            //6 根据缓冲区的数据长度创建相应大小的byte数组，接收缓冲区的数据
            byte[] bytes = new byte[readBuf.remaining()];
            //7 接收缓冲区数据
            readBuf.get(bytes);
            //8 打印结果
            String body = new String(bytes).trim();
            System.out.println("客户端已接受到服务端返回的数据: " + body);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	
 
	private static void log(String str) {
		System.out.println(str);
	}
}