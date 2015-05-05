package com.izlei.shlibrary.test;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.izlei.shlibrary.R;
import com.izlei.shlibrary.app.AppController;
import com.izlei.shlibrary.bean.User;
import com.izlei.shlibrary.utils.ToastUtil;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.EmailVerifyListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by zhouzili on 2015/4/3.
 */
public class SignUp extends Activity{
    EditText editName;
    EditText editPassword;
    EditText editEmail;
    EditText editAddress;
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        editName = (EditText) findViewById(R.id.edit_name);
        editPassword = (EditText) findViewById(R.id.edit_password);
        editEmail = (EditText) findViewById(R.id.edit_email);
        editAddress = (EditText) findViewById(R.id.edit_address);
        editPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        button = (Button) findViewById(R.id.button_ok);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp(editName.getText().toString(), editPassword.getText().toString(),
                        editEmail.getText().toString(), editAddress.getText().toString());


            }
        });
    }

    public void signUp(String name,String psd, final String email,String address) {
        User user = new User();
        user.setUsername(name);
        user.setPassword(psd);
        user.setEmail(email);
        user.setAdress(address);
        user.signUp(AppController.getInstance(),new SaveListener() {
            @Override
            public void onSuccess() {
                ToastUtil.show("SignUp Success!");
                //verifyEmail(email);
            }

            @Override
            public void onFailure(int i, String s) {
                ToastUtil.show("SignUp failure!"+s);
            }
        });
    }

    private void verifyEmail(final String email) {
        BmobUser.requestEmailVerify(this,email, new EmailVerifyListener() {
            @Override
            public void onSuccess() {
                ToastUtil.show("请求验证邮件成功，请到" + email + "邮箱中进行激活。");
            }

            @Override
            public void onFailure(int i, String s) {
                ToastUtil.show("\"请求验证邮件失败:\" + "+s);
            }
        });
    }


}
