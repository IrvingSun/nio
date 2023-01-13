package cn.sunway.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.List;

/**
 * 按字符处理接收到的报文
 * 1开头(49)  9结尾(57)
 * 基础包长度  4
 * @author sunw
 * @date 2023/1/13
 */
public class MyDecoder extends ByteToMessageDecoder{
    final int LENGTH = 11;
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if(byteBuf.readableBytes() < LENGTH ){
            return;
        }
        int beginIndex;

        while(true){
            beginIndex = byteBuf.readerIndex();
            byteBuf.markReaderIndex();
            byte first = byteBuf.readByte();
            if(first == 49){//字符转数字
                break;
            }
            byteBuf.resetReaderIndex();
            byteBuf.readByte();
            if(byteBuf.readableBytes() < LENGTH){
                return;
            }
        }
        //如果已经没有可读
        int readableCount = byteBuf.readableBytes();
        if(readableCount <= 1){
            byteBuf.readerIndex(beginIndex);
            return;
        }
        //如果长度不够(包含尾部)
        int msgLength = LENGTH - 2;//除去头部已读
        if(readableCount < msgLength){
            byteBuf.readerIndex(beginIndex);
            return;
        }

        ByteBuf msgContent = byteBuf.readBytes(LENGTH - 4);//不读尾部

        byte end = byteBuf.readByte();//判断尾部是否正常结束
        if(end != 57){
            byteBuf.readerIndex(beginIndex);
        }
        list.add(msgContent.toString(Charset.forName("UTF-8")));
    }
}
