package com.cninsure.quartz.test;

import org.quartz.SchedulerException;

public class HelloSimpleJobFactory {
	static  HelloSimpleJob  helloSimpleJob = new HelloSimpleJob();
    
    public static HelloSimpleJob  createHelloSimpleJob(){
    	return helloSimpleJob;
    }
    
	public static void main(String[] args) {
		for (int i = 0; i < 100; i++) {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					HelloSimpleJob.j++;
					try {
						HelloSimpleJobFactory.createHelloSimpleJob().execute(String.valueOf(HelloSimpleJob.j), "group","trigger"+HelloSimpleJob.j);
					} catch (SchedulerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			t.start();
		}
	}
}
