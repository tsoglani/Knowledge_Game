package game.knowledge.tsoglanakos.knowledgegame;



import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MyMenu extends ViewGroup {
	private Button newGameButton = null;
	private Button options = null;

	public MyMenu(final Context context) {
		super(context);
		newGameButton = new Button(context);
		options = new Button(context);
		newGameButton.setText("New Game ");
		options.setText("Options");
		addView(newGameButton);
		addView(options);
		newGameButton.setTextSize(25);
		options.setTextSize(25);
		setBackgroundResource(R.drawable.quizbook);
		//options.setBackgroundColor(Color.TRANSPARENT);
		options.getBackground().setAlpha(101);
		newGameButton.getBackground().setAlpha(101);
		//newGameButton.setBackgroundColor(Color.TRANSPARENT);
		options.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent in = new Intent(context, Options.class);
				context.startActivity(in);
			}
		});
	}

	public Button getNewButton() {

		return newGameButton;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int length = getChildCount();
		int space = getHeight() / 8;
		for (int i = 0; i < length; i++) {
			getChildAt(i).layout(0, space + i * getHeight() / 4, getWidth(),
					space + i * getHeight() / 4 + getHeight() / 5);
		}
	}
}
