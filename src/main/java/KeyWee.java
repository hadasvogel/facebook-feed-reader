import com.google.gson.Gson;

import java.io.FileWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by hadas on 3/15/16.
 */
public class KeyWee {

    final private static Gson GSON = new Gson();
    final private static String OUTPUT_FILENAME = "OutputKeywee.csv";
    final private static String IDS_OUTPUT_FILENAME = "ids_"+ OUTPUT_FILENAME;
    // Url
    final private static String BASE_URL = "https://graph.facebook.com/v2.5/nytimes/feed";
    final private static String FIELDS = "id,message,link,name,caption,description";
    final private static String FORMAT = "json";
    final private static int LIMIT = 100;

    public static void main(String[] args) throws Exception {

        // Set to contain all unique posts
        Set<Post> finalPosts = new HashSet<Post>();

        String accessToken = (args!=null && args.length==1 && args[0]!=null && !args[0].isEmpty())? args[0] : "CAACEdEose0cBAFFp9Rt2DL5oWfiznokBhSZAsuKZCCZAxLTQ3qPAdPaoQN2EauZBFjcdUhcOu3GfOQkxF2KLYFa7uoByIOZBOewCK3iLrqTExfslSInfZB4dEfsfxnrkzipJ7JgMqej03XIdYn2cKwn8aDJh9KtH56jDA0RMzPWHaNS4lnL40MnZBLrvghDU5HhpVhmhh9B3FlsTZBtdkcjm";
        String url = String.format("%s?fields=%s&format=%s&access_token=%s&limit=%d", BASE_URL, FIELDS, FORMAT, accessToken, LIMIT);


        String response = HttpRequester.sendGet(url);
        PostsResponse postsResponse = GSON.fromJson(response, PostsResponse.class);
        addNewPosts(finalPosts, postsResponse);

        int reqAmount = 1;
        // As long as we still do not have 1000 unique posts, perform a fetch from Facebook
        while (finalPosts.size()<1000) {
            // Get new posts from FB:
            String nextUrl = postsResponse.getPaging().getNext();
            int left = 1000-finalPosts.size();
            if (left<100)
            {
                nextUrl = nextUrl.replaceAll("limit=\\d+","limit="+left);
            }
            response = HttpRequester.sendGet(nextUrl);
            postsResponse = GSON.fromJson(response, PostsResponse.class);
            addNewPosts(finalPosts, postsResponse);
            reqAmount++;
        }
        System.out.println("final posts size: ["+finalPosts.size()+"], in ["+reqAmount+"] requests");

        // Write to file:
        FileWriter writer = new FileWriter(OUTPUT_FILENAME);
        FileWriter writerIds = new FileWriter(IDS_OUTPUT_FILENAME);

        for (Post post : finalPosts) {
            writer.write(post.toString()+"\n");
            writerIds.write(post.getId()+"\n");
        }
        writer.close();
        writerIds.close();

        System.out.println("=======DONE=======");
        System.out.println("wrote posts to a file named: ["+ OUTPUT_FILENAME +"]");
        System.out.println("wrote only ids to a file named: ["+ IDS_OUTPUT_FILENAME +"]");
    }

    private static void addNewPosts(Set<Post> finalPosts, PostsResponse response) {

        List<Post> originalPosts = response.getData();
        System.out.println("originalSize:" +originalPosts.size());
        finalPosts.addAll(originalPosts);
        System.out.println("finalSize:"+finalPosts.size());
    }
}
