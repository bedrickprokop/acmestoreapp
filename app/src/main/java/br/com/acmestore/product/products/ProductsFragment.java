package br.com.acmestore.product.products;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import br.com.acmestore.Constants;
import br.com.acmestore.Injection;
import br.com.acmestore.MainActivity;
import br.com.acmestore.R;
import br.com.acmestore.commons.EndlessScrolling;
import br.com.acmestore.commons.ItemListListener;
import br.com.acmestore.data.entity.Product;
import br.com.acmestore.data.entity.User;
import br.com.acmestore.product.ProductsAdapter;
import br.com.acmestore.product.productdetail.ProductDetailActivity;

public class ProductsFragment extends Fragment implements ProductsContract.View {

    private ProductsContract.UserActionListener mPresenter;
    private ProductsAdapter mAdapter;

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefresh;

    private User currentUser;
    private String productStatus;
    private boolean isClickable;
    private boolean isLongClickable;
    private String fromView;

    private Menu mMenu;
    private boolean isAnyProductSelected;

    private ItemListListener<Product> mListener = new ItemListListener<Product>() {

        @Override
        public void onClickedItem(Product item) {
            mPresenter.openProductDetail(item);
        }

        @Override
        public void onLongClickedSelectItem(int index) {
            Product selectedProduct = mAdapter.getItem(index);
            mPresenter.selectProduct(index, selectedProduct);
        }

        @Override
        public void onLongClickedUnselectItem(int index) {
            mPresenter.unselectProduct(index);
        }
    };

    private EndlessScrolling.OnLoadMoreListener mOnLoadMoreListener = new EndlessScrolling.OnLoadMoreListener() {
        @Override
        public void onLoadMore(int page) {
            mPresenter.loadProductList(null == currentUser ? null : currentUser.getId(), productStatus, page, false);
        }
    };

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(Constants.INTENT_KEY_ISANYPRODUCTSELECTED, isAnyProductSelected);
        outState.putSerializable(Constants.INTENT_KEY_PRODUCTLIST, mAdapter.getList());
        outState.putSerializable(Constants.INTENT_KEY_CURRENTPAGE, mAdapter.getPage());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null) {
            this.currentUser = (User) arguments.get(Constants.INTENT_KEY_USER);
            this.productStatus = arguments.getString("productStatus");
            this.isClickable = arguments.getBoolean("isClickable");
            this.isLongClickable = arguments.getBoolean("isLongClickable");
            this.fromView = arguments.getString(Constants.INTENT_KEY_FROMVIEW);
        }

        mPresenter = new ProductsPresenter(Injection.provideProductServiceApiImpl(), this);
        isAnyProductSelected = false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_products, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.product_list);
        //Se os itens do recyclerView têm um tamanho fixo(largura e altura) então defina true para este método
        //fazer a lista mais eficiente
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        mAdapter = new ProductsAdapter(getActivity(), new ArrayList<Product>(0), mRecyclerView,
                mListener, isClickable, isLongClickable, mOnLoadMoreListener);

        mRecyclerView.setAdapter(mAdapter);
        mSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.products_swipelayout);
        mSwipeRefresh.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadProductList(null == currentUser ? null : currentUser.getId(), productStatus, 1, true);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Control whether a fragment instance is retained across Activity re-creation. This can only
        //be used with fragments not in the back stack. If set, the fragment lifecycle will be slightly
        //different when an activity is recreated:
        //onDestroy will not be called (but onDetach still will be, because the fragment is being detached
        //from its current activity).
        //onCreate will not be called since the fragment is not being re-created
        //onAttach(Activity) and onActivityCreated(Bundle) will still be called
        setRetainInstance(true);
        loadData(savedInstanceState);
    }

    public void loadData(Bundle savedInstanceState) {
        if (null != savedInstanceState) {
            isAnyProductSelected = savedInstanceState.getBoolean(Constants.INTENT_KEY_ISANYPRODUCTSELECTED);
            mAdapter.addData((CopyOnWriteArrayList<Product>) savedInstanceState.get(Constants.INTENT_KEY_PRODUCTLIST));
            mAdapter.setPage(savedInstanceState.getInt(Constants.INTENT_KEY_CURRENTPAGE));
        } else {
            mPresenter.loadProductList(null == currentUser ? null : currentUser.getId(), productStatus, 1, true);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.mMenu = menu;
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                if (isAnyProductSelected) {
                    mPresenter.unselectProduct(-1);
                    return true;
                } else {
                    ((MainActivity) getActivity()).openDrawer();
                    return true;
                }
            case R.id.delete_product:
                mPresenter.deleteSelectedProduct();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setProgressIndicator(final boolean isActive) {
        if (null == getView()) {
            return;
        }
        mSwipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefresh.setRefreshing(isActive);
            }
        });
    }

    @Override
    public void setListProgressIndicator(boolean isActive) {
        if (isActive) {
            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    mAdapter.addProgressItem();
                }
            });
        } else {
            mAdapter.removeProgressItem();
        }
    }

    @Override
    public void showProductList(List<Product> productList, boolean doRefresh) {
        if (doRefresh) {
            mAdapter.replaceData(productList);
        } else {
            mAdapter.appendData(productList);
        }
    }

    @Override
    public void showProductDetailUi(Long productId) {

        Intent intent = new Intent(this.getActivity(), ProductDetailActivity.class);
        intent.putExtra(Constants.INTENT_KEY_PRODUCTID, productId);
        intent.putExtra(Constants.INTENT_KEY_FROMVIEW, this.fromView);
        getActivity().startActivityForResult(intent, Constants.REQUESTCODE_PRODUCTDETAIL);
    }

    @Override
    public void changeActionBarWhenProductSelected() {
        isAnyProductSelected = true;
        this.mMenu.getItem(0).setVisible(false);
        this.mMenu.getItem(1).setVisible(true);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        activity.getSupportActionBar().setTitle(null);
    }

    @Override
    public void changeActionBarWhenProductUnselected(int productPosition) {
        mAdapter.unselectItem(productPosition);
        isAnyProductSelected = false;

        this.mMenu.getItem(0).setVisible(true);
        this.mMenu.getItem(1).setVisible(false);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        activity.getSupportActionBar().setTitle("Sales");
    }

    @Override
    public void removeProduct(int position) {
        mAdapter.removeItem(position);
    }

    @Override
    public void showMessage(String message) {
        ((MainActivity) getActivity()).showMessage(message);
    }
}
