package bgu.spl.mics;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */

public class MessageBusImpl implements MessageBus {

	private static class MessageBusHolder {
		private static MessageBus instance = new MessageBusImpl();
	}


	private ConcurrentHashMap <MicroService,Queue<Message>> microServiceMap;
	private  ConcurrentHashMap <Class<? extends Message> ,Vector<MicroService>> MessagesMap;
	private  ConcurrentHashMap <Event ,Future > futureMap;
	private Object lock;
	private Object eventLock;
	private Object broadcastLock;


	private MessageBusImpl(){
		microServiceMap=new ConcurrentHashMap();
		MessagesMap=new ConcurrentHashMap();
		futureMap= new ConcurrentHashMap();
		lock=new Object();
		eventLock=new Object();
		broadcastLock=new Object();
	}

	public static MessageBus getInstance(){
		return  MessageBusHolder.instance;
	}

	@Override
	public  <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		synchronized (eventLock) {
			if (!MessagesMap.containsKey(type)) {
				MessagesMap.put(type, new Vector<>());
			}
		}
		MessagesMap.get(type).add(m);
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
			synchronized (broadcastLock) {
				if (!MessagesMap.containsKey(type)) {
					MessagesMap.put(type, new Vector<>());
				}
			}
		MessagesMap.get(type).add(m);

	}

	@Override @SuppressWarnings("unchecked")
	public <T> void complete(Event<T> e, T result) {
			futureMap.get(e).resolve(result);
	}
	@Override
	public void sendBroadcast(Broadcast b) {
		 synchronized (lock) {
			 Vector<MicroService> tmp=new Vector<>();
		 	for (int i=0; i<MessagesMap.get(b.getClass()).size(); i++) {
				tmp.add(MessagesMap.get(b.getClass()).elementAt(i));
			}
		 	for (int i=0; i<MessagesMap.get(b.getClass()).size(); i++) {
				microServiceMap.get(tmp.elementAt(i)).add(b);
			}
			lock.notifyAll();
		}

	}


	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		if ((!MessagesMap.containsKey(e.getClass())) || MessagesMap.get(e.getClass()).isEmpty())
			return null;
		synchronized (lock) {
			MicroService tmp =  MessagesMap.get(e.getClass()).remove(0);
			microServiceMap.get(tmp).add(e);
			MessagesMap.get(e.getClass()).add(tmp);
			futureMap.put(e, new Future());
			lock.notifyAll();
			return futureMap.get(e);
		}
	}

	@Override
	public void register(MicroService m) {
		Queue<Message> a=new <Message> LinkedList();
		microServiceMap.put(m,a);
	}

	@Override
	public void unregister(MicroService m) {
		while (!microServiceMap.get(m).isEmpty()){
			Message mes=microServiceMap.get(m).remove();
			if(MessagesMap.get(mes.getClass()).contains(m))
				MessagesMap.get(mes.getClass()).remove(m);
			futureMap.remove(mes);
			}
		microServiceMap.remove(m);
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		if (!microServiceMap.containsKey(m))
			throw new IllegalStateException();
		synchronized (lock) {
			while (microServiceMap.get(m).isEmpty())
				lock.wait();
			return microServiceMap.get(m).remove();
		}
	}
}

