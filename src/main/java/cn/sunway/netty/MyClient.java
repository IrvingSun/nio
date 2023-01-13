package cn.sunway.netty;

import com.sun.corba.se.internal.CosNaming.BootstrapServer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.charset.Charset;

/**
 * 客户端实现
 * @author sunw
 * @date 2023/1/12
 */
public class MyClient {

    public static void main(String[] args) throws InterruptedException {
        new MyClient().connect("localhost",6666);
    }

    private void connect(String host, int port) throws InterruptedException {
        EventLoopGroup worker = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(worker);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.AUTO_READ, true);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new LineBasedFrameDecoder(1024)); //基于换行符处理半包粘包
                    socketChannel.pipeline().addLast(new StringDecoder(Charset.forName("UTF-8")));//解码UTF-8  处理消息时就可以直接使用字符串了，不需要强转
                    socketChannel.pipeline().addLast(new StringEncoder(Charset.forName("UTF-8")));//编码UTF-8  发送消息时就可以直接使用字符串了，不需要强转
                    socketChannel.pipeline().addLast(new MyServerHandler());
                }
            });
            ChannelFuture f = bootstrap.connect(host, port).sync();
            f.channel().closeFuture().sync();
        }finally {
            worker.shutdownGracefully();
        }
    }
}
