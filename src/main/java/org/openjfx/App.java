package org.openjfx;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public class App extends Application {

    @Override public void start(Stage stage) {

        // Create the x-axis representing time
        NumberAxis xAxis = new NumberAxis(0, 1440, 60);
        xAxis.setTickLabelFormatter(new StringConverter<Number>() {
            @Override public Number fromString(String string) { return null; }
            @Override public String toString (Number object) {
                int minutes = object.intValue();
                return String.format("%02d:%02d", (minutes / 60) % 24, minutes % 60);
            }
        });

        // Create the y-axis representing integers
        NumberAxis yAxis = new NumberAxis();

        // Create the line chart
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);

        // Set the title of the chart
        lineChart.setTitle("Appointment Intersections");

        // Create a series to hold the data points
        XYChart.Series<Number, Number> series = new XYChart.Series<>();

        // Generate data for the series
        int maxIntersectionCount = generateAndScaleData(series);

        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0);        
        yAxis.setUpperBound(maxIntersectionCount + 1);
        yAxis.setTickUnit(1.0);
        yAxis.setMinorTickVisible(false);

        // Add the series to the line chart
        lineChart.getData().add(series);

        // Remove os circulos dos pontos da linha
        lineChart.setCreateSymbols(false);

        // Remove a legenda
        lineChart.setLegendVisible(false);

        // Create the scene and add the line chart
        Scene scene = new Scene(lineChart, 800, 600);

        // Set the scene on the stage
        stage.setScene(scene);

        // Display the stage
        stage.show();
    }


    private int generateAndScaleData(XYChart.Series<Number, Number> series) {
        // Assuming you have a list of appointments with start and end timestamps
        List<Appointment> appointments = getAppointments();

        // Add the data points to the series
        long start = Timestamp.valueOf("2023-01-01 00:00:00").getTime();
        long end = Timestamp.valueOf("2023-01-02 00:00:00").getTime();

        final LocalDate defaultDate = LocalDate.of(2023, 1, 1);
        final long day = 86400000;

        int maxIntersectionCount = 0;

        // Convert the timestamps to time values in minutes
        for (long time = start; time <= end; time += 60000) {
            
            double position = (double) (time - start) / (end - start) * 24 * 60;

            // Count the number of intersections for the current time
            int intersectionCount = 0;
            for (Appointment apt : appointments) {

                LocalDateTime aptStartDateTime = apt.getStart().toLocalDateTime();
                LocalDateTime aptEndDateTime = apt.getEnd().toLocalDateTime();   

                int dayCount = aptEndDateTime.toLocalDate().compareTo(aptStartDateTime.toLocalDate());
             
                long aptStart = Timestamp.valueOf(aptStartDateTime.toLocalTime().atDate(
                    defaultDate
                )).getTime();

                long aptEnd = Timestamp.valueOf(aptEndDateTime.toLocalTime().atDate(
                    defaultDate.plusDays(dayCount)
                )).getTime();

                // System.out.println("S: " + time + " >= " + aptStart + " && " + time + " <= " + aptEnd);
                for (int i = 0; i < 1 + dayCount; i++) {
                    if (time + (i * day) >= aptStart && time + (i * day) <= aptEnd) intersectionCount++;
                }
            }

            // Add the data point to the series
            series.getData().add(new XYChart.Data<>(position, intersectionCount));
            if (intersectionCount > maxIntersectionCount) maxIntersectionCount = intersectionCount;

        }

        return maxIntersectionCount;
    }
    
    private ObservableList<Appointment> getAppointments() {
        // Replace this with your actual data retrieval logic
        // Here, we use a dummy list of appointments for demonstration purposes
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        // appointments.add(new Appointment(Timestamp.valueOf("2023-01-01 08:30:00"), Timestamp.valueOf("2023-01-01 09:30:00")));
        // appointments.add(new Appointment(Timestamp.valueOf("2023-01-01 11:00:00"), Timestamp.valueOf("2023-01-01 12:30:00")));
        // appointments.add(new Appointment(Timestamp.valueOf("2023-01-01 12:00:00"), Timestamp.valueOf("2023-01-01 13:30:00")));
        // appointments.add(new Appointment(Timestamp.valueOf("2023-01-01 12:00:00"), Timestamp.valueOf("2023-01-01 12:30:00")));
        // appointments.add(new Appointment(Timestamp.valueOf("2023-01-01 15:00:00"), Timestamp.valueOf("2023-01-01 16:30:00")));
        appointments.add(new Appointment(Timestamp.valueOf("2023-01-01 23:00:00"), Timestamp.valueOf("2023-01-06 01:00:00")));

        return appointments;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
