class Solution {
  public String encode(List<String> strs) {
    String prefix = strs.size() + "#"
        + strs.stream().map(s -> String.valueOf(s.length())).collect(Collectors.joining("#")) + "#";

    return prefix + strs.stream().map(s -> s).collect(Collectors.joining(""));
  }

  public List<String> decode(String str) {
    String[] occurrencesAndRest = str.split("#", 2);
    int nOccurrences = Integer.parseInt(occurrencesAndRest[0]);

    String[] sizesAndRest = occurrencesAndRest[1].split("#", nOccurrences + 1);

    List<String> result = new ArrayList<>(nOccurrences);

    int offset = 0;
    for (int i = 0; i < nOccurrences; i++) {
      int size = Integer.parseInt(sizesAndRest[i]);
      result.add(sizesAndRest[nOccurrences].substring(offset, offset + size));
      offset = offset + size;
    }

    return result;
  }
}
