package game.knowledge.tsoglanakos.knowledgegame;

import java.util.ArrayList;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.*;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class Options extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_options);	
	final RadioGroup rg= (RadioGroup) findViewById(R.id.radioGroup);
	final TextView mistakeView = (TextView) findViewById(R.id.showTotalMistakes);
	final SeekBar mistakeBar = (SeekBar) findViewById(R.id.mistakesbar);
	getWindow().getDecorView().setBackgroundResource(R.drawable.p1);
	
	for(int i=0;i<rg.getChildCount();i++){
		View o = rg.getChildAt(i);
        if (o instanceof RadioButton) {
           RadioButton rb=(RadioButton) o;
           
        	   if(rb.getText().equals(Integer.toString(Game.totalRounds))){
        	   rb.setChecked(true);
           }
        }
	}
		Button save = (Button) findViewById(R.id.saveButton);
		save.setBackgroundColor(Color.DKGRAY);
		mistakeView.setText(Integer.toString(mistakeBar.getProgress()));
		
		mistakeBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
			mistakeView.setText(Integer.toString(progress));
				
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
		});
		mistakeBar.setProgress(Game.maxMistakes);
		mistakeBar.setMax(5);
		
	
save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Game.maxMistakes=mistakeBar.getProgress();
				 int count = rg.getChildCount();
			        ArrayList<RadioButton> listOfRadioButtons = new ArrayList<RadioButton>();
			        for (int i=0;i<count;i++) {
			            View o = rg.getChildAt(i);
			            if (o instanceof RadioButton) {
			               RadioButton rb=(RadioButton) o;
			               if(rb.isChecked()){
			            	   Game.totalRounds=Integer.parseInt((String) rb.getText());
			            	   
			               }
			            }
			        }
			   
			        Toast.makeText(Options.this, "Successfull Save \n Max Mistakes = "+Game.maxMistakes+" \nTotal Rounds = "+Game.totalRounds, Toast.LENGTH_LONG).show();
			}
		});
		
		
		
		Button goBack = (Button) findViewById(R.id.GoBack);
		goBack.setBackgroundColor(Color.RED);
goBack.setOnClickListener(new OnClickListener() {

	@Override
	public void onClick(View v) {
	Intent intent= new Intent(Options.this,MainActivity.class);
	startActivity(intent);
	}
});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	  Intent intent= new Intent(Options.this,MainActivity.class);
	  startActivity(intent);
	        return true;
	    }

	    return super.onKeyDown(keyCode, event);
	}
}
