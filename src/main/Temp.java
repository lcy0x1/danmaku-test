package main;

public class Temp {

	public static void main(String[] args) {
		int n = 10, ulrow = 2, ulcol = 2, lrrow = 6, lrcol = 8;
		int[][] rect = new int[n][n];

		for (int x = ulrow; x <= lrrow; x++) {
			rect[x][ulcol] = 1;
			rect[x][lrcol] = 1;
		}
		for (int y = ulcol; y <= lrcol; y++) {
			rect[ulrow][y] = 1;
			rect[lrrow][y] = 1;
		}

		for (int x = 0; x < n; x++) {
			for (int y = 0; y < n; y++)
				System.out.print(rect[x][y] == 0 ? "*" : " ");
			System.out.println();
		}
	}

}
