/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.10
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.grame.dsp_faust;

public class dsp_faust {
  public static void init(int arg0, int arg1) {
    dsp_faustJNI.init(arg0, arg1);
  }

  public static int start() {
    return dsp_faustJNI.start();
  }

  public static void stop() {
    dsp_faustJNI.stop();
  }

  public static boolean isRunning() {
    return dsp_faustJNI.isRunning();
  }

  public static int keyOn(int arg0, int arg1) {
    return dsp_faustJNI.keyOn(arg0, arg1);
  }

  public static int keyOff(int arg0) {
    return dsp_faustJNI.keyOff(arg0);
  }

  public static int pitchBend(int arg0, float arg1) {
    return dsp_faustJNI.pitchBend(arg0, arg1);
  }

  public static String getJSON() {
    return dsp_faustJNI.getJSON();
  }

  public static int getParamsCount() {
    return dsp_faustJNI.getParamsCount();
  }

  public static float getParam(String arg0) {
    return dsp_faustJNI.getParam(arg0);
  }

  public static void setParam(String arg0, float arg1) {
    dsp_faustJNI.setParam(arg0, arg1);
  }

  public static int setVoiceParam(String arg0, int arg1, float arg2) {
    return dsp_faustJNI.setVoiceParam(arg0, arg1, arg2);
  }

  public static int setVoiceGain(int arg0, float arg1) {
    return dsp_faustJNI.setVoiceGain(arg0, arg1);
  }

  public static String getParamAddress(int arg0) {
    return dsp_faustJNI.getParamAddress(arg0);
  }

  public static float getRecordingBuffer(int i) {
    return dsp_faustJNI.getRecordingBuffer(i);
  }

  public static void setDownloadBuffer(float value, int index) {
    dsp_faustJNI.setDownloadBuffer(value, index);
  }

}
