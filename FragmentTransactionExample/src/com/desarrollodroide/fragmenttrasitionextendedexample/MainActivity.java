package com.desarrollodroide.fragmenttrasitionextendedexample;

import com.desarrollodroide.libraryfragmenttransactionextended.FragmentTransactionExtended;

import android.app.Fragment;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Typeface;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;



public class MainActivity extends Activity implements AdapterView.OnItemSelectedListener{
	private int optionSelected = 0;
	private int fragmentActivo = 1;
	private SlidingListFragmentLeft mFirstFragment;
	private SlidingListFragmentRight secondFragment;
	private tercerFragment tercerFragment;


	public static Typeface MYRIADPRO_REGULAR;
	public static Typeface myriadpro_boldit;
	public static Typeface champagne_bold;
	public static Typeface champagne_italic;


	@Override

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		
		setContentView(R.layout.activity_main);
		Spinner spinner = (Spinner) findViewById(R.id.spinner);

		MYRIADPRO_REGULAR = Typeface.createFromAsset(getApplicationContext().getAssets(), "myriadpro_regular.otf");
		myriadpro_boldit = Typeface.createFromAsset(getApplicationContext().getAssets(), "myriadpro_boldit.otf");
		champagne_bold = Typeface.createFromAsset(getApplicationContext().getAssets(), "Champagne & Limousines Bold.ttf");
		champagne_italic = Typeface.createFromAsset(getApplicationContext().getAssets(), "Champagne & Limousines Italic.ttf");

		//Champagne & Limousines Italic

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.array_spinner, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);

		//Add first fragment
		mFirstFragment = new SlidingListFragmentLeft();
		secondFragment = new SlidingListFragmentRight();
		tercerFragment = new tercerFragment();

		FragmentManager fm = getFragmentManager();
		FragmentTransaction fragmentTransaction = fm.beginTransaction();
		fragmentTransaction.add(R.id.fragment_place, mFirstFragment);
		fragmentTransaction.commit();
	}
	/*
    public void addTransition(View view) {
        Button button = (Button) findViewById(R.id.button);

        if (getFragmentManager().getBackStackEntryCount()==0) {

        	Fragment secondFragment = new SlidingListFragmentRight();

            Fragment tercerFragment = new tercerFragment();

            FragmentManager fm = getFragmentManager();

            FragmentTransaction fragmentTransaction = fm.beginTransaction();

            FragmentTransactionExtended fragmentTransactionExtended = new FragmentTransactionExtended(this, fragmentTransaction, mFirstFragment, secondFragment, R.id.fragment_place);

            fragmentTransactionExtended.addTransition(optionSelected);

            fragmentTransactionExtended.commit();

            button.setText("Back");
        }else{
            getFragmentManager().popBackStack();
            button.setText("Push");
        }


    }
	 */

	public void addTransition(View view) {
		Button button = (Button) findViewById(R.id.button);

		if (getFragmentManager().getBackStackEntryCount()==0) {

			Fragment secondFragment = new SlidingListFragmentRight();

			Fragment tercerFragment = new tercerFragment();

			FragmentManager fm = getFragmentManager();

			FragmentTransaction fragmentTransaction = fm.beginTransaction();

			FragmentTransactionExtended fragmentTransactionExtended = new FragmentTransactionExtended(this, fragmentTransaction, mFirstFragment, secondFragment, R.id.fragment_place);

			fragmentTransactionExtended.addTransition(optionSelected);

			fragmentTransactionExtended.commit();

			button.setText("actual2");
		}else if (button.getText().toString()=="actual2"){
			
			FragmentManager fm = getFragmentManager();

			FragmentTransaction fragmentTransaction = fm.beginTransaction();

			FragmentTransactionExtended fragmentTransactionExtended = new FragmentTransactionExtended(this, fragmentTransaction, secondFragment, tercerFragment, R.id.fragment_place);

			fragmentTransactionExtended.addTransition(optionSelected);

			fragmentTransactionExtended.commit();

			button.setText("actual3");
		}else{	

			getFragmentManager().popBackStack();
			button.setText("Push");
		}


	}
	@Override
	public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
		optionSelected = i;
	}

	@Override
	public void onNothingSelected(AdapterView<?> adapterView) {
	}

	@Override
	public void onBackPressed()
	{
		Button button = (Button) findViewById(R.id.button);
		button.setText("Push");
		super.onBackPressed();
	}

}
