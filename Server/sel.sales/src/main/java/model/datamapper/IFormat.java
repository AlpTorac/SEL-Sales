package model.datamapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface IFormat {
	Pattern getPattern();
	
	default String getMatch(String textToSearch) {
		Matcher m = this.getPattern().matcher(textToSearch);
		if (m.find()) {
			return m.group();
		}
		return null;
	}
	
	default Collection<String> getMatches(String textToSearch) {
		Collection<String> col = new ArrayList<String>();
		Matcher m = this.getPattern().matcher(textToSearch);
		while (m.find()) {
			String match = m.group();
//			System.out.println("matched: " + match);
			col.add(match);
		}
		return col;
	}
}
