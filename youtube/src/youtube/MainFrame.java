package youtube;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.TextArea;
import java.net.URLDecoder;

import javax.swing.JPanel;

import org.omg.CORBA.NVList;

import java.awt.Color;

public class MainFrame {

	private JFrame frame;
	private JButton btnNewButton;
	private JTextField txtHttpwwwtabnakir;
	private JLabel lblYoutubeVideoLink;
	private TextArea textArea;
	private JLabel lblResult;
	private JPanel panel;
	private JLabel label;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame window = new MainFrame();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 949, 616);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		btnNewButton = new JButton("Get Link");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				
				//ساخته شده youtube_search در اینجا یک متغیر از نوع کلاس   
				youtube_source youtube_source;
				
				try {

					//در اینجا آبجکت بالایی نمونه سازی شده و تکست داخل تکست باکس به عنوان ارگومان به کانستراکتور کلاس فرستاده شده 
				    youtube_source = new youtube_source((txtHttpwwwtabnakir.getText()));
					
				    // در اینجا تابع دیتکیت سیستم پروکسیز فراخانی شده
				    youtube_source.detect_system_proxies();
				    
				    
				    // در اینجا تابع اکسترکت یو آر الز فراخانی شده و یو ار ال های استخراج شده در یک ارایه از نوع رشته قرار میکیرن
				    // دقت کنید که یو ار ال های استخراج شده باید دیکد بشن
					String[] urls = youtube_source.extract_urls().split(",");
					
					for (String string : urls) {

						string = URLDecoder.decode(string, "UTF-8");
						textArea.append(string + "\r\n");
					}
					
					/*
					در بخش زیر هم ادرس عکس فیلمی که میخاد
					دانلود بشه توسط تابع گت پیک پیدا میشه 
					و روی پنل نمایش داده میشه
					 */
					Image img = youtube_source.get_pic();
					ImageIcon icon = new ImageIcon(img);
				    img = icon.getImage();
					Image newimg = img.getScaledInstance(panel.getWidth(), panel.getHeight(),  java.awt.Image.SCALE_SMOOTH);
					ImageIcon newicon = new ImageIcon(newimg);
					if (label == null) {
						
						    label = new JLabel("", newicon, JLabel.CENTER);
							label.setSize(panel.getWidth(), panel.getHeight());
							panel.add( label, BorderLayout.CENTER );
							panel.revalidate();
							panel.repaint();
						
					} else {
						label.setIcon(newicon);
						panel.revalidate();
						panel.repaint();
					}
					
				} catch (Exception e2) {

					System.out.println(e2.getMessage());
				}
			}
		});
		btnNewButton.setBounds(10, 41, 113, 23);
		frame.getContentPane().add(btnNewButton);

		txtHttpwwwtabnakir = new JTextField();
		txtHttpwwwtabnakir.setText("https://www.youtube.com/watch?v=wAmq8eIkdI8&amp");
		txtHttpwwwtabnakir.setBounds(181, 13, 716, 20);
		frame.getContentPane().add(txtHttpwwwtabnakir);
		txtHttpwwwtabnakir.setColumns(10);

		lblYoutubeVideoLink = new JLabel("Youtube Video Link");
		lblYoutubeVideoLink.setBounds(26, 16, 113, 14);
		frame.getContentPane().add(lblYoutubeVideoLink);

		textArea = new TextArea();
		textArea.setBounds(179, 401, 718, 132);
		frame.getContentPane().add(textArea);

		lblResult = new JLabel("Result");
		lblResult.setBounds(181, 381, 73, 14);
		frame.getContentPane().add(lblResult);
		
		panel = new JPanel();
		panel.setBackground(Color.PINK);
		panel.setBounds(181, 56, 443, 303);
		frame.getContentPane().add(panel);
		
	}
}
