package resources;

 import java.io.File;
import java.util.List;
import java.util.concurrent.BlockingQueue;

 import misc.PDF;

 public class ThreadPDFReader implements Runnable{
	List<File> files = null;
	BlockingQueue<PDF> queue = null;

 	public ThreadPDFReader(List<File> files, BlockingQueue<PDF> queue) {
		super();
		this.files = files;
		this.queue = queue;
	}

 	@Override
	public void run() {
		for(File f : files) {
			PDF p = new PDF(f);
			try {
				queue.put(p);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ResourcesLoader.poisonQueue();
	}

 }