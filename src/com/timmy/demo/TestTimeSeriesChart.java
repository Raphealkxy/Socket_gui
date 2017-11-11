package com.timmy.demo;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;

public class TestTimeSeriesChart {

	public static void createTimeSeriesChart() {
		TimeSeriesCollection dataset = new TimeSeriesCollection();

		final TimeSeries timeSeries1 = new TimeSeries("", Minute.class);


		new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					double value = 10;
               
					while(true){
						Date date=new Date(System.currentTimeMillis());
						
						SimpleDateFormat simpleFormatter=new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
						String dateString=simpleFormatter.format(date);
						String[]dStrings=dateString.split("-");
						Day day = new Day(Integer.parseInt(dStrings[2]), Integer.parseInt(dStrings[1]), Integer.parseInt(dStrings[0]));
						Hour hour=new Hour(Integer.parseInt(dStrings[3]),day);
						timeSeries1.addOrUpdate(new Minute(Integer.parseInt(dStrings[4]), hour), value);
						if (dStrings[3].equals("00")) {
							timeSeries1.clear();
						}
						
						value = Math.random() * 2 + 3;
						Thread.sleep(60000);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
        
			}
		}).start();

		dataset.addSeries(timeSeries1);

		JFreeChart timeSeriesChart = ChartFactory.createTimeSeriesChart("", "",
				"", dataset, false, true, false);
		timeSeriesChart.setBackgroundPaint(Color.YELLOW);
		XYPlot plot = timeSeriesChart.getXYPlot();
		setXYPolt(plot);

		ChartFrame frame = new ChartFrame("TestPieChart", timeSeriesChart);
		frame.pack();
		frame.setVisible(true);

	}

	public static void setXYPolt(XYPlot plot) {
		plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
		plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
		XYItemRenderer r = plot.getRenderer();
		if (r instanceof XYLineAndShapeRenderer) {
			XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
			renderer.setBaseShapesVisible(true);
			renderer.setBaseShapesFilled(false);
		}
	}

	public static void main(String[] args) {
		createTimeSeriesChart();
	}

}