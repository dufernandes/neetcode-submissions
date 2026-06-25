# Is Palindrome

## Problem

Given a string `s`, return `true` if it is a palindrome, `false` otherwise.

A palindrome reads the same forwards and backwards. Only alphanumeric characters are considered — spaces, punctuation, and symbols are ignored. The comparison is case-insensitive.

**Example:**
```
Input:  "A man, a plan, a canal: Panama"
Output: true

Input:  "race a car"
Output: false
```

## Pattern

**Two Pointers** — one starting at the left, one at the right, moving toward each other.

## Approach

1. Place `left` at index 0 and `right` at the last index
2. Skip non-alphanumeric characters by advancing the relevant pointer and continuing
3. Compare the characters at both pointers case-insensitively
4. If they don't match, return `false`
5. If they match, move both pointers inward
6. If the pointers meet or cross without a mismatch, return `true`

The key insight: non-alphanumeric characters are irrelevant, so skip them before comparing — don't pre-process the string into a clean version first.

## Complexity

| | |
|---|---|
| Time | O(n) — each character visited at most once |
| Space | O(1) — no extra data structure, pointers only |

## What to remember

- Skip invalid characters **before** comparing, not after
- Use `Character.isLetterOrDigit()` to filter
- Use `Character.toLowerCase()` for case-insensitive comparison
- This avoids creating a cleaned copy of the string, keeping space at O(1)

## Review log

| Date solved | Review due |
|---|---|
| — | — |
