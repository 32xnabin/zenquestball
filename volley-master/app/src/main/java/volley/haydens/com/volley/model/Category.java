package volley.haydens.com.volley.model;

public class Category {

   int id;
   String title;
   String description;
   String tType;
   int author;

    public Category(int id, String title, String description, String tType, int author) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.tType = tType;
        this.author = author;
    }
}
