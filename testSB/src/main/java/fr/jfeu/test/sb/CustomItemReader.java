package fr.jfeu.test.sb;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

public class CustomItemReader implements ItemReader {
	
	private List<String> liste;
	
	private Iterator<String> iterator;
	
	public CustomItemReader() {
		liste = new ArrayList<String>();
		for(int i=0;i<100;i++) {
			liste.add("test"+i);
		}
		
	}

	public CustomPojo read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		
		if(getIterator().hasNext()) {
			CustomPojo cp = new CustomPojo();
			cp.setStr(getIterator().next());
			return cp;
		}
		
		return null;
	}

	
	public Iterator<String> getIterator(){
		if(iterator == null) {
		iterator = liste.iterator();
		}
		
		return iterator;
	}
}
