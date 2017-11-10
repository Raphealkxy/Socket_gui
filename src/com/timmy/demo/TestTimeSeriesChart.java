package com.timmy.demo;

import java.awt.Color;

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
		Day day = new Day(21, 9, 2008);
		Hour hour22 = new Hour(22, day);
		final Hour hour23 = new Hour(23, day);

		final TimeSeries timeSeries1 = new TimeSeries("", Minute.class);
		timeSeries1.add(new Minute(32, hour22), 10.38);
		timeSeries1.add(new Minute(14, hour22), 2.35);
		timeSeries1.add(new Minute(23, hour22), 2.25);
		timeSeries1.add(new Minute(46, hour22), 2.16);
		timeSeries1.add(new Minute(40, hour22), 2.16);
		timeSeries1.add(new Minute(6, hour22), 1.95);
		timeSeries1.add(new Minute(51, hour22), 1.93);

		new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					int min = 0;
					double value = 0;
					for (int i = 0; i < 10000; i++) {
						timeSeries1.add(new Minute(min, hour23), value);
						if (i == 100 || i == 300) {
							timeSeries1.clear();
						}
						min++;
						value = Math.random() * 2 + 3;
						Thread.sleep(100);
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