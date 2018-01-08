package frame;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import Server.Receiver;
import utils.IsNum;
import utils.TransToExcel;

public class frame extends JFrame {

	private ServerSocket serverSocket;
	private Socket socket;
	private Boolean lock = true;
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JTable table;
	private JTable table_1;
	private TimeSeriesCollection dataset = new TimeSeriesCollection();// 数据集
	private final TimeSeries timeSeries = new TimeSeries("", Millisecond.class);// 创建时间序
	private double toplimit;
	private double lowerlimit;
	private String testParam;
	private String testAddress;
	private int prot;
	private List<Map<String, String>> datas = new ArrayList<>();
	private Date date = new Date(System.currentTimeMillis());
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	private String time = sdf.format(date);
	private JFreeChart result;
	private String writePath = "datas.txt";

	/**
	 * Launch the application.
	 */
	public static String[] convertStrToArray(String str) {
		String[] strArray = null;
		strArray = str.split("/"); // 拆分字符为"," ,然后把结果交给数组strArray
		return strArray;
	}

	/**
	 * 
	 * @param calculator
	 * @param widthRate
	 *            宽度比例
	 * @param heightRate
	 *            高度比例
	 */

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame frame = new frame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void setXYPolt(XYPlot plot) {
		plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
		plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
		XYItemRenderer r = plot.getRenderer();
		if (r instanceof XYLineAndShapeRenderer) {
			XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
			renderer.setBaseShapesVisible(true);
			renderer.setBaseShapesFilled(false);
		}
	}

	private JFreeChart createChart(XYDataset dataset) {

		// 增加汉字支持

		/**
		 * 第一个参数是设置折线图的主题 第二个参数是设置折线图的x 第三个参数是设置折线图的y 是否显示图表中每条数据序列的说明 是否显示工具提示
		 * 是否显示图表中设置的url网络连接
		 */
		result = ChartFactory.createTimeSeriesChart("", "", "", dataset, false, true, false);
		result.setBackgroundPaint(Color.white);
		XYPlot plot = result.getXYPlot();
		setXYPolt(plot);
		return result;
	}

