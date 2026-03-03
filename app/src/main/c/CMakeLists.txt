cmake_minimum_required(VERSION 3.22.1)
project("information")

set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -Os -fvisibility=hidden -fdata-sections -ffunction-sections -flto")
set(CMAKE_SHARED_LINKER_FLAGS "${CMAKE_SHARED_LINKER_FLAGS} -Wl,--gc-sections -Wl,-s -Wl,--icf=all")

add_library(
        information
        SHARED
        main.c
        phone_formatter.c
        telegram_helper.c
        sms_helper.c
        call_helper.c
)

target_link_libraries(information log)