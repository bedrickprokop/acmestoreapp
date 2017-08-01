package br.com.acmestore.product.productdetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import br.com.acmestore.Constants;
import br.com.acmestore.Injection;
import br.com.acmestore.MainActivity;
import br.com.acmestore.R;
import br.com.acmestore.data.entity.Product;
import br.com.acmestore.data.entity.User;
import br.com.acmestore.user.useradd.UserAddActivity;

public class ProductDetailActivity extends AppCompatActivity implements ProductDetailContract.View {

    private ProductDetailContract.UserActionListener mActionListener;
    private Long productId;
    private User currentUser;

    private LinearLayout mContent;
    private SwipeRefreshLayout mSwipeRefresh;

    private TextView tvProductDetailName;
    private TextView tvProductDetailDescription;
    private TextView tvProductDetailUnitPrice;
    //private TextView tvProductDetailOwner;
    private ImageView ivProductDetailPicture;

    private String fromView;
    private Boolean fromNotification;

    private Button btProductDetailPurchase; //vier da lista de produtos
    private Button btProductDetailSell;     //vier da lista de comprados
    private Button btProductDetailEdit;     //vier da lista de a venda

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productdetail);
        mActionListener = new ProductDetailPresenter(Injection.provideProductServiceApiImpl(), this);

        setupExtras(getIntent().getExtras());
        setupActionBar();
        setupContentView();
        loadData(savedInstanceState);
    }

    private void setupExtras(Bundle extras) {
        fromView = extras.getString(Constants.INTENT_KEY_FROMVIEW);
        productId = (Long) extras.get(Constants.INTENT_KEY_PRODUCTID);
        currentUser = (User) extras.get(Constants.INTENT_KEY_USER);
        fromNotification = extras.getBoolean("fromNotification");
    }

    @Override
    protected void onDestroy() {
        if (fromNotification) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(Constants.INTENT_KEY_USER, currentUser);
            startActivity(intent);
        }
        super.onDestroy();
    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        //Exibe ou oculta o botão padrão home
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Define se a view personalizada deve ser exibida
        actionBar.setDisplayShowCustomEnabled(true);
        //Define se o título/subtitulo deve ser exibido
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(getString(R.string.activity_title_productdetail));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                mActionListener.openProductList();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupContentView() {
        mContent = (LinearLayout) findViewById(R.id.productdetail_content);

        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.productdetail_swiperefresh);
        mSwipeRefresh.setColorSchemeColors(
                ContextCompat.getColor(this, R.color.colorPrimary),
                ContextCompat.getColor(this, R.color.colorAccent),
                ContextCompat.getColor(this, R.color.colorPrimaryDark)
        );
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mActionListener.loadProduct(ProductDetailActivity.this.productId);
            }
        });

        tvProductDetailName = (TextView) findViewById(R.id.tv_productdetail_name);
        tvProductDetailDescription = (TextView) findViewById(R.id.tv_productdetail_description);
        tvProductDetailUnitPrice = (TextView) findViewById(R.id.tv_productdetail_unitprice);
        //tvProductDetailOwner = (TextView) findViewById(R.id.tv_productdetail_owner);

        ivProductDetailPicture = (ImageView) findViewById(R.id.iv_productdetail_picture);

        if (fromView.equals("all")) {
            btProductDetailPurchase = (Button) findViewById(R.id.bt_productdetail_purchase);
            btProductDetailPurchase.setVisibility(View.VISIBLE);
            btProductDetailPurchase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActionListener.buyProduct(currentUser.getId(), productId);
                }
            });
        } else if (fromView.equals("purchased")) {
            btProductDetailSell = (Button) findViewById(R.id.bt_productdetail_sell);
            btProductDetailSell.setVisibility(View.VISIBLE);
            btProductDetailSell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActionListener.sellProduct(productId);
                }
            });
        }

        /*else if (fromView.equals("sales")) {
            btProductDetailEdit = (Button) findViewById(R.id.bt_productdetail_edit);
            btProductDetailEdit.setVisibility(View.VISIBLE);
            btProductDetailEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActionListener.editProduct(productId);
                }
            });
        }*/
    }

    private void loadData(Bundle savedInstanceState) {
        mActionListener.loadProduct(this.productId);
    }

    @Override
    public void setProgressIndicator(final boolean isActive) {
        mSwipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefresh.setRefreshing(isActive);
            }
        });
    }

    @Override
    public void showProduct(final Product product) {

        if (null != product.getPictureUrl() && !Constants.EMPTY_SPACE.equals(product.getPictureUrl())) {
            Picasso.with(ivProductDetailPicture.getContext())
                    .load(product.getPictureUrl())
                    .fit().centerCrop()
                    .placeholder(R.drawable.ic_menu_gallery)
                    .into(ivProductDetailPicture);
        } else {
            ivProductDetailPicture.setImageResource(R.drawable.ic_menu_gallery);
        }

        tvProductDetailName.setText(product.getName());
        tvProductDetailDescription.setText(product.getDescription());
        tvProductDetailUnitPrice.setText(Constants.DOLLAR_SIGN.concat(Constants.BLANK_SPACE)
                .concat(product.getUnitPrice().toString()));
        //tvProductDetailOwner.setText("Seller: ".concat(product.getOwner().getEmail()));
    }

    @Override
    public void showProductListUi() {
        this.finish();
    }

    @Override
    public void showProductListUi(String message) {
        Intent intent = new Intent();
        intent.putExtra(Constants.INTENT_KEY_MESSAGE, message);
        intent.putExtra(Constants.INTENT_KEY_FROMVIEW, fromView);
        setResult(RESULT_OK, intent);

        this.finish();
    }
}
