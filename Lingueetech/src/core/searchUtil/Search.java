package core.searchUtil;

import java.util.TreeSet;

import core.eng.Index;

public abstract class Search {

	/* ATTRIBUTES */
	protected Index index;

	/* CONSTRUCTOR */
	public Search(Index index) {
		this.index = index;
	}

	/* METHODS */
	public abstract TreeSet<Integer> search(String keywords);
}
