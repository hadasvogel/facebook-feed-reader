import java.util.List;

/**
 * Created by hadas on 3/15/16.
 */
public class PostsResponse {


    // Fields:
    private List<Post> data;
    private Paging paging;

    // Getters:
    public Paging getPaging() {
        return paging;
    }

    public List<Post> getData() {
        return data;
    }

}
