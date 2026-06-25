# Top K Elements in List (Top K Frequent Elements)

**Difficulty:** Medium  
**Pattern:** Top-K Elements (Heap / PriorityQueue)  
**NeetCode:** [Top K Elements in List](https://neetcode.io/problems/top-k-elements-in-list)  
**LeetCode:** [#347 – Top K Frequent Elements](https://leetcode.com/problems/top-k-frequent-elements/)

---

## Problem

Given an integer array `nums` and an integer `k`, return the `k` most frequent elements. The answer may be returned in any order.

```
Input:  nums = [1,1,1,2,2,3], k = 2
Output: [1, 2]
```

---

## Key Insight

Two steps: count frequencies with a HashMap, then find the top-k by frequency. The submitted solution sorts the map entries by frequency descending and takes the first k — clean and correct, though not optimal.

---

## Solution (Sort-based — submitted)

```java
class Solution {
    public int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> freq = new HashMap<>();
        for (int num : nums) {
            freq.merge(num, 1, Integer::sum);
        }

        List<Integer> sortedNumbers = freq.entrySet().stream()
            .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());

        int[] result = new int[k];
        for (int i = 0; i < k; i++) {
            result[i] = sortedNumbers.get(i);
        }
        return result;
    }
}
```

---

## Alternative: Min-Heap (optimal for large input)

Keep a min-heap of size k. For each entry in the frequency map, add it to the heap; if the heap exceeds k, remove the lowest-frequency element. The heap always holds the top-k.

```java
class Solution {
    public int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> freq = new HashMap<>();
        for (int num : nums) freq.merge(num, 1, Integer::sum);

        // min-heap ordered by frequency
        PriorityQueue<Map.Entry<Integer, Integer>> heap =
            new PriorityQueue<>(Map.Entry.comparingByValue());

        for (Map.Entry<Integer, Integer> entry : freq.entrySet()) {
            heap.offer(entry);
            if (heap.size() > k) heap.poll(); // evict lowest
        }

        int[] result = new int[k];
        for (int i = k - 1; i >= 0; i--) {
            result[i] = heap.poll().getKey();
        }
        return result;
    }
}
```

---

## Alternative: Bucket Sort (O(n))

Since frequency can be at most `n`, use an array of lists indexed by frequency. Scan from the end to collect the top-k.

```java
List<Integer>[] buckets = new List[nums.length + 1];
// fill frequency map, then:
for (Map.Entry<Integer, Integer> e : freq.entrySet()) {
    int f = e.getValue();
    if (buckets[f] == null) buckets[f] = new ArrayList<>();
    buckets[f].add(e.getKey());
}
// collect from the end
```

---

## Complexity

| Approach | Time | Space |
|---|---|---|
| Sort (submitted) | O(n log n) | O(n) |
| Min-Heap | O(n log k) | O(n + k) |
| Bucket Sort | O(n) | O(n) |

---

## Interview Tips

- The sort approach is an acceptable starting point but be ready to discuss the heap as an optimization when `k << n`.
- A **min-heap of size k** is the canonical Top-K pattern — evicting the minimum keeps only the largest k values. This pattern recurs in many problems.
- Bucket sort achieves O(n) but is an advanced follow-up; interviewers will be impressed if you mention it unprompted.
