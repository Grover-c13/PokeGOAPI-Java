package com.pokegoapi.main;

public class BlockingCallback implements PokemonCallback {
	private final Object lock = new Object();
	private Exception exception;

	@Override
	public void onCompleted(Exception e) {
		this.exception = e;
		synchronized (this.lock) {
			this.lock.notify();
		}
	}

	/**
	 * Blocks the current thread until this callback is called
	 */
	public void block() throws Exception {
		synchronized (this.lock) {
			this.lock.wait();
		}
		if (this.exception != null) {
			throw this.exception;
		}
	}

	/**
	 * Wraps the given callback in a BlockingCallback, allowing block functionality.
	 * If blocking is required, make sure to give this wrapper to all methods, and not the original callback!
	 * @param callback to wrap
	 * @return a new blocking callback wrapper
	 */
	public static BlockingCallback wrap(PokemonCallback callback) {
		return new Wrapper(callback);
	}

	public static class Wrapper extends BlockingCallback {
		private final PokemonCallback callback;

		private Wrapper(PokemonCallback callback) {
			this.callback = callback;
		}

		@Override
		public void onCompleted(Exception e) {
			this.callback.onCompleted(e);
			super.onCompleted(e);
		}
	}
}
