package game.knowledge.tsoglanakos.knowledgegame;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Win extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final RelativeLayout r= new RelativeLayout(this);
		Settings.System.getInt(getContentResolver(),Settings.System.ALWAYS_FINISH_ACTIVITIES, 0);
		setContentView(r);
		r.addView(new VG(this));
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	  Intent intent= new Intent(Win.this,MainActivity.class);
	  startActivity(intent);
	        return true;
	    }

	    return super.onKeyDown(keyCode, event);
	}
	
	
	private class VG extends ViewGroup{
	private	Button goBack ;
		public VG(Context context) {
			super(context);
			Toast.makeText(Win.this, "Correct = "+Integer.toString(Game.corrects)+"  \n  Mistakes = "+Integer.toString(Game.mistakes)+"\n Score : "+ Game.score , Toast.LENGTH_LONG).show();
			goBack= new Button(context);
		goBack.setText("Go Back");
		    setBackgroundDrawable(getResources().getDrawable(R.drawable.win));
			goBack.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent= new Intent(Win.this,MainActivity.class);
						startActivity(intent);
					}
				});
			addView(goBack);
		}

		@Override
		protected void onLayout(boolean changed, int l, int t, int r, int b) {
			 int length =getChildCount();
		     int space=getHeight()/10;
		     for(int i=0;i<length;i++){
		    	getChildAt(i).layout(0, getHeight()/2+getHeight()/5,getWidth(), getHeight()) ; 
		     }
		}
		}
}
