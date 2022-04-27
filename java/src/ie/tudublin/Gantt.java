package ie.tudublin;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.data.*;

public class Gantt extends PApplet
{	
	ArrayList<Task> tasks = new ArrayList<Task>();
	
	public void settings()
	{
		size(800, 600);
	}

	public void loadTasks()
	{
		Table table = loadTable("tasks.csv", "header");
		for(TableRow row:table.rows()){
			Task task = new Task(row);
			tasks.add(task);
		}

	}

	public void printTasks()
	{
		for(Task t:tasks){
			System.out.println(t);
		}
	}
	
	private int days = 30;
	private float TaskPart = 160;
	private float border = width * 0.5f;
	public void displayTasks(){
		textSize(14);
		textAlign(CENTER, CENTER);
		stroke(128);

		for(int i = 1; i <= days; i++){
			float x = map(i, 1, days, TaskPart, width - border);
			line(x, border, x, height - border);
			text(i, x, border/2);
		}
		for(int i=0; i < tasks.size(); i++){
			fill(map(i, 0, tasks.size(), 0, 255), 255,255);
			float y = border*2 + border * i;
			text(tasks.get(i).getName(), border, y);

			float x1 = map(tasks.get(i).getStart(), 1, days, TaskPart, width-border);
			float x2 = map(tasks.get(i).getEnd(), 1, days, TaskPart, width-border);
			rect(x1, y-(border/2), x2-x1, border-10, 10);
		}
	}

	private int selected = -1;
	private boolean isEnd = false;
	public void mousePressed()
	{
		for(int i = 0; i < tasks.size(); i++){
			float y1 = (border*2 + border * i) - border/2;
			float y2 = (border*2 + border * i) + border/2 + 10;
			
			float x1 = map(tasks.get(i).getStart(), 1, days, TaskPart, width-border);
			float x2 = map(tasks.get(i).getEnd(), 1, days, TaskPart, width-border);
		
			if (mouseX >= x1 && mouseX <= x1 + border/2 + 10 && mouseY >= y1 && mouseY <= y2){
				selected = i;
				isEnd = false;
				return;
			}
			if (mouseX <= x2 && mouseX >= x2 - border/2 + 10 && mouseY >= y1 && mouseY <= y2)
			{
				selected = i;
				isEnd = true;
				return;
			}
			
		}
		selected = -1;
		println("Mouse pressed");	
	}

	public void mouseDragged()
	{
		if (selected != -1)
		{
			int month = (int)map(mouseX, TaskPart, width - border, 1, days);
			
			if (month >= 1 && month <= days)
			{
				Task task = tasks.get(selected); 
				if (isEnd)
				{
					if (month - task.getStart() > 0)
					{
						task.setEnd(month);
					}
				}
				else
				{
					if (task.getEnd() - month > 0 )
					{
						task.setStart(month);
					}
				}
			}
		}
		println("Mouse dragged");
	}

	
	
	public void setup() 
	{
		colorMode(HSB);
		loadTasks();
		printTasks();
	}
	
	
	
	public void draw()
	{	

		background(0);
		displayTasks();
	}
}
