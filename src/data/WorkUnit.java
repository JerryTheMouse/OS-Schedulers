package data;

/**
 * Created by Shady Atef on 3/18/16.
 * Copyrights Shadyoatef@gmail.com
 */
public class WorkUnit {
    private int time;
    private Process p;

    public WorkUnit(int time, Process p) {
        this.time = time;
        this.p = p;
    }

    public int getTime() {
        return time;
    }

    public Process getP() {
        return p;
    }
}
