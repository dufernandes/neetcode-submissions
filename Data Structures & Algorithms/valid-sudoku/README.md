# Valid Sudoku

**Difficulty:** Medium  
**Pattern:** Hash Map Lookup / Matrix Traversal  
**NeetCode:** [Valid Sudoku](https://neetcode.io/problems/valid-sudoku)  
**LeetCode:** [#36 – Valid Sudoku](https://leetcode.com/problems/valid-sudoku/)

---

## Problem

Determine if a 9×9 Sudoku board is valid. Only filled cells need to be validated according to these rules:

1. Each **row** must contain digits 1–9 with no repetition.
2. Each **column** must contain digits 1–9 with no repetition.
3. Each of the nine **3×3 sub-boxes** must contain digits 1–9 with no repetition.

Empty cells are represented by `'.'`.

---

## Key Insight

Validate all three constraints in **a single pass** through the board. For each cell `(i, j)`, check whether the digit has already been seen in:

- **Row `i`**
- **Column `j`**
- **Box `(i/3)*3 + (j/3)`** — the box index formula maps each cell to one of the nine 3×3 boxes (0–8)

Use three 9×9 boolean/int arrays as "seen" trackers — one dimension is the row/col/box index, the other is the digit (1–9 mapped to 0–8).

```
Board position (4, 7):
  row    = 4
  column = 7
  box    = (4/3)*3 + (7/3) = 1*3 + 2 = 5  (middle-right box)
```

---

## Solution

```java
class Solution {
    public boolean isValidSudoku(char[][] board) {
        int[][] rows    = new int[9][9];
        int[][] columns = new int[9][9];
        int[][] squares = new int[9][9];

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == '.') continue;

                int value = board[i][j] - '0';  // '1'-'9' → 1-9

                // Check and mark row
                if (rows[i][value - 1] == 1) return false;
                rows[i][value - 1] = 1;

                // Check and mark column
                if (columns[j][value - 1] == 1) return false;
                columns[j][value - 1] = 1;

                // Check and mark 3×3 box
                int boxIndex = (i / 3) * 3 + (j / 3);
                if (squares[boxIndex][value - 1] == 1) return false;
                squares[boxIndex][value - 1] = 1;
            }
        }
        return true;
    }
}
```

---

## Box Index Formula

The `(i/3)*3 + (j/3)` formula is the key to this problem. Integer division groups rows and columns into bands of 3:

```
(i/3) gives: rows 0-2 → 0,  rows 3-5 → 1,  rows 6-8 → 2
(j/3) gives: cols 0-2 → 0,  cols 3-5 → 1,  cols 6-8 → 2

Box layout:
  (0,0)=0  (0,1)=1  (0,2)=2
  (1,0)=3  (1,1)=4  (1,2)=5
  (2,0)=6  (2,1)=7  (2,2)=8
```

---

## Complexity

| | |
|---|---|
| **Time** | O(1) — the board is always 9×9, so exactly 81 cells |
| **Space** | O(1) — the tracker arrays are fixed size (3 × 9×9) |

In theory this is O(n²) for an n×n Sudoku, but for this problem the input is constant-sized.

---

## Interview Tips

- The box index formula `(i/3)*3 + (j/3)` is the one thing to memorize — derive it on the whiteboard by drawing the 3×3 grid of boxes.
- `board[i][j] - '0'` converts a char digit to its integer value — a classic Java char arithmetic trick.
- The three 2D arrays can alternatively be replaced with three arrays of `HashSet<Integer>`, which trades a tiny bit of performance for readability.
- This problem is purely about **constraint checking**, not solving — make sure to state that upfront.
