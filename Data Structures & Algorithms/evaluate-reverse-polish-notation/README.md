# Evaluate Reverse Polish Notation

**Difficulty**: Medium  
**Pattern**: Stack  
**NeetCode**: [Evaluate Reverse Polish Notation](https://neetcode.io/problems/evaluate-reverse-polish-notation)  
**LeetCode**: [#150 – Evaluate Reverse Polish Notation](https://leetcode.com/problems/evaluate-reverse-polish-notation/)  

---

## Problem

Evaluate an arithmetic expression given in [Reverse Polish Notation](https://en.wikipedia.org/wiki/Reverse_Polish_notation). Valid operators are `+`, `-`, `*`, `/`. Each operand may be an integer or another expression.

```
tokens = ["2","1","+","3","*"]
// (2 + 1) * 3 = 9
```

---

## Key Insight

RPN removes the need for parentheses or operator precedence because the order of operations is already encoded in the token sequence: by the time you reach an operator, its two operands are always the two most recently seen values. A **stack** captures exactly that "most recently seen" relationship — push numbers as you see them, and when you hit an operator, pop the last two, apply it, and push the result back. The final value left on the stack is the answer.

---

## Two Solutions — Evolution from Working to Clean

### Solution 1 — First working solution

```java
class Solution {

    public int evalRPN(String[] tokens) {
        Stack<Integer> numbers = new Stack<Integer>();
        for (String token : tokens) {
            if (this.isNumber(token)) {
                numbers.push(this.getNumber(token));
            } else {
                int rightNumber = numbers.pop();
                int leftNumber = numbers.pop();
                int result = 0;
                switch (token) {
                    case "+":
                        result = leftNumber + rightNumber;
                        break;
                    case "-":
                        result = leftNumber - rightNumber;
                        break;
                    case "*":
                        result = leftNumber * rightNumber;
                        break;
                    case "/":
                        result = leftNumber / rightNumber;
                        break;
                }

                numbers.push(result);
            }
        }
        return numbers.pop();
    }
    private Integer getNumber(String token) {
        try {
            return Integer.parseInt(token);
        } catch (NumberFormatException nfe) {
            return null;
        }
    }
    private boolean isNumber(String token) {
        try {
            Integer.parseInt(token);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
```

**What it does:** classic stack-based evaluation — pushes numbers, pops two operands on an operator, applies it, pushes the result back.

**Rough edges:**
- `isNumber` and `getNumber` both call `Integer.parseInt` on the same token, so every number gets parsed twice.
- `Stack` extends the legacy, synchronized `Vector` class — unnecessary overhead for a single-threaded solution.
- The classic `switch`/`break`/`result` pattern is verbose for four one-line arithmetic cases.
- "Is this a number?" is inferred indirectly, by trying to parse it and catching the failure, rather than checking directly against the small, known set of operators.
- An unrecognized token silently falls through to `result = 0` instead of failing loudly.

**Verdict:** correct and passes, but has real style and idiom issues worth fixing.

---

### Solution 2 — AI-assisted cleanup

```java
class Solution {
    private static final Set<String> OPERATORS = Set.of("+", "-", "*", "/");

    public int evalRPN(String[] tokens) {
        Deque<Integer> numbers = new ArrayDeque<>();
        for (String token : tokens) {
            if (OPERATORS.contains(token)) {
                int right = numbers.pop();
                int left = numbers.pop();
                numbers.push(apply(token, left, right));
            } else {
                numbers.push(Integer.parseInt(token));
            }
        }
        return numbers.pop();
    }
    private int apply(String operator, int left, int right) {
        return switch (operator) {
            case "+" -> left + right;
            case "-" -> left - right;
            case "*" -> left * right;
            case "/" -> left / right;
            default -> throw new IllegalArgumentException("Unknown operator: " + operator);
        };
    }
}
```

I asked an AI to help make this version more elegant. What changed:
- **`Deque<Integer>`/`ArrayDeque` instead of `Stack`** — the modern, unsynchronized replacement recommended for single-threaded use.
- **`Set.of("+", "-", "*", "/")`** replaces the parse-and-catch trick — checking membership in the operator set directly answers "is this an operator?" instead of inferring it from a parse failure.
- **Single `Integer.parseInt` per numeric token** — no duplicate parsing.
- **Arrow-style `switch` expression** replaces `switch`/`break`/`result` — each case is a one-line expression, and the compiler enforces exhaustiveness.
- **`default -> throw new IllegalArgumentException(...)`** replaces the silent `result = 0` fallback, so an unexpected token fails loudly instead of quietly producing a wrong answer.

**Verdict:** same algorithm, meaningfully cleaner code — this is the version to present.

---

## Solution Comparison

| | Solution 1 | Solution 2 |
|---|---|---|
| **Stack type** | `Stack` (legacy) | `ArrayDeque` |
| **Operator check** | parse-and-catch | `Set` lookup |
| **Number parsing** | parsed twice per token | parsed once |
| **Dispatch** | `switch`/`break` | `switch` expression |
| **Unknown token** | silently `0` | throws `IllegalArgumentException` |
| **Time** | O(n) | O(n) |
| **Space** | O(n) | O(n) |

---

## Complexity (both solutions)

| | |
|---|---|
| **Time** | O(n) — single pass over the tokens |
| **Space** | O(n) — worst case, all tokens are numbers and sit on the stack at once |

---

## Gotchas

- **Operand order matters for `-` and `/`** — pop the *right* operand first, then the *left*. `leftNumber - rightNumber`, not the reverse, or subtraction/division will be backwards.
- **Integer division truncates toward zero** — matches the problem's expected behavior, but worth calling out explicitly since Java's `/` on negative integers can otherwise surprise you.
- **`Stack` is legacy in Java** — in a real codebase (or an interview), prefer `Deque<Integer> stack = new ArrayDeque<>()`.

---

## Interview Tips

- Start by stating the core idea in one sentence: operators always apply to the two most recently pushed operands, which is exactly what a stack gives you for free.
- Be explicit about operand order (`left op right`, not `right op left`) before writing code — this is the most common bug in this problem.
- If asked to avoid a stack, mention it's possible to evaluate recursively from the end of the token list, but the stack approach is simpler and the expected solution.
