#include "call_helper.h"
#include <android/log.h>

#ifndef NDEBUG
    #define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, "Call", __VA_ARGS__)
#else
    #define LOGD(...) ((void)0)
#endif

int makePhoneCall(const char* phone) {
    LOGD("call: %s", phone);
    return 1;
}
