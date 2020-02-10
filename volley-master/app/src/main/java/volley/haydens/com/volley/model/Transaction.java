package volley.haydens.com.volley.model;

public class Transaction {

   int id;
   String title;
   Float rate;
   Float qty;
   int categoryId;
   String tType;
   int author;
   String attachment;
   String description;

    public Transaction(int id, String title, Float rate, Float qty, int categoryId, String tType, int author, String attachment, String description) {
        this.id = id;
        this.title = title;
        this.rate = rate;
        this.qty = qty;
        this.categoryId = categoryId;
        this.tType = tType;
        this.author = author;
        this.attachment = attachment;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    public Float getQty() {
        return qty;
    }

    public void setQty(Float qty) {
        this.qty = qty;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String gettType() {
        return tType;
    }

    public void settType(String tType) {
        this.tType = tType;
    }

    public int getAuthor() {
        return author;
    }

    public void setAuthor(int author) {
        this.author = author;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
