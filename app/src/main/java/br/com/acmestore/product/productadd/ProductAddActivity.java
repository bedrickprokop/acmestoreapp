package br.com.acmestore.product.productadd;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import br.com.acmestore.Constants;
import br.com.acmestore.Injection;
import br.com.acmestore.R;
import br.com.acmestore.data.entity.Product;
import br.com.acmestore.data.entity.User;

public class ProductAddActivity extends AppCompatActivity implements ProductAddContract.View {

    private ProductAddContract.UserActionListener mActionListener;

    private User currentUser;

    private EditText etProductaddName;
    private EditText etProductaddUnitprice;
    private EditText etProductaddDescription;

    private FloatingActionButton fabSubmitProduct;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productadd);

        currentUser = (User) getIntent().getExtras().get(Constants.INTENT_KEY_USER);

        mActionListener = new ProductAddPresenter(Injection.provideProductServiceApiImpl(), this);

        setupActionBar();
        setupContentView();
    }

    public void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        //Exibe ou oculta o botão padrão home
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Define se a view personalizada deve ser exibida
        actionBar.setDisplayShowCustomEnabled(true);

        //Define se o título/subtítulo deve ser exibido
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(getString(R.string.activity_title_productadd));
    }

    private void showSoftKeyboard(View view, int id, boolean hasFocus) {
        if (view.getId() == id && !hasFocus) {
            InputMethodManager inputMethodManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);

            inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public void setupContentView() {
        etProductaddName = (EditText) findViewById(R.id.et_productadd_name);
        etProductaddName.setOnFocusChangeListener(new FocusListener(R.id.et_productadd_name));

        etProductaddUnitprice = (EditText) findViewById(R.id.et_productadd_unitprice);
        etProductaddUnitprice.setOnFocusChangeListener(new FocusListener(R.id.et_productadd_unitprice));

        etProductaddDescription = (EditText) findViewById(R.id.et_productadd_description);
        etProductaddDescription.setOnFocusChangeListener(new FocusListener(R.id.et_productadd_description));

        fabSubmitProduct = (FloatingActionButton) findViewById(R.id.fab_submitproduct);
        fabSubmitProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etProductaddName.getText().toString();
                String unitPrice = etProductaddUnitprice.getText().toString();
                String description = etProductaddDescription.getText().toString();

                if (isFormValid(name, unitPrice, description)) {
                    Product product = new Product(name, description,
                            Constants.BASE_RANDOM_IMAGE.concat(name.split(Constants.BLANK_SPACE)[0]),
                            Double.parseDouble(unitPrice), "TOSELL", currentUser);

                    mActionListener.createNewProduct(product);
                }
            }
        });
    }

    private boolean isFormValid(String name, String unitPrice, String description) {
        if (null == name || name.isEmpty()) {
            etProductaddName.setError(getString(R.string.et_productadd_name_required));
            etProductaddName.requestFocus();
            return false;
        }
        if (null == unitPrice || unitPrice.isEmpty()) {
            etProductaddUnitprice.setError(getString(R.string.et_productadd_unitprice_required));
            etProductaddUnitprice.requestFocus();
            return false;
        } else {
            try {
                Double.parseDouble(unitPrice);
            } catch (NumberFormatException e) {
                etProductaddUnitprice.setError(getString(R.string.et_productadd_unitprice_notnumber));
                etProductaddUnitprice.requestFocus();
                return false;
            }
        }

        if (null == description || description.isEmpty()) {
            etProductaddDescription.setError(getString(R.string.et_productadd_description_required));
            etProductaddDescription.requestFocus();
            return false;
        }

        return true;
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

    @Override
    public void showProductsUi() {
        this.finish();
    }

    @Override
    public void showProductsUi(String message, int activityResult) {
        Intent intent = new Intent();
        intent.putExtra(Constants.INTENT_KEY_MESSAGE, message);

        setResult(activityResult, intent);
        this.finish();
    }

    class FocusListener implements View.OnFocusChangeListener {
        private int mViewId;

        public FocusListener(int viewId) {
            this.mViewId = viewId;
        }

        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            showSoftKeyboard(view, mViewId, hasFocus);
        }
    }
}
