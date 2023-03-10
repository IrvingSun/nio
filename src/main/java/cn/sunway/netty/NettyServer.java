package cn.sunway.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.charset.Charset;

/**
 * @author sunw
 * @date 2023/1/11
 */
public class NettyServer {
    public static void main(String[] args) throws Exception{
        //创建两个线程组，bossGroup和workerGroup
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.option(ChannelOption.SO_BACKLOG, 128);
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);

            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new LineBasedFrameDecoder(1024)); //基于换行符处理半包粘包
//                    socketChannel.pipeline().addLast(new StringDecoder(Charset.forName("UTF-8")));//解码UTF-8  处理消息时就可以直接使用字符串了，不需要强转
                    socketChannel.pipeline().addLast(new MyDecoder());//处理粘包半包解码器
                    socketChannel.pipeline().addLast(new StringEncoder(Charset.forName("UTF-8")));//编码UTF-8  发送消息时就可以直接使用字符串了，不需要强转
                    socketChannel.pipeline().addLast(new MyServerHandler());
                }
            });
            System.out.println("服务器端已准备就绪...");
            ChannelFuture channelFuture = bootstrap.bind(6666);
            channelFuture = channelFuture.sync();
            channelFuture.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
