class Solution {
    public boolean isValidSudoku(char[][] board) {
        int[] rows = new int[9];
        int[][] columns = new int[9][9];
        int[][] squares = new int[9][9];

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) { 
                if (board[i][j] == '.') continue;
                int value = board[i][j] - '0';
                
                // process row
                if (rows[value - 1] == 1) return false;
                rows[value - 1] = 1;

                // process column
                if (columns[j][value - 1] == 1) return false;
                columns[j][value - 1] = 1;

                // process squares
                if (squares[(i / 3) * 3 + (j / 3)][value - 1] == 1) return false;
                squares[(i / 3) * 3 + (j / 3)][value - 1] = 1;

            }
            rows = new int[9];
        }
        return true;
    }
}
