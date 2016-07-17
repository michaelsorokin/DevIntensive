package devintensive.softdesign.com.devintensive.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import java.util.List;

import devintensive.softdesign.com.devintensive.R;
import devintensive.softdesign.com.devintensive.data.managers.DataManager;
import devintensive.softdesign.com.devintensive.data.network.res.UserListRes;
import devintensive.softdesign.com.devintensive.data.storage.models.UserDTO;
import devintensive.softdesign.com.devintensive.ui.adapters.UsersAdapter;
import devintensive.softdesign.com.devintensive.utils.ConstantManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserListActivity extends AppCompatActivity {

    static final String TAG = ConstantManager.TAG_PREFIX + " UserListActivity";

    private CoordinatorLayout mCoordinatorLayout;
    private Toolbar mToolbar;
    private DrawerLayout mNavigationDrawer;
    private RecyclerView mRecyclerView;

    private DataManager mDataManager;
    private UsersAdapter mUsersAdapter;
    private List<UserListRes.UserData> mUsers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        mDataManager = DataManager.getInstance();
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator_container);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mNavigationDrawer = (DrawerLayout) findViewById(R.id.navigation_drawer);
        mRecyclerView = (RecyclerView) findViewById(R.id.user_list);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        setupToolbar();
        setupDrawer();
        loadUsers();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home) {
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSnackbar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void setupToolbar() {

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupDrawer() {

    }

    private void loadUsers() {

        Call<UserListRes> call = mDataManager.getUserList();

        call.enqueue(new Callback<UserListRes>() {
            @Override
            public void onResponse(Call<UserListRes> call, Response<UserListRes> response) {

                try {
                    if (response.code() == 200) {
                        mUsers = response.body().getData();
                        mUsersAdapter = new UsersAdapter(mUsers, new UsersAdapter.UserViewHolder.CustomClickListener() {
                            @Override
                            public void onUserItemClickListener(int position) {

                                UserDTO userDTO = new UserDTO(mUsers.get(position));
                                Intent profileIntent = new Intent(UserListActivity.this, ProfileUserActivity.class);
                                profileIntent.putExtra(ConstantManager.PARCELABLE_KEY, userDTO);
                                startActivity(profileIntent);
                            }
                        });
                        mRecyclerView.setAdapter(mUsersAdapter);
                    }
                } catch (NullPointerException ex) {
                    Log.e(TAG, ex.toString());
                    showSnackbar("Ошибка в программе!");
                }
            }

            @Override
            public void onFailure(Call<UserListRes> call, Throwable t) {
                // TODO: 15.07.2016 обработать ошибки
            }
        });

    }

}