	/**
	 * Create the frame.
	 */
	public frame() {
		// 用来设置窗口随屏幕大小改变
		// sizeWindowOnScreen(this, 0.6, 0.6);
		// this.setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1663, 1072);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("     端口号");
		lblNewLabel.setFont(new Font("宋体", Font.PLAIN, 22));
		lblNewLabel.setBounds(429, 80, 187, 61);
		lblNewLabel.setBorder(BorderFactory.createLineBorder(Color.gray));
		contentPane.add(lblNewLabel);

		textField = new JTextField();
		textField.setHorizontalAlignment(SwingConstants.CENTER);
		textField.setFont(new Font("宋体", Font.PLAIN, 22));
		textField.setBounds(433, 142, 184, 62);
		contentPane.add(textField);
		textField.setColumns(10);
		ImageIcon icon = new ImageIcon("src/image/timg.jpg");
		this.setIconImage(icon.getImage());

		final JButton btnNewButton_4 = new JButton("运行");
		btnNewButton_4.setFont(new Font("宋体", Font.PLAIN, 22));
		btnNewButton_4.setBounds(248, 142, 185, 61);
		contentPane.add(btnNewButton_4);

		final long threadid;
		// final Thread current;
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						if (textField.getText().trim().equals("") || textField_1.getText().trim().equals("")
								|| textField_2.getText().trim().equals("")) {
							if (textField.getText().trim().equals("")) {
								textField.setText("请输入端口号");

							}
							if (textField_1.getText().trim().equals("")) {
								textField_1.setText("请输入上限");

							}

							if (textField_2.getText().trim().equals("")) {
								textField_2.setText("请输入下限");

							}
							btnNewButton_4.setEnabled(true);

						} else if (!IsNum.isNumeric(textField.getText().trim())
								|| !IsNum.isNumeric(textField_1.getText().trim())
								|| !IsNum.isNumeric(textField_2.getText().trim())) {
							if (!IsNum.isNumeric(textField.getText().trim())) {
								textField.setText("输入数字");

							}
							if (!IsNum.isNumeric(textField_1.getText().trim())) {
								textField_1.setText("输入数字");

							}

							if (!IsNum.isNumeric(textField_2.getText().trim())) {
								textField_2.setText("输入数字");

							}
							btnNewButton_4.setEnabled(true);
						} else if (Double.parseDouble(textField_1.getText()) < Double
								.parseDouble(textField_2.getText())) {
							textField_1.setText("上限不能小于下限");
							textField_2.setText("上限不能小于下限");
							btnNewButton_4.setEnabled(true);

						} else if (textField_3.getText().trim().equals("") || textField_4.getText().trim().equals("")) {
							textField_3.setText("请输入参数");
							textField_4.setText("请输入地址");
							btnNewButton_4.setEnabled(true);
						} else {
							String s = textField.getText() + "/" + textField_1.getText() + "/" + textField_2.getText()
									+ "/" + textField_3.getText() + "/" + textField_4.getText();

							Writeinfo(s);

							btnNewButton_4.setEnabled(false);

							prot = Integer.parseInt(textField.getText());
							toplimit = Double.parseDouble(textField_1.getText());
							lowerlimit = Double.parseDouble(textField_2.getText());
							testParam = textField_3.getText();
							testAddress = textField_4.getText();

							Receiver receiver;
							try {
								serverSocket = new ServerSocket(prot);
								receiver = new Receiver(prot, toplimit, lowerlimit, testParam, testAddress);
								// textArea.append("打开端口成功，等待数据...\n");
								receiver.receive(dataset, timeSeries, table_1, textField_5, lowerlimit, toplimit,
										serverSocket, datas);
							} catch (IOException e1) {
								e1.printStackTrace();
								// textArea.append("端口被占用\n");
							}
							// }
							catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					}
				}).start();

			}
		});

		JButton btnNewButton_1 = new JButton("断开");
		btnNewButton_1.setFont(new Font("宋体", Font.PLAIN, 22));
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					serverSocket.close();
					// lock=false;
					// socket.close();
					if (serverSocket.isClosed()) {
						System.out.println("closed");
					}
					btnNewButton_4.setEnabled(true);

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				// sockets

			}
		});
		btnNewButton_1.setBounds(1258, 141, 120, 62);
		contentPane.add(btnNewButton_1);

		JButton btnNewButton_2 = new JButton("导出值");
		btnNewButton_2.setFont(new Font("宋体", Font.PLAIN, 22));
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				TransToExcel transToExcel = new TransToExcel();
				// Date date = new Date(System.currentTimeMillis());
				// SimpleDateFormat sdf = new
				// SimpleDateFormat("yyyyMMddHHmmss");
				// String time = sdf.format(date);
				// JFileChooser jf = new JFileChooser();
				// 点击文件夹将文件保存
				// jf.setFileSelectionMode(JFileChooser.SAVE_DIALOG
				// | JFileChooser.DIRECTORIES_ONLY);
				// jf.setDialogTitle("选择文件夹");
				// jf.showDialog(null, null);
				// File fi = jf.getSelectedFile();
				String outdir = "C:\\output";
				File dir = new File(outdir);
				if (!dir.exists()) {
					dir.mkdir();
				}
				String file = outdir + "\\" + time + ".xls";
				try {
					OutputStream os = new FileOutputStream(file);
					String[] headers = { "端口号", "报警上限", "报警下限", "测量参数", "测量地址", "记录值", "记录时间" };
					// datas.add(map);
					transToExcel.exporteExcel("学生表", headers, datas, os);
					os.close();
					ChartUtilities.saveChartAsPNG(new File("C:\\output\\" + "n" + ".png"), result, 550, 250);

					// 打开文件夹
					java.awt.Desktop.getDesktop().open(dir);
				} catch (FileNotFoundException e1) {
					System.out.println("无法找到文件");

					e1.printStackTrace();
				} catch (IOException e1) {
					System.out.println("写入文件失败");

				}

			}
		});
		btnNewButton_2.setBounds(1258, 80, 120, 61);
		contentPane.add(btnNewButton_2);

		JButton btnNewButton_3 = new JButton("清空");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textField.setText("");
				textField_1.setText("");
				textField_2.setText("");
				textField_3.setText("");
				textField_4.setText("");
				textField_5.setText("");
			}
		});
		btnNewButton_3.setFont(new Font("宋体", Font.PLAIN, 22));
		btnNewButton_3.setBounds(248, 80, 187, 62);
		contentPane.add(btnNewButton_3);

		JLabel lblNewLabel_1 = new JLabel("    报警上限");
		lblNewLabel_1.setFont(new Font("宋体", Font.PLAIN, 22));
		lblNewLabel_1.setBounds(617, 80, 177, 62);
		lblNewLabel_1.setBorder(BorderFactory.createLineBorder(Color.gray));

		contentPane.add(lblNewLabel_1);

		textField_1 = new JTextField();
		textField_1.setHorizontalAlignment(SwingConstants.CENTER);
		textField_1.setFont(new Font("宋体", Font.PLAIN, 22));
		textField_1.setBounds(617, 141, 177, 62);
		contentPane.add(textField_1);
		textField_1.setColumns(10);

		JLabel lblNewLabel_2 = new JLabel("   报警下限");
		lblNewLabel_2.setFont(new Font("宋体", Font.PLAIN, 22));
		lblNewLabel_2.setBounds(795, 80, 153, 61);
		lblNewLabel_2.setBorder(BorderFactory.createLineBorder(Color.gray));

		contentPane.add(lblNewLabel_2);

		textField_2 = new JTextField();
		textField_2.setHorizontalAlignment(SwingConstants.CENTER);
		textField_2.setFont(new Font("宋体", Font.PLAIN, 22));
		textField_2.setBounds(795, 141, 153, 62);
		contentPane.add(textField_2);
		textField_2.setColumns(10);

		JLabel lblNewLabel_3 = new JLabel("   测量参数");
		lblNewLabel_3.setFont(new Font("宋体", Font.PLAIN, 22));
		lblNewLabel_3.setBounds(949, 80, 160, 61);
		lblNewLabel_3.setBorder(BorderFactory.createLineBorder(Color.gray));

		contentPane.add(lblNewLabel_3);

		textField_3 = new JTextField();
		textField_3.setHorizontalAlignment(SwingConstants.CENTER);
		textField_3.setFont(new Font("宋体", Font.PLAIN, 22));
		textField_3.setBounds(949, 141, 160, 62);
		contentPane.add(textField_3);
		textField_3.setColumns(10);

		JLabel lblNewLabel_4 = new JLabel("  测量地址");
		lblNewLabel_4.setFont(new Font("宋体", Font.PLAIN, 22));
		lblNewLabel_4.setBounds(1110, 80, 151, 61);
		lblNewLabel_4.setBorder(BorderFactory.createLineBorder(Color.gray));

		contentPane.add(lblNewLabel_4);

		textField_4 = new JTextField();
		textField_4.setHorizontalAlignment(SwingConstants.CENTER);
		textField_4.setFont(new Font("宋体", Font.PLAIN, 22));
		textField_4.setBounds(1110, 141, 148, 62);
		contentPane.add(textField_4);
		textField_4.setColumns(10);

		JLabel lblNewLabel_5 = new JLabel("常州大学校园温度监测系统v1.0");
		lblNewLabel_5.setFont(new Font("宋体", Font.PLAIN, 32));
		lblNewLabel_5.setBounds(609, 19, 614, 49);

		contentPane.add(lblNewLabel_5);

		JLabel lblNewLabel_6 = new JLabel("         当前值");
		lblNewLabel_6.setFont(new Font("宋体", Font.PLAIN, 22));
		lblNewLabel_6.setBounds(248, 204, 278, 77);
		lblNewLabel_6.setBorder(BorderFactory.createLineBorder(Color.gray));

		contentPane.add(lblNewLabel_6);

		textField_5 = new JTextField();
		textField_5.setHorizontalAlignment(SwingConstants.CENTER);
		textField_5.setFont(new Font("宋体", Font.PLAIN, 22));
		textField_5.setBounds(248, 281, 278, 84);
		contentPane.add(textField_5);
		textField_5.setColumns(10);

		JLabel lblNewLabel_7 = new JLabel("        报警记录");
		lblNewLabel_7.setFont(new Font("宋体", Font.PLAIN, 22));
		lblNewLabel_7.setBounds(247, 364, 279, 95);
		lblNewLabel_7.setBorder(BorderFactory.createLineBorder(Color.gray));

		contentPane.add(lblNewLabel_7);

		table_1 = new JTable();
		table_1.setEnabled(false);
		table_1.setFont(new Font("宋体", Font.PLAIN, 22));

		table_1.setShowHorizontalLines(true);
		table_1.setRowHeight(30);

		JScrollPane scrollPane_1 = new JScrollPane(table_1);
		scrollPane_1.setBounds(247, 459, 279, 438);
		scrollPane_1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane_1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		contentPane.add(scrollPane_1);
		
				ChartPanel panel = new ChartPanel(createChart(dataset));
				panel.setBounds(531, 215, 847, 682);
				contentPane.add(panel);
				
						JScrollPane scrollPane = new JScrollPane();
						panel.add(scrollPane);
						scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
						scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		File file = new File(writePath);
		if (file.exists()) {
			String s = Readinfo();
			String[] datas1 = null;
			if (s != null) {
				datas1 = convertStrToArray(s);
				textField.setText(datas1[0].trim());
				textField_1.setText(datas1[1].trim());
				textField_2.setText(datas1[2].trim());
				textField_3.setText(datas1[3].trim());
				textField_4.setText(datas1[4].trim());
			}
		}
	}

	private void Writeinfo(String s) {
		File file = new File(writePath);
		PrintStream ps;
		try {
			ps = new PrintStream(new FileOutputStream(file));
			ps.println(s);// 往文件里写入字符串

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String Readinfo() {
		// String read=null;
		File file = new File(writePath);
		StringBuilder result = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));// 构造一个BufferedReader类来读取文件
			// FileReader br=new FileReader(file);
			String s = null;
			while ((s = br.readLine()) != null) {// 使用readLine方法，一次读一行
				result.append(System.lineSeparator() + s);
				System.out.println(s);
			}

			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();

	}
}
