package schedulers;

import data.Process;
import data.WorkUnit;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

/**
 * Created by Shady Atef on 3/20/16.
 * Copyrights Shadyoatef@gmail.com
 */
public class RoundRobin extends AbstractScheduler {
    private int quantumTime;

    public int getQuantumTime() {
        return quantumTime;
    }

    public void setQuantumTime(int quantumTime) {
        if (quantumTime < 1)
            throw new IllegalArgumentException();
        this.quantumTime = quantumTime;
    }


    @Override
    public String getName() {
        return "Round Robin";
    }

    @Override
    public String getDescription() {
        return "The scheduler puts processes to execution for a time unit then switches to another one in a circular manner";
    }

    @Override
    public LinkedList<WorkUnit> apply(ArrayList<Process> processes) {

       if(processes.isEmpty())
           return new LinkedList<>();
        //Sort process according to their arrival
        processes.sort(Comparator.comparingInt(Process::getArrival));

        //Make a hashmap for remaining bursts
        HashMap<Process, Integer> remainingBurst = new HashMap<>();
        for (Process p : processes) {
            remainingBurst.put(p, p.getDuration());
        }

        LinkedList<WorkUnit> schedule = new LinkedList<>();
        int timer = 0;
        Queue<Process> readyQueue = new LinkedList<>();

        Process currentExecution = null;
        int currentExecutionStartTime = 0;
        while (true) {
            // Add newly arrived processes
            final int finalTimer = timer;
            for (Object p :
                    processes.stream().filter(process -> process.getArrival() == finalTimer).toArray()
                    ) {
                readyQueue.add((Process) p);

            }
            //wait if the ready queue is empty & there are still remaining bursts in future & the processor is idle
            if (readyQueue.isEmpty() && !remainingBurst.isEmpty() && currentExecution == null) {

                timer++;
                continue;
            }
            //Current execution will be null'ed if the readyQueue is empty & current process finishes execution
            if (currentExecution == null) {
                //need to inject empty workUnits
                if (timer - currentExecutionStartTime != 0) {
                    WorkUnit e = new WorkUnit(timer - currentExecutionStartTime, null);
                    schedule.add(e);
                }
                currentExecution = readyQueue.remove();
                currentExecutionStartTime = timer;
            }
            if (timer != currentExecutionStartTime && ((timer - currentExecutionStartTime) % quantumTime == 0 || remainingBurst.get(currentExecution) < quantumTime)) {
                //Add workUnit
                WorkUnit e = new WorkUnit(Math.min(remainingBurst.get(currentExecution), quantumTime), currentExecution);
                schedule.add(e);
                //update the remaining time
                if (remainingBurst.get(currentExecution) <= quantumTime) {
                    currentExecution.setFinish(timer);
                    remainingBurst.remove(currentExecution);
                } else {
                    remainingBurst.put(currentExecution, remainingBurst.get(currentExecution) - quantumTime);
                    readyQueue.add(currentExecution);
                }
                //update the current Execution
                currentExecution = readyQueue.poll();
                currentExecutionStartTime = timer;

            }
            if (remainingBurst.isEmpty())
                break;
            timer++;
        }
        return schedule;
    }
}
