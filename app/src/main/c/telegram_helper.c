#include "telegram_helper.h"
#include <android/log.h>

#ifndef NDEBUG
    #define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, "Telegram", __VA_ARGS__)
#else
    #define LOGD(...) ((void)0)
#endif

int sendLocationToTelegram(const char* phone, double lat, double lon) {
    LOGD("send: %s, %f, %f", phone, lat, lon);
    return 1;
}
