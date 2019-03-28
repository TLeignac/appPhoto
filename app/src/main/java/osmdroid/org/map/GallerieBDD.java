package osmdroid.org.map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class GallerieBDD {
    private static final int VERSION_BDD = 1;
    private static final String NOM_BDD="galerie.db";

    private static final String TABLE_IMAGES="table_images";
    private static final String COL_ID = "ID";
    private static final int NUM_COL_ID=0;
    private static final String COL_LOCALISATION="LOCALISATION";
    private static final int NUM_COL_LOCALISATION=1;
    private static final String COL_DATA="DATA";
    private static final int NUM_COL_DATA=2;

    private SQLiteDatabase bdd;

    private MaBaseSqLite maBaseSQLite;

    public GallerieBDD(Context context){
        maBaseSQLite = new MaBaseSqLite(context, NOM_BDD, null, VERSION_BDD);
    }
    public void open(){
        bdd = maBaseSQLite.getWritableDatabase();
    }
    public void close(){
        bdd.close();
    }
    public SQLiteDatabase getBDD(){
        return bdd;
    }

    public long insertImage(Image image){
        ContentValues values = new ContentValues();

        values.put(COL_LOCALISATION,image.getLocalisation());
        values.put(COL_DATA,image.getData());
        return bdd.insert(TABLE_IMAGES,null,values);

    }
    public int updateImage(int id, Image image){
        ContentValues values = new ContentValues();
        values.put(COL_LOCALISATION,image.getLocalisation());
        values.put(COL_DATA,image.getData());
        return bdd.update(TABLE_IMAGES,values,COL_ID+" = "+id,null);
    }
    public int removeLivreWithID(int id){
        return bdd.delete(TABLE_IMAGES, COL_ID+" = "+id,null);

    }
    public List<Image> getImageWithLocalisation(String localisation){
        Cursor c = bdd.query(TABLE_IMAGES, new String[] {COL_ID,COL_LOCALISATION,COL_DATA},COL_LOCALISATION+
                " LIKE \""+localisation+"\"",null,null,null,null);
        return tabImage(c);
    }
    public List<Image> getAllImage(){
        Cursor c = bdd.query(TABLE_IMAGES, new String[] {COL_ID,COL_LOCALISATION,COL_DATA},null,null,null,null,null);
        return tabImage(c);
    }
    public int SuppressionImage(String data){
        return bdd.delete(TABLE_IMAGES,COL_DATA+" = '"+data+"'",null);
    }
    private Image cursorToImage (Cursor c){
        if(c.getCount()==0){
            return null;

        }
        c.moveToFirst();
        Image image = new Image();
        image.setId(c.getInt(NUM_COL_ID));
        image.setLocalisation(c.getString(NUM_COL_LOCALISATION));
        image.setData(c.getString(NUM_COL_DATA));
        c.close();
        return image;
    }
    private List<Image> tabImage (Cursor c){
        List<Image> tab = new ArrayList<Image>();
        if(c.getCount()==0){
            return null;

        }

        c.moveToFirst();
        while(c.isLast()!=true){
            Image image = new Image();
            image.setId(c.getInt(NUM_COL_ID));
            image.setLocalisation(c.getString(NUM_COL_LOCALISATION));
            image.setData(c.getString(NUM_COL_DATA));
            tab.add(image);
            c.moveToNext();
        }
        Image image = new Image();
        image.setId(c.getInt(NUM_COL_ID));
        image.setLocalisation(c.getString(NUM_COL_LOCALISATION));
        image.setData(c.getString(NUM_COL_DATA));
        tab.add(image);


        c.close();
        return tab;
    }
}
