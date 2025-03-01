package bsu.rfe.java.group6.lab6.matyrkin.var7B;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.Timer;
@SuppressWarnings("serial")
public class Field extends JPanel {

    private boolean paused;// Флаг, который указывает, приостановлено ли движение
    private boolean charismaEnabled = false; // Флаг для режима "харизма"
    private Point charismaPoint = new Point();
    // Динамический список скачущих мячей
    private ArrayList<BouncingBall> balls = new ArrayList<>(10);
    // Класс таймер отвечает за регулярную генерацию событий ActionEvent
// При создании его экземпляра используется анонимный класс,
// реализующий интерфейс ActionListener
    private Timer repaintTimer = new Timer(10, ev -> repaint());
    // Конструктор класса BouncingBall
    public Field() {
        setBackground(Color.WHITE);
// Запускаем таймер для обновления поля
        repaintTimer.start();
        // Добавляем обработчик движения мыши:
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                charismaPoint = e.getPoint(); // Обновляем координаты курсора, когда он перемещается.
            }
        });
    }

    // Унаследованный от JPanel метод перерисовки компонента
    public void paintComponent(Graphics g) {
// Вызвать версию метода, унаследованную от предка
        super.paintComponent(g);
        Graphics2D canvas = (Graphics2D) g;
// Последовательно запросить прорисовку от всех мячей из списка
        for (BouncingBall ball: balls) {
            ball.paint(canvas);
        }
    }
    // Метод добавления нового мяча в список
    public void addBall() {
//Заключается в добавлении в список нового экземпляра BouncingBall
// Всю инициализацию положения, скорости, размера, цвета
// BouncingBall выполняет сам в конструкторе
        balls.add(new BouncingBall(this));
    }
    // Метод синхронизированный, т.е. только один поток может
// одновременно быть внутри
    public synchronized void pause() {
// Включить режим паузы
        paused = true;
    }
    // Метод синхронизированный, т.е. только один поток может
// одновременно быть внутри
    public synchronized void resume() {
// Выключить режим паузы
        paused = false;
// Будим все ожидающие продолжения потоки
        notifyAll();
    }
    // Синхронизированный метод проверки, может ли мяч двигаться
// (не включен ли режим паузы?)
    public synchronized void setCharismaEnabled(boolean enabled) {
        charismaEnabled = enabled;
    }
    // Проверка, включен ли режим "харизма"
    public synchronized boolean isCharismaEnabled() {
        return charismaEnabled;
    }
    // Получение текущей позиции курсора
    public synchronized Point getCharismaPoint() {
        return charismaPoint;
    }
    // Проверка, можно ли двигаться мячу (если пауза, то поток мяча засыпает)
    public synchronized void canMove(BouncingBall ball) throws
            InterruptedException {
        if (paused) {
// Если режим паузы включен, то поток, зашедший
// внутрь данного метода, засыпает
            wait();
        }
    }
}

