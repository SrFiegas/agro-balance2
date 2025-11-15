package com.agrobalance.config;

public class AuditorContext {
  private static final ThreadLocal<String> ACTOR = new ThreadLocal<>();
  public static void set(String v){ ACTOR.set(v); }
  public static String get(){ return ACTOR.get(); }
  public static void clear(){ ACTOR.remove(); }
}
