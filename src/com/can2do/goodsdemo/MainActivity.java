package com.can2do.goodsdemo;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener{
	
	public void onClick(View v)
	{
		switch(v.getId()){
		case R.id.about_button:
			Intent i=new Intent(this,About.class);
			startActivity(i);
			break;
		case R.id.goods1_button:
			Intent j=new Intent(this,Goods1.class);
			startActivity(j);
			break;
		case R.id.goods2_button:
			Intent m=new Intent(this,Goods2.class);
			startActivity(m);
			break;
		case R.id.exit_button:
			Intent n=new Intent(this,Exit.class);
			startActivity(n);
			break;
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		View goods1Button = this.findViewById(R.id.goods1_button);
		goods1Button.setOnClickListener(this);
		View goods2Button = this.findViewById(R.id.goods2_button);
		goods2Button.setOnClickListener(this);
		View aboutButton = this.findViewById(R.id.about_button);
		aboutButton.setOnClickListener(this);
		View exitButton = this.findViewById(R.id.exit_button);
		exitButton.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
