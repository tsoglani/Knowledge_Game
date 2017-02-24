package game.knowledge.tsoglanakos.knowledgegame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class Game extends ViewGroup implements Runnable {
	private Hashtable<String, String> hash;
	private InteractiveParts interactivePart;
	private Screen screen;
	private String corectAns = "";
	private Activity context;
	public static int mistakes = 0;
	public static int maxMistakes = 3, totalRounds = 10;
	private int curRound = 0;
	private SeekBar progressBar;
	public static int corrects;
	private final int maxTime = 15;
	private int timer = maxTime;
	static boolean isGameOver = false;
	static int score = 0;
	private int maxScore = 0;

	public Game(Context context) {
		super(context);
	
		this.context = (Activity) context;
		init();
		addView(screen);
		progressBar = new SeekBar(context, null,
				android.R.attr.progressBarStyleHorizontal);
		progressBar.setIndeterminate(false);
		progressBar.setEnabled(false);
		progressBar.setMax(maxTime);
		// progressBar.setProgressDrawable(new ColorDrawable(Color.rgb(
		// Color.RED, Color.GREEN, Color.BLUE)));
		progressBar.setProgress(maxTime);
		// progressBar.setSoundEffectsEnabled(true);
		new Thread(this).start();
		addView(progressBar);
		addView(interactivePart);
	}

	private void init() {
		isGameOver = false;
		
		resetValues();
		hash = new Hashtable<String, String>();
		interactivePart = new InteractiveParts();
		screen = new Screen(context);
		generateDB();
		generateNextQuiz();
		

	}
	private void resetValues(){
		DB db= new DB(context);
		maxScore = db.getMaxScore(totalRounds);
		corrects = 0;
		score = 0;
		mistakes = 0;
	}

	public void run() {
		while (!isGameOver) {
			try {
				Thread.sleep(1000);
				timer--;
				if (timer < 0) {
					context.runOnUiThread(new Thread() {
						public void run() {

							Toast.makeText(context, "Tou miss the time .. ",
									Toast.LENGTH_SHORT).show();

							generateNextQuiz();
						}
					});

					mistakes++;

				}
				progressBar.setProgress(timer);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int length = getChildCount();

		for (int i = 0; i < length; i++) {
			if (i == 0) {
				getChildAt(i).layout(0, 0, getWidth(),
						getHeight() / 2 - getHeight() / 20);
			} else if (i == 1) {
				getChildAt(i).layout(0, getHeight() / 2 - getHeight() / 20,
						getWidth(), getHeight() / 2);
			} else {
				getChildAt(i).layout(0, getHeight() / 2, getWidth(),
						getHeight());
			}

		}

	}

	private void setTextInteractiveButtons(ArrayList<String> arrayList) {
		for (int i = 0; i < interactivePart.getButtons().length; i++) {
			final int asdas = i;
			final int curRand = (int) (Math.random() * arrayList.size());
			final String output = arrayList.remove(curRand);
			context.runOnUiThread(new Thread() {
				public void run() {
					interactivePart.getButtons()[asdas].setText(output);
				}
			});

		}

	}

	private void resetInteractiveButtons() {
		for (int i = 0; i < interactivePart.getButtons().length; i++) {

			final int asdas = i;

			context.runOnUiThread(new Thread() {
				public void run() {
					interactivePart.getButtons()[asdas].setText("");
				}
			});

		}

	}

	private void generateNextQuiz() {

		curRound++;
		timer = maxTime;

		for (int i = 0; i < interactivePart.getButtons().length; i++) {
			interactivePart.getButtons()[i].setBackgroundColor(Color.YELLOW);
		}
		if (mistakes >= maxMistakes) {
			Intent intent = new Intent(context, GameOver.class);
			isGameOver = true;
			context.startActivity(intent);

		}
		if (curRound > totalRounds) {
			Intent intent = new Intent(context, Win.class);
			context.startActivity(intent);
			isGameOver = true;

		}

		if (isGameOver) {
			DB db = new DB(context);
			db.addScore(score,totalRounds);
			
			return;
		}
		resetInteractiveButtons();
		int rand = getRandomNumber();
		final String question = getQuestion(rand);
		String ans = getAnsrsFromQuest(question);
		context.runOnUiThread(new Thread() {
			public void run() {
				screen.setText("\n\n" + question
						+ "\n\n\n\n\n remaining lives : "
						+ (maxMistakes - mistakes)
						+ "                  Score : " + score
						+" \n                                       Max Score :"+maxScore
						+ "\n\n correct : " + corrects + "\n mistakes : "
						+ mistakes);
			}
		});
		String[] pinax = ans.split("_");
		corectAns = pinax[0];
		final ArrayList<String> arrayList = new ArrayList<String>();
		for (int i = 0; i < pinax.length; i++) {
			arrayList.add(pinax[i]);
		}

		setTextInteractiveButtons(arrayList);
		screen.randomImage();
		removeRandomNumberNdQuest(question);

	}

	private void generateDB() {
		hash.put("kandinsky is a famous? ", "Painter_Actor_Singer_Reporter");
		hash.put(" Country with stars in her Flag ? ",
				"United States_Greece_Mexico_Italy");
		hash.put("Which one is a Spain Soccer team ? ",
				"Valencia_Napoli_Vasko Da Gama_Maritmo");
		hash.put("In Which continent is Amazon ? ",
				"South America_North America_Europe_Africa");
		hash.put("When the first Olymbic Game took place ? ",
				"1896_1888_1892_1884");
		hash.put("Actor who played at Fight Club? ",
				"Eduard Norton_Leonardo Di Caprio_Al Paccino_Johny Depp");
		hash.put("Which is the capital of Colombia? ",
				"Bokota_Lima_Santiago_Banchi");
		hash.put("How many times has the England team won the world cup? ",
				"0_1_2_3");
		hash.put("Which animal is blaimed for the most kills  ? ",
				"Mosquito_Combra_Crocodile_Lion");
		hash.put("Which one is not a drug ? ", "Alkohol_Hasis_Heroin_Ecstasy");
		hash.put("Who was Batman  ? ",
				"Brus Wein _Clark Kent_Tony Stark_Bruce Banne ");
		hash.put("Who was Superman ? ",
				"Clark Kent _Barry Allen_Peter Parker _ Alan Scott");
		hash.put("Who was Flash  ? ",
				"Barry Allen _Peter Parker _Bruce Banne _ Tony Stark_");
		hash.put("Who was Spiderman  ? ",
				"Peter Parker _Clark Kent _Bruce Banne _Brus Wein ");
		hash.put("Was playing at  'The Shinning'  ? ",
				"Jack Nicholson _Edward Bunker _Tom Hanks _Morgan Freeman ");
		hash.put("Which one is not a programming language ? ",
				"HTML_JAVA_PYTHON_C# ");
		hash.put(
				"Which one movie has the less Oscars ? ",
				"Gone With The Wind _Titanic _Ben-Hur _The Lord of the Rings: The Return of the King ");
		hash.put("How champions league has Liverpool? ", "5_4_7_6");
		hash.put("How champions league has Manchester United FC? ", "3_5_7_1");
		hash.put("Which one has different result? ",
				"3+3-5-1+9-6_1+5-3-7+6+2_5-3-2+4-2+3-1_8-4+2-7+5 ");
		hash.put("When the World War II start ? ", "1936_1938 _1940_1942 ");
		hash.put("When the World War I start ? ", " 1914_1912 _1910 _1908 ");
		hash.put("3,6,12,24 ...? ", "48_52_54_56");
		hash.put("1,2,6,18,72 ...? ", "360_350_340_370");
		hash.put("Which one is RPG Game ? ", "Pokemon_GTA_PES_Call Of Duty");
		
		hash.put("Which planet is nearest the sun ?", " Mercury_Venus_Uranus_Pluto");
		hash.put("Which country has the longest coastline? ", "Canada_Greece_USA_Italy");
		hash.put("Which country is reputed to have the world's oldest flag design? ", "Demark_Italy_Africa_Spain");
		hash.put("Who was the youngest ever American President?", "Theodore Roosevelt_Thomas Jefferson_George Washington_Grover Cleveland");
		hash.put("Grand Central Terminal, Park Avenue, New York is the world's? ", "largest railway station_highest railway station_ 	longest railway station_None of the above");
		hash.put("Entomology is the science that studies ? ", "Insects_Behavior of human beings_ Technical and scientific terms_The formation of rocks");
		hash.put("Garampani sanctuary is located at ? ", "Diphu, Assam_Junagarh, Gujarat_Kohima, Nagaland_Gangtok, Sikkim");
		hash.put("For which of the following disciplines is Nobel Prize awarded?? ", "All of the above_Literature, Peace and Economics_Physiology or Medicine_Physics and Chemistry");
		hash.put("'OS' computer abbreviation usually means ? ", "Operating System_Open Software_Optical Sensor_Order of Significance");
		hash.put("Who is the father of Geometry? ", "Euclid_Kepler_Pythagoras_Aristotle");
		hash.put("Charles Correa has distinguished himself in which of the following fields? ", "Architecture_Western Music_Painter_Actor");
		hash.put("The light of distant stars is affected by ? ", "both _interstellar dust_the earth's atmosphere_None of them");
		hash.put("Which scientist discovered the radioactive element radium?", "Marie Curie_Benjamin Franklin_Albert Einstein_Isaac Newton");
		hash.put("What Galileo invented? ", "Thermometer_Microscope_Pendulum clock_Barometer");
		hash.put("Who invented Gramophone ?", "Thomas Alva Edison_Sir Alexander Graham Bell_Fahrenheit_Michael Faraday");
		hash.put("The first hand glider was designed by...? ", "Leonardo DaVinci_Galileo_Albert Einstein_Francis Rogallo");
				
		////41
		hash.put("For the Olympics and World Tournaments, the dimensions of basketball court are? ", "28 m x 15 m_26 m x 14 m_26 m x 17 m_27 m x 16 m ");
		hash.put("The largest gold producing country in the world(in 2006) is ? ", "South Africa_USA_Canada_China ");
		hash.put("The largest country of the world by geographical area is ? ", "Russia_Australia_USA_Canada");
		hash.put("Which of the following operating systems is produced by IBM ? ", "OS-2_DOS_UNIX_Windows");
		hash.put("What is a GPU ? ", "Graphics Processing Unit_Grouped Processing Unit_ 	Graphical Performance Utility_Graphical Portable Unit");
		hash.put("What is a MAC ? ", "Media Access Control_Mediocre Apple Computer_Memory Address Corruption_A Computer made by Apple");
		hash.put("Which of the following is used as a moderator in nuclear reactor?? ", "Graphite_Ordinary water_Radium_Thorium");
		hash.put("What is the unit for measuring the amplitude of a sound?? ", "Decibel_Hum_Cycles_Coulomb");
		hash.put("One kilometre is equal to how many miles ? ", "0.62_0.5_0.84_1.2");
		hash.put("One horse power is equal to ? ", "746 watts_748 watts_756 watts_736 watts");
		hash.put("Balloons are filled with ? ", "helium_argon_oxygen_nitrogen");
		hash.put("The planet that takes the highest time for completing a rotation is? ", "Venus_Pluto_Earth_Mercury ");
		////53	
		
		hash.put("The famous book 'Anna Karenina' written by ? ", " 	Leo Tolstoy_Lewis Carroll_Victor Hugo_Boris Pasternak");
		hash.put("The headquarters of the UNESCO is at ? ", "Paris_New York_Geneva_Rome");
		hash.put("How many players are there in Water Polo team?? ", "7_6_5_8");
		hash.put("Most highly intelligent mammals are ? ", "Dolphins_Elephants_Whales_Kangaroos");
		hash.put("Most abundant tissues of our body are ? ", "Connective_Nervous_Epithelial_Muscular");
		hash.put("The nucleus of an atom consists of? ", "Protons and neutrons_All of the above_Electrons and protons_Electrons and neutrons");
		hash.put("Light year is a unit of ? ", "Distance_Time_Light_Intensity of light");
		hash.put("Light from the Sun reaches us in nearly? ", "8 minutes_16 minutes_5 minutes_10 minutes");
		hash.put("When was Mahatma Gandhi assassinated ? ", "1948_1958_1951_1971 ");
		hash.put("The number of already named bones in the human skeleton is ? ", "206_201_215_212");
		hash.put("The 2006 World Cup Football Tournament held in ? ", "Germany_France_Brazil_China ");
		hash.put("Joule is the unit of ? ", "Energy_Temperature_Heat_Pressure");
		hash.put("Which one is the best source of protein? ? ", "Fish_Milk_Lettuce_Rizoto ");
		hash.put("How many red blood cells does the bone marrow produce every second ? ", "10 million_8 million_6 million_4 million");
		//67
		
hash.put("How many players are there on each side in the game of Basketball?? ", "5_4_6_7");
hash.put("In cricket, the two sets of wickets are? ", "22_20_18_16");
hash.put("Fathometer is used to measure? ", "Ocean depth_Earthquakes_Rainfall_Sound intensity");
//70			
hash.put("Maji de spell is an enemy to? ", "Scrooge mc duck _Spiderman _Fantastic 4 _Bugs Bunny ");
				hash.put("Which one does not match ? ", "Trombon _Contrabasso_Violoncello _Baglamas");
				hash.put("Which one does not match ? ", "Windows_Ubudu_Mach Os_Linux");
				hash.put("Which one does not match ? ", "Dreamcast_Wii_GameBoy_Nintendo");
				hash.put("F=M*A want invented by ? ", " Newton_Albert Einstain_Madam Quri_Galileo Galilei ");
				//hash.put("  ? ", " _ _ _ ");11
				//hash.put("  ? ", " _ _ _ ");
				//hash.put("  ? ", " _ _ _ ");
		//hash.put("  ? ", " _ _ _ ");
				//hash.put("  ? ", " _ _ _ ");
				//hash.put("  ? ", " _ _ _ ");
				//hash.put("  ? ", " _ _ _ ");
				//hash.put("  ? ", " _ _ _ ");
				//hash.put("  ? ", " _ _ _ ");
				//hash.put("  ? ", " _ _ _ ");
				//hash.put("  ? ", " _ _ _ ");
				//hash.put("  ? ", " _ _ _ ");
				//hash.put("  ? ", " _ _ _ ");
				//hash.put("  ? ", " _ _ _ ");
				//hash.put("  ? ", " _ _ _ ");
				//hash.put("  ? ", " _ _ _ ");
				//hash.put("  ? ", " _ _ _ ");
				//hash.put("  ? ", " _ _ _ ");
				//hash.put("  ? ", " _ _ _ ");
				//hash.put("  ? ", " _ _ _ ");
				//hash.put("  ? ", " _ _ _ ");
				//hash.put("  ? ", " _ _ _ ");
		//hash.put("  ? ", " _ _ _ ");
				//hash.put("  ? ", " _ _ _ ");
				//hash.put("  ? ", " _ _ _ ");
				//hash.put("  ? ", " _ _ _ ");
				//hash.put("  ? ", " _ _ _ ");
				//hash.put("  ? ", " _ _ _ ");
				//hash.put("  ? ", " _ _ _ ");
				//hash.put("  ? ", " _ _ _ ");
				//hash.put("  ? ", " _ _ _ ");
				//hash.put("  ? ", " _ _ _ ");
				//hash.put("  ? ", " _ _ _ ");
				//hash.put("  ? ", " _ _ _ ");
				//hash.put("  ? ", " _ _ _ ");
				//hash.put("  ? ", " _ _ _ ");
				//hash.put("  ? ", " _ _ _ ");
				//hash.put("  ? ", " _ _ _ ");
				//hash.put("  ? ", " _ _ _ ");
				//hash.put("  ? ", " _ _ _ ");
				//hash.put("  ? ", " _ _ _ ");
		

	}

	/**
	 * 
	 * @return
	 */
	private int getRandomNumber() {
		Random r = new Random();
		int rn = -1;
		while (rn == -1) {
			try {
				rn = r.nextInt(hash.size());
			} catch (Exception e) {
			}

		}

		return rn;
	}

	/**
	 * 
	 * @param index
	 * @return
	 */
	private String getQuestion(int index) {
		String question = null;

		Iterator iter = hash.keySet().iterator();

		int counter = 0;
		while (iter.hasNext()) {
			if (counter > index) {
				break;
			}
			question = (String) iter.next();
			counter++;
		}
		return question;
	}

	/**
	 * 
	 * @param index
	 * @return
	 */
	private String getAnsrsFromQuest(String index) {
		return hash.get(index);
	}

	/**
	 * 
	 * @param index
	 */
	private void removeRandomNumberNdQuest(String index) {
		hash.remove(index);
	}

	/**
	 * 
	 * @author Nikos
	 * 
	 */
	private class Screen extends TextView {

		public Screen(Context context) {
			super(context);
			randomImage();
			setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL
					| Gravity.CENTER | Gravity.VERTICAL_GRAVITY_MASK);
			// Log.e("execute here", "Constuctor");
		}

		public void randomImage() {
			// Log.e("execute here", "the son of the bitch");
			String[] fileList;
			try {
				fileList = context.getAssets().list("images");
				// Log.e(Integer.toString(fileList.length), "filelist length ");

				int im = (int) (Math.random() * 8);
				Log.e(Integer.toString(im), "im");
				Drawable d = Drawable.createFromStream(context.getAssets()
						.open("images/" + Integer.toString(im) + ".jpg"), null);
				setBackgroundDrawable(d);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// context.runOnUiThread(new Thread() {
			// public void run() {
			//
			//
			// }
			// });

		}

	}

	/**
	 * 
	 * @author Nikos
	 * 
	 */
	private class InteractiveParts extends ViewGroup {
		private Button[] buttons = new Button[4];
		private Button selectedButton = null;
		private ViewGroup vg = new ViewGroup(context) {

			@Override
			protected void onLayout(boolean changed, int l, int t, int r, int b) {

				this.getChildAt(0).layout(getWidth() / 2, getHeight() / 6,
						getWidth(), getHeight());
			}

		};

		public InteractiveParts() {
			super(context);
			setBackgroundColor(Color.BLACK);
			for (int i = 0; i < buttons.length; i++) {
				buttons[i] = new Button(context);

				final int asfafsdafsafsd = i;
				buttons[i].setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						Button b = (Button) v;
						if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
							if (b.getText().equals(corectAns)) {
								new MusicPlayer(context).playSound("cor.mp3");
								Log.e("You Found it", "CONGRATULATIONS");
								b.setBackgroundColor(Color.GREEN);
								corrects++;
								score += timer * 2;
							} else {
								new MusicPlayer(context).playSound("error.mp3");
								Log.e("You Faild Corect ans is  ---->",
										corectAns);
								score -= 7;
								mistakes++;
								b.setBackgroundColor(Color.RED);

								for (int i = 0; i < interactivePart
										.getButtons().length; i++) {
									if (interactivePart.getButtons()[i]
											.getText().equals(corectAns)) {
										interactivePart.getButtons()[i]
												.setBackgroundColor(Color.GREEN);
									}
								}
							}
						} else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
							generateNextQuiz();
							return true;
						}
						return false;
					}
				});
				// buttons[i].setOnClickListener(new OnClickListener() {
				//
				// @Override
				// public void onClick(View v) {
				// Button b = (Button) v;
				//
				//
				//
				//
				// }
				// });
				addView(buttons[i]);
			}
			addView(vg);

			Button goBack = new Button(context);
			goBack.setText("Go Back");
			goBack.setBackgroundColor(Color.RED);
			vg.addView(goBack);
			goBack.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
				
					Intent intent = new Intent(context, MainActivity.class);
					context.startActivity(intent);
					resetValues();
					isGameOver = true;
				}
			});
		}

		public Button[] getButtons() {
			return buttons;
		}

		@Override
		protected void onLayout(boolean changed, int l, int t, int r, int b) {
			int length = getChildCount();
			int space = getHeight() / 50;
			for (int i = 0; i < length; i++) {
				getChildAt(i).layout(0, space + i * getHeight() / 5,
						getWidth(),
						i * getHeight() / 5 + getHeight() / 6 + space);
			}
		}

	}
}
