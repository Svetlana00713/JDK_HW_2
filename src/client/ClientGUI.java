package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ClientGUI extends JFrame implements ClientView{
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;

    private JTextArea log;
    private JTextField tfLogin;
    private JTextField tfMessage;
    private JPanel headerPanel;

    private ClientController clientController;

    public ClientGUI() {
        setting();
        createPanel();

        setVisible(true);
    }

    @Override
    public void setClientController(ClientController clientController) {
        this.clientController = clientController;
    }

    private void setting() {
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setTitle("Chat client");
        setDefaultCloseOperation(HIDE_ON_CLOSE);
    }

    @Override
    public void showMessage(String msg) {
        log.append(msg);
    }

    public void disconnectFromServer(){
        clientController.disconnectFromServer();
    }

    public void login(){
        if (clientController.connectToServer(tfLogin.getText())){
            headerPanel.setVisible(false);
        }
    }

    private void message(){
        clientController.message(tfMessage.getText());
        tfMessage.setText("");
    }

    private void createPanel() {
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createLog());
        add(createFooter(), BorderLayout.SOUTH);
    }

    private Component createHeaderPanel() {
        headerPanel = new JPanel(new GridLayout(2, 3));
        JTextField tfIPAddress = new JTextField("127.0.0.1");
        JTextField tfPort = new JTextField("8189");
        tfLogin = new JTextField(" \n");
        JPasswordField password = new JPasswordField("12345");
        JButton btnLogin = new JButton("login");
        btnLogin.addActionListener(e -> login());

        headerPanel.add(tfIPAddress);
        headerPanel.add(tfPort);
        headerPanel.add(new JPanel());
        headerPanel.add(tfLogin);
        headerPanel.add(password);
        headerPanel.add(btnLogin);

        return headerPanel;
    }

    private Component createLog() {
        log = new JTextArea();
        log.setEditable(false);
        return new JScrollPane(log);
    }

    private Component createFooter() {
        JPanel panel = new JPanel(new BorderLayout());
        tfMessage = new JTextField();
        tfMessage.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n') {
                    message();
                }
            }
        });
        JButton btnSend = new JButton("send");
        btnSend.addActionListener(e -> message());
        panel.add(tfMessage);
        panel.add(btnSend, BorderLayout.EAST);
        return panel;
    }

    @Override
    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING){
            disconnectFromServer();
        }
    }
}
