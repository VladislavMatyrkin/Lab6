package bsu.rfe.java.group6.lab6.matyrkin.var7B;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.Point;
public class BouncingBall implements Runnable {
    // Максимальный радиус, который может иметь мяч
    private static final int MAX_RADIUS = 40;
    // Минимальный радиус, который может иметь мяч
    private static final int MIN_RADIUS = 3;
    // Максимальная скорость, с которой может летать мяч
    private static final int MAX_SPEED = 15;
    private Field field;
    private int radius;
    private Color color;
    // Текущие координаты мяча
    private double x;
    private double y;
    // Вертикальная и горизонтальная компонента скорости
    private int speed;
    private double speedX;
    private double speedY;
    // Конструктор класса BouncingBall
    public BouncingBall(Field field) {
        this.field = field;
        radius = new Double(Math.random() * (MAX_RADIUS - MIN_RADIUS)).intValue() + MIN_RADIUS;
        speed = new Double(Math.round(5 * MAX_SPEED / radius)).intValue();
        if (speed > MAX_SPEED) {
            speed = MAX_SPEED;
        }
        // Генерация случайного направления движения
        double angle = Math.random() * 2 * Math.PI;
        speedX = 3 * Math.cos(angle);
        speedY = 3 * Math.sin(angle);

        color = new Color((float) Math.random(), (float) Math.random(), (float) Math.random());
        // Генерация случайного положения
        x = Math.random() * (field.getSize().getWidth() - 2 * radius) + radius;
        y = Math.random() * (field.getSize().getHeight() - 2 * radius) + radius;
        // Запуск нового потока для мяча
        Thread thisThread = new Thread(this);
        thisThread.start();
    }

    public void run() {
        try {
            while (true) {
                // Проверка, можно ли двигаться (пауза)
                field.canMove(this);
                if (field.isCharismaEnabled()) {
                    // Включен режим "харизма" - движемся к точке курсора
                    Point target = field.getCharismaPoint();
                    double deltaX = target.x - x;
                    double deltaY = target.y - y;
                    double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

                    // Если точка далеко, изменяем скорость
                    if (distance > 1) {
                        speedX = deltaX / distance * speed;
                        speedY = deltaY / distance * speed;
                    }
                } else {
                    // Обычное движение и отражение от стен
                    if (x + speedX <= radius) {
                        speedX = -speedX;// Отражение от левой стены
                        x = radius;
                    } else if (x + speedX >= field.getWidth() - radius) {
                        speedX = -speedX;// Отражение от правой стены
                        x = field.getWidth() - radius;
                    }
                    if (y + speedY <= radius) {
                        speedY = -speedY;
                        y = radius;
                    } else if (y + speedY >= field.getHeight() - radius) {
                        speedY = -speedY;
                        y = field.getHeight() - radius;
                    }
                }
                // Смещение мяча
                x += speedX;
                y += speedY;
                // Задержка в зависимости от скорости
                Thread.sleep(16 - speed);
            }
        } catch (InterruptedException ex) {
            // Игнорируем
        }
    }
    // Прорисовка мяча
    public void paint(Graphics2D canvas) {
        canvas.setColor(color);// Устанавливаем цвет мяча.
        canvas.setPaint(color);
        Ellipse2D.Double ball = new Ellipse2D.Double(x - radius, y - radius, 2 * radius, 2 * radius);
        canvas.draw(ball);
        canvas.fill(ball);
    }
}
