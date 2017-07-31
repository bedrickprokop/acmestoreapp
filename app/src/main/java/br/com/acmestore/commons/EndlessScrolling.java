package br.com.acmestore.commons;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

public class EndlessScrolling extends RecyclerView.OnScrollListener {

    private int currentPage = 1;
    private boolean loading = false;

    private int visibleThreshold = 3;
    private int lastVisibleItem, totalItemCount;

    private RecyclerView.LayoutManager mLayoutManager;
    private OnLoadMoreListener mOnLoadMoreListener;

    public EndlessScrolling(RecyclerView.LayoutManager layoutManager, OnLoadMoreListener onLoadMoreListener) {
        this.mLayoutManager = layoutManager;
        this.mOnLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (mLayoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) mLayoutManager;

            //Retorna o número de items no adapter vinculados ao RecyclerView pai.
            totalItemCount = gridLayoutManager.getItemCount();
            lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();

            //se não estiver carregando e a soma do ultimoItemVisivel com o limiteDeVisibilidade
            //for menor do que o totalDeItemsContatos
            if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                //Alcançou o fim, execute algo
                this.loading = true;
                this.currentPage++;
                this.mOnLoadMoreListener.onLoadMore(this.currentPage);
            }
        }

    }

    public void resetPagination(){
        this.currentPage = 1;
    }

    public void loaded() {
        this.loading = false;
    }

    public int getCurrentPage() {
        return this.currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public interface OnLoadMoreListener {
        void onLoadMore(int page);
    }
}
