/*
 * File: Breakout.java
 * -------------------
 * Name:
 * Section Leader:
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {

/** Width and height of application window in pixels.  IMPORTANT NOTE:
  * ON SOME PLATFORMS THESE CONSTANTS MAY **NOT** ACTUALLY BE THE DIMENSIONS
  * OF THE GRAPHICS CANVAS.  Use getWidth() and getHeight() to get the 
  * dimensions of the graphics canvas. */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

/** Dimensions of game board.  IMPORTANT NOTE: ON SOME PLATFORMS THESE 
  * CONSTANTS MAY **NOT** ACTUALLY BE THE DIMENSIONS OF THE GRAPHICS
  * CANVAS.  Use getWidth() and getHeight() to get the dimensions of
  * the graphics canvas. */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

/** Separation between bricks */
	private static final int BRICK_SEP = 4;

/** Width of a brick */
	private static final int BRICK_WIDTH =
	  (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

/** Number of turns */
	private static final int NTURNS = 3;
	
/** Total number of bricks */
	private int TOTAL_BRICKS = NBRICKS_PER_ROW * NBRICK_ROWS;
	
/** Number of turns remaining */
	private int NUM_TURNS_LEFT = NTURNS;
	
/** Delay for runGame event */	
	private int DELAY = 10;
	
/** vx and vy variables */
	private double vx, vy;
	
/** Random Number Generator for vx */	
	private RandomGenerator rgen = RandomGenerator.getInstance();
	
/* Method: run() */
/** Runs the Breakout program. */
	public void run() {
		setUpPaddle();
		setUpBricks();
		while (NUM_TURNS_LEFT > 0) {	
			createBall();
			playGame();
		}
		ball.setVisible(false);
	}
	
	private void setUpBricks() {
		int rowCounter = 0;
		int nextBrick = BRICK_SEP;
		int nextRow = BRICK_Y_OFFSET;
		for (int x = 0; x < NBRICK_ROWS; x++) {	
			for (int i = 1; i <= NBRICKS_PER_ROW; i++) {
				GRect grect = new GRect(nextBrick, nextRow, BRICK_WIDTH, BRICK_HEIGHT);
				add(grect);
				nextBrick += (BRICK_SEP + BRICK_WIDTH);
				switch (rowCounter) {
					case 0: grect.setFillColor(Color.red);
						grect.setFilled(true);
						break;
					case 1: grect.setFillColor(Color.red);
						grect.setFilled(true);
						break;
					case 2: grect.setFillColor(Color.orange);
						grect.setFilled(true);
						break;
					case 3: grect.setFillColor(Color.orange);
						grect.setFilled(true);
						break;
					case 4: grect.setFillColor(Color.yellow);
						grect.setFilled(true);
						break;
					case 5: grect.setFillColor(Color.yellow);
						grect.setFilled(true);
						break;
					case 6: grect.setFillColor(Color.green);
						grect.setFilled(true);
						break;
					case 7: grect.setFillColor(Color.green);
						grect.setFilled(true);
						break;
					case 8: grect.setFillColor(Color.cyan);
						grect.setFilled(true);
						break;
					case 9: grect.setFillColor(Color.cyan);
						grect.setFilled(true);
						break;
					default: grect.setFilled(false); 
					break;
				}
			}	
				nextBrick = BRICK_SEP;
				nextRow += (BRICK_SEP + BRICK_HEIGHT);
				rowCounter++;
		}		
	}
	
	private GRect paddle;
	
	private void setUpPaddle() {
		double x = (getWidth() - PADDLE_WIDTH) / 2;
		double y = getHeight() - (PADDLE_Y_OFFSET + PADDLE_HEIGHT);
		paddle = new GRect(x, y, PADDLE_WIDTH, PADDLE_HEIGHT);
		add(paddle);
		paddle.setFillColor(Color.black);
		paddle.setFilled(true);
		addMouseListeners();
	}
	
	public void mouseMoved(MouseEvent e) {
		/*
		 * Paddle can't exit the screen on the left side or right side
		 * check e.getX() for the location of the paddle
		 * setLocation sets location of center of paddle
		 */
		if ((e.getX() < getWidth() - PADDLE_WIDTH/2) && (e.getX() > PADDLE_WIDTH/2)) {
			paddle.setLocation(e.getX() - PADDLE_WIDTH / 2, getHeight() - PADDLE_Y_OFFSET);
		}
	}
	
	private GOval ball;
	
	private void createBall() {
		double x = (getWidth() - BALL_RADIUS) / 2;
		double y = (getHeight() - BALL_RADIUS) / 2; 
		ball = new GOval(x, y, BALL_RADIUS * 2, BALL_RADIUS * 2);
		add(ball);
		ball.setFillColor(Color.black);
		ball.setFilled(true);
	}
	
	private void playGame() {
		if (TOTAL_BRICKS == 0) {
			ball.setVisible(false);
			GLabel winner = new GLabel("Congrats, you are a winner!");
			add(winner);
		}
		waitForClick();
		getBallVelocity();
		while (true) {
			moveBall();
			if (ball.getY() >= getHeight()) {
				NUM_TURNS_LEFT --;
				break;
			} 
			if (TOTAL_BRICKS == 0) {
				break;
			}
		}		
	}
	
	private void getBallVelocity() {
		vy = 3.0;
		vx = rgen.nextDouble(1.0, 6.0);
		if (rgen.nextBoolean(0.5)) {
			vx = -vx;
		}
	}
	
	private void moveBall() {
		ball.move(vx, vy);
		/** check for ball bouncing off the wall, need to invert the vx */
		if ((ball.getX() <= 0) || (ball.getX() >= (getWidth() - BALL_RADIUS*2))) {
			vx = -vx;
		}
		
		if (ball.getY() <= 0) {
			vy = -vy;
		}
		
		/** check for collider objects */
		GObject collider = getCollidingObject();
		
		if (collider == paddle) {
			vy = -vy;
		} else if (collider != null) {
			remove(collider);
			TOTAL_BRICKS --;
			vy = -vy;
		}
		pause(DELAY);
	}
	
	private GObject getCollidingObject() {
		if ( (getElementAt(ball.getX(), ball.getY()) ) != null) {
			return getElementAt(ball.getX(), ball.getY());
		} else if ( getElementAt((ball.getX() + BALL_RADIUS*2), ball.getY()) != null) {
			return getElementAt((ball.getX() + BALL_RADIUS*2), ball.getY());
		} else if ( getElementAt( ball.getX(), ball.getY() + BALL_RADIUS*2 ) != null) {
			return getElementAt( ball.getX(), ball.getY() + BALL_RADIUS*2 );
		} else if ( getElementAt(ball.getX() + BALL_RADIUS*2, ball.getY() + BALL_RADIUS*2) != null) {
			return getElementAt(ball.getX() + BALL_RADIUS*2, ball.getY() + BALL_RADIUS*2);
		} else {
			return null;
		}
	}
}
