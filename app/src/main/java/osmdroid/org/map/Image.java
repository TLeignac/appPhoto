package osmdroid.org.map;

public class Image {
    private int id;
    private String data;
    private String localisation;

    public Image(){

    }
    public Image(String data,String localisation){
        this.data=data;
        this.localisation=localisation;
    }

    public int getId(){
        return this.id;
    }
    public String getData(){
        return this.data;
    }
    public String getLocalisation(){
        return this.localisation;
    }
    public void setId(int id){
        this.id=id;
    }
    public void setLocalisation(String localisation){
        this.localisation=localisation;
    }
    public void setData(String data){
        this.data=data;
    }

    public String toString(){
        return ""+this.id+" "+this.localisation;
    }
}
