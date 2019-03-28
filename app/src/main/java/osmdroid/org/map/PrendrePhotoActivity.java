package osmdroid.org.map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PrendrePhotoActivity extends Activity {
    // constante
    private static final int RETOUR_PRENDRE_PHOTO = 1;
    //propriétés
    private Button btnPrendrePhoto;
    private String photoPath = null;
    private ImageView decor;

    private ImageView retour;


    LinearLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prendre_photo);
        initActivity();
        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent main;
                main = new Intent(PrendrePhotoActivity.this,MainActivity.class);
                startActivity(main);

            }
        });

    }
    private void initActivity(){
        //récupération des objets graphiques
        btnPrendrePhoto=(Button)findViewById(R.id.btnPrendrePhoto);
        retour = findViewById(R.id.retour);
        decor = findViewById(R.id.decor);

        retour.setImageResource(R.drawable.fleche_retour);
        decor.setImageResource(R.drawable.appareil_photo);

        //méthode pour créer les évènements
        createOnClicBtnPrendrePhoto();
    }
    private void createOnClicBtnPrendrePhoto(){
        btnPrendrePhoto.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                prendreUnePhoto();
            }
        });
    }
    private void prendreUnePhoto(){
        //Créer un Intent pour ouvrir une fenetre pour prendre la photo
        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Test pour controle que l'intent preut être géré
        if(intent.resolveActivity(getPackageManager())!=null){
            //créer un non de fichier temporaire unique
            String time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File photoDir= getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            try {
                File photoFile = File.createTempFile("photo"+time,".jpg",photoDir);
                // enregistrer le chemin complet
                photoPath = photoFile.getAbsolutePath();
                // créer l'URI
                Uri photoUri = FileProvider.getUriForFile(PrendrePhotoActivity.this,
                        PrendrePhotoActivity.this.getApplicationContext().getPackageName()+".provider",
                        photoFile);
                // transfert uri vers l'intent pour enregistrement photo dans fichier temporaire
                intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
                // ouvrir l'activity par rapport à l'intent
                startActivityForResult(intent, RETOUR_PRENDRE_PHOTO);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    /**
     * retour de l'appel de l'appareil photo (startActivityForResult)
     * @param requestcode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestcode, int resultCode, Intent data){
        super.onActivityResult(requestcode,resultCode,data);
        // vérifie le bon code de retour  et l'état du retour ok
        if(requestcode==RETOUR_PRENDRE_PHOTO && resultCode==RESULT_OK){
            //récupérer l'image
            Bitmap image = BitmapFactory.decodeFile(photoPath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 1, baos);
            image = new Conversion().RotateBitmap(image,90);
            ImageView img=new ImageView(PrendrePhotoActivity.this);
            new EcritureCarteSd(getApplicationContext()).writeFile(img,image);

        }
    }
}
