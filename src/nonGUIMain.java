import data.Process;
import data.WorkUnit;
import schedulers.AbstractScheduler;
import schedulers.FCFS;
import schedulers.RoundRobin;

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
        processes.add(new Process(9, 10));
        processes.add(new Process(5, 6));
        processes.add(new Process(2, 3));
        processes.add(new Process(25, 6));

        AbstractScheduler scheduler;
        //Run Round Robin
        scheduler = new RoundRobin();
        ((RoundRobin) scheduler).setQuantumTime(2);
        RunScheduler(processes, scheduler);
        ResetProcessFinishTimes(processes);
        //Run FCFS
        scheduler = new FCFS();
        RunScheduler(processes, scheduler);

    }


    private static void ResetProcessFinishTimes(ArrayList<Process> processes) {
        processes.forEach(Process::resetFinish);
    }

    private static void RunScheduler(ArrayList<Process> processes, AbstractScheduler scheduler) {
        System.out.println(String.format("The used scheduler is : %s", scheduler.getName()));
        System.out.println(String.format("It works as the following : %s", scheduler.getDescription()));

        LinkedList<WorkUnit> output = scheduler.apply(processes);
        PrintSchedule(output);
        System.out.println();
    }

    private static void PrintSchedule(LinkedList<WorkUnit> output) {
        //Printing the output in an elegant way

        int totalTime = 0;
        for (WorkUnit u :
                output) {
            totalTime += u.getTime();
            if (u.getP() == null)
                System.out.print(String.format("| Idle "));
            else
                System.out.print(String.format("| P%s ", u.getP().getId()));

            System.out.print(String.format(" %s %d", (new String(new char[u.getTime()])).replace("\0", " "), totalTime));
        }
        System.out.println("|");
    }


}
