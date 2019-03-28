package osmdroid.org.map;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

public class DetailsPhotoActivity extends Activity {
    private RelativeLayout layout;
    private ImageView imv;
    private ImageView back;
    private Button partage;
    private Button supprimer;
    private Button carte;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_details_photo);
        layout = findViewById(R.id.photoLayout);
        imv = findViewById(R.id.imageView);
        back = findViewById(R.id.back);
        partage = findViewById(R.id.partage);
        supprimer = findViewById(R.id.supprimer);
        carte = findViewById(R.id.carte);

        imv.setImageBitmap(BitmapFactory.decodeFile(getIntent().getStringExtra("details")));
        back.setImageResource(R.drawable.fleche_retour);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailsPhotoActivity.this,VoirPhotoActivity.class);
                startActivity(intent);
            }
        });

        partage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap b =BitmapFactory.decodeFile(getIntent().getStringExtra("details"));
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/jpeg");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                b.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(getContentResolver(),
                        b, "Title", null);
                Uri imageUri =  Uri.parse(path);
                share.putExtra(Intent.EXTRA_STREAM, imageUri);
                startActivity(Intent.createChooser(share, "Par quelle voie ?"));
            }
        });

        supprimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailsPhotoActivity.this,VoirPhotoActivity.class);
                GallerieBDD galerie = new GallerieBDD(getApplicationContext());
                galerie.open();
                galerie.SuppressionImage(getIntent().getStringExtra("details"));
                galerie.close();
                startActivity(intent);

            }
        });
        carte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailsPhotoActivity.this,CarteActivity.class);
                startActivity(intent);
            }
        });
    }
}
