package MultiThreadingChat;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SimpleChatClientA {

    private  JTextArea incoming;
    private JTextField outgoing;
    private PrintWriter writer;

    private BufferedReader reader;

    public void go()
    {
        setUpNetworking();

        JScrollPane scroller = createScrollableTextArea();

        outgoing = new JTextField(20);

        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(e -> sendMessage());

        JPanel mainPanel = new JPanel();

        mainPanel.add(scroller);
        mainPanel.add(outgoing);
        mainPanel.add(sendButton);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new IncomingReader());

        JFrame frame = new JFrame("Ludicrously Simple Chat Client A");
        frame.getContentPane().add(BorderLayout.CENTER,mainPanel);
        frame.setSize(400,100);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

    private JScrollPane createScrollableTextArea()
    {
        incoming = new JTextArea(15,30);
        incoming.setLineWrap(true);
        incoming.setWrapStyleWord(true);
        incoming.setEditable(false);

        JScrollPane scroller = new JScrollPane(incoming);
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        return  scroller;
    }

    private void sendMessage() {

        writer.println(outgoing.getText());
        writer.flush();
        outgoing.setText("");
        outgoing.requestFocus();
    }

    private void setUpNetworking() {
        try{
            InetSocketAddress serverAddress = new InetSocketAddress("127.0.0.1",5000);
            SocketChannel socketChannel = SocketChannel.open(serverAddress);
            reader = new BufferedReader(Channels.newReader(socketChannel,StandardCharsets.UTF_8));
            writer = new PrintWriter(Channels.newWriter(socketChannel, StandardCharsets.UTF_8));
            System.out.println("Networking Established");

        }catch (IOException ioException)
        {
            ioException.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("Hello Client A!!!");
        new SimpleChatClientA().go();
    }

    public class IncomingReader implements Runnable
    {
        @Override
        public void run() {
            String message;
            try{
                while((message = reader.readLine()) != null)
                {
                    System.out.println("read by A " + message);
                    incoming.append(message + "\n");
                }
            } catch (IOException e) {
               e.printStackTrace();
            }
        }
    }
}
