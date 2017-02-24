package game.knowledge.tsoglanakos.knowledgegame;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.provider.Settings;

public class GameOver extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Settings.System.getInt(getContentResolver(),
				Settings.System.ALWAYS_FINISH_ACTIVITIES, 0);
		RelativeLayout rl = new RelativeLayout(this);
		setContentView(rl);
		Toast.makeText(this, "  Score : " + Game.score, Toast.LENGTH_LONG)
				.show();
		rl.addView(new gop(this));
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(GameOver.this, MainActivity.class);
			startActivity(intent);
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	private class gop extends ViewGroup {
		private Button goHome;

		public gop(Context context) {
			super(context);
			goHome = new Button(context);
			goHome.setText("Go Back");
			setBackgroundDrawable(getResources().getDrawable(R.drawable.loser));
			addView(goHome);
			goHome.setGravity(Gravity.CENTER_HORIZONTAL);
			goHome.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(GameOver.this,
							MainActivity.class);
					startActivity(intent);
				}
			});
		}

		@Override
		protected void onLayout(boolean changed, int l, int t, int r, int b) {
			int length = getChildCount();
			int space = getHeight() / 10;
			for (int i = 0; i < length; i++) {
				getChildAt(i).layout(0, getHeight() / 2 + getHeight() / 5,
						getWidth(), getHeight());
			}
		}

	}
}
