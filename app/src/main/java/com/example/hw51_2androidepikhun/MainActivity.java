package com.example.hw51_2androidepikhun;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_PERMISSION_READ_STORAGE = 10;
    public static final int REQUEST_CODE_PERMISSION_WRITE_STORAGE = 11;


    final int[] buttonsId = new int[]{};
    final Button[] buttons = new Button[buttonsId.length];

    private LinearLayout container;
    private TextView calcScreen;
    private TextView calcTypeTxt;
    private View standardKeyboard;
    private View sciKeyboard;


    private ImageView backgroundImg;

    private View filenameLayout;
    private static String fileName = null;
    private EditText editTextFileName;


    private static final int CLEAR_BACKROUND = 0;
    private static final int DEFAULT_BACKROUND = 1;
    private static final int LOADED_BACKROUND = 2;
    private static int backgroundMode = DEFAULT_BACKROUND;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //получаем статус разрешения на чтение из файлового хранилища
        int permissionStatus = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_PERMISSION_READ_STORAGE);
        }


        Toolbar settingsToolbar = findViewById(R.id.settings_toolbar);
        setSupportActionBar(settingsToolbar);
        getSupportActionBar().setTitle("");

        InitViews();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.filemenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.clearfont) {
            backgroundMode = CLEAR_BACKROUND;
            setBackgroundMode();
            return true;
        }

        if (id == R.id.defaultfont) {
            backgroundMode = DEFAULT_BACKROUND;
            setBackgroundMode();
            return true;
        }

        if (id == R.id.load_font) {
            filenameLayout.setVisibility(View.VISIBLE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void InitViews() {

        for (int i = 0; i < buttonsId.length; i++) {
            buttons[i] = findViewById(buttonsId[i]);
            buttons[i].setOnClickListener(onClickListener);
        }
        calcScreen = findViewById(R.id.calcScreen);
        container = findViewById(R.id.linLayout);
        Switch calcTypeSwitch = findViewById(R.id.calc_type_switch);
        standardKeyboard = findViewById(R.id.std_keyboard);
        sciKeyboard = findViewById(R.id.sci_keyboard);
        calcTypeTxt = findViewById(R.id.calc_type_txt);

        backgroundImg = findViewById(R.id.backroundImg);

        editTextFileName = findViewById(R.id.editTextFileName);
        Button loadButton = findViewById(R.id.loadButton);
        filenameLayout = findViewById(R.id.filenameLayout);


        loadButton.setOnClickListener(onClickListener);


        loadImage(fileName);

        setBackgroundMode();

        calcTypeSwitch.setOnCheckedChangeListener(switchOnChecked);
        calcScreen.setText("0");
    }

    public void setBackgroundMode() {
        switch (backgroundMode) {
            case LOADED_BACKROUND:
                backgroundImg.setImageResource(R.drawable.klip_1);

                break;
            case CLEAR_BACKROUND:
                backgroundImg.setImageResource(R.drawable.ic_launcher_background);

                break;
            default:
                backgroundImg.setImageResource(R.drawable.klip_2);

                break;
        }
    }


    final Switch.OnCheckedChangeListener switchOnChecked = new Switch.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                calcTypeTxt.setText(getString(R.string.sci_type));
                standardKeyboard.setVisibility(View.GONE);
                sciKeyboard.setVisibility(View.VISIBLE);
            } else {
                calcTypeTxt.setText(getString(R.string.std_type));
                standardKeyboard.setVisibility(View.VISIBLE);
                sciKeyboard.setVisibility(View.GONE);
            }
        }
    };


    final Button.OnClickListener onClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            String result = "0";

            switch (v.getId()) {
                case R.id.loadButton:
                    fileName = editTextFileName.getText().toString();
                    filenameLayout.setVisibility(View.INVISIBLE);
                    if (loadImage(fileName)) {
                        Toast.makeText(MainActivity.this, getString(R.string.file_loaded), Toast.LENGTH_LONG).show();
                        backgroundMode = LOADED_BACKROUND;
                        setBackgroundMode();
                    } else {
                        Toast.makeText(MainActivity.this, getString(R.string.load_error), Toast.LENGTH_LONG).show();
                    }
                    break;


                default:
                    break;
            }

            calcScreen.setText(result);

        }


    };


    public boolean loadImage(String fileName) {

        if (fileName == null) return false;
        if (fileName.length() == 0) return false;

        if (isExternalStorageWritable()) {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    fileName);
            Bitmap b = BitmapFactory.decodeFile(file.getAbsolutePath());
            if (b == null) return false;
//
            backgroundImg.setImageBitmap(b);
        } else {
            Toast.makeText(this, "File Error", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

}