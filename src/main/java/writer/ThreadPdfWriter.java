package writer;

import java.util.concurrent.BlockingQueue;

import misc.Const;
import misc.PDF;

public class ThreadPdfWriter implements Runnable{
	private BlockingQueue<PDF> queue;
	private String file;
	
	
	public ThreadPdfWriter(BlockingQueue<PDF> queue, String file) {
		super();
		this.queue = queue;
		this.file = file;
	}
	
	@Override
	public void run() {
		
		PDF p =null;
		try {
			p = queue.take();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(!p.getName().equalsIgnoreCase("END")) {
			Writer.appendPdfTo(p, file);
			try {
				p = queue.take();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
