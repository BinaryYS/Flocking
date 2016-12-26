package com.Flock;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JComboBox;
import tfidf.CosineSimilarAlgorithm;
import tfidf.ReadFiles;

public class Simulator extends java.applet.Applet implements Runnable {

	Flock flock;
	Thread thread;
	SimulatorCanvas canvas;
	Panel parameter;
	Panel act;
	FileDialog openDia;
	ArrayList<String> fileList;
	int DEFAULT_NUMBER_GREEN = 15;
	int DEFAULT_NUMBER_BLUE = 15;
	int DEFAULT_NUMBER_YELLOW = 15;
	int DEFAULT_GREEN_THETA = 15;
	int DEFAULT_BLUE_THETA = 15;
	int DEFAULT_YELLOW_THETA = 15;
	int DEFAULT_SPEED = 5;
	int DEFAULT_SEPARATE = 30;
	int DEFAULT_DETECT = 60;

	int numberOfGreenBirds;
	int numberOfBlueBirds;
	int numberOfYellowBirds;
	int speed;
	int count;
	int startCount;

	
	int separateDistance;
	double reultArr[][];
	int detectDistance;
	String control = "";
	boolean suspend = false;

	Button addDocuments = new Button("����ĵ�");
	Button beginning = new Button("��ʼ����");
	Button startBtn = new Button("��ʼ");
	Button suspendBtn = new Button("��ͣ");
	Button stopBtn = new Button("ֹͣ");
	Button exitBtn = new Button("�˳�");

	TextField birdSpeedText = new TextField();
	TextField separateDistText = new TextField();
	TextField detectDistText = new TextField();
	TextField timeText = new TextField();
	TextField addNumText = new TextField();

	JComboBox selectBird = new JComboBox();

	Button setBtn = new Button("����");
	Label flockingSpeedLabel = new Label("�����ٶȣ�");
	Label separateDistanceLabel = new Label("�ָ��뾶��");
	Label detectDistanceLabel = new Label("���뾶��");
	Label addNum = new Label("���������");
	private boolean isPause = false;

	public void init() {
		resize(700, 600);
		fileList = new ArrayList<String>();
		reultArr = new double[10][10];
		canvas = new SimulatorCanvas();
		canvas.simulator = this;
		act = new Panel();
		act.setLayout(new GridLayout(6, 1, 0, 0));

		parameter = new Panel();
		parameter.setLayout(new FlowLayout());
		parameter.setBackground(new Color(245, 245, 245));

		// act.add(addBirds);
		openDia = new FileDialog(openDia, "��Ӿ����ļ�", FileDialog.LOAD);
		AddDocumentsListener addDocumentsListener = new AddDocumentsListener();
		addDocuments.addActionListener(addDocumentsListener);
		act.add(addDocuments);

		BeginListener beginListener = new BeginListener();
		beginning.addActionListener(beginListener);
		act.add(beginning);

		StartListener startListener = new StartListener();
		startBtn.addActionListener(startListener);
		act.add(startBtn);

		SuspendListener suspendListener = new SuspendListener();
		suspendBtn.addActionListener(suspendListener);
		act.add(suspendBtn);

		StopListener stopListener = new StopListener();
		stopBtn.addActionListener(stopListener);
		act.add(stopBtn);

		ExitListener exitListener = new ExitListener();
		exitBtn.addActionListener(exitListener);
		act.add(exitBtn);

		parameter.add(flockingSpeedLabel);
		parameter.add(birdSpeedText);
		parameter.add(separateDistanceLabel);
		parameter.add(separateDistText);
		parameter.add(detectDistanceLabel);
		parameter.add(detectDistText);
		SetListener setListener = new SetListener();
		setBtn.addActionListener(setListener);
		parameter.add(setBtn);
		parameter.add(addNum);
		parameter.add(addNumText);
		selectBird.addItem("��ɫ");
		selectBird.addItem("��ɫ");
		selectBird.addItem("��ɫ");
		ItemListen itemListener = new ItemListen();
		selectBird.addItemListener(itemListener);
		parameter.add(selectBird);
		// parameter.add(addBirds);
		// ��ʼ����������
		numberOfGreenBirds = DEFAULT_NUMBER_GREEN;
		numberOfBlueBirds = DEFAULT_NUMBER_BLUE;
		numberOfYellowBirds = DEFAULT_NUMBER_YELLOW;
		
		speed = DEFAULT_SPEED;
		separateDistance = DEFAULT_SEPARATE;
		detectDistance = DEFAULT_DETECT;

		Bird.separateDistance = separateDistance;
		Bird.detectDistance = detectDistance;

		flock = new Flock();

		// ��ʾ
		setLayout(new BorderLayout());
		add("Center", canvas);
		add("East", act);
		add("North", parameter);
		Bird.setMapSize(canvas.getSize());
		Flock.setMapSize(canvas.getSize());
	}

