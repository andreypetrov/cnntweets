package com.petrov.cnntweets;

import java.util.List;

/**
 * String Comparator utility. Non-instantiable.
 */
public class StringComparatorUtil {
	private static final String EMPTY_STRING = "";

	private StringComparatorUtil() {
	}

	/**
	 * Find the two strings with longest common substring in a list and produce a user friendly message with them and their substring.
	 * 
	 * @param list
	 *            List of tweet strings
	 * @return message to the user, in which we have the two strings and the substring.
	 */
	public static String findLongestCommonSubstring(List<String> list) {
		String longestCommonSubstring = EMPTY_STRING;
		String firstParent = EMPTY_STRING;
		String secondParent = EMPTY_STRING;

//		int newLongestSubstringNumber = 0;

		// Compare every string to all strings after it
		for (int i = 0; i < list.size(); i++) {
			for (int j = i + 1; j < list.size(); j++) {
				String currentLongestCommonSubstring = findLongestCommonSubstring(list.get(i), list.get(j));
				if (currentLongestCommonSubstring.length() > longestCommonSubstring.length()) {
					longestCommonSubstring = currentLongestCommonSubstring;
					firstParent = list.get(i);
					secondParent = list.get(j);
					
					//For debugging only
//					System.out.println(newLongestSubstringNumber++);
//					System.out.println(firstParent);
//					System.out.println(secondParent);
//					System.out.println(longestCommonSubstring);
				}
			}
		}
		return "The two strings with longest common substring are\n[" + firstParent + "]\nand\n[" + secondParent
				+ "]\nTheir longest commong substring is\n[" + longestCommonSubstring + "]";
	}

	
	/**
	 * Finds one of the Longest Common Substrings (LCS) between string1 and string2. If there is more than one, the rest are ignored.
	 * Time complexity O(nm), where n = string1.length, m = string2.length
	 * Space complexity O(m), where m = string2.length
	 * 
	 * @param string1
	 *            first string parameter
	 * @param string2
	 *            second string parameter
	 * @return one of the Longest Common Substrings
	 */
	private static String findLongestCommonSubstring(String string1, String string2) {
		// We can use only the current and the previous row for memory optimization 
		// instead of int[][] matrix = new int[string1.length()][string2.length()];
		
		//Dummy initialization, to suppress error when used during first outer-most loop iteration
		//on subsequent iterations it will have the currentRow reference passed to it		
		int[] previousRow = null; 
		
		int[] currentRow = new int[string2.length()];

		int lcsLength = 0; // the length of the LCS
		int start = 0; // where the LCS starts in string1

		for (int i = 0; i < string1.length(); i++) {
			for (int j = 0; j < string2.length(); j++) {
				if (string1.charAt(i) == string2.charAt(j)) {
					if (i == 0 || j == 0) { // base case - first row or column
						currentRow[j] = 1;
					} else {
						currentRow[j] = previousRow[j - 1] + 1;
					}
					// Checks if we have a longer LCS. Does not keep track of other LCS with equal length, thus strictly >, not >=
					// Only one LCS is enough for our task.
					if (currentRow[j] > lcsLength) {
						lcsLength = currentRow[j];
						start = i - lcsLength + 1; // get the new starting index of the LCS
					}
				} else { //not matching - assign zero weight
					currentRow[j] = 0;
				}
			}		
			//Advance to the next row
			previousRow = currentRow;
			currentRow = new int[string2.length()];
		}
		return string1.substring(start, (start + lcsLength));
	}

}
