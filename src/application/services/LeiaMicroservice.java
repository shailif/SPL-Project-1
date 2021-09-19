package bgu.spl.mics.application.services;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.TerminateBroudcast;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.MicroService;

/**
 * LeiaMicroservices Initialized with Attack objects, and sends them as  {@link //AttackEvents}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link //AttackEvents}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LeiaMicroservice extends MicroService {
    private Attack[] attacks;
    private Future[] futures;


    public LeiaMicroservice(Attack[] attacks) {
        super("Leia");
        this.attacks = attacks;
        futures=new Future[attacks.length];
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(TerminateBroudcast.class, (terminate) -> {
            terminate();
            diary.setLeiaTerminate();});

        try {//waits until the 2 micro-services subscribe
            Thread.sleep(1000);
        } catch (InterruptedException e) {}

        for (int i = 0; i < attacks.length; i++) {
            AttackEvent tmp = new AttackEvent(attacks[i]);
            futures[i] = sendEvent(tmp);
        }
        for (int i = 0; i < futures.length ; i++)
            futures[i].get();

        Future R = sendEvent(new DeactivationEvent());
        R.get();

        Future L = sendEvent(new BombDestroyerEvent());
        L.get();

        sendBroadcast(new TerminateBroudcast());
    }
}
