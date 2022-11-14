package com.asatria.lock.benchmark.implementations;

import com.asatria.lock.benchmark.Counter;

public class Synchronized implements Counter
{
	private final Object lock = new Object();
	
	private int counter;
	
	public long get()
	{
		synchronized (lock)
		{
			return counter;
		}
	}
	
	public void increment()
	{
		synchronized (lock)
		{
			++counter;
		}
	}
}
