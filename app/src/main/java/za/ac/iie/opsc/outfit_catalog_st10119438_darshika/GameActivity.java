package za.ac.iie.opsc.outfit_catalog_st10119438_darshika;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        ImageView percentage1 = (ImageView) findViewById(R.id.percentage1);
        ImageView percentage2 = (ImageView) findViewById(R.id.percentage2);
        ImageView towels1 = (ImageView) findViewById(R.id.towels1);
        ImageView towels2 = (ImageView) findViewById(R.id.towels2);
        ImageView ribbon1 = (ImageView) findViewById(R.id.ribbon1);
        ImageView ribbon2 = (ImageView) findViewById(R.id.ribbon2);


        percentage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                percentage1.setImageResource(R.drawable.circlestyle2);
                percentage2.setImageResource(R.drawable.circlestyle2);
            }
        });

        percentage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               percentage1.setImageResource(R.drawable.circlestyle2);
                percentage2.setImageResource(R.drawable.circlestyle2);
            }
        });

        towels1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                towels1.setImageResource(R.drawable.circlestyle1);
                towels2.setImageResource(R.drawable.circlestyle1);
            }
        });

        towels2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                towels1.setImageResource(R.drawable.circlestyle1);
                towels2.setImageResource(R.drawable.circlestyle1);
            }
        });

        ribbon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ribbon1.setImageResource(R.drawable.circlestyle3);
                ribbon2.setImageResource(R.drawable.circlestyle3);
            }
        });

        ribbon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ribbon1.setImageResource(R.drawable.circlestyle3);
                ribbon2.setImageResource(R.drawable.circlestyle3);
            }
        });
    }
}


