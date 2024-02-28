package com.example.utils;

public class Logger {
  private static final String ANSI_RESET = "\u001B[0m";
  private static final String ANSI_RED = "\u001B[31m";
  private static final String ANSI_GREEN = "\u001B[32m";
  private static final String ANSI_YELLOW = "\u001B[33m";
  private static final String ANSI_BLUE = "\u001B[34m";
  private static final String ANSI_RED_BACKGROUND = "\u001B[41m";
  private static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
  private static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";

  public static void printError(String message) {
    System.out.println(ANSI_RED + "ERROR : " + ANSI_RESET + ANSI_RED_BACKGROUND + message + ANSI_RESET);
  }

  public static void printDebug(String message) {
    System.out.println(ANSI_BLUE + message + ANSI_RESET);
  }

  public static void printSucess(String message) {
    System.out.println(ANSI_GREEN + message + ANSI_RESET);
  }

  public static void printWarning(String message) {
    System.out.println(ANSI_YELLOW + "WARNING : " + ANSI_RESET + ANSI_YELLOW_BACKGROUND + message + ANSI_RESET);
  }

  public static void printYellow(String message) {
    System.out.println(ANSI_YELLOW +  message + ANSI_RESET);
  }

  public static String getErrorLog(String message) {
    return ANSI_RED + "ERROR : " + ANSI_RED_BACKGROUND + message + ANSI_RESET;
  }

  public static String getDebugLog(String message) {
    return ANSI_BLUE + "DEBUG: " + ANSI_BLUE_BACKGROUND + message + ANSI_RESET;
  }

  public static String getSuccessLog(String message) {
    return ANSI_GREEN + message + ANSI_RESET;
  }

  public static String getWarningLog(String message) {
    return ANSI_YELLOW + "WARNING : " + ANSI_RESET + ANSI_YELLOW_BACKGROUND + message + ANSI_RESET;
  }

  public static String getBlue(String message) {
    return ANSI_BLUE + message + ANSI_RESET;
  }
}

