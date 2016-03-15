/**
 * Created by hadas on 3/15/16.
 */
public class Post {

    // Fields:
    private String id;
    private String message;
    private String link;
    private String name;
    private String caption;
    private String description;


    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()){
            return false;
        }

        Post post = (Post) o;
        return !(id != null ? !id.equals(post.id) : post.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return id+"###"+message+"###"+link+"###"+name+"###"+caption+"###"+description;
    }
}
