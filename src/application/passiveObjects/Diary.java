package bgu.spl.mics.application.passiveObjects;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Passive data-object representing a Diary - in which the flow of the battle is recorded.
 * We are going to compare your recordings with the expected recordings, and make sure that your output makes sense.
 * <p>
 * Do not add to this class nothing but a single constructor, getters and setters.
 */
public class Diary {
    private static class DiaryHolder {
        private static Diary instance = new Diary();
    }


    private AtomicInteger totalAttacks;
    private long C3POFinish;
    private long HanSoloFinish;
    private long R2D2Deactivate;
    private long LeiaTerminate;
    private long HanSoloTerminate;
    private long C3POTerminate;
    private  long R2D2Terminate;
    private long LandoTerminate ;

    public static Diary getInstance(){
        return  Diary.DiaryHolder.instance;
    }

    private Diary(){
        totalAttacks=new AtomicInteger(0);
        C3POFinish=0;
        HanSoloFinish=0;
        R2D2Deactivate=0;
        LandoTerminate=0;
        HanSoloTerminate=0;
        C3POTerminate=0;
        R2D2Deactivate=0;
        LandoTerminate=0;
    }


    public void add() {
        int val;
        do {
            val = totalAttacks.get();
        }while (!totalAttacks.compareAndSet(val, val + 1));
    }
    //settings
    public void setHanSoloFinish(){this.HanSoloFinish=System.currentTimeMillis();}
    public void setC3POFinish(){this.C3POFinish=System.currentTimeMillis();}
    public void setR2D2Deactivate(){this.R2D2Deactivate=System.currentTimeMillis();}
    public void setLeiaTerminate(){this.LeiaTerminate=System.currentTimeMillis();}
    public void setHanSoloTerminate(){this.HanSoloTerminate=System.currentTimeMillis();}
    public void setC3POTerminate(){this.C3POTerminate=System.currentTimeMillis();}
    public void setR2D2Terminate(){this.R2D2Terminate=System.currentTimeMillis();}
    public void setLandoTerminate(){this.LandoTerminate=System.currentTimeMillis();}


    //getters
    public long getHanSoloFinish(){return HanSoloFinish;}
    public long getC3POFinish(){return C3POFinish;}
    public long getR2D2Deactivate(){return R2D2Deactivate;}
    public long getLeiaTerminate(){return LeiaTerminate;}
    public long getHanSoloTerminate(){return HanSoloTerminate;}
    public long getC3POTerminate(){return C3POTerminate;}
    public long getR2D2Terminate(){return R2D2Terminate;}
    public long getLandoTerminate(){return LandoTerminate;}

}

    