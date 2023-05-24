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
import java.util.List;


public class App extends Application {

    @Override public void start(Stage stage) {

        // Create the x-axis representing time
        NumberAxis xAxis = new NumberAxis(0, 24 * 60, 60);
        xAxis.setTickLabelFormatter(new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                int minutes = object.intValue();
                int hours = minutes / 60;
                return String.format("%02d:%02d", hours % 24, minutes % 60);
            }

            @Override
            public Number fromString(String string) {
                return null;
            }
        });

        // Create the y-axis representing integers
        NumberAxis yAxis = new NumberAxis();
        // yAxis.setTickLabelFormatter(new StringConverter<Number>() {
        //     @Override
        //     public String toString(Number object) {
        //         return String.valueOf(object.intValue());
        //     }

        //     @Override
        //     public Number fromString(String string) {
        //         return null;
        //     }
        // });

        // Create the line chart
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);

        // Set the title of the chart
        lineChart.setTitle("Appointment Intersections");

        // Create a series to hold the data points
        XYChart.Series<Number, Number> series = new XYChart.Series<>();

        // Generate data for the series
        generateAndScaleData(series);

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


    private void generateAndScaleData(XYChart.Series<Number, Number> series) {
        // Assuming you have a list of appointments with start and end timestamps
        List<Appointment> appointments = getAppointments();
    
        // Convert the timestamps to time values in minutes
        for (int i = 0; i < 24; i++) {

            // Add the data points to the series
            long start = Timestamp.valueOf("2023-01-01 00:00:00").getTime() / (1000 * 60);
            long end = Timestamp.valueOf("2023-01-01 00:00:00").getTime() / (1000 * 60);

            double position = i / 24.0;

            long time = (long) (start + (end - start) * position);

            // Count the number of intersections for the current time
            int intersectionCount = 0;
            for (Appointment apt : appointments) {
                long aptStart = apt.getStart().toLocalDateTime().toLocalTime().atDate(LocalDate.of(2023, 1, 1));
                long aptEnd = apt.getEnd().toLocalDateTime().toLocalTime().atDate(LocalDate.of(2023, 1, 1));
                if (time >= aptStart && time <= aptEnd) {
                    intersectionCount++;
                }
            }

            // Add the data point to the series
            series.getData().add(new XYChart.Data<>(position, intersectionCount));

        }
    }
    
    private void generateData(XYChart.Series<Number, Number> series) {
        // Sample list of appointments
        ObservableList<Appointment> appointments = getAppointments();

        // Iterate over the x-axis values (0.0 to 1.0)
        for (double x = 0; x <= 1; x += 0.01) {
            // Calculate the time for the current x-axis value
            long start = Timestamp.valueOf("2023-01-01 00:00:00").getTime();
            long end = Timestamp.valueOf("2023-01-02 00:00:00").getTime();
            long time = (long) (start + (end - start) * x);

            // Count the number of intersections for the current time
            int intersectionCount = 0;
            for (Appointment apt : appointments) {
                if (time >= apt.getStart().getTime() && time <= apt.getEnd().getTime()) {
                    intersectionCount++;
                }
            }

            // Add the data point to the series
            series.getData().add(new XYChart.Data<>(x, intersectionCount));
        }
    }

    private ObservableList<Appointment> getAppointments() {
        // Replace this with your actual data retrieval logic
        // Here, we use a dummy list of appointments for demonstration purposes
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        appointments.add(new Appointment(Timestamp.valueOf("2023-01-01 08:30:00"), Timestamp.valueOf("2023-01-01 09:30:00")));
        appointments.add(new Appointment(Timestamp.valueOf("2023-01-01 11:00:00"), Timestamp.valueOf("2023-01-01 12:30:00")));
        appointments.add(new Appointment(Timestamp.valueOf("2023-01-01 12:00:00"), Timestamp.valueOf("2023-01-01 13:30:00")));
        appointments.add(new Appointment(Timestamp.valueOf("2023-01-01 12:00:00"), Timestamp.valueOf("2023-01-01 12:30:00")));
        appointments.add(new Appointment(Timestamp.valueOf("2023-01-01 15:00:00"), Timestamp.valueOf("2023-01-01 16:30:00")));

        return appointments;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
