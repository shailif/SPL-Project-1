package bgu.spl.mics.application.passiveObjects;
import java.util.Vector;

/**
 * Passive object representing the resource manager.
 * <p>
 * This class must be implemented as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */
public class Ewoks {

    private static class EwoksHolder {
        private static Ewoks instance = new Ewoks();
    }

    private Vector<Ewok>vectorE;
    private Object alllock=new Object();

    public static Ewoks getInstance(){
        return  Ewoks.EwoksHolder.instance;
    }

    public Ewoks() {
        vectorE=new Vector<>();}

    public Vector<Ewok> getVectorE() { return vectorE; }

    public void acquireAll(Vector<Integer> neededEwoks){
        synchronized (alllock) {

            for (int i = 0; i < neededEwoks.size(); i++) {
                if (this.getVectorE().elementAt(neededEwoks.elementAt(i) - 1).getAvailable()){
                    this.getVectorE().elementAt(neededEwoks.elementAt(i) - 1).acquire();

                }
                else {
                    releaseAll(neededEwoks,i);
                    i=-1;
                    try {
                        alllock.wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }
    public void releaseAll(Vector<Integer> neededEwoks,int index){
        synchronized (alllock) {
            for (int i = 0; i < index; i++)
                this.getVectorE().elementAt(neededEwoks.elementAt(i) - 1).release();
            alllock.notifyAll();
        }
    }


}

