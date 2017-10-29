package ru.fuldaros.imgf.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;

import ru.fuldaros.imgf.R;
import ru.fuldaros.imgf.activity.BaseActivity;
import ru.fuldaros.imgf.core.ImageFactory;
import ru.fuldaros.imgf.core.Invoker;
import ru.fuldaros.imgf.ui.FileChooseDialog;
import ru.fuldaros.imgf.ui.TerminalDialog;
import ru.fuldaros.imgf.util.DeviceUtils;
import ru.fuldaros.imgf.util.FileUtils;

public class Sdat2imgFragment extends BaseFragment implements View.OnClickListener, TextWatcher {
    private AppCompatButton selectTransfer;
    private AppCompatButton selectDat;
    private AppCompatButton perfromTask;
    private TextInputLayout outputFile;
    private TextInputLayout transferPath;
    private TextInputLayout datPath;

    public static BaseFragment newInstance(BaseActivity activity) {
        Sdat2imgFragment fragment = new Sdat2imgFragment();
        fragment.setActivity(activity);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: Implement this method
        View root = getContentView();
        if (root == null) {
            root = inflater.inflate(R.layout.fragment_sdat2img, container, false);
            setContentView(root);
            selectTransfer = (AppCompatButton) findViewById(R.id.sdat2img_select_transferl_list);
            selectDat = (AppCompatButton) findViewById(R.id.sdat2img_select_sysdat_image);
            datPath = (TextInputLayout) findViewById(R.id.sdat2img_sysdat_image_path);
            transferPath = (TextInputLayout) findViewById(R.id.sdat2img_transfer_path);
            outputFile = (TextInputLayout) findViewById(R.id.sdat2img_sysdat_image_output_name);
            perfromTask = (AppCompatButton) findViewById(R.id.sdat2img_sysdat_image_perform_task);
            selectDat.setOnClickListener(this);
            selectTransfer.setOnClickListener(this);
            perfromTask.setOnClickListener(this);
            datPath.getEditText().addTextChangedListener(this);
            transferPath.getEditText().addTextChangedListener(this);
            outputFile.getEditText().addTextChangedListener(this);
        }
        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sdat2img_select_sysdat_image:
                new FileChooseDialog(getActivity()).choose("system.new.dat", new FileChooseDialog.Callback() {
                    @Override
                    public void onSelected(File file) {
                        datPath.getEditText().setText(file.getPath());
                    }
                });
                break;
            case R.id.sdat2img_select_transferl_list:
                new FileChooseDialog(getActivity()).choose("system.transfer.list", new FileChooseDialog.Callback() {
                    @Override
                    public void onSelected(File file) {
                        transferPath.getEditText().setText(file.getPath());
                    }
                });
                break;
            case R.id.sdat2img_sysdat_image_perform_task:
                File transferFile = new File(getText(transferPath));
                File datFile = new File(getText(datPath));
                File outFile = new File(ImageFactory.IMAGE_CONVERTED, getText(outputFile));
                new DoConvert(transferFile, datFile, outFile).execute();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        File dat = new File(getText(datPath));
        File list = new File(getText(transferPath));
        if (TextUtils.isEmpty(getText(outputFile).trim())) {
            outputFile.setErrorEnabled(true);
            outputFile.setError(getString(R.string.output_filename_cannot_be_empty));
        } else {
            outputFile.setError(null);
            outputFile.setErrorEnabled(false);
        }
        if (!FileUtils.isFileExists(dat)) {
            datPath.setErrorEnabled(true);
            datPath.setError(getString(R.string.source_file_not_exists));
        } else if (TextUtils.isEmpty(getText(datPath).trim())) {
            datPath.setErrorEnabled(true);
            datPath.setError(getString(R.string.input_filename_cannot_be_empty));
        } else {
            datPath.setError(null);
            datPath.setErrorEnabled(false);
        }
        if (!FileUtils.isFileExists(list)) {
            transferPath.setErrorEnabled(true);
            transferPath.setError(getString(R.string.source_file_not_exists));
        } else if (TextUtils.isEmpty(getText(transferPath).trim())) {
            transferPath.setErrorEnabled(true);
            transferPath.setError(getString(R.string.input_filename_cannot_be_empty));
        } else {
            transferPath.setError(null);
            transferPath.setErrorEnabled(false);
        }
        if (!outputFile.isErrorEnabled() && !datPath.isErrorEnabled() && !transferPath.isErrorEnabled()) {
            perfromTask.setEnabled(true);
        } else {
            perfromTask.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public String getText(TextInputLayout inputLayout) {
        return inputLayout.getEditText().getText().toString();
    }

    class DoConvert extends AsyncTask<Void, Void, File> {
        private File transferFile;
        private File datFile;
        private File outputFile;
        private TerminalDialog dialog;

        public DoConvert(File transferFile, File datFile, File outputFile) {
            this.outputFile = outputFile;
            this.transferFile = transferFile;
            this.datFile = datFile;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new TerminalDialog(getActivity());
            dialog.setTitle(R.string.converting);
            dialog.show();
        }

        @Override
        protected void onPostExecute(final File file) {
            super.onPostExecute(file);
            if (file != null) {
                dialog.writeStdout(String.format(getString(R.string.converted_to_file), file.getPath()));
                dialog.setSecondButton(R.string.browse, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DeviceUtils.openFile(getActivity(), file);
                    }
                });
            } else {
                dialog.writeStderr(String.format(getString(R.string.operation_failed), file.getPath()));
            }
        }

        @Override
        protected File doInBackground(Void... params) {
            return Invoker.sdat2img(transferFile, datFile, outputFile, dialog) ? outputFile : transferFile;
        }
    }

}

