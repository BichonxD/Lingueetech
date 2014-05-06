/**
 * Certaine code dans cette classe issu du travail de Princeton University
 * http://algs4.cs.princeton.edu/63suffix/SuffixArray.java.html
 */
package core.eng;

import java.util.Arrays;

public class SuffixArray {
	private Suffix[] suffixes;
	private int[] lcp;

	/**
	 * Initializes a suffix array for the given <tt>text</tt> string.
	 * 
	 * @param text
	 *            the input string
	 */
	public SuffixArray(String text) {
		int N = text.length();
		this.suffixes = new Suffix[N];
		for (int i = 0; i < N; i++)
			suffixes[i] = new Suffix(text, i);
		Arrays.sort(suffixes);
		this.lcp = new int[N];
		lcp[0]=0;
		for (int i = 1; i < suffixes.length; i++) {
			lcp[i]=lcp(suffixes[i],suffixes[i-1]);
		}
	}

	/**
	 * Returns the length of the input string.
	 * 
	 * @return the length of the input string
	 */
	public int length() {
		return suffixes.length;
	}

	/**
	 * Returns the index into the original string of the <em>i</em>th smallest
	 * suffix. That is, <tt>text.substring(sa.index(i))</tt> is the <em>i</em>th
	 * smallest suffix.
	 * 
	 * @param i
	 *            an integer between 0 and <em>N</em>-1
	 * @return the index into the original string of the <em>i</em>th smallest
	 *         suffix
	 * @throws java.lang.IndexOutOfBoundsException
	 *             unless 0 &le; <em>i</em> &lt; <Em>N</em>
	 */
	public int index(int i) {
		if (i < 0 || i >= suffixes.length)
			throw new IndexOutOfBoundsException();
		return suffixes[i].index;
	}

	/**
	 * Returns the length of the longest common prefix of the <em>i</em>th
	 * smallest suffix and the <em>i</em>-1st smallest suffix.
	 * 
	 * @param i
	 *            an integer between 1 and <em>N</em>-1
	 * @return the length of the longest common prefix of the <em>i</em>th
	 *         smallest suffix and the <em>i</em>-1st smallest suffix.
	 * @throws java.lang.IndexOutOfBoundsException
	 *             unless 1 &le; <em>i</em> &lt; <em>N</em>
	 */
	public int lcp(int i) {
		return this.lcp[i];
	}

	// longest common prefix of s and t
	public static int lcp(Suffix s, Suffix t) {
		int N = Math.min(s.length(), t.length());
		for (int i = 0; i < N; i++) {
			if (s.charAt(i) != t.charAt(i))
				return i;
		}
		return N;
	}

	/**
	 * Returns the <em>i</em>th smallest suffix as a string.
	 * 
	 * @param i
	 *            the index
	 * @return the <em>i</em> smallest suffix as a string
	 * @throws java.lang.IndexOutOfBoundsException
	 *             unless 0 &le; <em>i</em> &lt; <Em>N</em>
	 */
	public String select(int i) {
		if (i < 0 || i >= suffixes.length)
			throw new IndexOutOfBoundsException();
		return suffixes[i].toString();
	}

	/**
	 * Returns the number of suffixes strictly less than the <tt>query</tt>
	 * string. We note that <tt>rank(select(i))</tt> equals <tt>i</tt> for each
	 * <tt>i</tt> between 0 and <em>N</em>-1.
	 * 
	 * @param query
	 *            the query string
	 * @return the number of suffixes strictly less than <tt>query</tt>
	 */
	public int rank(String query) {
		int lo = 0, hi = suffixes.length - 1;
		while (lo <= hi) {
			int mid = lo + (hi - lo) / 2;
			int cmp = compare(query, suffixes[mid]);
			if (cmp < 0)
				hi = mid - 1;
			else if (cmp > 0)
				lo = mid + 1;
			else
				return mid;
		}
		return lo;
	}

	// compare query string to suffix
	public static int compare(String query, Suffix suffix) {
		int N = Math.min(query.length(), suffix.length());
		for (int i = 0; i < N; i++) {
			if (query.charAt(i) < suffix.charAt(i))
				return -1;
			if (query.charAt(i) > suffix.charAt(i))
				return +1;
		}
		return 0;
	}
	

	/**
	 * @return the suffixes
	 */
	public Suffix[] getSuffixes() {
		return suffixes;
	}

	/**
	 * @return the lcp
	 */
	public int[] getLcp() {
		return lcp;
	}

	/**
	 * Unit tests the <tt>SuffixArray</tt> data type.
	 */
	/*
	public static void main(String[] args) {
		String input = "How do you do";
		String s = input.replaceAll("\\s+", " ").trim();
		SuffixArray suffix = new SuffixArray(s);

		// System.out.println("rank(" + args[0] + ") = " +
		// suffix.rank(args[0]));

		System.out.println("  i ind lcp rnk select");
		System.out.println("---------------------------");

		for (int i = 0; i < s.length(); i++) {
			int index = suffix.index(i);
			String ith = "\""
					+ s.substring(index, Math.min(index + 50, s.length()))
					+ "\"";
			assert s.substring(index).equals(suffix.select(i));
			int rank = suffix.rank(s.substring(index));
			if (i == 0) {
				System.out.printf("%3d %3d %3s %3d %s\n", i, index, "-", rank,
						ith);
			} else {
				System.out.printf("%3d %3d %3d %3d %s\n", i, index, suffix.lcp(i), rank,
						ith);
			}
		}
		System.out.println(suffix.rank("do you"));
		System.out.println(suffix.rank("to"));
	}
	*/

}