	public void begin() {
		startCount+=1;
		if(startCount>1){
			return;
		}
		else{
			for (int i = 0; i < numberOfGreenBirds; i++) {
				Bird bird = new Bird(Color.green);
				bird.setSpeed(speed);
				flock.addBird(bird);
			}
			for (int i = 0; i < numberOfBlueBirds; i++) {
				Bird bird = new Bird(Color.blue);
				bird.setSpeed(speed);
				flock.addBird(bird);
			}
			for (int i = 0; i < numberOfYellowBirds; i++) {
				Bird bird = new Bird(Color.yellow);
				bird.setSpeed(speed);
				flock.addBird(bird);
			}
			if (thread == null) {
				thread = new Thread(this);
				thread.start();
			}

		}
		
	}

	public void stop() {
		if (thread != null) {
			thread.stop();
			thread = null;
		}
	}

	public void setSuspend(boolean suspend) { // ������ͣ
		if (!suspend) {
			synchronized (control) {
				control.notifyAll();
			}
		}
		this.suspend = suspend;
	}

	public boolean isSuspend() {
		return this.suspend;
	}
   public void clearCanvas(){
	   for ( int i = 0; i < numberOfBlueBirds; i++ ) {
           flock.removeBird(Color.blue);
       }
		for ( int i = 0; i < numberOfGreenBirds; i++ ) {
           flock.removeBird(Color.green);
       }
		for ( int i = 0; i < numberOfYellowBirds; i++ ) {
           flock.removeBird(Color.yellow);
       }
		for (String file : fileList) {
			System.out.println(file);
		}
   }
   
   
	public void run() {
		while (true) {
			synchronized (control) {
				if (suspend) {
					try {
						control.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			Bird.setMapSize(canvas.getSize());
			Flock.setMapSize(canvas.getSize());
			flock.move();
			canvas.repaint(canvas.getLocation().x, canvas.getLocation().y, canvas.getSize().width,
					canvas.getSize().height);
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
			}
		}
	}

	class SimulatorCanvas extends Canvas {

		Image canvasImage;
		Graphics canvasGraphics;
		Simulator simulator;

		public synchronized void update(Graphics g) {
			paint(g);
		}

		public synchronized void paint(Graphics g) {

			canvasImage = createImage(canvas.getWidth(), canvas.getHeight());
			canvasGraphics = canvasImage.getGraphics();

			canvasGraphics.setColor(Color.white);
			canvasGraphics.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
			simulator.flock.draw(canvasGraphics);
			canvas.getGraphics().drawImage(canvasImage, 0, 0, this);
		}
	}

	class AddDocumentsListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			openDia.setVisible(true);
			String dirPath = openDia.getDirectory();// ��ȡ�ļ�·��
			String fileName = openDia.getFile();// ��ȡ�ļ�����
			dirPath = dirPath.replaceAll("\\\\", "/");
			System.out.println(dirPath + fileName);
			fileList.add(dirPath + fileName);
			/*
			 * for(String file:fileList){ System.out.println(file); }
			 */
		}
	}

