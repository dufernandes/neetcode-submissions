# Is Palindrome (Valid Palindrome)

**Difficulty:** Easy  
**Pattern:** Two Pointers  
**NeetCode:** [Is Palindrome](https://neetcode.io/problems/is-palindrome)  
**LeetCode:** [#125 – Valid Palindrome](https://leetcode.com/problems/valid-palindrome/)

---

## Problem

Given a string `s`, return `true` if it is a palindrome, `false` otherwise.

A palindrome reads the same forward and backward. Only alphanumeric characters count — spaces, punctuation, and symbols are ignored. The comparison is case-insensitive.

```
Input:  "A man, a plan, a canal: Panama"
Output: true

Input:  "race a car"
Output: false
```

---

## Key Insight

Skip non-alphanumeric characters **on the fly** rather than pre-processing the string into a cleaned copy. This keeps space at O(1) — no extra string allocation needed.

Two pointers start at opposite ends and move inward. At each step, skip invalid characters before comparing. If a mismatch is found, return `false`. If the pointers meet or cross without a mismatch, return `true`.

---

## Solution

```java
class Solution {
    public boolean isPalindrome(String s) {
        int left = 0, right = s.length() - 1;
        while (left < right) {
            char leftChar = s.charAt(left);
            if (!Character.isLetterOrDigit(leftChar)) {
                left++;
                continue;
            }
            char rightChar = s.charAt(right);
            if (!Character.isLetterOrDigit(rightChar)) {
                right--;
                continue;
            }
            if (Character.toLowerCase(leftChar) != Character.toLowerCase(rightChar)) {
                return false;
            }
            left++; right--;
        }
        return true;
    }
}
```

> Note: Skip the invalid character and `continue` immediately — don't advance both pointers in the same iteration when skipping.

---

## Alternative: Brute Force (O(n) space)

Build a cleaned string first, then compare it to its reverse. Simpler to read but allocates extra memory.

```java
class Solution {
    public boolean isPalindrome(String s) {
        StringBuilder clean = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                clean.append(Character.toLowerCase(c));
            }
        }
        String cleaned = clean.toString();
        return cleaned.equals(new StringBuilder(cleaned).reverse().toString());
    }
}
```

The two-pointer solution is preferred in interviews — same O(n) time, but O(1) space.

---

## Complexity

| | |
|---|---|
| **Time** | O(n) — each character visited at most once |
| **Space** | O(1) — no extra data structure, pointers only |

---

## Interview Tips

- Always mention the brute force first (clean + reverse), then explain why the two-pointer approach is better (O(1) space).
- Use `Character.isLetterOrDigit()` — don't write your own alphanumeric check.
- Use `Character.toLowerCase()` — don't convert the whole string upfront.
- Edge cases worth mentioning: empty string (returns `true`), single character (returns `true`), all non-alphanumeric characters (returns `true` — empty after cleaning).
