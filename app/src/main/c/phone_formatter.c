#include "phone_formatter.h"
#include <string.h>

void formatNumber(const char* input, char* output, int maxLen) {
    if (!input || !output || maxLen < 2) return;

    // Оставляем только цифры
    char digits[32] = {0};
    int j = 0;
    for (int i = 0; input[i] != '\0' && j < 31; i++) {
        if (input[i] >= '0' && input[i] <= '9') {
            digits[j++] = input[i];
        }
    }

    // Форматируем белорусский номер
    if (j >= 3 && digits[0] == '3' && digits[1] == '7' && digits[2] == '5') {
        char* p = output;
        *p++ = '+';
        *p++ = '3';
        *p++ = '7';
        *p++ = '5';
        *p++ = ' ';
        *p++ = '(';
        for (int i = 3; i < 6 && i < j; i++) *p++ = digits[i];
        *p++ = ')';
        *p++ = ' ';
        for (int i = 6; i < 8 && i < j; i++) *p++ = digits[i];
        if (j > 8) {
            *p++ = '-';
            for (int i = 8; i < 10 && i < j; i++) *p++ = digits[i];
        }
        if (j > 10) {
            *p++ = '-';
            for (int i = 10; i < 12 && i < j; i++) *p++ = digits[i];
        }
        *p = '\0';
    } else {
        strncpy(output, input, maxLen - 1);
        output[maxLen - 1] = '\0';
    }
}