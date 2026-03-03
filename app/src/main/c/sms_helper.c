#include "sms_helper.h"
#include <android/log.h>

#ifndef NDEBUG
    #define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, "SMS", __VA_ARGS__)
#else
    #define LOGD(...) ((void)0)
#endif

int sendSmsMessage(const char* phone, const char* message) {
    LOGD("sms: %s, %s", phone, message);
    return 1;
}
