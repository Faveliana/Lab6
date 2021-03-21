package bsu.rfe.java.group6.lab6.Seyidova.var7;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Field extends JPanel {

    // Флаг приостановленности движения
    private boolean paused;
    private boolean paused1;
    private boolean resumeLol;
    // Динамический список скачущих мячей
    private ArrayList<BouncingBall> balls = new ArrayList<BouncingBall>(10);

    // Класс таймер отвечает за регулярную генерацию событий ActionEvent
    // При создании его экземпляра используется анонимный класс,
    // реализующий интерфейс ActionListener
    private Timer repaintTimer = new Timer(10, new ActionListener() {
        public void actionPerformed(ActionEvent ev) {
            // Задача обработчика события ActionEvent - перерисовка окна
            repaint();
        }
    });
    public Field() {
        // Установить цвет заднего фона белым
        setBackground(Color.WHITE);
        // Запустить таймер
        repaintTimer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D canvas = (Graphics2D) g;
        for (BouncingBall ball: balls) {
            ball.paint(canvas);
        }
    }

    // Метод добавления нового мяча в список
    public void addBall() {
        balls.add(new BouncingBall(this));
    }
    public  void pause() {
        paused = true;
    }
    public  void pause1() {
        paused1 = true;
        paused = true;
        resumeLol = false;

    }
    public synchronized void resumeLol() {
        paused = false;
        resumeLol = true;
        notifyAll();
    }

    // Метод синхронизированный,только один поток может одновременно быть внутри
    public synchronized void resume() {
        // Выключить режим паузы
        paused = false;
        paused1 = false;
        notifyAll();
    }

    // Синхронизированный метод проверки, может ли мяч двигаться
    public synchronized void canMove(BouncingBall ball) throws
            // (не включен ли режим паузы?)
            InterruptedException {

        if (paused) {
            if (ball.getRadius() < 10)
                wait();
        }
        if(paused1)
            if(resumeLol)
            {
                if (ball.getRadius() > 10)
                    wait();
            }
            else
                wait();

        // Если режим паузы включен, то поток, зашедший
        // внутрь данного метода, засыпает
        //wait();
    }
}
