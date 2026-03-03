#include <jni.h>
#include <android/log.h>
#include <string.h>

// Свои заголовки
#include "phone_formatter.h"  // ← ассемблерный заголовок УДАЛЕН
#include "telegram_helper.h"
#include "sms_helper.h"
#include "call_helper.h"

#define LOG_TAG "InfoJNI"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)

JNIEXPORT jstring JNICALL
Java_com_printer_information_MainActivity_formatPhoneNumberNative(
        JNIEnv* env, jobject thiz, jstring input) {

    const char* inputStr = (*env)->GetStringUTFChars(env, input, NULL);
    char result[32] = {0};

    // Используем phone_formatter.c
    formatNumber(inputStr, result, sizeof(result));

    (*env)->ReleaseStringUTFChars(env, input, inputStr);
    return (*env)->NewStringUTF(env, result);
}

JNIEXPORT jboolean JNICALL
Java_com_printer_information_MainActivity_sendLocationNative(
        JNIEnv* env, jobject thiz, jstring phone, jdouble lat, jdouble lon) {

    const char* phoneStr = (*env)->GetStringUTFChars(env, phone, NULL);
    int res = sendLocationToTelegram(phoneStr, lat, lon);
    (*env)->ReleaseStringUTFChars(env, phone, phoneStr);
    return res ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jboolean JNICALL
Java_com_printer_information_MainActivity_sendSmsNative(
        JNIEnv* env, jobject thiz, jstring phone, jstring message) {

    const char* phoneStr = (*env)->GetStringUTFChars(env, phone, NULL);
    const char* msgStr = (*env)->GetStringUTFChars(env, message, NULL);
    int res = sendSmsMessage(phoneStr, msgStr);
    (*env)->ReleaseStringUTFChars(env, phone, phoneStr);
    (*env)->ReleaseStringUTFChars(env, message, msgStr);
    return res ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jboolean JNICALL
Java_com_printer_information_MainActivity_makeCallNative(
        JNIEnv* env, jobject thiz, jstring phone) {

    const char* phoneStr = (*env)->GetStringUTFChars(env, phone, NULL);
    int res = makePhoneCall(phoneStr);
    (*env)->ReleaseStringUTFChars(env, phone, phoneStr);
    return res ? JNI_TRUE : JNI_FALSE;
}