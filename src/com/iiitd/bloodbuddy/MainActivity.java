package com.iiitd.bloodbuddy;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v4.app.*;

import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.*;



public class MainActivity extends FragmentActivity {
	
	private MainFragment mainFragment;
	Button btnSignIn,btnSignUp;
    LoginDataBaseAdapter loginDataBaseAdapter;
	
	public void onCreate(Bundle savedInstanceState) {
		
		
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
	    
	    try {
	        PackageInfo info = getPackageManager().getPackageInfo(
	                "com.iiitd.bloodbuddy", 
	                PackageManager.GET_SIGNATURES);
	        for (Signature signature : info.signatures) {
	            MessageDigest md = MessageDigest.getInstance("SHA");
	            md.update(signature.toByteArray());
	            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
	            }
	    } catch (NameNotFoundException e) {

	    } catch (NoSuchAlgorithmException e) {

	    }
	    
	    
	    if (savedInstanceState == null) {
	        // Add the fragment on initial activity setup
	      mainFragment = new MainFragment();  
	      FragmentManager fragmentManager = getSupportFragmentManager();
	        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
	        fragmentTransaction.add(android.R.id.content, mainFragment);
	        fragmentTransaction.commit();   
     }
     else
     {
         mainFragment =(MainFragment)getSupportFragmentManager()
         .findFragmentById(android.R.id.content);
     }  
	    
	    
	// create a instance of SQLite Database
      loginDataBaseAdapter=new LoginDataBaseAdapter(this);
      loginDataBaseAdapter=loginDataBaseAdapter.open();

      // Get The Reference Of Buttons
      btnSignIn=(Button)findViewById(R.id.buttonSignIN);
      btnSignUp=(Button)findViewById(R.id.buttonSignUP);

     // Set OnClick Listener on SignUp button 
     btnSignUp.setOnClickListener(new View.OnClickListener() {
     public void onClick(View v) {
         // TODO Auto-generated method stub

         /// Create Intent for SignUpActivity  abd Start The Activity
    	 
         Intent intentSignUP=new Intent(getApplicationContext(),SignUPActivity.class);
         startActivity(intentSignUP);
         }
     });
     
     
     btnSignIn.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			signIn(v);
			
		}
	});
     
     
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	 // Methods to handleClick Event of Sign In Button
    public void signIn(View V)
       {
            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.login);
            dialog.setTitle("Login");
 
            // get the References of views
            final  EditText editTextUserName=(EditText)dialog.findViewById(R.id.editTextUserNameToLogin);
            final  EditText editTextPassword=(EditText)dialog.findViewById(R.id.editTextPasswordToLogin);
 
            Button btnSignIn=(Button)dialog.findViewById(R.id.buttonSignIn);
 
            // Set On ClickListener
            btnSignIn.setOnClickListener(new View.OnClickListener() {
 
                public void onClick(View v) {
                    // get The User name and Password
                    String userName=editTextUserName.getText().toString();
                    String password=editTextPassword.getText().toString();
 
                    // fetch the Password form database for respective user name
                    String storedPassword=loginDataBaseAdapter.getSinlgeEntry(userName);
 
                    // check if the Stored password matches with  Password entered by user
                    if(password.equals(storedPassword))
                    {
                        Toast.makeText(MainActivity.this, "Congrats: Login Successfull", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "User Name or Password does not match", Toast.LENGTH_LONG).show();
                    }
                }
            });
 
            dialog.show();
    }
 
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close The Database
        loginDataBaseAdapter.close();
    }
}

