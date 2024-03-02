package org.example;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Random;

public class DailyAdviserServerCode {

    final private String[] adviceList = {
            "Take smaller bites",
            "Medidate daily",
            "Exercise Daily",
            "Code daily",
            "Sleep well"
    };

    private final Random random = new Random();

    public void go()
    {
        try(ServerSocketChannel serverSocketChannel = ServerSocketChannel.open())
        {
            serverSocketChannel.bind(new InetSocketAddress(5000));

            while (serverSocketChannel.isOpen())
            {
                SocketChannel clientChannel = serverSocketChannel.accept();
                PrintWriter writer = new PrintWriter(Channels.newOutputStream(clientChannel));

                String advice = getAdvice();
                writer.println(advice);
                writer.close();
                System.out.println(advice);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getAdvice() {

        int nextAdvice = random.nextInt(adviceList.length);
        return adviceList[nextAdvice];
    }

    public static void main(String[] args) {
        System.out.println("Hello world!");
        new DailyAdviserServerCode().go();
    }



}
