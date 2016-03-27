package gui;

import data.Process;
import data.WorkUnit;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import schedulers.*;

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
    public RadioButton SJF_NPRadio;

    // Canvas
    public Canvas GanttChartCanvas;
    public Button scheduleBtn;
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
            scheduler = new SJF_P();
        } else if (toggle == SJF_NPRadio)
            scheduler = new SJF_NP();

        processes.clear();
        observableProcesses.forEach(process -> {
            try {
                processes.add(process.clone());
            } catch (CloneNotSupportedException e1) {
                e1.printStackTrace();

            }
        });

        drawGanttChart(scheduler, scheduler.apply(processes));

    }


    private void drawGanttChart(AbstractScheduler scheduler, LinkedList<WorkUnit> output) {

        double requiredCanvasHeight;
        GraphicsContext gc = GanttChartCanvas.getGraphicsContext2D();
        gc.setFill(Color.BLUE);
        //Clear the previous chart
        gc.clearRect(0, 0, GanttChartCanvas.getWidth(), GanttChartCanvas.getHeight());
        double x = 10, y = 10, w = 40, h = 20;
        requiredCanvasHeight = y +  4* h ;
        GanttChartCanvas.setHeight(requiredCanvasHeight);
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

        // Draw a table which has the average waiting time
        double nusOfProcesses = processes.size();
        // the parameters for the table
        x = 10;
        w = 90;
        y = y + h * 2.0;

        // Check for the size of the canvas
        if (GanttChartCanvas.getWidth() < 3 * w)
            GanttChartCanvas.setWidth(3 * w);
        requiredCanvasHeight += nusOfProcesses * h + h * 2;
        GanttChartCanvas.setHeight(requiredCanvasHeight);

        //The process name
        gc.strokeRect(x, y, w, h);
        gc.fillText("Process ID", x + 5, y + 0.75 * h);

        // The process average time
        gc.strokeRect(x + w, y, w, h);
        gc.fillText("Avg waiting", x + w + 5, y + 0.75 * h);
        y += h;
        for (Process p : processes) {
            //The process name
            gc.strokeRect(x, y, w, h);
            gc.fillText(String.format("P %d", p.getId()), x + 5, y + 0.75 * h);

            // The process averge time
            gc.strokeRect(x + w, y, w, h);
            gc.fillText(String.format("%.2f", p.getWaitingTime() / nusOfProcesses), x + w + 5, y + 0.75 * h);

            y += h;
        }

        Text SchedulerInfo = new Text(String.format("The Scheduler: %s \n Description : %s", scheduler.getName(), scheduler.getDescription()));
        if (GanttChartCanvas.getWidth() < SchedulerInfo.getLayoutBounds().getWidth())
            GanttChartCanvas.setWidth(SchedulerInfo.getLayoutBounds().getWidth() + 10);


        requiredCanvasHeight += SchedulerInfo.getLayoutY() + 10;
        GanttChartCanvas.setHeight(requiredCanvasHeight);

        gc.fillText(SchedulerInfo.getText(), x, y + h);


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

        scheduleBtn.setDisable(true);
        observableProcesses.addListener((ListChangeListener<Process>) c -> scheduleBtn.setDisable(c.getList().isEmpty()));


        // Make pressing Del on tableview row to delete it
        processesTable.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.DELETE) {
                    Process p = processesTable.getSelectionModel().getSelectedItem();
                    observableProcesses.remove(p);
                }
            }
        });
    }
}
