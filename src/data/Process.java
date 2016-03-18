package data;

/**
 * Created by Shady Atef on 3/18/16.
 * Copyrights Shadyoatef@gmail.com
 */
public class Process {
    private static int nextID = 1;

    // we need an automated identifier
    private int id;
    private int priority;
    private int arrival;
    private int duration;
    private int finish;


    public Process(int arrival, int duration) {
        this(arrival,duration,0);

    }

    public Process(int arrival, int duration, int priority) {

        if (duration < 1 || arrival < 0 || priority < 0)
            throw new IllegalArgumentException();

        this.id = nextID;
        nextID++;
        this.priority = priority;
        this.arrival = arrival;
        this.duration = duration;
    }

    public int getFinish() {
        return finish;
    }

    public void setFinish(int finish) {
        if (finish < (this.arrival + this.duration))
            throw new IllegalArgumentException("A process can't finish before its arrival & execution");
        this.finish = finish;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        if (duration < 1)
            throw new IllegalArgumentException("A process duration can't be less than one time unit");

        this.duration = duration;
    }

    public int getArrival() {
        return arrival;
    }

    public void setArrival(int arrival) {
        if (arrival < 0)
            throw new IllegalArgumentException("A process arrival can't be negative");

        this.arrival = arrival;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        if (priority < 1)
            throw new IllegalArgumentException("A process priority can't be negative");

        this.priority = priority;
    }

    public int getWaitingTime() {

        return finish - arrival - duration;
    }

    public int getId() {
        return id;
    }

    public enum Attributes {
        PRIORITY, ARRIVAL, DURATION
    }
}
