package ie.gmit.sw.ui;

import java.io.File;
import java.io.IOException;

import ie.gmit.sw.JarProcessor;
import ie.gmit.sw.db.MetricStore;
import ie.gmit.sw.metrics.Metric;
import javafx.application.*;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.*;

/**
 * Displays the application GUI window.
 * <p>
 * The object persistence database is initialised on startup, and shut down when the
 * application window is closed.
 */
public class AppWindow extends Application {
    private final ObservableList<Metric> metrics = FXCollections.observableArrayList();
    private final JarProcessor jp = new JarProcessor();

    @Override
    public void init() {
        System.out.println("[INFO] Initialising Metric Store...");
        MetricStore.getInstance().init();
    }

    @Override
    public void stop() {
        MetricStore.getInstance().shutDown();
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("GMIT - B.Sc. in Computing (Software Development)");
        stage.setWidth(850);
        stage.setHeight(600);

        stage.setOnCloseRequest((e) -> System.exit(0));

        VBox box = new VBox();
        box.setPadding(new Insets(10));
        box.setSpacing(8);

        Scene scene = new Scene(box);
        stage.setScene(scene);

        box.getChildren().add(getFileChooserPane(stage));
        box.getChildren().add(getTableView());
        box.getChildren().add(getToolbar());

        stage.show();
        stage.centerOnScreen();
    }

    private TitledPane getFileChooserPane(Stage stage) {
        VBox panel = new VBox();

        TextField textField = new TextField();

        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JAR Files", "*.jar"));

        Button btnOpen = new Button("Select File");
        btnOpen.setOnAction(e -> {
            File f = fc.showOpenDialog(stage);

            if (f != null) {
                textField.setText(f.getAbsolutePath());
            }
        });

        Button btnProcess = new Button("Process");
        btnProcess.setOnAction(e -> {
            metrics.clear();

            try {
                metrics.addAll(jp.process(textField.getText()));
            } catch (IOException ignored) {
            }
        });

        ToolBar tb = new ToolBar();
        tb.getItems().add(btnOpen);
        tb.getItems().add(btnProcess);

        panel.getChildren().add(textField);
        panel.getChildren().add(tb);

        TitledPane tp = new TitledPane("Select JAR File to Process", panel);
        tp.setCollapsible(false);
        return tp;
    }

    private TableView<Metric> getTableView() {
        TableView<Metric> tv = new TableView<>(metrics);
        tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Metric, String> className = new TableColumn<>("Class");
        className.setCellValueFactory(new Callback<>() {
            public ObservableValue<String> call(CellDataFeatures<Metric, String> p) {
                return new SimpleStringProperty(p.getValue().getClassName());
            }
        });

        TableColumn<Metric, String> metric = new TableColumn<>("Number of Children in Tree (NOC)");
        metric.setCellValueFactory(new Callback<>() {
            public ObservableValue<String> call(CellDataFeatures<Metric, String> p) {
                return new SimpleStringProperty(p.getValue().measure().toString());
            }
        });

        tv.getColumns().add(className);
        tv.getColumns().add(metric);
        return tv;
    }

    private ToolBar getToolbar() {
        ToolBar toolBar = new ToolBar();

        Button btnQuit = new Button("Quit");
        btnQuit.setOnAction(e -> System.exit(0));
        toolBar.getItems().add(btnQuit);

        Button btnDelete = new Button("Clear DB");
        btnDelete.setOnAction(e -> {
            System.out.println("[INFO] Clearing Metric Store...");
            MetricStore.getInstance().clear();
        });
        toolBar.getItems().add(btnDelete);

        return toolBar;
    }
}
