package com.ditagis.hcm.docsotanhoa.utities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ditagis.hcm.docsotanhoa.R;
import com.ditagis.hcm.docsotanhoa.async.ChangePassswordAsync;
import com.ditagis.hcm.docsotanhoa.conectDB.ChangePasswordDB;

/**
 * Created by ThanLe on 11/22/2017.
 */

public class ChangePassword {
    private String mUserName;
    private String oldPassword;

    private Context mContext;
    private Activity mActivity;

    public ChangePassword(String userName, String oldPassword, Context mContext, Activity mActivity) {
        this.mUserName = userName;
        this.oldPassword = oldPassword;

        this.mContext = mContext;
        this.mActivity = mActivity;

        changePassword();
    }

    private void changePassword() {
        if (!CheckConnect.isOnline(mActivity)) {
            MySnackBar.make(mActivity.findViewById(R.id.container), R.string.no_connect, true);
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity, android.R.style.Theme_Material_Light_Dialog_Alert);
        builder.setTitle("Đổi mật khẩu");
        builder.setCancelable(false);
        final AlertDialog dialogChangePw = builder.create();
        LayoutInflater inflater = mActivity.getLayoutInflater();
        final View dialogLayout = inflater.inflate(R.layout.layout_change_password, null);
        dialogChangePw.setView(dialogLayout);
        dialogChangePw.requestWindowFeature(Window.FEATURE_NO_TITLE);



        dialogChangePw.show();


        final TextView txtChangePwNewPw = (TextView) dialogChangePw.findViewById(R.id.txt_changePw_newPw);
        final TextView txtChangePwConfirmPw = (TextView) dialogChangePw.findViewById(R.id.txt_changePw_confirmPw);
        final TextView txtAlertNotCorrect = (TextView) dialogChangePw.findViewById(R.id.txt_changePassword_Alert_NotCorrect);

        final EditText etxtChangePwNewPw = (EditText) dialogChangePw.findViewById(R.id.etxt_changePw_NewPw);
        etxtChangePwNewPw.setBackgroundResource(R.layout.edit_text_styles2);
        final EditText etxtChangePwConfirmPw = (EditText) dialogChangePw.findViewById(R.id.etxt_changePw_ConfirmPw);
        etxtChangePwConfirmPw.setBackgroundResource(R.layout.edit_text_styles2);

        etxtChangePwNewPw.setOnFocusChangeListener(new View.OnFocusChangeListener()

        {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    txtChangePwNewPw.setVisibility(View.VISIBLE);
                    etxtChangePwNewPw.setHint("");
                } else {
                    txtChangePwNewPw.setVisibility(View.INVISIBLE);
                    etxtChangePwNewPw.setHint(mActivity.getString(R.string.hint_new_password));
                }
            }
        });
        etxtChangePwConfirmPw.setOnFocusChangeListener(new View.OnFocusChangeListener()

        {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    txtChangePwConfirmPw.setVisibility(View.VISIBLE);
                    etxtChangePwConfirmPw.setHint("");
                } else {
                    txtChangePwConfirmPw.setVisibility(View.INVISIBLE);
                    etxtChangePwConfirmPw.setHint(mActivity.getString(R.string.hint_confirm_password));
                }
            }
        });


        ((ImageButton) dialogChangePw.findViewById(R.id.imgBtn_changePassword_viewNewPassword)).

                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (etxtChangePwNewPw.getTransformationMethod() == null) {
                            ((ImageButton) v).setImageResource(R.drawable.un_view_password);
                            etxtChangePwNewPw.setTransformationMethod(new PasswordTransformationMethod());
                        } else {
                            ((ImageButton) v).setImageResource(R.drawable.view_password);
                            etxtChangePwNewPw.setTransformationMethod(null);
                        }
                    }
                });

        ((ImageButton) dialogChangePw.findViewById(R.id.imgBtn_changePassword_viewConfirmPassword)).

                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (etxtChangePwConfirmPw.getTransformationMethod() == null) {
                            ((ImageButton) v).setImageResource(R.drawable.un_view_password);
                            etxtChangePwConfirmPw.setTransformationMethod(new PasswordTransformationMethod());
                        } else {
                            ((ImageButton) v).setImageResource(R.drawable.view_password);
                            etxtChangePwConfirmPw.setTransformationMethod(null);
                        }
                    }
                });

        ((Button) dialogChangePw.findViewById(R.id.btn_changePassword_Dismiss)).

                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogChangePw.dismiss();
                    }
                });

        final Button btnOK = (Button) dialogChangePw.findViewById(R.id.btn_changePassword_OK);
        btnOK.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                final ChangePassswordAsync changePassswordAsync = new ChangePassswordAsync(dialogLayout, mActivity, mContext,
                        mUserName,
                        oldPassword,
                        etxtChangePwNewPw.getText().toString(),
                        etxtChangePwConfirmPw.getText().toString(),

                        new ChangePassswordAsync.AsyncResponse() {
                            @Override
                            public void processFinish(final ChangePasswordDB.Result output) {

                                if (output == null) {
                                    ;
                                } else {
                                    dialogChangePw.dismiss();
                                    Toast.makeText(mContext, mActivity.getString(R.string.change_password_success), Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                changePassswordAsync.execute();


            }
        });
        etxtChangePwConfirmPw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etxtChangePwNewPw.getText().toString().equals(etxtChangePwConfirmPw.getText().toString())) {
                    txtAlertNotCorrect.setVisibility(View.INVISIBLE);
                    btnOK.setEnabled(true);

                } else {
                    txtAlertNotCorrect.setVisibility(View.VISIBLE);
                    btnOK.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


}
