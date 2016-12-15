package com.example.hello;

import java.io.IOException;

import com.example.hello.PasswordRecoverActivity;
import com.example.hello.R;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.example.hello.api.Server;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import inputcells.SimpleTextInputCellFragment;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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

//				goHelloWorld();
				Login_into();
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
	
	
	
	protected void Login_into(){
		OkHttpClient client = Server.getSharedClient();

		MultipartBody requestBody = new MultipartBody.Builder()
				.addFormDataPart("account", fragInputAccount.getText())
				.addFormDataPart("passwordHash", MD5.getMD5(fragInputPassword.getText()))
				.build();

		Request request = Server.requestBuilderWithApi("login")
				.method("post", null)
				.post(requestBody)
				.build();

		final ProgressDialog dlg = new ProgressDialog(this);
		dlg.setCancelable(false);
		dlg.setCanceledOnTouchOutside(false);
		dlg.setMessage("正在登陆");
		dlg.show();

		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				
				final String responseString = arg1.body().string();
				ObjectMapper mapper = new ObjectMapper();
				final User user = mapper.readValue(responseString, User.class);
				
				runOnUiThread(new Runnable() {
					public void run() {
						dlg.dismiss();
						new AlertDialog.Builder(LoginActivity.this)
						.setMessage("Hello,"+user.getName())
//						.setMessage(responseString)
						.setPositiveButton("OK", new DialogInterface.OnClickListener(){
							@Override
							public void onClick(DialogInterface dialog, int which) {
								Intent itnt = new Intent(LoginActivity.this, HelloWorldActivity.class);
								startActivity(itnt);	
							}	
						})
						.show();
					}
				});
			}

			@Override
			public void onFailure(Call arg0, final IOException arg1) {
				runOnUiThread(new Runnable() {
					public void run() {
						dlg.dismiss();

						Toast.makeText(LoginActivity.this, arg1.getLocalizedMessage(), Toast.LENGTH_LONG).show();
					}
				});
			}
		});
//
//		OkHttpClient client=Server.getSharedClient();
//		
//		MultipartBody requestBody=new MultipartBody
//				.Builder()
//				.addFormDataPart("account", fragInputAccount.getText())
//				.addFormDataPart("password", MD5.getMD5(fragInputPassword.getText()))
//				.build();
//		
//		Request request=Server.requestBuilderWithApi("login")
//				.method("post", null)
//				.post(requestBody)
//				.build();
//				
//		final ProgressDialog progressDialog=new ProgressDialog(this);
//		progressDialog.setTitle("登录中");
//		progressDialog.setMessage("请稍后");
//		progressDialog.setCancelable(false);
//		progressDialog.setCanceledOnTouchOutside(false);
//		progressDialog.show();
//		
//		client.newCall(request).enqueue(new Callback() {
//			
//			@Override
//			public void onResponse(Call arg0, Response arg1) throws IOException {
//
//				final String responseString = arg1.body().string();
//				ObjectMapper mapper=new ObjectMapper();
//				final User user =mapper.readValue(responseString, User.class);
//				
//				runOnUiThread(new Runnable() {
//					
//					@Override
//					public void run() {
//						progressDialog.dismiss();
//						new AlertDialog.Builder(LoginActivity.this)
//						.setMessage("Hello,"+user.getName())
//						.setPositiveButton("OK", new OnClickListener() {
//							
//							@Override
//							public void onClick(DialogInterface dialog, int which) {
//								Intent itnt=new Intent(LoginActivity.this,HelloWorldActivity.class);
//								startActivity(itnt);
//								
//							}
//						})
//						.show();
//						
//					}
//				});
//				
//
////				LoginActivity.this.runOnUiThread(new Runnable() {
////					
////					@Override
////					public void run() {
////						User user;
////						try {
////							progressDialog.dismiss();
////							ObjectMapper objectMapper=new ObjectMapper();
////							user = objectMapper.readValue(arg1.body().string(), User.class);
////							new AlertDialog.Builder(LoginActivity.this)
////							.setTitle("登陆成功")
////							.setMessage(user.getName()+","+user.getAccount())
////							.setPositiveButton("确定",new OnClickListener() {
////								
////								@Override
////								public void onClick(DialogInterface dialog, int which) {
////									Intent intent=new Intent(LoginActivity.this,HelloWorldActivity.class);
////									startActivity(intent);
////									finish();
////									
////								}
////							})
////							.show();
////						} catch (Exception e) {
////							e.printStackTrace();
////						}
////						
////					}
////				});
//				
//			}
//			
//			@Override
//			public void onFailure(Call arg0, final IOException arg1) {
//				LoginActivity.this.runOnUiThread(new Runnable() {
//					
//					@Override
//					public void run() {
//						progressDialog.dismiss();
//						Toast.makeText(LoginActivity.this, arg1.getLocalizedMessage(), Toast.LENGTH_LONG).show();
//					}
//				});
//				
//			}
//		});
//		
	}
}
