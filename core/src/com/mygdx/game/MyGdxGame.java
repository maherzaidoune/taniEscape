package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.GdxFileSystem;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.sun.corba.se.impl.oa.toa.TOA;
import java.awt.event.ActionListener;
import java.util.Random;


// game class

public class MyGdxGame extends ApplicationAdapter  {
	SpriteBatch batch;
	float speed;
	int backgroundNumber = 5;
	Texture[] background = new Texture[backgroundNumber];
	private OrthographicCamera camera;
	int level;
	float timePassed;
	float TaniY;
	float TaniX;
	float velocity;
	boolean gamePaused;
	float gravity;
	Random random;
	Texture start;
	Texture exit;
	BitmapFont font;
    int score = 0;
	float an = 0;
    int scorer = 0;
	int state = 0; // 0 : move / 1 : jump / 2 : die
	int srcX = 1;
	int zstate = 0; // 0 move , 1 attack , 2 die
	int numberOfZombie = 5;
	float[] zombieX = new float[numberOfZombie];
	float zombieSpeed = 1.5f;
	int randomAttac;
	Music zombiewalk;
	Music zombievoice;
	Music zombievoice1;
	//monster
	Animation[] taniMovAnnimation = new Animation[3];
	Animation[] rourou = new Animation[3];
	Animation[] mon = new Animation[3];
	Animation[] saf = new Animation[3];
	Animation[] levelOneZombie = new Animation[3];
	Rectangle[] zombie = new Rectangle[numberOfZombie];
	Rectangle hero;
	int GameChoice ; // 0 tani , 1 rourou , 2  mon ,3 saf
	int d ;

	public Texture createTexture(String name) {
		Texture img = new Texture(Gdx.files.internal(name));
		return img;
	}

