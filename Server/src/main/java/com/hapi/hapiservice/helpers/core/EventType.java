package com.hapi.hapiservice.helpers.core;

public enum EventType {

    HELLO, MESSAGE, DIRECT_MENTION, DIRECT_MESSAGE, USER_TYPING, CHANNEL_MARKED, CHANNEL_CREATED, CHANNEL_JOINED,
    CHANNEL_LEFT, CHANNEL_DELETED, CHANNEL_RENAME, CHANNEL_ARCHIVE, CHANNEL_UNARCHIVE, CHANNEL_HISTORY_CHANGED,
    DND_UPDATED, DND_UPDATED_USER, IM_CREATED, IM_OPEN, IM_CLOSE, IM_MARKED, IM_HISTORY_CHANGED, GROUP_JOINED,
    GROUP_LEFT, GROUP_OPEN, GROUP_CLOSE, GROUP_ARCHIVE, GROUP_UNARCHIVE, GROUP_RENAME, GROUP_MARKED,
    GROUP_HISTORY_CHANGED, FILE_CREATED, FILE_SHARED, FILE_UNSHARED, FILE_PUBLIC, FILE_PRIVATE, FILE_CHANGE,
    FILE_DELETED, FILE_COMMENT_ADDED, FILE_COMMENT_EDITED, FILE_COMMENT_DELETED, PING, PONG, PIN_ADDED, PIN_REMOVED,
    PRESENCE_CHANGE, MANUAL_PRESENCE_CHANGE, PREF_CHANGE, USER_CHANGE,
    TEAM_JOIN, STAR_ADDED, STAR_REMOVED, REACTION_ADDED, REACTION_REMOVED, EMOJI_CHANGED, COMMANDS_CHANGED,
    TEAM_PLAN_CHANGE, TEAM_PREF_CHANGE, TEAM_RENAME, TEAM_DOMAIN_CHANGE, EMAIL_DOMAIN_CHANGED, TEAM_PROFILE_CHANGE,
    TEAM_PROFILE_DELETE, TEAM_PROFILE_REORDER, BOT_ADDED, BOT_CHANGED, ACCOUNTS_CHANGED, TEAM_MIGRATION_STARTED,
    RECONNECT_URL, SUBTEAM_CREATED, SUBTEAM_UPDATED, SUBTEAM_SELF_ADDED, SUBTEAM_SELF_REMOVED, ACK, SUBSCRIBE,
    MESSAGE_DELIVERED, MESSAGE_READ, MESSAGE_ECHO, POSTBACK, OPT_IN, REFERRAL, PAYMENT, CHECKOUT_UPDATE,
    PRE_CHECKOUT, ACCOUNT_LINKING, QUICK_REPLY, MEMBER_JOINED_CHANNEL, MEMBER_LEFT_CHANNEL

}
