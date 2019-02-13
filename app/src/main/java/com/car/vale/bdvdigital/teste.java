package com.car.vale.bdvdigital;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import estruturas.BancoDados;

public class teste extends AppCompatActivity {

    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teste);

        BancoDados db = new BancoDados(this);

        this.img = (ImageView)findViewById(R.id.imageView54);

        this.img.setImageBitmap(db.getImage());

        //startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), 1);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK){
            Bundle extra = data.getExtras();
            Bitmap bmp = (Bitmap) extra.get("data");



        }
    }
}
