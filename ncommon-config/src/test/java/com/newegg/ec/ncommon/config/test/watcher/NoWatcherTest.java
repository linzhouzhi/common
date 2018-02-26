package com.newegg.ec.ncommon.config.test.watcher;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.Test;

import com.newegg.ec.ncommon.config.LocalConfig;
import com.newegg.ec.ncommon.config.listener.AbstractConfigListener;
import com.newegg.ec.ncommon.config.schema.InputStreamSchema;
import com.newegg.ec.ncommon.config.schema.InputStreamSchemaFactory;
import com.newegg.ec.ncommon.config.watcher.NoWatcher;
import com.newegg.ec.ncommon.config.watcher.Watcher;

import junit.framework.Assert;

public class NoWatcherTest {
	
	private static final InputStreamSchema<Properties> PROPERTIES_SCHEMA = InputStreamSchemaFactory.createPropertiesSchema(StandardCharsets.UTF_8);
	private static final AbstractConfigListener<LocalConfig<?>> listener = new LoggerConfigListener2<>();
	
	private static final File DIR = new File("aaXml").getParentFile();
	private static final File PATH1 = new File(DIR, "cc1.properties");
	private static final File PATH2 = new File(DIR, "cc2.properties");
	private static final File PATH3 = new File(DIR, "cc3.properties");
	private static final File PATH4 = new File(DIR, "cc4.properties");
	private static final File PATH5 = new File(DIR, "cc5.properties");
	private static final File PATH6 = new File(DIR, "cc6.properties");
	
	private static final Watcher<LocalConfig<?>> watcher = new NoWatcher<>();
	private static LocalConfig<Properties> CONFIG1 = null;
	private static LocalConfig<Properties> CONFIG2 = null;
	private static LocalConfig<Properties> CONFIG3 = null;
	private static LocalConfig<Properties> CONFIG4 = null;
	private static LocalConfig<Properties> CONFIG5 = null;
	private static LocalConfig<Properties> CONFIG6 = null;
	
