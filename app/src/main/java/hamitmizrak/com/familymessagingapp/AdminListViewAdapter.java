package hamitmizrak.com.familymessagingapp;

public class AdminListViewAdapter {

    //Object variable
    private String emailAddres;
    private String image;

    //parametrsiz constructor
    public AdminListViewAdapter() {
    }

    //parametreli constructor
    public AdminListViewAdapter(String emailAddres, String image) {
        this.emailAddres = emailAddres;
        this.image = image;
    }
    //getter and setter
    public String getEmailAddres() {
        return emailAddres;
    }

    public void setEmailAddres(String emailAddres) {
        this.emailAddres = emailAddres;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}//end AdminListViewAdapter
