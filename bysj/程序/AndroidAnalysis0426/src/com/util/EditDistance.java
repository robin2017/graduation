package com.util;

/**
 * @author Robin
 * @date 2016年4月26日 下午4:17:15
 * @version 1.0
 */

public class EditDistance {
	public static void main(String[] args){
		String str1="intention";
		String str2="execution";
		System.out.println(minDistance(str1,str2));
	}
	
	
	private static final int WEIGHT_SUBSTITUTION = 2;

	/*
	 * 时间空间复杂度都为 nm 标准的动态规划算法
	 * 18ms,击败14%
	 */
	public static int minDistance(String str1, String str2) {
		int m = str1.length();
		int n = str2.length();
		int[][] table = new int[m + 1][n + 1];
		for (int i = 0; i <= m; i++)
			table[i][0] = i;
		for (int j = 0; j <= n; j++)
			table[0][j] = j;
		for (int i = 1; i <= m; i++)
			for (int j = 1; j <= n; j++) {
				table[i][j]=minThree(
						table[i - 1][j] + 1,
						table[i][j - 1] + 1,
						table[i - 1][j - 1]
								+ (str1.charAt(i - 1) == str2.charAt(j - 1) ? 0:WEIGHT_SUBSTITUTION
										));
			}
		return table[m][n];
	}

	private static int minThree(int a, int b, int c) {
		return a < b ? (a < c ? a : c) : (b < c ? b : c);
	}
}
