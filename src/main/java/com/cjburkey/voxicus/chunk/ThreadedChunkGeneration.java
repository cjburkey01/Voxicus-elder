package com.cjburkey.voxicus.chunk;

import java.util.Stack;
import org.joml.Vector3i;
import com.cjburkey.voxicus.core.Debug;

public class ThreadedChunkGeneration {
	
	private static final long NANO = 1000000000L;
	
	public static int updatesPerSecond = 100;
	private static Stack<ChunkActionPair> generationQueue = new Stack<>();
	private static final Thread generationThread = new Thread(() -> doGenLoop());
	private static boolean started = false;
	private static boolean killed = false;
	
	public static void startSystem() {
		if (started) {
			Debug.warn("Threaded generation system already started");
			return;
		}
		if (killed) {
			Debug.warn("Threaded generation system was already destroyed");
			return;
		}
		Debug.log("Threaded generation system starting");
		started = true;
		generationThread.setName("ThreadedGenerationSystem");
		generationThread.start();
	}
	
	public static void stopSystem() {
		started = false;
	}
	
	public static boolean getHasStarted() {
		return started && !killed;
	}
	
	public static void addChunk(IChunkHandler world, Vector3i position, Runnable onDone) {
		generationQueue.push(new ChunkActionPair(world.getChunk(position), onDone));
	}
	
	private static final void doGenLoop() {
		long currentTime = 0L;
		long nextTime = 0L;
		Debug.log("Threaded generation system started");
		while (true) {
			if (!started) {
				killed = true;
				generationQueue.clear();
				break;
			}
			
			// Timing to prevent freezing EVERYTHING
			currentTime = System.nanoTime();
			if (currentTime < nextTime) {
				continue;
			}
			nextTime = currentTime + NANO / updatesPerSecond;
			
			if (generationQueue.isEmpty()) {
				continue;
			}
			ChunkActionPair cap = generationQueue.pop();
			if (cap == null) {
				continue;
			}
			IChunkHandler w = cap.chunk.getWorld();
			w.getGenerator().generate(w, cap.chunk);
			w.addSynchronousAction(cap.onDone);
		}
		Debug.log("Cleaning up threaded generation system");
	}
	
	private static class ChunkActionPair {
		
		public final Chunk chunk;
		public final Runnable onDone;
		
		public ChunkActionPair(Chunk chunk, Runnable onDone) {
			this.chunk = chunk;
			this.onDone = onDone;
		}
		
	}
	
}