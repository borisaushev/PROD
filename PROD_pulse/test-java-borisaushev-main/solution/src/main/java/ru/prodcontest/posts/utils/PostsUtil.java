package ru.prodcontest.posts.utils;

import org.json.JSONException;
import org.json.JSONObject;
import ru.prodcontest.posts.classes.Post;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;

public class PostsUtil {

    private final static HashMap<String, LinkedList<Post>> postsMap = new HashMap<>();
    private final static HashSet<Post> postsSet = new LinkedHashSet<>();

    public static void addPost(String author, Post post) {
        if(postsMap.get(author) == null)
            postsMap.put(author, new LinkedList<>());

        postsMap.get(author).add(post);
        postsSet.add(post);
    }

    public static Post getPostByPostId(String postId) {
        for(Post post : postsSet)
            if(post.id().equals(postId))
                return post;
        return null;
    }

    public static LinkedList<Post> getSortedPostsList(String login) {
        if(postsMap.get(login) == null)
            postsMap.put(login, new LinkedList<>());

        var postList = postsMap.get(login);

        postList.sort((o1, o2) -> o1.createdAt().after(o2.createdAt()) ? -1 : 1);
        return postList;
    }

    public static JSONObject getPostJsonObject(Post post) throws JSONException {
        JSONObject postJson = new JSONObject();
        postJson.put("id", post.id());
        postJson.put("content", post.content());
        postJson.put("tags", post.tags());
        postJson.put("createdAt", post.createdAt());
        postJson.put("likesCount", post.likesCount());
        postJson.put("dislikesCount", post.dislikesCount());

        return postJson;
    }

}
