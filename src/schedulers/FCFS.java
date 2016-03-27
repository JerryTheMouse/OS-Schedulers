package schedulers;

import data.Process;
import data.WorkUnit;

import java.util.*;

/**
 * Created by Shady Atef on 3/18/16.
 * Copyrights Shadyoatef@gmail.com
 */
public class FCFS extends AbstractScheduler {

    @Override
    public String getName() {
        return "First Come First Served";
    }

    @Override
    public String getDescription() {
        return "The scheduler puts processes to execution in the order they come";
    }

    @Override
    public LinkedList<WorkUnit> apply(ArrayList<Process> processes) {
        //The scheduler will not run on empty process
        if (processes.size() == 0)
            return new LinkedList<>();

        //Sort process according to their arrival
        processes.sort(Comparator.comparingInt(Process::getArrival));

        //Get reference for the last arrived process
        Process lastProcess = processes.get(processes.size() - 1);
        LinkedList<WorkUnit> schedule = new LinkedList<>();
        int timer = 0;
        Queue<Process> readyQueue = new LinkedList<>();

        Process currentExecutedProcess = null;
        int currentExecutionStartTime = 0;
        while (true) {
            // Add newly arrived processes
            final int finalTimer = timer;
            for (Object p :
                    processes.stream().filter(process -> process.getArrival() == finalTimer).toArray()
                    ) {
                readyQueue.add((Process) p);

            }
            //wait if the ready queue is empty
            if (readyQueue.isEmpty() && currentExecutedProcess == null) {

                timer++;
                continue;
            }
            //Current execution will be null'ed if the readyQueue is empty & current process finishes execution
            if (currentExecutedProcess == null) {
                //need to inject empty workUnits
                if (timer - currentExecutionStartTime != 0) {
                    //Add idle time
                    WorkUnit e = new WorkUnit(timer - currentExecutionStartTime, null);
                    schedule.add(e);
                }
                currentExecutedProcess = readyQueue.remove();
                currentExecutionStartTime = timer;
            }
            if (timer - currentExecutionStartTime == currentExecutedProcess.getDuration() ) {
                //Add workUnit
                WorkUnit e = new WorkUnit(currentExecutedProcess.getDuration(), currentExecutedProcess);
                schedule.add(e);
                //update the finished time
                currentExecutedProcess.setFinish(timer);


                //update the current Execution
                currentExecutedProcess = readyQueue.poll();
                currentExecutionStartTime = timer;

            }
            if (lastProcess.getFinish() != 0)
                break;
            timer++;
        }
        return schedule;
    }
}
