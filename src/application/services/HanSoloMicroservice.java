package bgu.spl.mics.application.services;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.TerminateBroudcast;


import java.util.Vector;


/**
 * HanSoloMicroservices is in charge of the handling {@link //AttackEvents}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link //AttackEvents}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class HanSoloMicroservice extends MicroService {

    public HanSoloMicroservice() {
        super("Han");
    }


    @Override
    protected void initialize() {
        subscribeEvent(AttackEvent.class, (AttackEvent event)->{

            Vector<Integer> neededEwoks=new Vector<>();
            for (Integer item:event.getAttack().getSerials())
                neededEwoks.add(item);
            ewoks.acquireAll(neededEwoks);
            try{

                Thread.sleep(event.getAttack().getDuration());
            }catch (InterruptedException e){}
            ewoks.releaseAll(neededEwoks,neededEwoks.size());
            complete(event, true);
            diary.setHanSoloFinish();
            diary.add();
        });


        subscribeBroadcast(TerminateBroudcast.class, (terminate) -> {
            terminate();
            diary.setHanSoloTerminate();
        });
    }

}



