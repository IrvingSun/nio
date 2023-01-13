package cn.sunway.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

/**
 * @author sunw
 * @date 2023/1/11
 */
public class MyClientHandler extends ChannelInboundHandlerAdapter{

    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ChannelGroupHandler.channels.add(ctx.channel());
        String str = "通知服务端链接建立成功" + " " + new Date() + " " + ctx.channel().remoteAddress() + "\r\n";
        ctx.writeAndFlush(str);
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("收到服务端端" + ctx.channel().remoteAddress() + "发送的消息：" + msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //发送消息给客户端
//        ctx.writeAndFlush(Unpooled.copiedBuffer("服务端已收到消息，并给你发送一个问号?\n", CharsetUtil.UTF_8));
//        ctx.writeAndFlush("服务端已收到消息，并给你发送一个问号?（字符串方式发送）\n");
    }

    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ChannelGroupHandler.channels.remove(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //发生异常，关闭通道
        ctx.close();
    }
}
