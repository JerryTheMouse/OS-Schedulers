package schedulers;

import data.Process;
import data.WorkUnit;

import java.util.*;

/**
 * Created by Dalia on 3/24/2016.
 */
public class SJF_P extends AbstractScheduler {
    @Override
    public Process.Attributes[] getRequiredAttributes() {
        return new Process.Attributes[0];
    }

    @Override
    public String getName() {
        return "Shortest Job First Preemptive";
    }

    @Override
    public String getDescription() {
        return "Puts processes in order according to burst time ";
    }

    @Override
    public LinkedList<WorkUnit> apply(ArrayList<Process> processes) {
        processes.sort(Comparator.comparingInt(Process::getArrival));

        LinkedList<WorkUnit> schedule = new LinkedList<>();

        HashMap<Process, Integer> remainingBursts = new HashMap<>();

        for (Process p :
                processes) {
            remainingBursts.put(p, p.getDuration());
        }

        int currentTime = 0;

        Process currentExecutedProcess = null;
        int currentExecutedStartTime = 0;

        Queue<Process> readyQueue = new LinkedList<>();


        while (true) {
            for (Process p : processes
                    ) {
                if (p.getArrival() == currentTime)
                    readyQueue.add(p);

            }

            Process NextExecute = null;

            if (currentExecutedProcess != null) {
                remainingBursts.put(currentExecutedProcess, remainingBursts.get(currentExecutedProcess) - 1);
                if (remainingBursts.get(currentExecutedProcess) == 0)
                    readyQueue.remove(currentExecutedProcess);
                currentExecutedProcess.setFinish(currentTime);
            }

            for (Process p : readyQueue) {
                int burst = remainingBursts.get(p);
                if (NextExecute == null || remainingBursts.get(NextExecute) > burst)
                    NextExecute = p;
            }


            if (NextExecute != currentExecutedProcess) {

                int workUnitLength = currentTime - currentExecutedStartTime;
                if (workUnitLength != 0) {
                    WorkUnit u = new WorkUnit(workUnitLength, currentExecutedProcess);
                    schedule.add(u);
                }

                currentExecutedProcess = NextExecute;
                currentExecutedStartTime = currentTime;
            }
            currentTime++;

            final boolean[] finished = {true};
            remainingBursts.forEach((process, integer) -> {
                if (integer != 0)
                    finished[0] = false;

            });
            if (finished[0])
                return schedule;

        }


    }
}
