# Products of Array Excluding Self

**Difficulty:** Medium  
**Pattern:** Prefix / Postfix Products  
**NeetCode:** [Products of Array Discluding Self](https://neetcode.io/problems/products-of-array-discluding-self)  
**LeetCode:** [#238 – Product of Array Except Self](https://leetcode.com/problems/product-of-array-except-self/)

---

## Problem

Given an integer array `nums`, return an array `result` where `result[i]` is the product of all elements in `nums` except `nums[i]`. You must solve it **without using division** and in O(n) time.

```
Input:  nums   = [1, 2,  3,  4]
Output: result = [24, 12, 8,  6]
//  result[0] = 2*3*4 = 24
//  result[1] = 1*3*4 = 12
```

---

## Key Insight

For each position `i`, the answer is:

```
result[i] = (product of everything to the LEFT of i)
          × (product of everything to the RIGHT of i)
```

Compute these independently in two passes (prefix and postfix), then combine.

```
nums    =  [1,  2,  3,  4]

prefix[i] = product of nums[0..i] inclusive
prefix  =  [1,  2,  6, 24]

postfix[i] = product of nums[i..n-1] inclusive
postfix =  [24, 24, 12, 4]

result[i] = prefix[i-1] (or 1) × postfix[i+1] (or 1)
result  =  [24, 12,  8,  6]  ✓
```

---

## Solution

```java
class Solution {
    public int[] productExceptSelf(int[] nums) {
        int n = nums.length;
        int[] prefix  = new int[n];
        int[] postfix = new int[n];

        // prefix[i] = product of nums[0..i]
        int product = 1;
        for (int i = 0; i < n; i++) {
            prefix[i] = nums[i] * product;
            product = prefix[i];
        }

        // postfix[i] = product of nums[i..n-1]
        product = 1;
        for (int i = n - 1; i >= 0; i--) {
            postfix[i] = nums[i] * product;
            product = postfix[i];
        }

        // result[i] = left product × right product
        int[] result = new int[n];
        for (int i = 0; i < n; i++) {
            int left  = (i == 0)     ? 1 : prefix[i - 1];
            int right = (i == n - 1) ? 1 : postfix[i + 1];
            result[i] = left * right;
        }

        return result;
    }
}
```

---

## Space-Optimised Version (O(1) extra space)

Reuse the `result` array for the prefix pass, then apply the postfix on the fly:

```java
class Solution {
    public int[] productExceptSelf(int[] nums) {
        int n = nums.length;
        int[] result = new int[n];

        // First pass: result[i] = product of everything to the left
        result[0] = 1;
        for (int i = 1; i < n; i++) {
            result[i] = result[i - 1] * nums[i - 1];
        }

        // Second pass: multiply in the right product on the fly
        int right = 1;
        for (int i = n - 1; i >= 0; i--) {
            result[i] *= right;
            right *= nums[i];
        }

        return result;
    }
}
```

---

## Complexity

| Approach | Time | Space |
|---|---|---|
| Two arrays (submitted) | O(n) | O(n) |
| Single result array | O(n) | O(1)* |

*\* The output array itself doesn't count as extra space per the problem.*

---

## Interview Tips

- Division would make this trivial (`totalProduct / nums[i]`) but the problem explicitly forbids it — and it breaks on zeros anyway.
- The O(1) space follow-up is almost always asked. Know the two-pass trick: prefix forward into `result`, then postfix backward with a running `right` variable.
- Handle the edge case where `nums` contains zeros — the prefix/postfix approach handles this naturally; a division-based approach requires special-casing.