	class BeginListener implements ActionListener {// ��ʼ���ఴť����
		public void actionPerformed(ActionEvent e) {
            
			if (fileList.size() == 0) {// ���û���ĵ�����ʼģ�����
				Flock.separateDistance = separateDistance;
				Flock.detectDistance = detectDistance;

			} else if (fileList.size() > 0) {// ������ĵ�����ʼ�ĵ�����
				Flock.separateDistance = separateDistance;
				Flock.detectDistance = detectDistance;
				clearCanvas();
				for (int i = 0; i < fileList.size(); i++) {
					for (int j = i + 1; j < fileList.size(); j++) {
						 
						try {
							double result = CosineSimilarAlgorithm.getSimilarity(ReadFiles.readFiles(fileList.get(i)),
									ReadFiles.readFiles(fileList.get(j)));
							System.out.println(i + "��" + j + "�����ƶ�Ϊ"+result);
							if (result >= 0.4) {
								reultArr[i][j] = 1;
							} else {
								reultArr[i][j] = 0;
							}

						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
				for (int i = 0; i < fileList.size(); i++) {
					for (int j = i + 1; j < fileList.size(); j++) {
						System.out.println(reultArr[i][j]);
					}
				}
				fileList.removeAll(fileList);
			}
		}
	}

	class SuspendListener implements ActionListener {// ��ͣ��ť����
		public void actionPerformed(ActionEvent e) {
			count += 1;
			// System.out.println(count);
			if (count % 2 == 1) {
				setSuspend(true);
			} else {
				setSuspend(false);
			}
		}
	}

	class ExitListener implements ActionListener {// �˳���ť����
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}

	class SetListener implements ActionListener {// ���ð�ť����
		public void actionPerformed(ActionEvent e) {
			if (!"".equals(birdSpeedText.getText())) {// �ٶ�
				speed = Integer.parseInt(birdSpeedText.getText());
				flock.setBirdParameters(Color.green, speed);
				flock.setBirdParameters(Color.blue, speed);
				flock.setBirdParameters(Color.yellow, speed);
			}
			if (!"".equals(separateDistText.getText())) {// �ָ�����
				separateDistance = Integer.parseInt(separateDistText.getText());
				if (detectDistance < separateDistance) {
					detectDistance = separateDistance;
				}
			}
			if (!"".equals(detectDistText.getText())) {// ������
				detectDistance = Integer.parseInt(detectDistText.getText());
				if (detectDistance < separateDistance) {
					separateDistance = detectDistance;
				}
			}

		}
	}

	class StartListener implements ActionListener {// ��ʼģ�����
		public void actionPerformed(ActionEvent e) {
			begin();
		}
	}

	class StopListener implements ActionListener {// ֹͣ��ť����
		public void actionPerformed(ActionEvent e) {
			stop();
		}
	}

	class ItemListen implements ItemListener {// ���������

		public void itemStateChanged(ItemEvent e) {

			if (e.getStateChange() == ItemEvent.SELECTED) {
				JComboBox jcb = (JComboBox) e.getSource();
				// System.out.println((String) (jcb.getSelectedItem()));
				int index = jcb.getSelectedIndex();
				int addNum = Integer.parseInt(addNumText.getText());
				if (index == 0) {
					for (int i = 0; i < addNum; i++) {
						Bird b = new Bird(Color.green);
						b.setSpeed(speed);
						flock.addBird(b);
					}
				}
				if (index == 1) {
					for (int i = 0; i < addNum; i++) {
						Bird b = new Bird(Color.yellow);
						b.setSpeed(speed);
						flock.addBird(b);
					}
				}
				if (index == 2) {
					for (int i = 0; i < addNum; i++) {
						Bird b = new Bird(Color.blue);
						b.setSpeed(speed);
						flock.addBird(b);
					}
				}

			}

		}
	}

}
