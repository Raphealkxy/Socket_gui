package Server;

import java.io.DataInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

public class Receiver {

	// private final ServerSocket serverSocket;
	private Socket socket;
	private Boolean lock;
	private int port;
	private double toplimit;
	private double lowerlimit;
	private String testparam;
	private String testAddress;
    
	public Receiver(int port, double toplimit, double lowerlimit, String testparam, String testAddress)
			throws IOException {

		this.port = port;
		this.toplimit = toplimit;
		this.lowerlimit = lowerlimit;
		this.testparam = testparam;
		this.testAddress = testAddress;

	}

	public void receive(TimeSeriesCollection dataset, TimeSeries timeSeries, JTable jTable, JTextField jTextField,
			double toplimit, double lowerlimit, ServerSocket serverSocket, List<Map<String, String>> datas)
			throws IOException {

		Vector vData = new Vector();
		Vector vName = new Vector();
		Vector vRow;
		DefaultTableModel tableModel;
		vName.add("时间");
		vName.add("记录值");
		if (!serverSocket.isClosed()) {
			socket = serverSocket.accept();
			lock = true;
			// break;
		} else {
			lock = false;
		}
		double value;
		// SimpleDateFormat sdf;
		Calendar calendar;
		Date date;
		Boolean flag = true;
		try (DataInputStream dis = new DataInputStream(socket.getInputStream())) {
			while (true) {

				if (serverSocket.isClosed()) {
					break;
				}

				byte[] bytes = new byte[8]; // 假设发送的字节数不超过 1024 个
				int size = dis.read(bytes); // size 是读取到的字节数

				if (size > 0) {
					Date now = new Date();

					SimpleDateFormat sFormat = new SimpleDateFormat("HH:mm:ss");

					try {
						// System.out.println(bytes.length);

						String string = bytesToHex(bytes, 0, 8);
						// System.out.println(string.length());
						Map<Integer, String> map1 = new HashMap<Integer, String>();
						map1.put(1, bytesToHex(bytes, 0, 1).trim());
						map1.put(2, bytesToHex(bytes, 1, 2).trim());
						map1.put(3, bytesToHex(bytes, 2, 3).trim());
						map1.put(4, bytesToHex(bytes, 3, 4).trim());
						map1.put(5, bytesToHex(bytes, 4, 5).trim());
						map1.put(6, bytesToHex(bytes, 5, 6).trim());
						map1.put(7, bytesToHex(bytes, 6, 7).trim());
						map1.put(8, bytesToHex(bytes, 7, 8).trim());

						int a = Integer.parseInt(map1.get(1), 16);
						int b = Integer.parseInt(map1.get(2), 16);
						int c = Integer.parseInt(map1.get(3), 16);
						int d = Integer.parseInt(map1.get(4), 16);
						int e = Integer.parseInt(map1.get(5), 16);
						int f = Integer.parseInt(map1.get(6), 16);
						int g = Integer.parseInt(map1.get(7), 16);
						int h = Integer.parseInt(map1.get(8), 16);
						// System.out.println(a);
						// System.out.println(b);
						// System.out.println(c);
						// System.out.println(d);
						// System.out.println(e);
						// System.out.println(f);
						// System.out.println(g);
						// System.out.println(h);
						String strnum;
						if (a != 0)
							strnum = a + "" + b + "" + c + "" + d + "" + e + "" + "." + f + "" + g + "" + h + "";
						else {
							strnum = b + "" + c + "" + d + "" + e + "" + "." + f + "" + g + "" + h + "";

						}
						// for()
						// System.out.println(strnum);
						value = Double.valueOf(strnum);
						System.out.println(value);
						// String str = new String(bytes);
						String str2 = new String(bytes, "GB18030");

						// value = Double.valueOf(str.trim());
						timeSeries.add(new Millisecond(), value);

						if (flag) {
							dataset.addSeries(timeSeries);
							flag = false;

						}

						try {
							Thread.currentThread().sleep(100);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						} //
							// System.out.println(value); //
						System.out.println(lowerlimit); //
						System.out.println(toplimit);
						Date date1 = new Date(System.currentTimeMillis());
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String time = sdf.format(date1);

						Map<String, String> map = new HashMap<>();
						map.put("端口号", port + "");
						map.put("报警上限", toplimit + "");
						map.put("报警下限", lowerlimit + "");
						map.put("测量参数", testparam);
						map.put("测量地址", testAddress);
						map.put("记录值", value + "");
						map.put("记录时间", time);
						datas.add(map);

						if (value >= lowerlimit && value <= toplimit) {
							vRow = new Vector();
							vRow.add(sFormat.format(now));
							vRow.add(strnum);
							vData.add(vRow);
							tableModel = new DefaultTableModel(vData, vName);
							jTable.setModel(tableModel);

						}
						jTextField.setText(strnum);

					} catch (Exception e) {
						e.printStackTrace();

					}

				}

			}
		}

	}

	/**
	 * 将 byte 数组转化为十六进制字符串
	 *
	 * @param bytes
	 *            byte[] 数组
	 * @param begin
	 *            起始位置
	 * @param end
	 *            结束位置
	 * @return byte 数组的十六进制字符串表示
	 */
	private String bytesToHex(byte[] bytes, int begin, int end) {
		StringBuilder hexBuilder = new StringBuilder(2 * (end - begin));
		for (int i = begin; i < end; i++) {
			hexBuilder.append(Character.forDigit((bytes[i] & 0xF0) >> 4, 16)); // 转化高四位
			hexBuilder.append(Character.forDigit((bytes[i] & 0x0F), 16)); // 转化低四位
			hexBuilder.append(' '); // 加一个空格将每个字节分隔开
		}
		return hexBuilder.toString().toUpperCase();
	}


}
