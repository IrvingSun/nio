package cn.sunway.nio.component;

import java.nio.ByteBuffer;

/**
 * @author sunw
 * @date 2023/1/10
 */
public class BufferDemo {

    /**
     * pos/lim/cap
     *
     * @param args
     */
    public static void main(String[] args) {
        String message = "Hello NIO";
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put(message.getBytes());
        System.out.println(message.getBytes().length);
        System.out.println(byteBuffer);

//        ByteBuffer directByteBuffer = ByteBuffer.allocateDirect(1024);
//        directByteBuffer.put(message.getBytes());
//        System.out.println(directByteBuffer);
//        System.out.println(directByteBuffer.position());
//        System.out.println(directByteBuffer.limit());


        byteBuffer.flip();
        System.out.println(byteBuffer);

        byte[] dummy = new byte[message.getBytes().length];
        int i = 0;
        while(byteBuffer.hasRemaining()){
            byte item = byteBuffer.get();
            dummy[i] = item;
            i++;
        }
        System.out.println(new String(dummy));
        System.out.println(byteBuffer);
        byteBuffer.flip();
        System.out.println(byteBuffer);
    }
}
