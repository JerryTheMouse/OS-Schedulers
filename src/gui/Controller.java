package gui;

import data.Process;
import data.WorkUnit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import schedulers.AbstractScheduler;
import schedulers.FCFS;
import schedulers.RoundRobin;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public static final int MAX = 214483644;
    //New Process Input Data
    public Spinner<Integer> arrivalSpinner;
    public Spinner<Integer> durationSpinner;
    public Spinner<Integer> prioritySpinner;
    public Button addProcessBtn;

    //Process Tables & related columns
    public TableView<Process> processesTable;
    public TableColumn<Process, Integer> processPriorityTC;
    public TableColumn<Process, Integer> processIDTC;
    public TableColumn<Process, Integer> processDurationTC;
    public TableColumn<Process, Integer> processArrivalTC;
    // SchedulersToggleGroup Options
    public Spinner<Integer> quantumTimeSpinner;
    public ToggleGroup SchedulersToggleGroup;
    public RadioButton RoundRobinRadio;
    public RadioButton FCFSRadio;
    public RadioButton SJF_PRadio;
    // Canvas
    public Canvas GanttChartCanvas;
    private ArrayList<Process> processes = new ArrayList<>();
    private ObservableList<Process> observableProcesses = FXCollections.observableArrayList(processes);

    public void Schedule(ActionEvent e) {
        AbstractScheduler scheduler = null;
        Toggle toggle = SchedulersToggleGroup.getSelectedToggle();
        if (toggle == RoundRobinRadio) {
            scheduler = new RoundRobin();
            ((RoundRobin) scheduler).setQuantumTime(quantumTimeSpinner.getValue());

        } else if (toggle == FCFSRadio) {
            scheduler = new FCFS();
        } else if (toggle == SJF_PRadio) {
        }

        processes.clear();
        observableProcesses.forEach(process -> {
            try {
                processes.add(process.clone());
            } catch (CloneNotSupportedException e1) {
                e1.printStackTrace();

            }
        });

        drawGanttChart(scheduler.apply(processes));
    }

    private void drawGanttChart(LinkedList<WorkUnit> output) {

        GraphicsContext gc = GanttChartCanvas.getGraphicsContext2D();
        gc.setFill(Color.BLUE);
        //Clear the previous chart
        gc.clearRect(0, 0, GanttChartCanvas.getWidth(), GanttChartCanvas.getHeight());
        double x = 10, y = 10, w = 40, h = 20;

        GanttChartCanvas.setWidth(output.size() * w + 50);

        int counter = 0;
        for (WorkUnit u :
                output) {
            //Write the zero
            if (counter == 0)
                gc.fillText(String.valueOf(counter), x, y + 1.75 * h);

            gc.strokeRect(x, y, w, h);
            if (u.getP() == null)

                gc.fillText("Idle", x + 5, y + 0.75 * h);

            else
                gc.fillText(String.format("P %d", u.getP().getId()), x + 5, y + 0.75 * h);


            counter += u.getTime();
            x += w;
            //write the counter at the end of each box
            gc.fillText(String.valueOf(counter), x, y + 1.75 * h);

        }
    }

    public void AddProcess(ActionEvent e) {
        int arrival = arrivalSpinner.getValue();
        int duration = durationSpinner.getValue();
        int priority = prioritySpinner.getValue();
        Process p = new Process(arrival, duration, priority);
        observableProcesses.add(p);

        arrivalSpinner.getValueFactory().setValue(0);
        durationSpinner.getValueFactory().setValue(1);
        prioritySpinner.getValueFactory().setValue(0);

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        processesTable.setItems(observableProcesses);
        processIDTC.setCellValueFactory(new PropertyValueFactory<>("id"));
        processArrivalTC.setCellValueFactory(new PropertyValueFactory<>("arrival"));
        processDurationTC.setCellValueFactory(new PropertyValueFactory<>("duration"));
        processPriorityTC.setCellValueFactory(new PropertyValueFactory<>("priority"));


        arrivalSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, MAX, 0));
        durationSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, MAX, 1));
        prioritySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, MAX, 0));


        quantumTimeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, MAX, 1));
        SchedulersToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {

            quantumTimeSpinner.setDisable(newValue != RoundRobinRadio);
        });

        //Initially selected round robin
        RoundRobinRadio.setSelected(true);
    }
}
