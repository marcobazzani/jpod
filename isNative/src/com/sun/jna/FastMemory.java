package com.sun.jna;

public class FastMemory extends Pointer {

	protected long size; // Size of the malloc'ed space

	public FastMemory() {
		super();
	}

	public FastMemory(int size) {
		super(Memory.malloc(size));
		clear(size);
	}

	@Override
	protected void finalize() {
		Memory.free(peer);
		peer = 0;
	}

}
