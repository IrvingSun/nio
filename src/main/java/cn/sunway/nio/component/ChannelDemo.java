package cn.sunway.nio.component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * channel本身不存储数据，只是负责数据的传输
 *
 常见的channel：
 FileChannel，读写文件中的数据。
 SocketChannel，通过TCP读写网络中的数据。
 ServerSocketChannel，监听新进来的TCP连接，像Web服务器那样。对每一个新进来的连接都会创建一个SocketChannel。
 DatagramChannel，通过UDP读写网络中的数据。
 *
 * @author sunw
 * @date 2023/1/10
 */
public class ChannelDemo {

    static void fileChannel() throws Exception{
        File file = new File("file1.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel inputStreamChannel = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("file2.txt");
        FileChannel outputStreamChannel = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        inputStreamChannel.read(byteBuffer);

        byteBuffer.flip();
        outputStreamChannel.write(byteBuffer);

        fileOutputStream.close();
        fileInputStream.close();
        outputStreamChannel.close();
        inputStreamChannel.close();
    }

    static void fileChannelTransfer() throws Exception{
        File file = new File("file1.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel inputStreamChannel = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("file2.txt");
        FileChannel outputStreamChannel = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        inputStreamChannel.read(byteBuffer);

        inputStreamChannel.transferTo(0, byteBuffer.position(), outputStreamChannel);
//        byteBuffer.flip();
//        outputStreamChannel.write(byteBuffer);

        fileOutputStream.close();
        fileInputStream.close();
        outputStreamChannel.close();
        inputStreamChannel.close();
    }

    /**
     * 通过transferTo方法将数据从 源channel到 目标channel
     * @throws Exception
     */
    static void socketChannel() throws Exception{
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 6666);

        serverSocketChannel.bind(address);
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        while (true) {
            //获取SocketChannel
            SocketChannel socketChannel = serverSocketChannel.accept();
            while (socketChannel.read(byteBuffer) != -1){
                //打印结果
                System.out.println(new String(byteBuffer.array()));
                //清空缓冲区
                byteBuffer.clear();
            }
        }
    }

    public static void main(String[] args) throws Exception{
//        fileChannel();
//        socketChannel();
        fileChannelTransfer();
    }

}
