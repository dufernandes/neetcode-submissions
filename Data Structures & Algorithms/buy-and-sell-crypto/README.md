# Best Time to Buy and Sell Stock

**Difficulty:** Easy  
**Pattern:** Sliding Window / Two Pointers  
**NeetCode:** [Best Time to Buy and Sell Stock](https://neetcode.io/problems/buy-and-sell-crypto)  
**LeetCode:** [#121 – Best Time to Buy and Sell Stock](https://leetcode.com/problems/best-time-to-buy-and-sell-stock/)

---

## Problem

Given an array `prices` where `prices[i]` is the price of a stock on day `i`, return the maximum profit you can achieve from a single buy and sell. You must buy before you sell. If no profit is possible, return `0`.

```
Input:  prices = [7,1,5,3,6,4]
Output: 5  (buy at 1, sell at 6)

Input:  prices = [7,6,4,3,1]
Output: 0  (prices only fall, no profit possible)
```

---

## Key Insight

You want the **lowest buy price seen so far** and the **highest sell price after it**. You never need to look back — if you find a new minimum, any future profit calculation must use that as the new buy price. This is a classic sliding window where `left` is the buy day and `right` is the sell day.

The rule is simple:
- If `prices[left] > prices[right]` — found a cheaper buy day, move `left` to `right`
- Otherwise — calculate profit and move `right` forward

---

## Solution

```java
class Solution {
    public int maxProfit(int[] prices) {
        if (prices == null || prices.length == 1) return 0;

        int left = 0, right = 1;
        int profit = 0;

        while (right < prices.length) {
            if (prices[left] > prices[right]) {
                // found a cheaper price — reset buy day
                left = right;
                right++;
            } else {
                // prices[right] > prices[left] — valid transaction
                profit = Math.max(profit, prices[right] - prices[left]);
                right++;
            }
        }

        return profit;
    }
}
```

> Note: `left = right` before `right++` — the new buy day is the current right position, then right advances past it. Don't increment right before assigning left.

---

## Walk-through: `[7, 1, 5, 3, 6, 4]`

```
left=0 (7), right=1 (1): 7 > 1 → cheaper buy, left=1, right=2
left=1 (1), right=2 (5): 1 < 5 → profit=max(0, 5-1)=4, right=3
left=1 (1), right=3 (3): 1 < 3 → profit=max(4, 3-1)=4, right=4
left=1 (1), right=4 (6): 1 < 6 → profit=max(4, 6-1)=5, right=5
left=1 (1), right=5 (4): 1 < 4 → profit=max(5, 4-1)=5, right=6
Loop ends.

return 5 ✓
```

---

## Alternative: Track Minimum (same complexity, slightly simpler)

Instead of two explicit pointers, track the running minimum price. Functionally identical — just expressed differently.

```java
class Solution {
    public int maxProfit(int[] prices) {
        int minPrice = Integer.MAX_VALUE;
        int profit = 0;
        for (int price : prices) {
            if (price < minPrice) {
                minPrice = price;
            } else {
                profit = Math.max(profit, price - minPrice);
            }
        }
        return profit;
    }
}
```

Both approaches are O(n) time and O(1) space. The two-pointer version maps more naturally to the sliding window pattern and generalises better to related problems. The min-tracking version is marginally easier to read.

---

## Complexity

| | |
|---|---|
| **Time** | O(n) — single pass through the array |
| **Space** | O(1) — two pointers and a running max, nothing else |

---

## Gotchas

- **You can only buy and sell once.** This is not the "buy and sell multiple times" variant (LeetCode #122). Don't add profits across multiple transactions.
- **`left = right` before incrementing `right`** — when you find a new minimum, the buy day becomes the current right position. Incrementing right first and then assigning would skip a day.
- **Null and single-element guard** — `prices == null || prices.length == 1` returns 0 immediately. A single price means no transaction is possible.
- **All decreasing prices** — profit stays 0 the whole time. `Math.max(profit, ...)` handles this correctly without special casing.
- **`left` and `right` can never cross** — when `prices[left] > prices[right]`, you set `left = right` and advance right, so left always stays behind or equal to right before the increment.

---

## Interview Tips

- This is the gateway problem to the sliding window pattern — if you freeze here, it signals a gap in fundamentals. Practice until it's automatic.
- Mention both the two-pointer and min-tracking approaches and state they're equivalent — shows you see the same problem from multiple angles.
- The follow-up is almost always LeetCode #122 (buy/sell multiple times) or #123 (at most 2 transactions). Know that #122 just sums all upward moves: `if (prices[i] > prices[i-1]) profit += prices[i] - prices[i-1]`.
- Edge cases to mention: empty array, single element, all same price, strictly increasing (sell on last day), strictly decreasing (profit = 0).
