package com.example.realmtestapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.realmtestapp.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

import android.util.Log;
import android.widget.Button;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import com.example.realmtestapp.model.Task;

import org.bson.types.ObjectId;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    // addkey count
    private int AddCount =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Realm realm_instance = Realm.getDefaultInstance();
                // Log.v("EXAMPLE","Successfully opened the default realm at: " + realm_instance.getPath());
            }
        });
        // initial setting realm
        Realm.init(this); // context, usually an Activity or Application
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("default-realm")
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true)
                .compactOnLaunch()
                //.inMemory()   //インメモリ実行すると，closeで破棄する．
                .build();
        Realm.setDefaultConfiguration(config);

        //add button setting
        final Button buttonDBAdd = findViewById(R.id.button_add_data);
        buttonDBAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // check realm connection(local)
                Realm realm_instance = Realm.getDefaultInstance();
                Log.v("EXAMPLE","Successfully opened the default realm at: " + realm_instance.getPath());

                realm_instance.executeTransaction(r -> {
                    // Instantiate the class using the factory function.
                    ObjectId objectId = new ObjectId();
                    Task task = r.createObject(Task.class, objectId);
                    // Configure the instance.
                    task.setName(String.valueOf(AddCount));
                    AddCount++;
                    r.copyToRealm(task);
                });
                realm_instance.close();
            }
        });
        // list button setting
        final Button buttonDBList = findViewById(R.id.button_list_data);
        buttonDBList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                // check realm connection(local)
                Realm realm_instance = Realm.getDefaultInstance();
                Log.v("EXAMPLE","Successfully opened the default realm at: " + realm_instance.getPath());

                // pick a sample data from DB
                Task task = realm_instance.where(Task.class).findFirst();
                Log.v("EXAMPLE", "Find First object name: " + task.getName());
                // pick all data from DB
                // クエリを宣言
                RealmQuery<Task> searchTaskQuery = realm_instance.where(Task.class);
                //結果を取得
                RealmResults<Task> taskList = searchTaskQuery.findAll();
                Log.v("EXAMPLE", "Task size : " + taskList.size());
                realm_instance.close();
            }
        });
        // delete button setting
        final Button buttonDBDelete = findViewById(R.id.button_delete_data);

        buttonDBDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // check realm connection(local)
                Realm realm_instance = Realm.getDefaultInstance();
                Log.v("EXAMPLE","Successfully opened the default realm at: " + realm_instance.getPath());

                // pick a sample data from DB
                realm_instance.executeTransaction(r -> {
                    Task task = r.where(Task.class).findFirst();
                    if(task!=null){task.deleteFromRealm();}
                    // discard the reference (削除後は使用しない)
                    // task = null;
                });
                realm_instance.close();
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}