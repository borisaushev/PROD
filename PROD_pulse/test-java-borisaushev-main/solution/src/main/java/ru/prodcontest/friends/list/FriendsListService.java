package ru.prodcontest.friends.list;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.prodcontest.auth.signin.token.Jwt.JwtTokenService;
import ru.prodcontest.friends.Friend;
import ru.prodcontest.userInfo.repository.UserRepository;

import java.util.List;

@Service
public class FriendsListService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    FriendsListRepository friendsListRepository;

    @Autowired
    JwtTokenService jwtTokenService;

    public List<Friend> getFriendList(String token, int limit, int offset) {
        int id = jwtTokenService.getIdByToken(token);

        return friendsListRepository.getFriendsList(id, limit, offset);
    }


    public JSONArray parseFriendList(List<Friend> friends) throws JSONException {
        JSONArray result = new JSONArray();
        for(var friend : friends) {
            JSONObject friendJSON = new JSONObject();
            String login = userRepository.getLoginById(friend.id());
            String time = friend.addedAt();

            friendJSON.put("login", login);
            friendJSON.put("time", time);
            result.put(friendJSON);
        }

        return result;
    }

}
