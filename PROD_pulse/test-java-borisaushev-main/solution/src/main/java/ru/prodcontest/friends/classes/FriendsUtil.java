package ru.prodcontest.friends.classes;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;

public class FriendsUtil {
    private static final HashMap<String, LinkedList<Friend>> friendsMap = new HashMap<>();

    public static void addFriend(String login, Friend friend) {
        if(friendsMap.get(login) == null)
            friendsMap.put(login, new LinkedList<>());

        var friendsList = friendsMap.get(login);
        if(!Objects.equals(login, friend.login()))
            friendsList.add(friend);
    }

    public static void removeFriend(String login, String friendsLogin) {
        if(friendsMap.get(login) == null)
            friendsMap.put(login, new LinkedList<>());

        var friendsList = friendsMap.get(login);
        Friend friendToDelete = null;

        for(Friend friend : friendsList)
            if(Objects.equals(friend.login(), friendsLogin))
                friendToDelete = friend;
        if(friendToDelete != null)
            friendsList.remove(friendToDelete);

    }

    public static LinkedList<Friend> getFriendsList(String login) {
        return friendsMap.get(login);
    }

    public static LinkedList<Friend> getSortedFriendsList(String login) {
        if(friendsMap.get(login) == null)
            friendsMap.put(login, new LinkedList<>());

        var friendsList = friendsMap.get(login);

        friendsList.sort((o1, o2) -> o1.addDate().after(o2.addDate()) ? -1 : 1);
        return friendsList;
    }


}
