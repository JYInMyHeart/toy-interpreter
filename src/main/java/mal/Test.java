package mal;


import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test extends JFrame {

    private JLabel label = new JLabel("Status");
    private JTextField textField;
    private JPasswordField pwdField;
    private JTextArea textArea;
    public Test() throws HeadlessException {
        this.setSize(500, 100);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("my jframe");
        this.setVisible(true);
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println(e.getKeyChar());
            }
        });
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setLayout(new java.awt.FlowLayout());

        textField = new JTextField(15);
        /* 监听文本光标移动事件 */
        textField.addCaretListener(e -> {
            // 如果改变了内容，就可以即时更新 label 显示的内容
            label.setText(textField.getText());
        });

        pwdField = new JPasswordField(15);
        pwdField.setEchoChar('#');

        textArea = new JTextArea(5, 15);
        textArea.setLineWrap(true);

        getContentPane().add(textField);
        getContentPane().add(pwdField);
        getContentPane().add(textArea);
        getContentPane().add(label);

        setSize(200, 200);
    }



    public static void main(String[] args) {
        Test tt = new Test();
        tt.show();
    }


}

