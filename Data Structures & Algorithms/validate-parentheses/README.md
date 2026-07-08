# Valid Parentheses

**Difficulty:** Easy  
**Pattern:** Stack  
**NeetCode:** [Validate Parentheses](https://neetcode.io/problems/validate-parentheses)  
**LeetCode:** [#20 – Valid Parentheses](https://leetcode.com/problems/valid-parentheses/)

---

## Problem

Given a string `s` containing only the characters `(`, `)`, `{`, `}`, `[`, `]`, return `true` if the string is valid. A string is valid if every opening bracket is closed by the same type of bracket in the correct order.

```
Input:  s = "()"
Output: true

Input:  s = "()[]{}"
Output: true

Input:  s = "(]"
Output: false

Input:  s = "([)]"
Output: false
```

---

## Key Insight

Opening brackets must be matched by their corresponding closing bracket in LIFO order — the last opened must be the first closed. That's exactly what a stack gives you.

**Push opening brackets, pop and verify on closing brackets.**

The trick that simplifies the closing bracket check: instead of pushing the opening bracket and checking its corresponding closer on pop, push **the expected closing bracket** directly. Then when you hit a closing character, just check if it matches the top of the stack — no mapping needed.

---

## Three Solutions — Evolution from Obvious to Optimal

### Solution 1 — `Stack<Character>` with explicit mapping (most readable, slowest)

```java
class Solution {
    public boolean isValid(String s) {
        Stack<Character> stack = new Stack<>();
        if (s.length() % 2 != 0) return false;
        for (char c : s.toCharArray()) {
            switch (c) {
                case '{': stack.push('{'); break;
                case '[': stack.push('['); break;
                case '(': stack.push('('); break;
                case '}': if (stack.isEmpty() || stack.pop() != '{') return false; break;
                case ']': if (stack.isEmpty() || stack.pop() != '[') return false; break;
                case ')': if (stack.isEmpty() || stack.pop() != '(') return false; break;
                default: throw new RuntimeException("Unexpected char in input: " + c);
            }
        }
        return stack.isEmpty();
    }
}
```

**What it does:** Pushes the opening bracket itself, then when a closing bracket is encountered, checks if the top of the stack is the matching opener.

**Problem:** `Stack<Character>` in Java extends `Vector`, which is synchronized — thread-safe overhead you don't need in a single-threaded context. Also requires boxing `char` into `Character`. The explicit match per closing bracket (6 cases total) works but is verbose.

**Good for:** Explaining the algorithm clearly in an interview before optimising. The `default` throw is a nice defensive touch — signals awareness of invalid input.

---

### Solution 2 — `Deque<Character>` + push expected closer (cleaner, faster)

```java
class Solution {
    public boolean isValid(String s) {
        Deque<Character> stack = new ArrayDeque<>();
        if (s.length() % 2 != 0) return false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '{': stack.push('}'); break;
                case '[': stack.push(']'); break;
                case '(': stack.push(')'); break;
                default:
                    if (stack.isEmpty() || stack.pop() != c) return false;
            }
        }
        return stack.isEmpty();
    }
}
```

**Two improvements over Solution 1:**

**1. `ArrayDeque` instead of `Stack`.**
Java's own documentation recommends using `Deque` over `Stack`. `ArrayDeque` is not synchronized, uses a resizable array internally, and is faster in practice. This is the idiomatic Java stack.

**2. Push the expected closing bracket instead of the opener.**
When you see `{`, push `}`. When you see `[`, push `]`. When you see `(`, push `)`. Then the `default` case (any closing bracket) collapses to a single check: `stack.pop() != c`. No explicit mapping needed at pop time — the logic is already baked in at push time. 6 switch cases become 4.

**This is the solution to present in an interview.** Clean, idiomatic, O(n) time, O(n) space.

---

### Solution 3 — `int[]` as manual stack (fastest, lowest memory overhead)

```java
class Solution {
    public boolean isValid(String s) {
        int n = s.length();
        if (n % 2 != 0) return false;
        int[] stack = new int[n];
        int top = -1;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '{': stack[++top] = '}'; break;
                case '[': stack[++top] = ']'; break;
                case '(': stack[++top] = ')'; break;
                default:
                    if (top == -1 || stack[top--] != c) return false;
            }
        }
        return top == -1;
    }
}
```

**One further improvement over Solution 2:**

**Replace `Deque<Character>` with a primitive `int[]` and a manual `top` pointer.**

`ArrayDeque<Character>` still boxes `char` into `Character` objects — heap allocations per character. A raw `int[]` eliminates all boxing overhead. The array is pre-allocated at size `n` (the maximum possible stack depth — a string of all openers). `top = -1` signals an empty stack, `++top` pushes, `top--` pops.

**When does this matter?** In competitive programming or performance-critical code. In a typical interview, the difference is negligible. But knowing this optimisation exists and being able to explain it clearly is a strong signal.

**Gotcha:** `stack[top--] != c` — the post-decrement `top--` pops *after* reading the value. This is correct. Using `--top` instead would pop before reading, which is a bug.

---

## Solution Comparison

| | Solution 1 | Solution 2 | Solution 3 |
|---|---|---|---|
| **Stack type** | `Stack<Character>` | `ArrayDeque<Character>` | `int[]` + manual pointer |
| **Boxing overhead** | Yes | Yes | No (primitives) |
| **Thread-safe overhead** | Yes (`Vector`) | No | No |
| **Switch cases** | 6 | 4 | 4 |
| **Readability** | ★★★★☆ | ★★★★★ | ★★★☆☆ |
| **Performance** | Slowest | Fast | Fastest |
| **Interview recommendation** | Explain first | Present this | Mention as optimisation |

---

## Complexity

| | |
|---|---|
| **Time** | O(n) — single pass through the string |
| **Space** | O(n) — worst case all opening brackets, stack grows to n/2 |

All three solutions share the same asymptotic complexity. The differences are in constant factors and memory allocation patterns.

---

## Walk-through: `"([)]"`

Using Solution 2:

```
c='(' → push ')' → stack=[')']
c='[' → push ']' → stack=[')', ']']
c=')' → pop ']', ']' != ')' → return false ✓
```

The interleaving `([)]` is correctly rejected even though both pairs exist — order matters.

---

## Gotchas

- **`stack.isEmpty()` check before `pop()`** — always check first. An empty stack with a closing bracket means unmatched closer — return false immediately. Popping an empty stack throws `EmptyStackException` / `NoSuchElementException`.
- **`return stack.isEmpty()` not `return true`** — a string like `"((("` processes without error but leaves unpaired openers. The stack must be empty at the end.
- **Odd-length early exit** — any valid bracket string must have even length. `if (n % 2 != 0) return false` saves half the work on strings that can never be valid.
- **`top--` vs `--top`** in Solution 3 — post-decrement reads then decrements. Pre-decrement decrements then reads. Using `--top` in `stack[--top]` would read the wrong element.
- **`default: throw`** in Solution 1 is good defensive programming — if the problem guarantees only bracket characters this never fires, but it makes invalid inputs visible rather than silently ignored.

---

## Interview Tips

- Start with Solution 1 to explain the algorithm clearly, then say: *"I can improve this — Java's `Stack` is legacy and synchronized. I'd use `ArrayDeque` instead, and push the expected closer rather than the opener, which simplifies the closing bracket check to a single comparison."*
- Presenting Solution 2 unprompted as your first answer signals strong Java idiom knowledge.
- If asked about further optimisation, mention Solution 3 and explain the boxing overhead eliminated by using primitives.
- Common follow-up: *"What if the string can contain non-bracket characters?"* — add a check in `default` to skip unknown characters rather than throwing or returning false.
