//package com.example.demo.NIO;
//
//import io.netty.buffer.ByteBuf;
//import io.netty.buffer.Unpooled;
//
///**
// * Created by zhaohy on 2018/11/14.
// */
//public class ByteBufTest {
//
//	public static void main(String[] args) {
//
//		ByteBuf directBuf = Unpooled.directBuffer(16);
//		if (!directBuf.hasArray()) {
//			int len = directBuf.readableBytes();
//			byte[] arr = new byte[len];
//			directBuf.getBytes(0, arr);
//		}
////	但是上面的操作太过复杂，所以在使用时，建议一般是用heap buffer。
////
////	不过对于一些IO通信线程中读写缓冲时建议使用DirectByteBuffer，因为这涉及到大量的IO数据读写。对于后端的业务消息的编解码模块使用HeapByteBuffer。
//
//
//		//组合缓冲区
//		CompositeByteBuf compBuf = Unpooled.compositeBuffer(); //堆缓冲区
//		 ByteBuf heapBuf = Unpooled.buffer(8);
//		//直接缓冲区
//		ByteBuf directBuf = Unpooled.directBuffer(16); /
//		// /添加ByteBuf到CompositeByteBuf
//		// compBuf.addComponents(heapBuf, directBuf); //删除第一个ByteBuf   compBuf.removeComponent(0); Iterator<ByteBuf> iter = compBuf.iterator(); while(iter.hasNext()){ System.out.println(iter.next().toString()); } //使用数组访问数据      if(!compBuf.hasArray()){ int len = compBuf.readableBytes(); byte[] arr = new byte[len]; compBuf.getBytes(0, arr); }
//
//	}
//}
