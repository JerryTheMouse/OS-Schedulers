package schedulers;

import data.Process;
import data.WorkUnit;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Dalia on 3/26/2016.
 */
public class SJF_NP extends AbstractScheduler {
    @Override
    public Process.Attributes[] getRequiredAttributes() {
        return new Process.Attributes[0];
    }

    @Override
    public String getName() {
        return "Shortest Job First Non-Preemptive";
    }

    @Override
    public String getDescription() {
        return "Puts processes in order according to burst time ";
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
                int time = p.getDuration();
                if (NextExecute == null || NextExecute.getDuration() > time)
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
