import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


import org.example.BatchVtt2Lrc;

public class BatchVtt2LrcGui extends JFrame{

    public static void main(String[] args) {
        new BatchVtt2LrcGui("测试");
    }
    public BatchVtt2LrcGui(String name){
        super(name);
        JPanel jp1 = new JPanel();
        JLabel jl1 = new JLabel("输入文件夹");
        JTextField vtt_input_path = new JTextField(30);

        jp1.setLayout(new FlowLayout());
        jp1.add(jl1);
        jp1.add(vtt_input_path);

        JPanel jp2 = new JPanel();
        JLabel jl2 = new JLabel("输出文件夹");
        JTextField lrc_outpath = new JTextField(30);
        jp2.setLayout(new FlowLayout());
        jp2.add(jl2);
        jp2.add(lrc_outpath);

        JButton converter_button = new JButton("确定");
        JButton quit_button = new JButton("取消");

        //给转换按钮添加转换时间，输入为 输入文件夹label与输出文件夹的label

        converter_button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                //输出转换
                BatchVtt2Lrc event_BatchVtt2Lrc = new BatchVtt2Lrc();

                //设置输入路径
                String user_input_inputpath = vtt_input_path.getText();
                event_BatchVtt2Lrc.setInputFolder(user_input_inputpath);

                //设置输出路径
                String user_input_outputpath = lrc_outpath.getText();
                event_BatchVtt2Lrc.setOutputFolder(user_input_outputpath);

                //开始就行转换

                event_BatchVtt2Lrc.startconvert();

                JOptionPane.showMessageDialog(null, "转换完成");

            }
        });






        JPanel jp3 = new JPanel();
        jp3.setLayout(new FlowLayout());
        jp3.add(converter_button);
        jp3.add(quit_button);

        this.setLayout(new GridLayout(3,1));
        this.add(jp1);
        this.add(jp2);
        this.add(jp3);
        this.setAlwaysOnTop(false);
        this.setSize(1000, 1000);
        this.setVisible(true);
        this.setResizable(false);
        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}