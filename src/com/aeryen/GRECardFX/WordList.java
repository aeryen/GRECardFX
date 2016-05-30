package com.aeryen.GRECardFX;

import java.io.*;
import java.util.ArrayList;

import static java.lang.System.exit;

/**
 * Created by aeryen on 4/5/2016.
 */
public class WordList {
	ArrayList<Word> CompleteList = null;
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
			CompleteList = new ArrayList<Word>(120);
			while ((thisLine = br.readLine()) != null) {
//				System.out.println(thisLine);
				Word w = new Word(thisLine);
				CompleteList.add(w);

				w.multilinePrint();
			}

			System.out.println("A total of: " + CompleteList.size() + " is loaded from toDoList: " + dataFile.getName() + ".");

			toDoList.addAll(CompleteList);
			doneList = new ArrayList<>();
		} catch(IOException e) {
			e.printStackTrace();
			exit(-1);
		}
	}

	public boolean checkRemainTodo() {
		if(this.toDoList.size() >= 1) {
			return true;
		} else {
			this.toDoList = doneList;
			this.doneList = new ArrayList<>();
			return false;
		}
	}

	public void putInToDo(Word w) {
		toDoList.add(w);
	}

	public void putInDone(Word w) {
		toDoList.remove(w);
		doneList.add(w);
	}
}
