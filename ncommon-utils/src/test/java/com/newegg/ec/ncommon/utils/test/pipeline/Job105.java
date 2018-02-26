package com.newegg.ec.ncommon.utils.test.pipeline;

import org.junit.Assert;

import com.newegg.ec.ncommon.utils.pipeline.Job;
import com.newegg.ec.ncommon.utils.pipeline.JobContext;
import com.newegg.ec.ncommon.utils.pipeline.JobContexts;

public class Job105 implements Job<String> {

	@Override
	public String jobId() {
		return "105";
	}

	@Override
	public String execute(JobContexts jobContexts) throws Exception {
		System.out.println(jobId() + " start");
		JobContext<?> job101Context = jobContexts.findJobContext("104");
		if(job101Context.getException() == null)
			System.out.println(jobId() + " print job 104 result:" + job101Context.getResult());
		job101Context = jobContexts.findJobContext("102");
		if(job101Context.getException() == null)
			System.out.println(jobId() + " print job 102 result:" + job101Context.getResult());
		
		job101Context = jobContexts.findJobContext("103");
		Assert.assertTrue(job101Context.getException() != null);
		System.out.println(jobId() + " end");
		return "105 result";
	}
}
