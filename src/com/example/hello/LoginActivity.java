package com.example.hello;

import com.example.hello.PasswordRecoverActivity;
import com.example.hello.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import inputcells.SimpleTextInputCellFragment;

public class LoginActivity extends Activity {
	
	SimpleTextInputCellFragment fragInputAccount;
	SimpleTextInputCellFragment fragInputPassword;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		fragInputAccount=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_login_account);
		fragInputPassword=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_login_password);

		findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {

				goHelloWorld();
			}
		});
		
		findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {

				goRegister();
			}
		});
		
		findViewById(R.id.btn_forget_password).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				goRecoverPassword();
			}
		});
		
		
	}
	
	void goHelloWorld(){
		Intent itnt = new Intent(this,HelloWorldActivity.class);
		startActivity(itnt);
	}

	void goRegister(){
		Intent itnt = new Intent(this,RegisterActivity.class);
		startActivity(itnt);
	}
	
	void goRecoverPassword(){
		Intent itnt = new Intent(this, PasswordRecoverActivity.class);
		startActivity(itnt);
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		
		fragInputAccount.setLabelText("用户名");
		fragInputAccount.setHintText("请输入用户名");
		
		
		fragInputPassword.setLabelText("密码");
		fragInputPassword.setHintText("请输入密码");
		fragInputPassword.setIsPassword(true);
		
	}
}
