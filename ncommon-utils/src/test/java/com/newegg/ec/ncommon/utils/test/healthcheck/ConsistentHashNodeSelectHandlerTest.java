package com.newegg.ec.ncommon.utils.test.healthcheck;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.newegg.ec.ncommon.utils.healthcheck.ConsistentHashNodeSelectHandler;
import com.newegg.ec.ncommon.utils.test.healthcheck.TeskHealthCkecker.TestServerTransaction;

/**
 * 
 * @author lz31
 *
 */
public class ConsistentHashNodeSelectHandlerTest {
	
	@Test
	public void test() {
		
		for(int i = 0; i < 500; i ++) {
			test(i + "");
		}
	}
	
	private void test(String payload) {
		
		List<TestServerTransaction> list = new ArrayList<>();
		int size = 3;
		for(int i = 0; i < size; i ++) {
			list.add(new TestServerTransaction(i).turnOn());
		}
		ConsistentHashNodeSelectHandler<TestServerTransaction> handler = new ConsistentHashNodeSelectHandler<>(list);
		TestServerTransaction t1 = handler.select(list, payload);
		TestServerTransaction t2 = handler.select(list, payload);
		TestServerTransaction t3 = handler.select(list, payload);
		Assert.assertEquals(t1, t2);
		Assert.assertEquals(t1, t3);
		
		int id = t1.getI();
		int removeId  = (id == size -1)?0 : id + 1;
		list.remove(removeId);
		TestServerTransaction t4 = handler.select(list, payload);
		Assert.assertEquals(id, t4.getI());
	}
}
