package br.com.acmestore;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import br.com.acmestore.data.entity.User;
import br.com.acmestore.product.productadd.ProductAddActivity;
import br.com.acmestore.product.products.ProductsFragment;
import br.com.acmestore.user.userdetail.UserDetailFragment;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ProductsFragment mProductsFragment;
    private ActionBar mActionBar;
    private User currentUser;

    private FloatingActionButton mFabProductAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentUser = (User) getIntent().getExtras().get(Constants.INTENT_KEY_USER);

        setupActionBar();
        setupComponents();

        if (null == savedInstanceState) {
            mProductsFragment = createProductsFragment(null, "TOSELL", true, false, "all");
            initFragment(mProductsFragment);
        }
    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mActionBar = getSupportActionBar();

        mActionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        //Exibe o botão home
        mActionBar.setDisplayShowHomeEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);

        //Habilita a sobrescrita do layout da toolbar padrão
        mActionBar.setDisplayShowCustomEnabled(true);

        mActionBar.setDisplayShowTitleEnabled(true);
        mActionBar.setTitle("All");
    }

    private void setupComponents() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackground(R.color.colorPrimary);

        mFabProductAdd = (FloatingActionButton) findViewById(R.id.fab_productadd);
        mFabProductAdd.setImageResource(R.drawable.ic_add_white_24dp);
        mFabProductAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProductAddActivity.class);
                intent.putExtra(Constants.INTENT_KEY_USER, currentUser);
                startActivityForResult(intent, Constants.REQUESTCODE_PRODUCTADD);
            }
        });
        mFabProductAdd.hide();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        setupDrawerContent(navigationView);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setCheckedItem(R.id.nav_products);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.nav_products:

                                mActionBar.setTitle("All");
                                mFabProductAdd.hide();
                                initFragment(mProductsFragment);
                                break;

                            case R.id.nav_purchases:

                                mActionBar.setTitle("Purchased");
                                mFabProductAdd.hide();
                                ProductsFragment productsPurchasedFragment =
                                        createProductsFragment(currentUser, "BOUGHT", true, false, "purchased");
                                initFragment(productsPurchasedFragment);
                                break;

                            case R.id.nav_sales:

                                mActionBar.setTitle("Sales");
                                mFabProductAdd.show();
                                ProductsFragment productsToSellFragment =
                                        createProductsFragment(currentUser, "TOSELL", true, true, "sales");
                                initFragment(productsToSellFragment);
                                break;

                            case R.id.nav_account:

                                mActionBar.setTitle("Account");
                                mFabProductAdd.hide();

                                UserDetailFragment userDetailFragment = new UserDetailFragment();
                                Bundle args = new Bundle();
                                args.putSerializable(Constants.INTENT_KEY_USER, currentUser);
                                userDetailFragment.setArguments(args);
                                initFragment(userDetailFragment);
                                break;

                            default:
                                break;
                        }

                        item.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    private ProductsFragment createProductsFragment(User user, String productStatus, boolean isClickable,
                                                    boolean isLongClickable, String fromView) {
        ProductsFragment productsFragment = new ProductsFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.INTENT_KEY_USER, user);
        args.putSerializable("productStatus", productStatus);
        args.putSerializable("isClickable", isClickable);
        args.putSerializable("isLongClickable", isLongClickable);
        args.putSerializable(Constants.INTENT_KEY_FROMVIEW, fromView);
        productsFragment.setArguments(args);

        return productsFragment;
    }

    private void initFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();
    }

    public void openDrawer() {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Constants.REQUESTCODE_PRODUCTADD:
                if (resultCode == RESULT_OK) {
                    String message = data.getStringExtra(Constants.INTENT_KEY_MESSAGE);
                    showMessage(message);

                    mActionBar.setTitle("Sales");
                    mFabProductAdd.show();
                    ProductsFragment productsToSellFragment =
                            createProductsFragment(currentUser, "TOSELL", true, true, "sales");
                    initFragment(productsToSellFragment);

                }
                break;
            case Constants.REQUESTCODE_PRODUCTDETAIL:
                if (resultCode == RESULT_OK) {
                    String message = data.getStringExtra(Constants.INTENT_KEY_MESSAGE);
                    String fromView = data.getStringExtra(Constants.INTENT_KEY_FROMVIEW);
                    showMessage(message);

                    //fromView = all, purchased, sales
                    ProductsFragment productsToSellFragment = null;
                    if (fromView.equals("all")) {
                        mActionBar.setTitle("All");
                        productsToSellFragment = createProductsFragment(null, "TOSELL", true, false, fromView);
                    } else if (fromView.equals("purchased")) {
                        mActionBar.setTitle("Purchased");
                        productsToSellFragment = createProductsFragment(currentUser, "BOUGHT", true, false, fromView);
                    } else if (fromView.equals("sales")) {
                        mActionBar.setTitle("Sales");
                        mFabProductAdd.show();
                        productsToSellFragment = createProductsFragment(currentUser, "TOSELL", true, true, fromView);
                    }
                    initFragment(productsToSellFragment);
                }
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {

            case R.id.notification:
                Toast.makeText(this, "notification", Toast.LENGTH_SHORT).show();
                return true;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showMessage(String message) {
        Snackbar.make(mDrawerLayout, message, Snackbar.LENGTH_SHORT).show();
    }
}
