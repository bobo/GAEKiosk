package nu.lan.kiosk.util;

import java.util.HashMap;
import java.util.Map;

public class Parser
{
  StringBuilder parsed = new StringBuilder();
  int thisCount = 0;
  Map<Long, String> map = new HashMap<Long, String>();

  public Parser(Map<Long, String> map) {
    this.map.putAll(map);
  }

  public Map<Long, String> parseMap() {
    for (Long l : map.keySet()) {
      map.put(l, parseEntry(l));
    }
    return map;
  }

  private String parseEntry(Long l) {
    String[] split = map.get(l).trim().split("#");
    parsed = new StringBuilder();
    for (int i = 0; i < split.length; i++) {
      String current = split[i];
      if (i == 0) {
        parsed.append(current);
      }
      else if (sameAsLast(current, split, i)) {
        thisCount++;
        if(i==split.length-1)
          printCountOfPrevious();
      }
      else {
        printCountOfPrevious();
        parsed.append(current);
      }
    }
    return parsed.toString().trim();
  }

  private boolean sameAsLast(String current, String[] split, int i) {
    return current.equals(split[i - 1]);
  }

  private void printCountOfPrevious() {
    if (thisCount > 0)
      parsed.append("*").append(++thisCount);
    parsed.append(" ");
    thisCount = 0;
  }
}
