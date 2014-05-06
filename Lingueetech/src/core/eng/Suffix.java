/**
 * Certaine code dans cette classe issu du travail de Princeton University
 * http://algs4.cs.princeton.edu/63suffix/SuffixArray.java.html
 */
package core.eng;

public class Suffix implements Comparable<Suffix> {
	public final String text;
	public final int index;

	public Suffix(String text, int index) {
		this.text = text;
		this.index = index;
	}

	public int length() {
		return text.length() - index;
	}

	public char charAt(int i) {
		return text.charAt(index + i);
	}

	public int compareTo(Suffix that) {
		if (this == that)
			return 0; // optimization
		int N = Math.min(this.length(), that.length());
		for (int i = 0; i < N; i++) {
			if (this.charAt(i) < that.charAt(i))
				return -1;
			if (this.charAt(i) > that.charAt(i))
				return +1;
		}
		return this.length() - that.length();
	}
	/**
	 * 
	 * @param s le string à comparer
	 * @return le longeur de partie commune entre s et this
	 */
	public int lcp(String s){
		int N = Math.min(s.length(), this.length());
		for (int i = 0; i < N; i++) {
			if (s.charAt(i) != this.charAt(i))
				return i;
		}
		return N;
	}

	public String toString() {
		return text.substring(index);
	}
}