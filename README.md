# NeetCode Solutions — Java

> **Language:** Java 21  
> **Source:** [NeetCode 150](https://neetcode.io/practice) · auto-synced via GitHub integration  
> **Author:** [@dufernandes](https://github.com/dufernandes)

---

## Solutions

Problems are ordered following the [NeetCode 150 Roadmap](https://neetcode.io/practice).

### 📦 Arrays & Hashing

| Problem | Difficulty | Pattern | My README |
|---|---|---|---|
| [Duplicate Integer](https://neetcode.io/problems/duplicate-integer) | Easy | Hash Set | [📄](Data%20Structures%20%26%20Algorithms/duplicate-integer/README.md) |
| [Is Anagram](https://neetcode.io/problems/is-anagram) | Easy | Hash Map Lookup | [📄](Data%20Structures%20%26%20Algorithms/is-anagram/README.md) |
| [Two Integer Sum](https://neetcode.io/problems/two-integer-sum) | Easy | Hash Map Lookup | [📄](Data%20Structures%20%26%20Algorithms/two-integer-sum/README.md) |
| [Anagram Groups](https://neetcode.io/problems/anagram-groups) | Medium | Hash Map Lookup | [📄](Data%20Structures%20%26%20Algorithms/anagram-groups/README.md) |
| [Top K Elements in List](https://neetcode.io/problems/top-k-elements-in-list) | Medium | Top-K / Heap | [📄](Data%20Structures%20%26%20Algorithms/top-k-elements-in-list/README.md) |
| [String Encode and Decode](https://neetcode.io/problems/string-encode-and-decode) | Medium | Custom Serialization | [📄](Data%20Structures%20%26%20Algorithms/string-encode-and-decode/README.md) |
| [Products of Array Excluding Self](https://neetcode.io/problems/products-of-array-discluding-self) | Medium | Prefix / Postfix | [📄](Data%20Structures%20%26%20Algorithms/products-of-array-discluding-self/README.md) |
| [Valid Sudoku](https://neetcode.io/problems/valid-sudoku) | Medium | Hash Map / Matrix | [📄](Data%20Structures%20%26%20Algorithms/valid-sudoku/README.md) |
| [Longest Consecutive Sequence](https://neetcode.io/problems/longest-consecutive-sequence) | Medium | Hash Set | [📄](Data%20Structures%20%26%20Algorithms/longest-consecutive-sequence/README.md) |

### 👉 Two Pointers

| Problem | Difficulty | Pattern | My README |
|---|---|---|---|
| [Is Palindrome](https://neetcode.io/problems/is-palindrome) | Easy | Two Pointers | [📄](Data%20Structures%20%26%20Algorithms/is-palindrome/README.md) |
| [Two Integer Sum II](https://neetcode.io/problems/two-integer-sum-ii) | Medium | Two Pointers | [📄](Data%20Structures%20%26%20Algorithms/two-integer-sum-ii/README.md) |
| [Three Integer Sum](https://neetcode.io/problems/three-integer-sum) | Medium | Two Pointers | [📄](Data%20Structures%20%26%20Algorithms/three-integer-sum/README.md) |
| [Container With Most Water](https://neetcode.io/problems/max-water-container) | Medium | Two Pointers | [📄](Data%20Structures%20%26%20Algorithms/max-water-container/README.md) |

### 🛠️ Data Structures

| Problem | Difficulty | Pattern | My README |
|---|---|---|---|
| [Dynamic Array](https://neetcode.io/problems/dynamic-array) | Easy | Data Structure Implementation | [📄](Data%20Structures%20%26%20Algorithms/dynamicArray/README.md) |

---

## Patterns Covered

### Hash Map / Hash Set Lookup
The dominant pattern in Arrays & Hashing. Trade O(n) space for O(1) lookups, turning O(n²) brute-force solutions into O(n).

Appears in: Two Sum, Contains Duplicate, Valid Anagram, Group Anagrams, Longest Consecutive Sequence, Valid Sudoku.

### Two Pointers
Place one pointer at each end of a sorted array (or string) and move them inward based on the comparison result. Avoids nested loops.

Appears in: Is Palindrome, Two Sum II, 3Sum.

### Prefix / Postfix Products
For each position, the answer depends on everything to its left and everything to its right — computed independently in two passes without using division.

Appears in: Product of Array Except Self.

### Top-K Elements (Heap)
Maintain a min-heap of size k. Every new element evicts the current minimum if it's larger, keeping only the k largest candidates.

Appears in: Top K Frequent Elements.

### Custom Serialization
Encode structured data into a flat string without ambiguity by prefixing lengths rather than relying on delimiters that may appear in the data.

Appears in: String Encode and Decode.

### Data Structure from Scratch
Implement a resizable array with amortized O(1) pushback via capacity doubling.

Appears in: Dynamic Array.

---

## Repository Structure

```
Data Structures & Algorithms/
  <problem-id>/
    submission-N.java   ← all submissions (latest = highest N)
    README.md           ← explanation, complexity, interview tips
README.md               ← this file
```

---

## How to Read Each README

Every problem README follows the same structure:

1. **Problem** — what's being asked, with an example
2. **Key Insight** — the single idea that unlocks the solution
3. **Solution** — the submitted code, cleaned up with comments
4. **Walk-through** — step-by-step trace on a concrete example
5. **Alternatives** — other valid approaches with tradeoff discussion
6. **Complexity** — time and space for each approach
7. **Interview Tips** — what interviewers look for, common mistakes, follow-ups
8. **Review Log** — date solved and review due date for spaced re-solving
