package br.com.acmestore.user.userdetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import br.com.acmestore.Constants;
import br.com.acmestore.Injection;
import br.com.acmestore.MainActivity;
import br.com.acmestore.R;
import br.com.acmestore.data.entity.User;
import br.com.acmestore.data.service.UserServiceApi;
import br.com.acmestore.view.LoaderDialog;

public class UserDetailFragment extends Fragment {

    private User currentUser;
    private UserServiceApi mApi;

    private LoaderDialog mLoaderDialog;

    private TextView tvUserDetailEmail;
    private TextView tvUserDetailType;
    private TextView tvUserDetailMoney;

    private Button btUserDetailDelete;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if (null != arguments) {
            this.currentUser = (User) arguments.get(Constants.INTENT_KEY_USER);
        }

        mApi = Injection.provideUserServiceApiImpl();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_userdetail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvUserDetailEmail = (TextView) view.findViewById(R.id.tv_userdetail_emal);
        tvUserDetailEmail.setText("User: ".concat(currentUser.getEmail()));

        tvUserDetailType = (TextView) view.findViewById(R.id.tv_userdetail_type);
        tvUserDetailType.setText("Type: ".concat(currentUser.getType()));

        tvUserDetailMoney = (TextView) view.findViewById(R.id.tv_userdetail_money);
        tvUserDetailMoney.setText("Money: U$".concat(currentUser.getMoney().toString()));

        btUserDetailDelete = (Button) view.findViewById(R.id.bt_userdetail_delete);
        btUserDetailDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mLoaderDialog = new LoaderDialog(getActivity());
                mLoaderDialog.setIndeterminate(true);
                mLoaderDialog.setCancelable(false);
                mLoaderDialog.show();

                mApi.delete(currentUser, new UserServiceApi.UserCallback<User>() {
                    @Override
                    public void onLoad(User data) {

                        mLoaderDialog.dismiss();
                        getActivity().finish();
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                ((MainActivity) getActivity()).openDrawer();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
