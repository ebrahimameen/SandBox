package com.example.emsam.archcomp.view;

import java.util.List;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.emsam.archcomp.R;
import com.example.emsam.archcomp.model.UserInfo;
import com.example.emsam.archcomp.viewmodel.UsersViewModel;

public class MainActivity extends AppCompatActivity
{
    private UsersViewModel usersViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final UserInfoListAdapter adapter = new UserInfoListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        usersViewModel = ViewModelProviders.of(this).get(UsersViewModel.class);
        usersViewModel.setLifecycle(getLifecycle());
        usersViewModel.getUsers().observe(this, new Observer<List<UserInfo>>()
        {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(@Nullable final List<UserInfo> infoList)
            {
                if (infoList != null && infoList.size() > 0)
                {
                    Log.e("GEN", "onChanged: new update: " + infoList.size());
                    adapter.setUsers(infoList);
                }
                else
                {
                    Toast.makeText(MainActivity.this, "List is empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                //                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://google.com/search?q=postman"));
                //                startActivity(intent);

                if (usersViewModel.toggle())
                {
                    fab.setImageDrawable(
                            ContextCompat.getDrawable(MainActivity.this, android.R.drawable.ic_media_pause));
                }
                else
                {
                    fab.setImageDrawable(
                            ContextCompat.getDrawable(MainActivity.this, android.R.drawable.ic_media_play));
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        Log.e("Gen", "MainActivity/onDestroy()");
    }
}
