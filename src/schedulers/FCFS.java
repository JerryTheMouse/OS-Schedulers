package schedulers;

import data.Process;
import data.WorkUnit;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;

/**
 * Created by Shady Atef on 3/18/16.
 * Copyrights Shadyoatef@gmail.com
 */
public class FCFS extends AbstractScheduler {
    @Override
    public Process.Attributes[] getRequiredAttributes() {
        return new Process.Attributes[]{Process.Attributes.ARRIVAL, Process.Attributes.DURATION};
    }

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
        processes.sort(Comparator.comparingInt(Process::getArrival));
        LinkedList<WorkUnit> schedule = new LinkedList<>();

        int totalConsumedTime = 0;
        for (Process p : processes) {
            totalConsumedTime += p.getDuration();
            p.setFinish(totalConsumedTime);
            schedule.add(new WorkUnit(p.getDuration(), p));
        }
        return schedule;
    }
}
