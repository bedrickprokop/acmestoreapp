package br.com.acmestore.product;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import br.com.acmestore.Constants;
import br.com.acmestore.R;
import br.com.acmestore.commons.EndlessScrolling;
import br.com.acmestore.commons.ItemListListener;
import br.com.acmestore.data.entity.Product;

public class ProductsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 1;
    private static final int TYPE_LOADING = 0;

    private LayoutInflater mInflater;

    private Activity mContext;
    private CopyOnWriteArrayList<Product> mDataList;
    private ItemListListener<Product> mItemListListener;
    private boolean isClickable;
    private boolean isLongClickable;

    private EndlessScrolling mEndlessScrolling;
    private RecyclerView mRecyclerView;

    private int selectedItemPosition;

    public ProductsAdapter(Activity context, List<Product> productList, RecyclerView recyclerView,
                           ItemListListener<Product> mItemListListener, boolean isClickable,
                           boolean isLongClickable, EndlessScrolling.OnLoadMoreListener onLoadMoreListener) {

        this.mContext = context;
        this.mDataList = new CopyOnWriteArrayList<>();
        this.mDataList.addAll(0, productList);
        this.mItemListListener = mItemListListener;
        this.isClickable = isClickable;
        this.isLongClickable = isLongClickable;

        this.mEndlessScrolling = new EndlessScrolling(recyclerView.getLayoutManager(), onLoadMoreListener);
        this.mRecyclerView = recyclerView;
        this.mRecyclerView.addOnScrollListener(mEndlessScrolling);

        if (isLongClickable) {
            this.selectedItemPosition = -1;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (null == mInflater) {
            mInflater = LayoutInflater.from(parent.getContext());
        }

        if (viewType == TYPE_ITEM) {
            View productView = mInflater.inflate(R.layout.item_product, parent, false);
            return new ItemViewHolder(productView, mItemListListener, isClickable, isLongClickable);
        } else {
            View loadingView = mInflater.inflate(R.layout.item_loading, parent, false);
            return new ProgressViewHolder(loadingView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {

            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            Product product = mDataList.get(position);

            if (isLongClickable) {
                if (selectedItemPosition == position) {
                    itemViewHolder.mLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.graye));
                } else {
                    itemViewHolder.mLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
                }
            }

            if (null != product.getPictureUrl() && !Constants.EMPTY_SPACE.equals(product.getPictureUrl())) {
                //TODO Picasso não baixa imagens https
                Picasso.with(itemViewHolder.ivProductPicture.getContext())
                        .load(product.getPictureUrl())
                        //.transform(new CircleTransform())
                        .fit().centerCrop()
                        .placeholder(R.drawable.ic_menu_gallery)
                        .into(itemViewHolder.ivProductPicture);
            } else {
                itemViewHolder.ivProductPicture.setImageResource(R.drawable.ic_menu_gallery);
            }

            itemViewHolder.tvProductName.setText(product.getName());
            //holder.tvProductDescription.setText(product.getDescription());
            itemViewHolder.tvProductUnitPrice.setText(Constants.DOLLAR_SIGN.concat(
                    Constants.EMPTY_SPACE).concat(product.getUnitPrice().toString()));
        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return this.mDataList.get(position) != null ? TYPE_ITEM : TYPE_LOADING;
    }

    public Product getItem(int position) {
        return mDataList.get(position);
    }

    public void removeItem(int position) {
        if (isLongClickable) {
            this.mDataList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void addProgressItem() {
        this.mDataList.add(null);
        notifyItemInserted(getItemCount() - 1);
    }

    public void removeProgressItem() {
        this.mDataList.remove(getItemCount() - 1);
        notifyItemRemoved(getItemCount() - 1);

        mEndlessScrolling.loaded();
    }

    public void unselectItem(int position) {
        if (isLongClickable) {
            if (position > -1) {
                RecyclerView.ViewHolder view = mRecyclerView.findViewHolderForLayoutPosition(position);
                if (view.getItemViewType() == TYPE_ITEM) {
                    ItemViewHolder itemViewHolder = (ItemViewHolder) view;
                    itemViewHolder.mLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
                }
            }
            selectedItemPosition = -1;
        }
    }

    public void replaceData(List<Product> productList) {
        int size = this.mDataList.size();
        this.mDataList.clear();
        notifyItemRangeRemoved(0, size);

        this.mDataList.addAll(productList);
        notifyDataSetChanged();

        mEndlessScrolling.resetPagination();
    }

    public void addData(List<Product> productList) {
        int size = mDataList.size();
        this.mDataList.clear();
        notifyItemRangeRemoved(0, size);

        this.mDataList.addAll(productList);
        notifyDataSetChanged();
    }

    public void appendData(List<Product> productList) {
        this.mDataList.addAll(getItemCount(), productList);
        notifyDataSetChanged();
    }

    public CopyOnWriteArrayList<Product> getList() {
        return this.mDataList;
    }

    public int getPage() {
        return mEndlessScrolling.getCurrentPage();
    }

    public void setPage(int page) {
        mEndlessScrolling.setCurrentPage(page);
    }

    class ProgressViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar progressBar;

        public ProgressViewHolder(View itemView) {
            super(itemView);
            this.progressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar_item);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        private ItemListListener<Product> mItemListListener;
        private boolean isClickable;
        private boolean isLongClickable;

        public LinearLayout mLayout;
        public TextView tvProductName;
        public TextView tvProductUnitPrice;

        public ImageView ivProductPicture;

        public ItemViewHolder(View itemView, ItemListListener<Product> listener,
                              boolean isClickable, boolean isLongClickable) {
            super(itemView);
            this.mItemListListener = listener;

            this.isClickable = isClickable;
            this.isLongClickable = isLongClickable;

            this.mLayout = (LinearLayout) itemView.findViewById(R.id.ll_item_product);

            this.tvProductName = (TextView) itemView.findViewById(R.id.tv_product_name);
            this.tvProductUnitPrice = (TextView) itemView.findViewById(R.id.tv_product_unitprice);
            this.ivProductPicture = (ImageView) itemView.findViewById(R.id.iv_product_picture);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (isClickable) {
                mItemListListener.onClickedItem(getItem(getAdapterPosition()));
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (isLongClickable) {
                int adapterPosition = getAdapterPosition();

                if (selectedItemPosition != adapterPosition) {
                    selectedItemPosition = adapterPosition;
                    notifyDataSetChanged();
                    ProductsAdapter.this.mItemListListener.onLongClickedSelectItem(selectedItemPosition);
                } else {
                    selectedItemPosition = -1;
                    notifyDataSetChanged();
                    ProductsAdapter.this.mItemListListener.onLongClickedUnselectItem(selectedItemPosition);
                }
                return true;
            } else {
                return false;
            }

        }
    }
}