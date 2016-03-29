package schedulers;

import data.Process;
import data.WorkUnit;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Doaa on 3/28/2016.
 */
public class Priority_NP extends AbstractScheduler {

    @Override
    public String getName() {
        return "Priority Schedule Non-Preemptive";
    }

    @Override
    public String getDescription() {
        return "Puts processes in order according to priority ";
    }

    @Override
    public LinkedList<WorkUnit> apply(ArrayList<Process> processes) {
        processes.sort(Comparator.comparingInt(Process::getArrival));
        LinkedList<WorkUnit> schedule = new LinkedList<>();


        Process currentExecutedProcess = null;
        int currentExecutedStartTime = 0;
        int currentTime = 0;


        Queue<Process> readyQueue = new LinkedList<>();

        //How many processes has finished its burst time
        int finishedProcesses = 0;

        while (true) {
            for (Process p : processes) {
                if (p.getArrival() == currentTime)
                    readyQueue.add(p);
            }

            Process NextExecute = null;

            if (currentExecutedProcess != null && currentExecutedProcess.getDuration() == (currentTime - currentExecutedStartTime)) {
                readyQueue.remove(currentExecutedProcess);
                currentExecutedProcess.setFinish(currentTime);
                finishedProcesses++;
            }

            for (Process p : readyQueue) {
                int priority = p.getPriority();
                if (NextExecute == null || NextExecute.getPriority() > priority)
                    NextExecute = p;
            }
            if (currentExecutedProcess == null || (NextExecute != currentExecutedProcess && currentExecutedProcess.getFinish() != 0)) {

                int workUnitLength = currentTime - currentExecutedStartTime;
                if (workUnitLength != 0) {
                    WorkUnit u = new WorkUnit(workUnitLength, currentExecutedProcess);
                    schedule.add(u);
                }

                currentExecutedProcess = NextExecute;
                currentExecutedStartTime = currentTime;
            }
            currentTime++;


            if (finishedProcesses == processes.size())
                return schedule;
        }
    }
}
