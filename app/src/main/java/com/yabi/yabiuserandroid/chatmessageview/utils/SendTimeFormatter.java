package com.yabi.yabiuserandroid.chatmessageview.utils;

import java.util.Calendar;

/**
 * Time formatter of the chat bubble
 * Created by nakayama on 2017/01/13.
 */
public class SendTimeFormatter implements ITimeFormatter {
    @Override
    public String getFormattedTimeText(Calendar createdAt) {
        return com.yabi.yabiuserandroid.chatmessageview.utils.TimeUtils.calendarToString(createdAt, "HH:mm");
    }
}
