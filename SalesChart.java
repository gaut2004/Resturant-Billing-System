package ResturantApp;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;

public class SalesChart {
    public static JPanel createChartPanel(String title, String category, String value, DefaultCategoryDataset dataset) {
        JFreeChart chart = ChartFactory.createLineChart(
                title,   // chart title
                category, // domain axis label
                value,    // range axis label
                dataset,  // data
                PlotOrientation.VERTICAL,
                true,     // include legend
                true,
                false
        );
        return new ChartPanel(chart);
    }
}

