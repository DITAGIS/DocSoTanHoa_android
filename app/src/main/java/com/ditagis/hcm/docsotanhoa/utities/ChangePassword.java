package com.ditagis.hcm.docsotanhoa.utities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;

/**
 * Created by ThanLe on 11/22/2017.
 */

public class ChangePassword {
    private String userName;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
    private ProgressDialog dialog;
    private Context mContext;
    private Activity mActivity;
    private View view;

    public ChangePassword(String userName, String oldPassword, String newPassword, String confirmPassword, ProgressDialog dialog, Context mContext, Activity mActivity, View view) {
        this.userName = userName;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
        this.dialog = dialog;
        this.mContext = mContext;
        this.mActivity = mActivity;
        this.view = view;

        changePassword();
    }

    private void changePassword() {
//        if (!CheckConnect.isOnline(LoginActivity.this)) {
//            MySnackBar.make(btnLogin, R.string.no_connect, true);
//            return;
//        }
//        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this, android.R.style.Theme_Material_Light_Dialog_Alert);
//        builder.setTitle("Đổi mật khẩu");
//        builder.setCancelable(false);
//        final AlertDialog dialogChangePw = builder.create();
//        LayoutInflater inflater = getLayoutInflater();
//        View dialogLayout = inflater.inflate(R.layout.layout_change_password, null);
//        dialogChangePw.setView(dialogLayout);
//        dialogChangePw.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialogChangePw.show();
//
//
//        final TextView txtChangePwNewPw = (TextView) dialogChangePw.findViewById(R.id.txt_changePw_newPw);
//        final TextView txtChangePwConfirmPw = (TextView) dialogChangePw.findViewById(R.id.txt_changePw_confirmPw);
//        final TextView txtAlertNotCorrect = (TextView) dialogChangePw.findViewById(R.id.txt_changePassword_Alert_NotCorrect);
//
//        final EditText etxtChangePwNewPw = (EditText) dialogChangePw.findViewById(R.id.etxt_changePw_NewPw);
//        final EditText etxtChangePwConfirmPw = (EditText) dialogChangePw.findViewById(R.id.etxt_changePw_ConfirmPw);
//
//
//        etxtChangePwNewPw.setOnFocusChangeListener(new View.OnFocusChangeListener()
//
//        {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    txtChangePwNewPw.setVisibility(View.VISIBLE);
//                    etxtChangePwNewPw.setHint("");
//                } else {
//                    txtChangePwNewPw.setVisibility(View.INVISIBLE);
//                    etxtChangePwNewPw.setHint(LoginActivity.this.getString(R.string.hint_new_password));
//                }
//            }
//        });
//        etxtChangePwConfirmPw.setOnFocusChangeListener(new View.OnFocusChangeListener()
//
//        {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    txtChangePwConfirmPw.setVisibility(View.VISIBLE);
//                    etxtChangePwConfirmPw.setHint("");
//                } else {
//                    txtChangePwConfirmPw.setVisibility(View.INVISIBLE);
//                    etxtChangePwConfirmPw.setHint(LoginActivity.this.getString(R.string.hint_confirm_password));
//                }
//            }
//        });
//
//
//        ((ImageButton) dialogChangePw.findViewById(R.id.imgBtn_changePassword_viewNewPassword)).
//
//                setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (etxtChangePwNewPw.getTransformationMethod() == null) {
//                            ((ImageButton) v).setImageResource(R.drawable.un_view_password);
//                            etxtChangePwNewPw.setTransformationMethod(new PasswordTransformationMethod());
//                        } else {
//                            ((ImageButton) v).setImageResource(R.drawable.view_password);
//                            etxtChangePwNewPw.setTransformationMethod(null);
//                        }
//                    }
//                });
//
//        ((ImageButton) dialogChangePw.findViewById(R.id.imgBtn_changePassword_viewConfirmPassword)).
//
//                setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (etxtChangePwConfirmPw.getTransformationMethod() == null) {
//                            ((ImageButton) v).setImageResource(R.drawable.un_view_password);
//                            etxtChangePwConfirmPw.setTransformationMethod(new PasswordTransformationMethod());
//                        } else {
//                            ((ImageButton) v).setImageResource(R.drawable.view_password);
//                            etxtChangePwConfirmPw.setTransformationMethod(null);
//                        }
//                    }
//                });
//
//        ((Button) dialogChangePw.findViewById(R.id.btn_changePassword_Dismiss)).
//
//                setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialogChangePw.dismiss();
//                    }
//                });
//
//        final Button btnOK = (Button) dialogChangePw.findViewById(R.id.btn_changePassword_OK);
//        btnOK.setOnClickListener(new View.OnClickListener()
//
//        {
//            @Override
//            public void onClick(View v) {
//                ChangePassswordAsync changePassswordAsync = new ChangePassswordAsync(btnLogin, LoginActivity.this, LoginActivity.this,
//                        etxtChangePwUsername.getText().toString(),
//                        etxtChangePwOldPw.getText().toString(),
//                        etxtChangePwNewPw.getText().toString(),
//                        etxtChangePwConfirmPw.getText().toString(),
//                        new ChangePassswordAsync.AsyncResponse() {
//                            @Override
//                            public void processFinish(final LogInDB.Result output) {
//
//                                if (output == null) {
//                                    ;
//                                } else if (output.getmStaffName().length() > 0) {
//                                    dialogChangePw.dismiss();
//
//                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this, android.R.style.Theme_Material_Light_Dialog_Alert);
//                                    builder.setTitle(LoginActivity.this.getString(R.string.change_password_success));
//                                    builder.setMessage(LoginActivity.this.getString(R.string.ques_login_this_account));
//                                    builder.setCancelable(false);
//                                    builder.setPositiveButton(LoginActivity.this.getString(R.string.ok), new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            LoginActivity.this.mUsername = output.getUsername();
//                                            LoginActivity.this.mPassword = output.getPassword();
//
//                                            LoginActivity.this.mStaffName = output.getmStaffName();
//                                            LoginActivity.this.mDot = output.getmDot();
//                                            doLayLoTrinh();
//                                        }
//                                    }).setNegativeButton(LoginActivity.this.getString(R.string.cancel), new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            dialog.dismiss();
//                                        }
//                                    });
//
//                                    AlertDialog dialog = builder.create();
//                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                                    dialog.show();
//                                }
//                            }
//                        });
//
//                changePassswordAsync.execute();
//
//
//            }
//        });
//        etxtChangePwConfirmPw.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (etxtChangePwNewPw.getText().toString().equals(etxtChangePwConfirmPw.getText().toString())) {
//                    txtAlertNotCorrect.setVisibility(View.INVISIBLE);
//                    btnOK.setEnabled(true);
//
//                } else {
//                    txtAlertNotCorrect.setVisibility(View.VISIBLE);
//                    btnOK.setEnabled(false);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
    }


}