	static {
		try {
			FileUtils.createNewFile(PATH1, new String("").getBytes());
			FileUtils.createNewFile(PATH2, new String("").getBytes());
			FileUtils.createNewFile(PATH3, new String("").getBytes());
			FileUtils.createNewFile(PATH4, new String("").getBytes());
			FileUtils.createNewFile(PATH5, new String("").getBytes());
			FileUtils.createNewFile(PATH6, new String("").getBytes());
			
			/**
			 *  1------>2--------->4---------->6
			 *          |
			 *          |
			 *  	    3--------->5 
			 * 
			 */
			CONFIG1 = new LocalConfig<>(PATH1.getPath(), PROPERTIES_SCHEMA);
			CONFIG2 = new LocalConfig<>(PATH2.getPath(), PROPERTIES_SCHEMA, CONFIG1);
			CONFIG3 = new LocalConfig<>(PATH3.getPath(), PROPERTIES_SCHEMA, CONFIG1);
			CONFIG4 = new LocalConfig<>(PATH4.getPath(), PROPERTIES_SCHEMA, CONFIG2);
			CONFIG5 = new LocalConfig<>(PATH5.getPath(), PROPERTIES_SCHEMA, CONFIG3);
			CONFIG6 = new LocalConfig<>(PATH6.getPath(), PROPERTIES_SCHEMA, CONFIG4);
			
			watcher.register(CONFIG1, listener);
			watcher.register(CONFIG2, listener);
			watcher.register(CONFIG3, listener);
			watcher.register(CONFIG4, listener);
			watcher.register(CONFIG5, listener);
			watcher.register(CONFIG6, listener);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Test
	public void test() throws Exception {
		
		Assert.assertNotNull(CONFIG1.getInstance());
		FileUtils.createNewFile(PATH1, new String("aa=111").getBytes());
		Assert.assertNull(CONFIG1.getInstance().getProperty("aa"));
		Assert.assertNull(CONFIG2.getInstance().getProperty("aa"));
		Assert.assertNull(CONFIG3.getInstance().getProperty("aa"));
		Assert.assertNull(CONFIG4.getInstance().getProperty("aa"));
		Assert.assertNull(CONFIG5.getInstance().getProperty("aa"));
		Assert.assertNull(CONFIG6.getInstance().getProperty("aa"));
		
		FileUtils.createNewFile(PATH4, new String("aa=222").getBytes());
		Assert.assertNull(CONFIG1.getInstance().getProperty("aa"));
		Assert.assertNull(CONFIG2.getInstance().getProperty("aa"));
		Assert.assertNull(CONFIG3.getInstance().getProperty("aa"));
		Assert.assertNull(CONFIG4.getInstance().getProperty("aa"));
		Assert.assertNull(CONFIG5.getInstance().getProperty("aa"));
		Assert.assertNull(CONFIG6.getInstance().getProperty("aa"));
		
		FileUtils.createNewFile(PATH1, new String("aa=333").getBytes());
		Assert.assertNull(CONFIG1.getInstance().getProperty("aa"));
		Assert.assertNull(CONFIG2.getInstance().getProperty("aa"));
		Assert.assertNull(CONFIG3.getInstance().getProperty("aa"));
		Assert.assertNull(CONFIG4.getInstance().getProperty("aa"));
		Assert.assertNull(CONFIG5.getInstance().getProperty("aa"));
		Assert.assertNull(CONFIG6.getInstance().getProperty("aa"));
		
		FileUtils.createNewFile(PATH4, new String("").getBytes());
		Assert.assertNull(CONFIG1.getInstance().getProperty("aa"));
		Assert.assertNull(CONFIG2.getInstance().getProperty("aa"));
		Assert.assertNull(CONFIG3.getInstance().getProperty("aa"));
		Assert.assertNull(CONFIG4.getInstance().getProperty("aa"));
		Assert.assertNull(CONFIG5.getInstance().getProperty("aa"));
		Assert.assertNull(CONFIG6.getInstance().getProperty("aa"));
		
		watcher.unregister(CONFIG4);
		FileUtils.createNewFile(PATH4, new String("aa=rr").getBytes());
		Assert.assertNull(CONFIG1.getInstance().getProperty("aa"));
		Assert.assertNull(CONFIG2.getInstance().getProperty("aa"));
		Assert.assertNull(CONFIG3.getInstance().getProperty("aa"));
		Assert.assertNull(CONFIG4.getInstance().getProperty("aa"));
		Assert.assertNull(CONFIG5.getInstance().getProperty("aa"));
		Assert.assertNull(CONFIG6.getInstance().getProperty("aa"));
		
		FileUtils.createNewFile(PATH1, new String("aa=444").getBytes());
		Assert.assertNull(CONFIG1.getInstance().getProperty("aa"));
		Assert.assertNull(CONFIG2.getInstance().getProperty("aa"));
		Assert.assertNull(CONFIG3.getInstance().getProperty("aa"));
		Assert.assertNull(CONFIG4.getInstance().getProperty("aa"));
		Assert.assertNull(CONFIG5.getInstance().getProperty("aa"));
		Assert.assertNull(CONFIG6.getInstance().getProperty("aa"));
		
		
		watcher.unregister(CONFIG1);
		FileUtils.createNewFile(PATH1, new String("aa=pp").getBytes());
		Assert.assertNull(CONFIG1.getInstance().getProperty("aa"));
		Assert.assertNull(CONFIG2.getInstance().getProperty("aa"));
		Assert.assertNull(CONFIG3.getInstance().getProperty("aa"));
		Assert.assertNull(CONFIG4.getInstance().getProperty("aa"));
		Assert.assertNull(CONFIG5.getInstance().getProperty("aa"));
		Assert.assertNull(CONFIG6.getInstance().getProperty("aa"));
		
		FileUtils.createNewFile(PATH6, new String("aa=555").getBytes());
		Assert.assertNull(CONFIG1.getInstance().getProperty("aa"));
		Assert.assertNull(CONFIG2.getInstance().getProperty("aa"));
		Assert.assertNull(CONFIG3.getInstance().getProperty("aa"));
		Assert.assertNull(CONFIG4.getInstance().getProperty("aa"));
		Assert.assertNull(CONFIG5.getInstance().getProperty("aa"));
		Assert.assertNull(CONFIG6.getInstance().getProperty("aa"));
	}
	
	@AfterClass
	public static void after() throws Exception {
		watcher.close();
		PATH1.delete();
		PATH2.delete();
		PATH3.delete();
		PATH4.delete();
		PATH5.delete();
		PATH6.delete();
	}
}
