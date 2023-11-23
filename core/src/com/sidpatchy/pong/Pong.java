package com.sidpatchy.pong;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Pong extends ApplicationAdapter {
	public final static float WIDTH = 800;
	public final static float HEIGHT = 600;

	FitViewport viewport;
	//ScreenViewport viewport;
	ShapeRenderer shape;
	OrthographicCamera camera;

	float rectLeftY = 200;
	float rectRightY = 200;
	float paddleSpeed = 500;

	float ballXStart = 300;
	float ballYStart = 400;
	float ballX = 300;
	float ballY = 400;
	float ballXSpeed = -250;
	float ballYSpeed = -250;

	int leftPoints = 0;
	int rightPoints = 0;

	@Override
	public void create () {
		shape = new ShapeRenderer();
		camera = new OrthographicCamera();
		camera.position.set((float) WIDTH / 2, (float) HEIGHT / 2, 0);
		camera.update();
		viewport = new FitViewport(WIDTH, HEIGHT, camera);
		//viewport = new ScreenViewport(camera);
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);
	}

	@Override
	public void render() {

		float deltaTime = Gdx.graphics.getDeltaTime(); // Time elapsed since last frame

		ballX += ballXSpeed * deltaTime;
		ballY += ballYSpeed * deltaTime;

		// Clear screen
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		shape.setProjectionMatrix(camera.combined);

		shape.begin(ShapeRenderer.ShapeType.Filled);
		shape.setColor(Color.WHITE);

		shape.rect(10, rectLeftY, 10, 100);
		shape.rect(780, rectRightY, 10, 100);
		shape.ellipse(ballX, ballY, 15, 15);

		shape.end();

		// Define bounding boxes
		Rectangle wallTop = new Rectangle(-1000, 600, 1000000, 1);
		Rectangle wallBottom = new Rectangle(-1000, 0, 1000000, 1);
		Rectangle wallLeft = new Rectangle(0, 0, 1, 800);
		Rectangle wallRight = new Rectangle(800, 0, 1, 800);

		Rectangle paddleLeftRect = new Rectangle(10, rectLeftY, 10, 100);
		Rectangle paddleRightRect = new Rectangle(780, rectRightY, 10, 100);
		Rectangle ballRect = new Rectangle(ballX, ballY, 15, 15);

		// Paddle & Top/Bottom wall collision logic
        if (ballRect.overlaps(wallTop) || ballRect.overlaps(wallBottom)) {
            ballYSpeed = -ballYSpeed;
        } else if (ballRect.overlaps(paddleLeftRect) || ballRect.overlaps(paddleRightRect)) {
            ballXSpeed = -ballXSpeed;
        }

		// Handle ball interacting with left/right wall
		if (ballRect.overlaps(wallLeft)) {
			resetBall("left");
		} else if (ballRect.overlaps(wallRight)) {
			resetBall("right");
		}

		// Left paddle movement
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			rectLeftY += paddleSpeed * deltaTime; // Move up
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			rectLeftY -= paddleSpeed * deltaTime; // Move down
		}

		// Right paddle movement
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			rectRightY += paddleSpeed * deltaTime; // Move up
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			rectRightY -= paddleSpeed * deltaTime; // Move down
		}

		// Prevent paddle moving off-screen
		if (paddleLeftRect.overlaps(wallTop)) {
			rectLeftY = 500;
		}
		if (paddleLeftRect.overlaps(wallBottom)) {
			rectLeftY = 1;
		}

		if (paddleRightRect.overlaps(wallTop)) {
			rectRightY = 500;
		}
		if (paddleRightRect.overlaps(wallBottom)) {
			rectRightY = 1;
		}
	}

	public void resetBall(String side) {
		ballX = ballXStart;
		ballY = ballYStart;

		if (side.equalsIgnoreCase("left")) {
			leftPoints += 1;

			ballXSpeed = 250;
			ballYSpeed = 250;
		}
		else if (side.equalsIgnoreCase("right")) {
			rightPoints += 1;
			ballXSpeed = -250;
			ballYSpeed = -250;
		}
	}
}