package com.asatria.lock.benchmark.implementations;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.asatria.lock.benchmark.Counter;

public class RWLock implements Counter
{
	private final ReadWriteLock rwlock = new ReentrantReadWriteLock();
	
	private final Lock rlock = rwlock.readLock();
	private final Lock wlock = rwlock.writeLock();
	
	private long counter;
	
	public long get()
	{
		try
		{
			rlock.lock();		
			return counter;
		}
		finally
		{
			rlock.unlock();
		}
	}
	
	public void increment()
	{
		try
		{
			wlock.lock();		
			++counter;
		}
		finally
		{
			wlock.unlock();
		}
	}
}
