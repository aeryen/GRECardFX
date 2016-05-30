package com.aeryen.GRECardFX;

import java.io.*;
import java.util.ArrayList;

import static java.lang.System.exit;

/**
 * Created by aeryen on 4/5/2016.
 */
public class WordList {
	ArrayList<Word> toDoList = null;
	ArrayList<Word> doneList = null;

	public WordList(File dataFile) throws FileNotFoundException {
		String thisLine;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(dataFile), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		try {
			toDoList = new ArrayList<Word>(120);
			while ((thisLine = br.readLine()) != null) {
//				System.out.println(thisLine);
				Word w = new Word(thisLine);
				toDoList.add(w);

				w.multilinePrint();
			}

			System.out.println("A total of: " + toDoList.size() + " is loaded from toDoList: " + dataFile.getName() + ".");

			doneList = new ArrayList<>();
		} catch(IOException e) {
			e.printStackTrace();
			exit(-1);
		}
	}

	public boolean checkHaveRemain() {
		if(this.toDoList.size() >= 1) {
			return true;
		} else {
			this.toDoList = doneList;
			this.doneList = new ArrayList<>();
			return false;
		}
	}

	public void putInToDo(Word w) {
//		toDoList.add();
	}

	public void putInDone(Word w) {
		toDoList.remove(w);
		doneList.add(w);
	}
}
