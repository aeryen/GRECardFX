package com.aeryen.GRECardFX;

/**
 * Created by aeryen on 4/5/2016.
 */
public class Word {
	String english = null;
	String chinese = null;
	public Word(String initStr) {
		String[] result = initStr.split("[ \t]+");
		if(result.length > 2) {
			boolean allEndWithSC = true;

			String combineChinese = "";

			for (int i = 1; i < result.length - 1; i++) {
				if (!result[i].endsWith("；"))
					allEndWithSC = false;
				combineChinese += result[i];
			}
			combineChinese += result[result.length - 1];

			if (!allEndWithSC && result.length != 2) {
				throw new RuntimeException("Error on line: [" + initStr + "]");
			} else {
				english = result[0];
				chinese = combineChinese;
			}
		} else {
			english = result[0];
			chinese = result[1];
		}
	}

	public void multilinePrint() {
		System.out.println(english);
		System.out.println("\t" + chinese);
	}

	public String toString() {
		return this.english + " " + this.chinese;
	}
}
