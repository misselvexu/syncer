package com.github.zzt93.syncer.data;


import java.io.Serializable;
import java.util.HashMap;

/**
 * @see ExtraQuery
 * @see SyncByQuery
 * https://www.elastic.co/guide/en/elasticsearch/reference/current/docs-update.html#_scripted_updates
 */
public class ESScriptUpdate extends SyncByQuery implements Serializable {


  // todo other script op: +=, contains
  private final HashMap<String, Object> append = new HashMap<>();
  private final HashMap<String, Object> remove = new HashMap<>();
  private final transient SyncData outer;

  public ESScriptUpdate(SyncData data) {
    outer = data;
  }

  public ESScriptUpdate updateList(String listField, Object delta) {
    switch (outer.getType()) {
      case DELETE_ROWS:
        remove.put(listField, delta);
        break;
      case WRITE_ROWS:
        append.put(listField, delta);
        break;
      default:
    }
    outer.toUpdate();
    return this;
  }

  public boolean needScript() {
    return !append.isEmpty() || !remove.isEmpty();
  }

  public HashMap<String, Object> getAppend() {
    return append;
  }

  public HashMap<String, Object> getRemove() {
    return remove;
  }

  @Override
  public String toString() {
    return "SyncByQueryES{" +
        ", append=" + append +
        ", remove=" + remove +
        ", outer=SyncData@" + Integer.toHexString(outer.hashCode()) +
        '}';
  }
}
