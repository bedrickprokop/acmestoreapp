package br.com.acmestore.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import br.com.acmestore.R;


public class LoaderDialog extends ProgressDialog {

    public LoaderDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loader);
    }
}
