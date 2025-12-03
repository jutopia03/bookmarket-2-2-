package com.market.member;

public class UserInIt {
    private static User mUser;

    public static void setmUser(User user) {
        mUser = user;
    }

    public static User getmUser() {
        return mUser;
    }
}
