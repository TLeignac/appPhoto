package osmdroid.org.map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VoirPhotoActivity extends Activity {
    private ImageView img;
    private GridLayout layout;
    private ImageView ret;
    private EditText editText;
    private Button filtrer;
    private TextView infos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_voir_photo);
        layout = findViewById(R.id.layout);
        ret = findViewById(R.id.ret);
        filtrer = findViewById(R.id.filtrer);
        editText = findViewById(R.id.editText);
        infos = findViewById(R.id.infos);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int heightScreen = displayMetrics.heightPixels;
        int widthScreen = displayMetrics.widthPixels;


        GallerieBDD galerie = new GallerieBDD(getApplicationContext());
        galerie.open();
        List<Image> tab = new ArrayList<>();
        if(getIntent().getStringExtra("filtre")==null){
            tab = galerie.getAllImage();
        }else{
            tab = galerie.getImageWithLocalisation(getIntent().getStringExtra("filtre"));
            infos.setText("Filtrer par \""+getIntent().getStringExtra("filtre")+"\"");
        }

        if(tab != null){

            layout.removeAllViews();

            int nombreElement = tab.size();
            int colonnes = 3;
            int lignes = (nombreElement / colonnes)+1;

            layout.removeAllViews();
            layout.setColumnCount(colonnes);
            layout.setRowCount(lignes);
            for(int i=0; i<tab.size();i++){
                final String pathImg = tab.get(i).getData();
                Bitmap bMap = BitmapFactory.decodeFile(pathImg);
                ImageView imv = new ImageView(VoirPhotoActivity.this);
                imv.setImageBitmap(bMap);
                imv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(VoirPhotoActivity.this,DetailsPhotoActivity.class);
                        intent.putExtra("details",pathImg);
                        startActivity(intent);
                    }
                });
                imv.setScaleType(ImageView.ScaleType.FIT_START);
                imv.setScaleType(ImageView.ScaleType.FIT_XY);




                imv.setLayoutParams(
                        new GridLayout.LayoutParams(
                                GridLayout.spec(i/colonnes, GridLayout.FILL), // lignes
                                GridLayout.spec(i%colonnes, GridLayout.FILL))); // colonnes
                layout.addView(imv);
                imv.getLayoutParams().height=widthScreen/3;
                imv.getLayoutParams().width=widthScreen/3;

                imv.requestLayout();
                imv.setImageBitmap(bMap);
            }

        }else{
            System.out.println("NULL");
        }
        ret.setImageResource(R.drawable.fleche_retour);
        ret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent act = new Intent(VoirPhotoActivity.this,MainActivity.class);
                startActivity(act);
            }
        });
        filtrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VoirPhotoActivity.this,VoirPhotoActivity.class);
                intent.putExtra("filtre",editText.getText().toString());
                startActivity(intent);
            }
        });
    }
}
