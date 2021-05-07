package ui;

import gamelogic.Camera;
import gamelogic.Controls;
import gamelogic.LogicController;
import objects.MyObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Arrays;

public class Display extends Canvas implements Runnable{
    public JFrame frame;

    private Thread thread;
    private static String title = "Straßensimulation Kreis Höxter";
    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;
    private boolean running = false;
    public static Display display;


    public Display(){
       this.frame = new JFrame();
       this.addKeyListener(Controls.key);
       this.addMouseListener(Controls.mouse);
       this.addMouseWheelListener(Controls.wheel);
       this.frame.setFocusable(true);
       this.frame.requestFocus();
       Display dis = this;
       this.addFocusListener(new FocusListener() {
           @Override
           public void focusGained(FocusEvent e) {

           }

           @Override
           public void focusLost(FocusEvent e) {
                dis.requestFocus();
           }
       });

       Dimension size = new Dimension(WIDTH,HEIGHT);
       this.setPreferredSize(size);
    }

    public static void create(){


        display = new Display();
        display.frame.setTitle(title);
        display.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        display.frame.setLayout(new BorderLayout());
        display.frame.add(display,BorderLayout.CENTER);
        display.frame.pack();
        display.frame.setLocationRelativeTo(null);
        display.frame.setResizable(true);
        display.frame.setVisible(true);
        display.frame.setIconImage(new ImageIcon("logo.png").getImage());
        display.start();
    }

    public synchronized void start(){
        this.running = true;
        this.thread = new Thread(this,"Display");
        this.thread.start();

    }
    public synchronized void stop(){
        try {
            this.thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        final double ns = 1000000000.0 / 60;
        double delta = 0;
        int frames = 0;


        while(running){
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            while(delta >= 1){


                delta--;
                render();

                frames++;
            }


            if(System.currentTimeMillis() - timer > 1000){
                timer += 1000;
                this.frame.setTitle(title + " | " + frames + " fps");
                frames = 0;
            }
        }
        stop();
    }
    private void render(){
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null){
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        g.setColor(new Color(219, 219, 219));
        g.fillRect(0,0,frame.getWidth(),frame.getHeight());
        g.setColor(Color.BLACK);
        for(ArrayList<MyObject> visible : LogicController.visible) {
            for (MyObject object : (ArrayList<MyObject>) visible.clone()) {
                g.setColor(object.color);
                for (Polygon poly : object.polys) {

                    int[] xs = Arrays.copyOf(poly.xpoints, poly.xpoints.length);
                    for (int i = 0; i < xs.length; i++) {
                        xs[i] -= Camera.posx;
                        xs[i] *= Camera.zoom;
                        xs[i] += frame.getWidth() / 2;


                    }
                    int[] ys = Arrays.copyOf(poly.ypoints, poly.ypoints.length);
                    for (int i = 0; i < ys.length; i++) {
                        ys[i] -= Camera.posy;
                        ys[i] *= Camera.zoom;
                        ys[i] = frame.getHeight() / 2 - ys[i];

                    }


                    g.fillPolygon(xs, ys, xs.length);


                }
            }
        }

        g.dispose();
        bs.show();
    }

}