	public Animation createAnimation(String name) {
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(name));
		Animation animation = new Animation(1 / 5f, atlas.getRegions());
		return animation;
	}

	@Override
	public void create() {

		exit = createTexture("EXIT.png");
		start = createTexture("START.png");
		batch = new SpriteBatch();
		speed = 1.5f;
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(3);
		timePassed = 0f;
		gamePaused = true;
		gravity = 0.5f;
		TaniY = 10;
		GameChoice = 4;
		TaniX = Gdx.graphics.getHeight() * 3/2 - 50;
		velocity = 0;
		level = 1;
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		random = new Random();
		randomAttac = random.nextInt(50);
        zombiewalk = Gdx.audio.newMusic(Gdx.files.internal("zombiewalk.wav"));
		zombievoice = Gdx.audio.newMusic(Gdx.files.internal("zombieSound1.wav"));
		zombievoice1 = Gdx.audio.newMusic(Gdx.files.internal("zombievoice.mp3"));
		zombievoice1.setLooping(true);
		zombievoice.setLooping(false);
		zombiewalk.setLooping(true);
		//monster create
		taniMovAnnimation[0] = createAnimation("taniMov.atlas");
		taniMovAnnimation[1] = createAnimation("TaniJump.atlas");
		rourou[0] = createAnimation("rourouWalk.atlas");
		rourou[1] = createAnimation("rourouJump.atlas");
		mon[0] = createAnimation("monWalk.atlas");
		mon[1] = createAnimation("monJump.atlas");
		saf[0] = createAnimation("safWalk.atlas");
		saf[1] = createAnimation("safJump.atlas");



		// zombie
		levelOneZombie[0] = createAnimation("zombieOneWalk.atlas");
		levelOneZombie[1] = createAnimation("zombieOneRun.atlas");
        // backs
		background[0] = createTexture("back0.png");
		background[1] = createTexture("back1.png");
		background[2] = createTexture("back2.png");
		background[3] = createTexture("back3.png");
		background[4] = createTexture("back4.png");

		for (int i = 1; i < backgroundNumber ; i++) {

			background[i].setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.ClampToEdge);

		}


		for (int i = 0 ; i< numberOfZombie ;i++){
			zombieX[i] =  - i * Gdx.graphics.getWidth()/2 - random.nextInt(200);
		}
	}

	@Override
	public void render() {

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// backgroun
		batch.begin();
		// parallax effect
		batch.draw(background[2], 0, 0, srcX, 0, background[2].getWidth(), Gdx.graphics.getHeight());
		batch.draw(background[0], 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.draw(background[1], 0, 0, -srcX/5, 0, background[1].getWidth(), Gdx.graphics.getHeight());
		batch.draw(background[3], 0, 0, srcX , 0, background[3].getWidth(), Gdx.graphics.getHeight());
		batch.draw(background[4], 0, 0, srcX, 0, background[3].getWidth(), Gdx.graphics.getHeight());
		srcX -= speed;

		if (gamePaused){

			if (zombievoice.isPlaying()) {
				zombievoice.stop();
			}
				if (zombievoice1.isPlaying()) {
					zombievoice1.stop();
				}
				if (zombiewalk.isPlaying()) {
					zombiewalk.stop();
				}

		}

		batch.end();

		//start menu
		if (gamePaused ) {

			batch.begin();
			batch.draw(start, Gdx.graphics.getWidth() / 4 - start.getWidth() / 2, Gdx.graphics.getHeight() / 2, exit.getWidth(), exit.getHeight());
			batch.draw(exit, Gdx.graphics.getWidth() *3/ 4 -  start.getWidth()/2, Gdx.graphics.getHeight()/2 , exit.getWidth(), exit.getHeight());
			batch.draw((TextureRegion) taniMovAnnimation[0].getKeyFrame(an, true), TaniX - 50, TaniY);
			batch.draw((TextureRegion) rourou[0].getKeyFrame(an, true), TaniX - 200, TaniY);
			batch.draw((TextureRegion) mon[0].getKeyFrame(an, true), TaniX - 350, TaniY);
			batch.draw((TextureRegion) saf[0].getKeyFrame(an, true), TaniX - 500, TaniY);
			an +=  Gdx.graphics.getDeltaTime();
            batch.end();
		}
		//choosing game car
		if(gamePaused && Gdx.input.isTouched())
		{
			Vector3 tmp=new Vector3(Gdx.input.getX(),Gdx.input.getY(),0);
			camera.unproject(tmp);
			Rectangle startRec=new Rectangle(-Gdx.graphics.getWidth() / 4 - start.getWidth() / 2, -start.getHeight() / 2 , exit.getWidth(), exit.getHeight());
			Rectangle exitRec=new Rectangle(Gdx.graphics.getWidth() / 4 - exit.getWidth() / 2,  -start.getHeight()/2 , exit.getWidth(), exit.getHeight());
			Rectangle tani = new Rectangle(270 ,-200,150,100);
			Rectangle rourou = new Rectangle( 100 ,-200,150,100);
			Rectangle mon = new Rectangle( -50,-200,150,100);
			Rectangle saf = new Rectangle(-200,-200,150,100);

			if(tani.contains(tmp.x,tmp.y)){
				GameChoice = 0;
			}
			else if(rourou.contains(tmp.x,tmp.y)){
				GameChoice = 1;
			}
			else if(mon.contains(tmp.x,tmp.y)){
				GameChoice = 2;
			}
			else if(saf.contains(tmp.x,tmp.y)){
				GameChoice = 3;
			}

			Gdx.app.log("toched",String.valueOf(GameChoice));
			Gdx.app.log("tmpx",String.valueOf(tmp.x));
			Gdx.app.log("tmpy",String.valueOf(tmp.y));
			Gdx.app.log("taniX",String.valueOf(TaniX));
			Gdx.app.log("Taniy",String.valueOf(TaniY));



			if(startRec.contains(tmp.x,tmp.y))
			{
				if (GameChoice != 4) {

					for (int i = 0; i < numberOfZombie; i++) {
						zombieX[i] = -i * Gdx.graphics.getWidth() / 2 - random.nextInt(200);
					}

					TaniX = Gdx.graphics.getHeight() * 3 / 2 - 50;
					gamePaused = false;
				}
				else {GameChoice = random.nextInt(4);}
			}
			else if(exitRec.contains(tmp.x,tmp.y)){
				super.dispose();
				Gdx.app.exit();
			}
		}


		// game Loop

			if (!gamePaused) {

                zombiewalk.setVolume(0.3f);
				zombiewalk.play();
				zombievoice1.setVolume(0.6f);
				zombievoice1.play();


				if (TaniX > 5 ) {

					if (Gdx.input.getDeltaY() < -1 && Gdx.input.getDeltaX() < 0) {

						if (TaniY <= 11) {


							velocity -= 18;
							state = 1;
							TaniX += 2;
							d = 0; // i9adm;
						}
					}
				}
				if (TaniX <= Gdx.graphics.getHeight() * 3/2 - 50 ){
					if (Gdx.input.getDeltaY() < -1 && Gdx.input.getDeltaX() > 0) {
						if (TaniY <= 11) {
							velocity = -15;
							state = 1;
							TaniX += 1.7f;
							d = 1; // ywa5r;
						}

					}
				}
					if ((TaniY > 11 || velocity < 0) && d == 0) {
						TaniY -= velocity;
						velocity += gravity;
						TaniX -= 1.9f;
						state = 1;

					} else if ((TaniY > 11 || velocity < 0) && d == 1) {
						TaniY -= velocity;
						velocity += gravity;
						TaniX += 1.8f;
						state = 1;
					}
					if (TaniY <= 11) {
						state = 0;
					}

				batch.begin();


                //scoring
                if (zombieX[scorer] > TaniX){
                    score ++;
                    scorer ++;
                }if(scorer > numberOfZombie - 1){
                    scorer = 0;
                }


				// draw monster
				timePassed += Gdx.graphics.getDeltaTime();




				if (GameChoice == 0) {

					batch.draw((TextureRegion) taniMovAnnimation[state].getKeyFrame(timePassed, true), TaniX, TaniY);
				}else if(GameChoice == 1){
					batch.draw((TextureRegion) rourou[state].getKeyFrame(timePassed, true), TaniX, TaniY);
				}
				else if (GameChoice == 2){
					batch.draw((TextureRegion) mon[state].getKeyFrame(timePassed, true), TaniX, TaniY);
				}
				else if (GameChoice ==3){
					batch.draw((TextureRegion) saf[state].getKeyFrame(timePassed, true), TaniX, TaniY);
				}


				hero = new Rectangle(TaniX ,TaniY,45,45);



				for (int i = 0;i < numberOfZombie ; i++) {

					zombie[i] = new Rectangle(zombieX[i] - 20, 10, 90, 45);

					if (TaniX - zombieX[i] < 300 + randomAttac ) {
                        zombievoice.setVolume(0.4f);
                        zombievoice.play();
						zstate = 1;
						zombieSpeed += 1;
					}
					else {
						zstate = 0;
						zombieSpeed = 1.5f ;
					}

					if (zombieX[i] > Gdx.graphics.getWidth() - 100) {
						zombieX[i] -= numberOfZombie * Gdx.graphics.getWidth() / 2 + random.nextInt(30);
						randomAttac = random.nextInt(50);
					} else {
						zombieX[i] += zombieSpeed;
					}

					//draw zombie 1

					batch.draw((TextureRegion) levelOneZombie[zstate].getKeyFrame(timePassed, true), zombieX[i], 10);

					if (Intersector.overlaps(zombie[i],hero)){
                        score = 0;
                        scorer = 0;
                        srcX = 0;
						TaniX = Gdx.graphics.getHeight() * 3/2 - 50;
						gamePaused = true;
					}


				}

                font.draw(batch,String.valueOf(score),Gdx.graphics.getWidth() - 100,Gdx.graphics.getHeight() - 50);



				batch.end();


			}


		}
		@Override
		public void dispose () {
			batch.dispose();
			zombievoice1.dispose();
			zombievoice.dispose();
			zombiewalk.dispose();
			background[level].dispose();
		}

}
