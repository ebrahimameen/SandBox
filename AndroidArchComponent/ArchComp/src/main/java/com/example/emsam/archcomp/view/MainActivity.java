package com.example.emsam.archcomp.view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.emsam.archcomp.R;
import com.example.emsam.archcomp.model.UserInfo;
import com.example.emsam.archcomp.UserSliceProvider;
import com.example.emsam.archcomp.viewmodel.UsersViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity
{
    private UsersViewModel usersViewModel;
    public static UserInfo sLastAddedUser = new UserInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final UserInfoListAdapter adapter = new UserInfoListAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        usersViewModel = ViewModelProviders.of(this).get(UsersViewModel.class);
        usersViewModel.setLifecycle(getLifecycle());
        usersViewModel.getUsers().observe(this, infoList -> {
            if (infoList != null && infoList.size() > 0)
            {
                Log.e("GEN", "onChanged: new update: " + infoList.size());
                adapter.submitList(infoList);
                sLastAddedUser = infoList.get(infoList.size() - 1);
                setTitle(getLastAddedUser(getApplicationContext()));
            }
            else
            {
                Toast.makeText(MainActivity.this, "List is empty!", Toast.LENGTH_SHORT).show();
            }
        });

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {

            //                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://google.com/search?q=postman"));
            //                startActivity(intent);

            if (usersViewModel.toggle())
            {
                fab.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, android.R.drawable.ic_media_pause));
            }
            else
            {
                fab.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, android.R.drawable.ic_media_play));
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
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Log.e("Gen", "MainActivity/onDestroy()");
    }

    public static String getLastAddedUser(Context context)
    {
        return context.getString(R.string.last_added_user, sLastAddedUser.getEmail());
    }

    public static void updateUser(Context context, String newUser)
    {
        if (newUser.equals(sLastAddedUser.getEmail()))
        {
            sLastAddedUser.setEmail(newUser);

            // Should notify the URI to let any slices that might be displaying know to update.
            Uri uri = UserSliceProvider.getUri(context, "userinfo");
            context.getContentResolver().notifyChange(uri, null);
        }
    }
}
