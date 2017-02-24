package game.knowledge.tsoglanakos.knowledgegame;

import java.io.IOException;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

public class MusicPlayer {
	Activity context;
	public MusicPlayer(Activity ctx){
		this.context=ctx;
	}
	 MediaPlayer p = null;
	 public void playSound(String filename) {
		 AssetFileDescriptor afd;
		try {
			afd = context.getAssets().openFd(filename);
		 
		 MediaPlayer player = new MediaPlayer();  
		 player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());  
		 player.prepare();  
		 player.start(); 
		 } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	 } 
}
