package osmdroid.org.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class EcritureCarteSd {

    private Context context;
    private static int cpt=0;


    public EcritureCarteSd(Context context){
        this.context=context;
    }



    private boolean isExternalStorageWritable(){
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            Log.i("State","Yes, it is writable!");
            return true;
        }else{
            return false;
        }
    }
    public void writeFile(View v,Bitmap photo){


        if(isExternalStorageWritable() && checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ){



            File file = new File(context.getExternalFilesDir(
                    Environment.DIRECTORY_DOWNLOADS), "SAUVEGARDE");
            if (!file.mkdirs()) {
                Log.e("1", "Directory not created");
            }
            File image = new File(file, "image"+cpt+".jpg");
            cpt++;
            System.out.print(image.getAbsolutePath());


            GallerieBDD galerie = new GallerieBDD(this.context);
            Image imgBD = new Image(image.getAbsolutePath(),"COUZEIX");
            galerie.open();
            galerie.insertImage(imgBD);
            galerie.close();


            try{

                FileOutputStream img = new FileOutputStream(image);

                photo.compress(Bitmap.CompressFormat.PNG, 100, img);
                img.close();


                System.out.println("File Saved");
            }catch (IOException e){
                e.printStackTrace();
            }

        }else{
            System.out.println("Cannot right to external storage");
        }
    }
    public boolean checkPermission(String permission){
        int check = ContextCompat.checkSelfPermission(context,permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }
    public static Bitmap loadFrom(String url, Context context, int defaultDrawable) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            try {
                return BitmapFactory.decodeStream(is);
            } finally {
                is.close();
            }
        } catch (Exception e) {
            return BitmapFactory.decodeResource(context.getResources(), defaultDrawable);
        }
    }
}
