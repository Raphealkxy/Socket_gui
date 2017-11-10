package com.timmy.demo;

import java.awt.BasicStroke;  
import java.awt.Color;  
import java.awt.Polygon;  
import java.awt.Shape;  
import java.awt.geom.Rectangle2D;  
  
import org.jfree.chart.ChartFactory;  
import org.jfree.chart.ChartPanel;  
import org.jfree.chart.JFreeChart;  
import org.jfree.chart.axis.NumberAxis;  
import org.jfree.chart.plot.CategoryPlot;  
import org.jfree.chart.plot.DefaultDrawingSupplier;  
import org.jfree.chart.plot.DrawingSupplier;  
import org.jfree.chart.plot.PlotOrientation;  
import org.jfree.chart.renderer.category.LineAndShapeRenderer;  
import org.jfree.data.category.CategoryDataset;  
import org.jfree.data.category.DefaultCategoryDataset;  
import org.jfree.ui.ApplicationFrame;  
import org.jfree.ui.RefineryUtilities;  
  
/**    
 * A line chart demo showing the use of a custom drawing supplier.    
 *    
 */  
public class LineChartDemo5 extends ApplicationFrame  
{  
  
    /**    
     * Creates a new demo.    
     *    
     * @param title the frame title.    
     */  
    public LineChartDemo5()  
    {  
        super(null);  
        
        //final CategoryDataset dataset = createDataset(); 
        final CategoryDataset dataset = new DefaultCategoryDataset();  

     //   CategoryDataset dataset=new C
        final JFreeChart chart = createChart(dataset);  
        final ChartPanel chartPanel = new ChartPanel(chart);  
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));  
        setContentPane(chartPanel);  
  
    }  
  
    /**    
     * Creates a sample dataset.    
     *    
     * @return a sample dataset.    
     */  
    private CategoryDataset createDataset()  
    {  
  
        // row keys...      
        final String series1 = "";   
  
        // column keys...      
        final String type1 = "1";  
        final String type2 = "2";  
        final String type3 = "3";  
        final String type4 = "4";  
        final String type5 = "5";  
        final String type6 = "6";  
        final String type7 = "7";  
        final String type8 = "8";  
        final String type9 = "9";  
        final String type10 = "10";  
        final String type11 = "11";  
        final String type12 = "12";  
        final String type13 = "13";  
        final String type14 = "14";  
        final String type15 = "15";  
        final String type16 = "16"; 
        final String type17 = "17";  
        final String type18 = "18";  
        final String type19 = "19";  
        final String type20 = "20";  
        final String type21 = "21";  
        final String type22 = "22";  
        final String type23 = "23";  
        final String type24 = "0"; 
        // create the dataset...      
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();  
        dataset.addValue(1.0, series1, type24);
        dataset.addValue(1.0, series1, type1);  
        dataset.addValue(4.0, series1, type2);  
        dataset.addValue(3.0, series1, type3);  
        dataset.addValue(5.0, series1, type4);  
        dataset.addValue(5.0, series1, type5);  
        dataset.addValue(7.0, series1, type6);  
        dataset.addValue(7.0, series1, type7);  
        dataset.addValue(10.0, series1, type8);  
        dataset.addValue(5.0, series1, type9);  
        dataset.addValue(7.0, series1, type10);  
        dataset.addValue(6.0, series1, type11);  
        dataset.addValue(8.0, series1, type12);  
        dataset.addValue(4.0, series1, type13);  
        dataset.addValue(4.0, series1, type14);  
        dataset.addValue(2.0, series1, type15);  
        dataset.addValue(1.0, series1, type16);  
        dataset.addValue(7.0, series1, type17);  
        dataset.addValue(6.0, series1, type18);  
        dataset.addValue(8.0, series1, type19);  
        dataset.addValue(4.0, series1, type20);  
        dataset.addValue(4.0, series1, type21);  
        dataset.addValue(2.0, series1, type22);  
        dataset.addValue(1.0, series1, type23); 
  
        return dataset;  
  
    }  
  
    /**    
     * Creates a sample chart.    
     *    
     * @param dataset the dataset.    
     *    
     * @return a chart.    
     */  
    private JFreeChart createChart(final CategoryDataset dataset)  
    {  
  
        final JFreeChart chart = ChartFactory.createLineChart(  
                "", // chart title      
                "", // domain axis label      
                "", // range axis label      
                dataset, // data      
                PlotOrientation.VERTICAL, // orientation      
                true, // include legend      
                false, // tooltips      
                false // urls      
                );  
  
        // final StandardLegend legend = (StandardLegend) chart.getLegend();      
        // legend.setDisplaySeriesShapes(true);      
  
        final Shape[] shapes = new Shape[3];  
        int[] xpoints;  
        int[] ypoints;  
  
        // right-pointing triangle      
        xpoints = new int[] { -3, 3, -3 };  
        ypoints = new int[] { -3, 0, 3 };  
        shapes[0] = new Polygon(xpoints, ypoints, 3);  
  
        // vertical rectangle      
        shapes[1] = new Rectangle2D.Double(-2, -3, 3, 6);  
  
        // left-pointing triangle      
        xpoints = new int[] { -3, 3, 3 };  
        ypoints = new int[] { 0, -3, 3 };  
        shapes[2] = new Polygon(xpoints, ypoints, 3);  
  
        final DrawingSupplier supplier = new DefaultDrawingSupplier(  
                DefaultDrawingSupplier.DEFAULT_PAINT_SEQUENCE,  
                DefaultDrawingSupplier.DEFAULT_OUTLINE_PAINT_SEQUENCE,  
                DefaultDrawingSupplier.DEFAULT_STROKE_SEQUENCE,  
                DefaultDrawingSupplier.DEFAULT_OUTLINE_STROKE_SEQUENCE, shapes);  
        final CategoryPlot plot = chart.getCategoryPlot();  
        plot.setDrawingSupplier(supplier);  
  
        chart.setBackgroundPaint(Color.green);  
  
        // set the stroke for each series...      
        plot.getRenderer().setSeriesStroke(  
                0,  
                new BasicStroke(2.0f, BasicStroke.CAP_ROUND,  
                        BasicStroke.JOIN_ROUND, 1.0f,  
                        new float[] { 10.0f, 6.0f }, 0.0f));  
        plot.getRenderer().setSeriesStroke(  
                1,  
                new BasicStroke(2.0f, BasicStroke.CAP_ROUND,  
                        BasicStroke.JOIN_ROUND, 1.0f,  
                        new float[] { 6.0f, 6.0f }, 0.0f));  
        plot.getRenderer().setSeriesStroke(  
                2,  
                new BasicStroke(2.0f, BasicStroke.CAP_ROUND,  
                        BasicStroke.JOIN_ROUND, 1.0f,  
                        new float[] { 2.0f, 6.0f }, 0.0f));  
  
        // customise the renderer...      
        final LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot  
                .getRenderer();  
        // renderer.setDrawShapes(true);      
        renderer.setItemLabelsVisible(true);  
        // renderer.setLabelGenerator(new StandardCategoryLabelGenerator());      
  
        // customise the range axis...      
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();  
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());  
        rangeAxis.setAutoRangeIncludesZero(false);  
        rangeAxis.setUpperMargin(0.12);  
  
        return chart;  
  
    }  
  

    public static void main(final String[] args)  
    {  
  
        final LineChartDemo5 demo = new LineChartDemo5();  
        demo.pack();  
        RefineryUtilities.centerFrameOnScreen(demo);  
        demo.setVisible(true);  
  
    }  
  
}
