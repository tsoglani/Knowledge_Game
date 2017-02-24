package game.knowledge.tsoglanakos.knowledgegame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final RelativeLayout r= new RelativeLayout(this);
		Settings.System.getInt(getContentResolver(),Settings.System.ALWAYS_FINISH_ACTIVITIES, 0);
		setContentView(r);
		MyMenu m= new MyMenu(this);
		r.addView(m);
		m.getNewButton().setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				r.removeAllViews();
				r.addView(new Game(MainActivity.this));
			}});
	}
	@Override
	public void onBackPressed() {
		AlertDialog.Builder alert;
		alert=  new AlertDialog.Builder(this);
		alert.setIcon(android.R.drawable.ic_dialog_alert);
	        alert.setTitle("Closing Activity");
	        alert.setMessage("Are you sure you want to Close this Application?");
	    
	        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		       Game.isGameOver=true;  
		       System.exit(0);
		        }

	    });
	        alert.setNegativeButton("No", null);
	        alert.show();
	}

}
