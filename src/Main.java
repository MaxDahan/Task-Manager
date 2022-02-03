import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class Main extends JFrame implements KeyListener {
	private static final long serialVersionUID = 1L;
	private static final int BSIZE = 10, GWIDTH = 600 - BSIZE * 2, GHEIGHT = 800 - BSIZE * 2;
	private JLayeredPane pane;
	private int mouseLastx, mouseLasty;
	private Color nothingColor = new Color(0, 0, 0, 0);
	private MyButton refresh, endTask;
	private JScrollPane jp;
	private ArrayList<MyButton> tasks = new ArrayList<MyButton>();
	private ArrayList<String> CPU = new ArrayList<String>(), taskName = new ArrayList<String>(), IDs = new ArrayList<String>();
	private int selectedButton;
	private JPanel readArea;

	public Main() {
		requestFocus();
		setAlwaysOnTop(true);
		setUndecorated(true);
		setLocation(1920/2 - GWIDTH/2, 1080/2 - GHEIGHT/2);
		getContentPane().setBackground(Color.WHITE);
		getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK, BSIZE));
		setSize(GWIDTH, GHEIGHT);
		this.addMouseListener(new MouseInput(this));
		this.addMouseListener(new MouseReFocus(this));
		this.addKeyListener(this);
		
		// first title screen jpanel
		pane = new JLayeredPane();
		pane.setLayout(null);
		add(pane);
		
		setUpTitlePane();
		
		setVisible(true);
	}
	public void setUpTitlePane() {
		JLabel title = new JLabel("TaskManager");
		title.setFont(new Font("Arial", Font.PLAIN, 50));
		title.setForeground(Color.BLACK);
		int width = title.getFontMetrics(title.getFont()).stringWidth("TaskManager");
		title.setBounds(GWIDTH/2 - width/2, 30, width, title.getFontMetrics(title.getFont()).getHeight());
		pane.setLayer(title, 2);
		pane.add(title);
		
		setUpScrollingArea();
		
		refresh = new MyButton("refresh");
		refresh.setFont(new Font("Arial", Font.PLAIN, 40));
		refresh.setForeground(Color.BLACK);
		refresh.setBackground(nothingColor);
		refresh.setHoverBackgroundColor(nothingColor);
		refresh.setPressedBackgroundColor(nothingColor);
		refresh.setHoverTextColor(Color.BLUE);
		refresh.setPressedTextColor(new Color(0, 0, 150));
		refresh.setBorder(null);
		refresh.setFocusable(false);
		width = refresh.getFontMetrics(refresh.getFont()).stringWidth("refresh");
		refresh.setBounds(readArea.getX() + 20, readArea.getY() + 20 + readArea.getHeight(), width, refresh.getFontMetrics(refresh.getFont()).getHeight());
		refresh.addActionListener(new RefreshEvent());
		pane.setLayer(refresh, 2);
		pane.add(refresh);
		
		endTask = new MyButton("endTask");
		endTask.setFont(new Font("Arial", Font.PLAIN, 40));
		endTask.setForeground(Color.BLACK);
		endTask.setBackground(nothingColor);
		endTask.setHoverBackgroundColor(nothingColor);
		endTask.setPressedBackgroundColor(nothingColor);
		endTask.setHoverTextColor(Color.GREEN);
		endTask.setPressedTextColor(new Color(0, 150, 0));
		endTask.setBorder(null);
		endTask.setFocusable(false);
		width = endTask.getFontMetrics(refresh.getFont()).stringWidth("endTask");
		endTask.setBounds(readArea.getX() + readArea.getWidth() - endTask.getFontMetrics(endTask.getFont()).stringWidth("endTask") - 20,
						readArea.getY() + 20 + readArea.getHeight(), width, refresh.getFontMetrics(refresh.getFont()).getHeight());
		endTask.addActionListener(new EndTaskEvent());
		pane.setLayer(endTask, 2);
		pane.add(endTask);
	}
	public void setUpScrollingArea() {
		readArea = new JPanel();
		readArea.setBounds((GWIDTH - 550 - BSIZE * 2)/2, 100, 550, 580);
		readArea.setBackground(Color.WHITE);
		readArea.addMouseListener(new MouseInput(this));
		readArea.addMouseListener(new MouseReFocus(this));
		readArea.setLayout(null);

		printCurrentTasks();
		
		jp = new JScrollPane(readArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jp.setBounds(readArea.getX(), readArea.getY(), readArea.getWidth(), readArea.getHeight());
		jp.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
		jp.setBackground(new Color(226, 226, 226));
		jp.getHorizontalScrollBar().setUI(new HorScrollBarUI());
		jp.getVerticalScrollBar().setUI(new VertScrollBarUI());
		pane.setLayer(jp, 1);
		pane.add(jp);

		readArea.setPreferredSize(new Dimension(tasks.get(tasks.size() - 1).getX() + tasks.get(tasks.size() - 1).getWidth(),
												tasks.get(tasks.size() - 1).getY() + tasks.get(tasks.size() - 1).getHeight()));
	}
	public void printCurrentTasks() {
		tasks.clear();
		try {
			// the command itself comes after powershell.exe
			String command = "powershell.exe ps";
			Process powerShellProcess = Runtime.getRuntime().exec(command);
			powerShellProcess.getOutputStream().close();
			String line;
			// prints output of command
			BufferedReader stdout = new BufferedReader(new InputStreamReader(
			powerShellProcess.getInputStream()));
			int position = 0;
			while ((line = stdout.readLine()) != null) {
				if(position >= 3) {
					MyButton taskButton = new MyButton(line);
					taskButton.setFocusable(false);
					taskButton.setFont(new Font("Arial", Font.BOLD, 20));
					taskButton.setHoverBackgroundColor(new Color(0, 255, 255, 50));
					taskButton.setPressedBackgroundColor(new Color(0, 50, 255, 50));
					taskButton.setHoverTextColor(Color.BLACK);
					taskButton.setPressedTextColor(Color.BLACK);
					taskButton.setForeground(Color.BLACK);
					taskButton.setBounds(2, (position - 3) * 40, taskButton.getFontMetrics(taskButton.getFont()).stringWidth(line), 
							taskButton.getFontMetrics(taskButton.getFont()).getHeight() + 17);
					taskButton.addActionListener(new TaskEvent(taskButton, position - 3));
					String taskText = taskButton.getText();
					Scanner scan = new Scanner(taskText);
					// gets to the CPU area
					try {
						scan.next(); scan.next(); scan.next(); scan.next();
						String maybeCPU = scan.next();
						scan.next(); scan.next();
						if(!scan.hasNext())
							CPU.add("0.0");
						else
							CPU.add(maybeCPU);
					} catch(Exception e) {}
					
					scan = new Scanner(taskText);
					// gets to the Name area
					String maybeName = "";
					try {
						scan.next(); scan.next(); scan.next(); scan.next(); scan.next(); scan.next();
						maybeName = scan.next();
						if(scan.hasNext())
							taskName.add(scan.next());
					} catch(Exception e) {
						taskName.add(maybeName);
					}
					
					scan = new Scanner(taskText);
					// gets the ID 
					String maybeID = "";
					String maybeID2 = "";
					try {
						scan.next(); scan.next(); scan.next(); scan.next();
						maybeID = scan.next();
						maybeID2 = scan.next();
						scan.next(); scan.next();
						if(scan.hasNext())
							IDs.add(maybeID2);
					} catch(Exception e) {
						IDs.add(maybeID);
					}
					
					System.out.println(line);
					
					tasks.add(taskButton);
					
				}
				position++;
			}

			// removes spaces at the end
			tasks.remove(tasks.size() - 1);
			tasks.remove(tasks.size() - 1);
			
			//CPUSort();
			
			for(int i = 0; i < tasks.size(); i++) {
				tasks.get(i).setText(taskName.get(i) + " : " + IDs.get(i) + " : " + CPU.get(i));
				readArea.add(tasks.get(i));
			}
			stdout.close();
			// prints error
			BufferedReader stderr = new BufferedReader(new InputStreamReader(
			powerShellProcess.getErrorStream()));
			while ((line = stderr.readLine()) != null) {
				MyButton errorButton = new MyButton("ERROR");
				errorButton.setFont(new Font("Arial", Font.BOLD, 50));
				errorButton.setHoverBackgroundColor(new Color(0, 255, 255, 50));
				errorButton.setPressedBackgroundColor(new Color(0, 200, 255, 50));
				errorButton.setForeground(Color.BLACK);
				errorButton.setBounds(10, 10, errorButton.getFontMetrics(errorButton.getFont()).stringWidth("ERROR"), 
						errorButton.getFontMetrics(errorButton.getFont()).getHeight());
				tasks.add(errorButton);
			}
			stderr.close();
		} catch(Exception e) {e.printStackTrace();}
	}

	
	// not working, reading file works but sort ruins everything
	public void CPUSort() {
		for(int i = 0; i < CPU.size(); i++) {
			if(CPU.size() > 0 && i + 1 <= tasks.size()) {
				if(!CPU.get(i).contains("CPU(s)") && !CPU.get(i).contains("------")) {
					cleanNumber(i);
					
					Double cp1 = Double.parseDouble(CPU.get(i));
					Double cp2 = Double.parseDouble(CPU.get(i + 1));
					if(cp1 < cp2) {
						String cpu1 = CPU.get(i);
						String cpu2 = CPU.get(i + 1);
						CPU.set(i, cpu2);
						CPU.set(i + 1, cpu1);

						String tn1 = taskName.get(i);
						String tn2 = taskName.get(i + 1);
						taskName.set(i, tn2);
						taskName.set(i, tn1);
						
						tasks.get(i).setText(taskName.get(i) + "\t " + CPU.get(i));
						tasks.get(i + 1).setText(taskName.get(i + 1) + "\t " + CPU.get(i + 1));
					}
				}
			}
		}
	}
	public void cleanNumber(int i) {
		if(!CPU.get(i).contains(".")) {
			CPU.set(i, CPU.get(i) + ".0");
		} 
		if(!CPU.get(i + 1).contains(".")) {
			CPU.set(i + 1, CPU.get(i + 1) + ".0");
		} 
		if(CPU.get(i).contains(",")) {
			CPU.set(i, (CPU.get(i).substring(0, CPU.get(i).indexOf(",") - 1)) + "" + 
					(CPU.get(i).substring(CPU.get(i).indexOf(",") + 1, CPU.get(i).length() - 1)));
		}
		if(CPU.get(i + 1).contains(",")) {
			CPU.set(i + 1, (CPU.get(i + 1).substring(0, CPU.get(i + 1).indexOf(",") - 1)) + "" + 
					(CPU.get(i + 1).substring(CPU.get(i + 1).indexOf(",") + 1, CPU.get(i + 1).length() - 1)));
		}
	}
	public double compare(String o1, String o2) {
	    return Double.parseDouble(o2) - Double.parseDouble(o1);
	}
	
	// BUTTONS
	private class TaskEvent implements ActionListener {
		private MyButton button;
		private int thisButton;
		public TaskEvent(MyButton button, int thisButton) {
			this.button = button;
			this.thisButton = thisButton;
		}
		public void actionPerformed(ActionEvent e) {
			selectedButton = thisButton;
			for(int i = 0; i < tasks.size(); i++) {
				if(thisButton == i) {
					tasks.get(i).setBackground(new Color(0, 50, 255, 50));
				} else {
					tasks.get(i).setBackground(Color.WHITE);
				}
			}
		}
	}
	private class EndTaskEvent implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
		}
	}
	// result of pressing refresh
	private class RefreshEvent implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			tasks.clear();
			CPU.clear();
			taskName.clear();
			printCurrentTasks();
		}
	}
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		if(key == KeyEvent.VK_ESCAPE) System.exit(0);
	}
	
	// U S E L E S S

	// MOUSE LISTENERS AND FOLLOWERS
	private class MouseReFocus extends MouseAdapter {
		private JFrame frame;
		public MouseReFocus (JFrame frame) {
			this.frame = frame;
		}
		public void mousePressed() {
			frame.requestFocus();
		}
	}
	private class MouseInput extends MouseAdapter {
		private int lastX = 0; private int lastY = 0;
    	private Thread plus_follow = new Thread();
    	private Main frame;
    	public MouseInput(Main frame) {
    		this.frame = frame;
    	}
        public void mousePressed(MouseEvent e) {
        	lastX = getX(); lastY = getY();
        	plus_follow = new MouseFollow();
        	plus_follow.start();
        	frame.requestFocus();
        }
        public void mouseReleased(MouseEvent e) {
        	plus_follow.stop();
        	lastX = Math.abs(lastX - getX());
        	lastY = Math.abs(lastY - getY());
        }
	}
	// makes the frame follow the mouse
	private class MouseFollow extends Thread {
		private boolean first = true;
		public void run() {
			while(true) {
				try {Thread.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
				Point mousePos = MouseInfo.getPointerInfo().getLocation();
				if(!first) {
					mouseLastx = (int)mousePos.getX() - mouseLastx;
					mouseLasty = (int)mousePos.getY() - mouseLasty;
					setLocation(getX() + mouseLastx, getY() + mouseLasty);
				}
				first = false;
				mouseLastx = (int)mousePos.getX();
				mouseLasty = (int)mousePos.getY();
			}
		}
	}

	public void keyTyped(KeyEvent e) {}
	public void keyReleased(KeyEvent arg0) {}
	public static void main(String[] args) {
		new Main();
	}
}
