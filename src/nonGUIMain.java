import data.Process;
import data.WorkUnit;
import schedulers.AbstractScheduler;
import schedulers.FCFS;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Shady Atef on 3/18/16.
 * Copyrights Shadyoatef@gmail.com
 */
public class nonGUIMain {

    public static void main(String[] args) {

        // Create a Few test Process
        ArrayList<Process> processes = new ArrayList<>();
        processes.add(new Process(0, 10));
        processes.add(new Process(5, 6));
        processes.add(new Process(2, 3));

        AbstractScheduler scheduler = new FCFS();
        System.out.println(String.format("The used scheduler is : %s", scheduler.getName()));
        System.out.println(String.format("It works as the following : %s", scheduler.getDescription()));

        LinkedList<WorkUnit> output = scheduler.apply(processes);
        PrintSchedule(output);


    }

    private static void PrintSchedule(LinkedList<WorkUnit> output) {
        //Printing the output in an elegant way

        int totalTime = 0;
        for (WorkUnit u :
                output) {
            totalTime+=u.getTime();
        System.out.print(String.format("| P%s ", u.getP().getId()));
        System.out.print(String.format(" %s %d", (new String(new char[u.getTime()])).replace("\0"," "), totalTime));
        }
        System.out.println("|");
    }
}
