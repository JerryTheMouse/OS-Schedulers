package schedulers;

import data.Process;
import data.WorkUnit;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Shady Atef on 3/18/16.
 * Copyrights Shadyoatef@gmail.com
 */
public abstract class AbstractScheduler {
    /**
     * @return Array of Required Process attributes
     */
    public abstract Process.Attributes[] getRequiredAttributes();

    /**
     * @return Human-Readable scheduler name
     */
    public abstract String getName();

    /**
     * @return A short description about the scheduler
     */
    public abstract String getDescription();

    /**
     * Scheduling the process according to the specified algorithm.
     *
     * @param processes The process list that needs to be scheduled
     * @return LinkedList of WorkUnits.
     */
    public abstract LinkedList<WorkUnit> apply(ArrayList<Process> processes);
}
