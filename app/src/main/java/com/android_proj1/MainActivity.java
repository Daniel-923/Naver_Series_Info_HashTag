package com.android_proj1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android_proj1.Fragment.FragmentRecommend;
import com.android_proj1.Fragment.FragmentSearch;
import com.android_proj1.Fragment.FragmentTop100;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    TextView textView1;
    EditText editText;
    private DbOpenHelper dbOpenHelper;
    private WebNovelManager webNovelManager;
    private UserInput userInput;
    Button btnSelete, btnUpdate, btnDelete;

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentSearch fragmentSearch = new FragmentSearch();
    private FragmentRecommend fragmentRecommend = new FragmentRecommend();
    private FragmentTop100 fragmentTop100 = new FragmentTop100();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
/*        textView1 = (TextView) findViewById(R.id.textView);
        editText = (EditText) findViewById(R.id.et_title);
        editText.setOnEditorActionListener(setTitle);
        btnSelete = (Button) findViewById(R.id.btn_selete);
        btnUpdate = (Button) findViewById(R.id.btn_update);*/
        final Bundle bundle = new Bundle();

        dbOpenHelper = new DbOpenHelper(this);

/*
        dbOpenHelper.open();
        dbOpenHelper.create();

        webNovelManager = new WebNovelManager(dbOpenHelper);


        String category = "?????????";
        String title = "??????";
        
        //userInput = new UserInput("category", category);
        userInput = new UserInput("title", title);
        webNovelManager.service(WebNovelService.Name.SEARCH_READ, userInput);*/

/*        ServeThread thread = new ServeThread(dbOpenHelper);
        thread.start();*/

        if (Build.VERSION.SDK_INT >= 30){
            if (!Environment.isExternalStorageManager()){
                Intent getpermission = new Intent();
                getpermission.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(getpermission);
            }
        }


        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragmentSearch).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());

    }


    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch(menuItem.getItemId())
            {
                case R.id.searchItem:
                    transaction.replace(R.id.frameLayout, fragmentSearch).commitAllowingStateLoss();
                    break;
                case R.id.recommendItem:
                    transaction.replace(R.id.frameLayout, fragmentRecommend).commitAllowingStateLoss();
                    break;
                case R.id.top100Item:
                    transaction.replace(R.id.frameLayout, fragmentTop100).commitAllowingStateLoss();
                    break;
            }
            return true;
        }
    }


    private class ServeThread extends Thread {
        private DbOpenHelper dbOpenHelper;

        public ServeThread(DbOpenHelper dbOpenHelper) {
            this.dbOpenHelper = dbOpenHelper;
        }

        @Override
        public void run() {
            super.run();

            dbOpenHelper.open();
            dbOpenHelper.create();

            webNovelManager = new WebNovelManager(dbOpenHelper);

            String category = "?????????";
            String title = "??????";

            //userInput = new UserInput("title", title);

            userInput = new UserInput("category", category);
            webNovelManager.service(WebNovelService.Name.TOP100_CREATE, userInput);


            dbOpenHelper.runCSV();


        }
    }



    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            //textView.setText(bundle.getString("numbers"));                      //??????????????? View??? ?????? ??????????????? ??????????????????.
        }
    };


    private TextView.OnEditorActionListener setTitle = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            // ????????? ????????? ????????????.
            String searchData = textView.getText().toString();


            // ????????? ????????? ???????????????...
            if (searchData.isEmpty()) {

                // ????????? ???????????? ?????????, ??? ????????? ?????????
                Toast.makeText(MainActivity.this, "????????? ??????????????????", Toast.LENGTH_SHORT).show();
                textView.clearFocus();
                textView.setFocusable(false);
                textView.setFocusableInTouchMode(true);
                textView.setFocusable(true);

                return true;
            }

            // ????????? ????????? ?????????????????????
            switch (i) {

                // Search ???????????????
                case EditorInfo.IME_ACTION_SEARCH:

                    String search_title = editText.getText().toString();
                    textView1.setText(search_title);

                    String title = "title";

                    userInput = new UserInput(title, search_title);

                    // top100 ??????
                    webNovelManager.service(WebNovelService.Name.SEARCH_CREATE, userInput);

                    break;

                // Enter ???????????????
                default:

                    // TODO : Write Your Code

                    return false;
            }

            // ?????? ????????? ?????? ????????? ???????????? ??????
            textView.clearFocus();
            textView.setFocusable(false);
            textView.setText("");
            textView.setFocusableInTouchMode(true);
            textView.setFocusable(true);

            return true;

        }
    };


}