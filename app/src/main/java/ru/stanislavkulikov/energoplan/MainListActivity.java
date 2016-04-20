package ru.stanislavkulikov.energoplan;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainListActivity extends AppCompatActivity implements
        AddNewHomesteadFragment.OnFragmentInteractionListener,
        MainListFragment.OnFragmentInteractionListener,
        HomesteadFragment.OnFragmentInteractionListener {

    DataBase myDataBase;
    FragmentTransaction fTrans;
    MainListFragment mainListFragment;
    AddNewHomesteadFragment newHomesteadFragment;
    HomesteadFragment homesteadFragment;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // открываем подключение к БД
        myDataBase = new DataBase(this);
        myDataBase.open();

        fTrans = getSupportFragmentManager().beginTransaction();
        mainListFragment = new MainListFragment();
        fTrans.add(R.id.fragmentContainer, mainListFragment);
        fTrans.commit();

        // открываем фрагмент новой записи по нажатию кнопки
        fab = (FloatingActionButton) findViewById(R.id.add_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fTrans = getSupportFragmentManager().beginTransaction();
                newHomesteadFragment = new AddNewHomesteadFragment();
                fTrans.replace(R.id.fragmentContainer, newHomesteadFragment);
                fTrans.commit();
                view.setVisibility(View.INVISIBLE);
            }
        });
    }

    protected void onDestroy() {
        super.onDestroy();
        // закрываем подключение при выходе
        myDataBase.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_list, menu);
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

    public void onFragmentAddNewHomestead() {
        fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.replace(R.id.fragmentContainer, mainListFragment);
        fTrans.commit();
        fab.setVisibility(View.VISIBLE);
        getSupportLoaderManager().getLoader(0).forceLoad();
    }

    public void onHomesteadListItemClick(Cursor cursor) {
        fTrans = getSupportFragmentManager().beginTransaction();
        homesteadFragment = HomesteadFragment.newInstance(cursor.getInt(cursor.getColumnIndex(DataBase.COLUMN_ID)));
        fTrans.replace(R.id.fragmentContainer, homesteadFragment);
        fTrans.commit();
        fab.setVisibility(View.INVISIBLE);
    }

    public void onFragmentBackPressed() {
        fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.replace(R.id.fragmentContainer, mainListFragment);
        fTrans.commit();
        fab.setVisibility(View.VISIBLE);
    }
}
