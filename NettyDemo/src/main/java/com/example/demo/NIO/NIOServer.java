package com.example.demo.NIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {

	public static int flag = 1;


	
	public static void main(String[] args) throws IOException {

		// 1. 定义ServerSocketChannel 并监听本机指定的端口
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		// 2. 设置非阻塞模式
		serverSocketChannel.configureBlocking(false);

		// 3. 检索与此通道关联的服务器套接字
		ServerSocket serverSocket = serverSocketChannel.socket();
		// 4. 进行服务的绑定
		serverSocket.bind(new InetSocketAddress(8889));

		// 5. 定义Selector
		Selector selector = Selector.open();

		// 6. 注册selector
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

		
		
		// 7.保持Server运行
		while (true) {

			// 7.1 返回读事件已经就绪的那些通道。 并进行判断
			if (selector.select(1000 * 4) == 0) {
				log("服务端在等待....");
				continue;
			}

			// 7.2 一旦调用了select()方法，并且返回值表明有一个或更多个通道就绪了
			// 然后可以通过调用selector的selectedKeys()方法，访问“已选择键集（selected key
			// set）”中的就绪通道。

			// --- 返回此选择器的已选择键集。
			Set<SelectionKey> selectionKeySet = selector.selectedKeys();
			Iterator<SelectionKey> iterator = selectionKeySet.iterator();

			SocketChannel client = null;
			


			// 7.3 遍历获取
			while (iterator.hasNext()) {

				SelectionKey myKey = iterator.next();
//
//				if (myKey.isAcceptable()) {
//					// a connection was accepted by a ServerSocketChannel.
//					System.out.println("aaaaaaaaaaaaaa");
//				} else if (myKey.isConnectable()) {
//					// a connection was established with a remote server.
//					System.out.println("bbbbbbbbbbbbbb");
//				} else if (myKey.isReadable()) {
//					// a channel is ready for reading
//					System.out.println("ccccccccccccc");
//
//				} else if (myKey.isWritable()) {
//					// a channel is ready for writing
//					System.out.println("dddddddddddddd");
//
//				}
				
				// 7.4 此键的通道是否已准备好接受新的套接字连接。
				if (myKey.isAcceptable()) {
					// 7.4.1接受到此通道套接字的连接。
					// 此方法返回的套接字通道（如果有）将处于阻塞模式。
					client = serverSocketChannel.accept();
					// 7.4.2配置为非阻塞
					client.configureBlocking(false);
					Socket socket = client.socket();
					SocketAddress remoteAddr = socket.getRemoteSocketAddress();
					log("Connected to: " + remoteAddr + "  \t Connection Accepted:   \n");

					// 7.4.3注册到selector，等待连接
					client.register(selector, SelectionKey.OP_READ);

				}
				if (myKey.isReadable()) {

					// 7.5.1 返回为之创建此键的通道。
					client = (SocketChannel) myKey.channel();
					ByteBuffer byteBuffer = ByteBuffer.allocate(1024 * 3);
					// 7.5.2 将缓冲区清空以备下次读取
					byteBuffer.clear();

					// 读取服务器发送来的数据到缓冲区中
					int readCount = -1;
					readCount = client.read(byteBuffer);

					// 如果没有数据 则关闭
					if (readCount == -1) {
						Socket socket = client.socket();
						SocketAddress remoteAddr = socket.getRemoteSocketAddress();
						log("Connection closed by client: " + remoteAddr);
					
						return;
					}

					// 有数据 则打印输出
					String receiveText = new String(byteBuffer.array(), 0, readCount);
					log("服务器端接受客户端数据--:" + receiveText + "当前线程"+":"+Thread.currentThread().getName());
					if(receiveText.equals("Lenovo1111")){
						System.out.println("阻塞开始");
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						System.out.println("阻塞完毕");
					}


					ByteBuffer writeBuf=ByteBuffer.allocate(1024);
					writeBuf.put(receiveText.getBytes());
		            //对缓冲区进行复位
					writeBuf.flip();
					client.write(writeBuf);
					 //清空缓冲区数据
		            writeBuf.clear();
//		            
//		        	client.close();
//					myKey.cancel();
				}

				// 移除
				iterator.remove();
			}
		}
	}

	private static void log(String str) {
		System.out.println(str);
	}
}