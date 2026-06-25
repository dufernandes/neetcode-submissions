# String Encode and Decode

**Difficulty:** Medium  
**Pattern:** Custom Serialization / String Manipulation  
**NeetCode:** [String Encode and Decode](https://neetcode.io/problems/string-encode-and-decode)  
**LeetCode:** [#271 – Encode and Decode Strings](https://leetcode.com/problems/encode-and-decode-strings/) *(Premium)*

---

## Problem

Design an algorithm to encode a list of strings into a single string, and decode it back to the original list. The strings may contain any character — including the delimiter you choose.

```
Input:  ["neet","code","love","you"]
Encode: "4#4#4#3#neetcodeloveyou"
Decode: ["neet","code","love","you"]
```

---

## Key Insight

You cannot use a simple delimiter like `","` because it may appear inside the strings themselves. Instead, **store the lengths explicitly** as a header before the concatenated payload. The decoder reads the header to know exactly how many bytes to slice from the payload — no ambiguity possible.

**Format:** `<count>#<len1>#<len2>#...#<lenN>#<str1><str2>...<strN>`

```
["hi", "there#!"]
→ "2#2#7#hithere#!"
   ^  ^ ^  ^
   2  2  7  payload (split never touches this part)
words  lengths
```

---

## Solution

```java
class Solution {
    public String encode(List<String> strs) {
        // Header: count # len1 # len2 # ... # lenN #
        String prefix = strs.size() + "#"
            + strs.stream()
                  .map(s -> String.valueOf(s.length()))
                  .collect(Collectors.joining("#"))
            + "#";

        // Payload: all strings concatenated (no separator)
        return prefix + String.join("", strs);
    }

    public List<String> decode(String str) {
        // Split off the count
        String[] parts = str.split("#", 2);
        int n = Integer.parseInt(parts[0]);

        // Split off the n length values, leaving the payload as the last element
        String[] sizesAndPayload = parts[1].split("#", n + 1);

        List<String> result = new ArrayList<>(n);
        int offset = 0;
        String payload = sizesAndPayload[n];

        for (int i = 0; i < n; i++) {
            int size = Integer.parseInt(sizesAndPayload[i]);
            result.add(payload.substring(offset, offset + size));
            offset += size;
        }

        return result;
    }
}
```

---

## How Decoding Works (Step by Step)

```
Encoded: "2#2#7#hithere#!"

split("#", 2) → ["2", "2#7#hithere#!"]
n = 2

split("2#7#hithere#!", "#", 3) → ["2", "7", "hithere#!"]
                                           ^^^^^^^^^
                                           this is the raw payload

offset=0: size=2 → "hi",      offset=2
offset=2: size=7 → "there#!", offset=9
```

---

## Complexity

| | |
|---|---|
| **Time** | O(n) for both encode and decode, where n = total characters |
| **Space** | O(n) |

---

## Interview Tips

- The naive approach (join with a special char like `|`) breaks as soon as strings contain that char — state this immediately to frame why a length-prefix approach is needed.
- The classic alternative is **length-prefixed encoding**: `"4#neet4#code"` (one length per string, no global header). It's arguably simpler and worth discussing.
- `split("#", limit)` — the `limit` parameter is critical here. Without it, Java's `split` drops trailing empty strings and silently corrupts the result.
- This problem tests your ability to design a **binary-safe protocol**, a concept that comes up in networking and serialization system design.
