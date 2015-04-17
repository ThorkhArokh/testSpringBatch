package fr.jfeu.test.sb;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemProcessor;

public class CustomItemProcessor implements ItemProcessor<CustomPojo, CustomPojo> {
	
	static Logger logger = Logger.getLogger(CustomItemProcessor.class);

	public CustomPojo process(final CustomPojo str) throws Exception {
		logger.info("Processing...");
		CustomPojo str2 = new CustomPojo();
		CustomPojo strTmp = str;
		str2.setStr(strTmp.getStr()+"EDIT");
		return str2;
	}
}
