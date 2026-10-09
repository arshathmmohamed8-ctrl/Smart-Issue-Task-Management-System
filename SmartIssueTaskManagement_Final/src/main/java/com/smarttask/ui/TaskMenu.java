package com.smarttask.ui;
import com.smarttask.Service.*; import com.smarttask.enums.*; import com.smarttask.exception.TaskValidationException; import com.smarttask.model.*; import java.time.*; import java.time.format.DateTimeParseException; import java.util.*;
public class TaskMenu {
 private final TaskService tasks; private final ProjectService projects; private final IssueService issues; private final Scanner sc=new Scanner(System.in);
 public TaskMenu(TaskService t,ProjectService p,IssueService i){tasks=t;projects=p;issues=i;}
 public void start(){while(true){menu();int c=integer("Enter choice: ");try{switch(c){
 case 1->addTask(); case 2->viewTasks(); case 3->findTask(); case 4->updateTask(); case 5->deleteTask(); case 6->search(); case 7->filterStatus(); case 8->filterPriority(); case 9->sortTasks(); case 10->addProject(); case 11->viewProjects(); case 12->assign(); case 13->updateProject(); case 14->deleteProject(); case 15->addIssue(); case 16->viewIssues(); case 17->updateIssue(); case 18->filterIssues(); case 0->{System.out.println("Goodbye!");return;} default->System.out.println("Choose 0-18.");}
 }catch(TaskValidationException e){System.out.println("Error: "+e.getMessage());}catch(IllegalArgumentException e){System.out.println("Invalid value. Please check your input.");}catch(DateTimeParseException e){System.out.println("Invalid date. Use YYYY-MM-DD.");}System.out.println();}}
 private void menu(){System.out.println("\n===== SMART ISSUE & TASK MANAGEMENT =====");String[] m={"Add Task","View Tasks","Find Task","Update Task","Delete Task","Search Task by Title","Filter Tasks by Status","Filter Tasks by Priority","Sort Tasks by Priority","Create Project","View Projects","Assign Task to Project","Update Project","Delete Project","Create Issue","View Issues","Update Issue Status","Filter Issues by Priority"};for(int i=0;i<m.length;i++)System.out.println((i+1)+". "+m[i]);System.out.println("0. Exit");}
 private String line(String p){System.out.print(p);return sc.nextLine().trim();}
 private int integer(String p){while(true){try{return Integer.parseInt(line(p));}catch(NumberFormatException e){System.out.println("Enter a valid whole number.");}}}
 private <E extends Enum<E>> E en(String p,Class<E> type){while(true){try{return Enum.valueOf(type,line(p).toUpperCase(Locale.ROOT));}catch(IllegalArgumentException e){System.out.println("Allowed: "+Arrays.toString(type.getEnumConstants()));}}}
 private LocalDate date(String p){while(true){try{return LocalDate.parse(line(p));}catch(DateTimeParseException e){System.out.println("Use YYYY-MM-DD.");}}}
 private void addTask(){int id=integer("Task ID: ");String title=line("Title: "),desc=line("Description: ");Priority pr=en("Priority LOW/MEDIUM/HIGH: ",Priority.class);Status st=en("Status PENDING/IN_PROGRESS/COMPLETED: ",Status.class);LocalDate d=date("Deadline YYYY-MM-DD: ");tasks.addTask(new Task(id,title,desc,pr,st,d));}
 private void viewTasks(){List<Task> l=tasks.viewTasks();if(l.isEmpty()){System.out.println("No tasks available.");return;}for(Task t:l)printTask(t);}
 private void printTask(Task t){System.out.println("\nID: "+t.getId()+" | "+t.getTitle()+" | "+t.getPriority()+" | "+(t.isOverdue()?"OVERDUE":t.getStatus())+" | Deadline: "+t.getDeadline()+"\nDescription: "+t.getDescription());}
 private void findTask(){Task t=tasks.findTaskById(integer("Task ID: "));if(t==null)System.out.println("Task not found.");else printTask(t);}
 private void updateTask(){int id=integer("Task ID to update: ");String title=line("New title: "),desc=line("New description: ");Priority p=en("Priority: ",Priority.class);Status s=en("Status: ",Status.class);tasks.updateTask(id,title,desc,p,s);System.out.println("Task updated successfully.");}
 private void deleteTask(){tasks.deleteTask(integer("Task ID to delete: "));System.out.println("Task deleted successfully.");}
 private void search(){printTasks(tasks.searchTaskByTitle(line("Search keyword: ")));}
 private void filterStatus(){Status s=en("Status: ",Status.class);printTasks(tasks.filterTaskByStatus(s));}
 private void filterPriority(){Priority p=en("Priority: ",Priority.class);printTasks(tasks.filterTaskByPriority(p));}
 private void sortTasks(){printTasks(tasks.sortTaskByPriority());}
 private void printTasks(List<Task> l){if(l.isEmpty())System.out.println("No matching tasks.");else for(Task t:l)System.out.println(t.getId()+" - "+t.getTitle()+" - "+t.getPriority()+" - "+t.getStatus());}
 private void addProject(){projects.addProject(new Project(integer("Project ID: "),line("Project name: "),line("Description: ")));}
 private void viewProjects(){List<Project> l=projects.viewProjects();if(l.isEmpty()){System.out.println("No projects.");return;}for(Project p:l){System.out.println("\n"+p.getId()+" - "+p.getName()+" - "+p.getDescription());if(p.getTasks().isEmpty())System.out.println("No tasks assigned.");else for(Task t:p.getTasks())System.out.println("  "+t.getId()+" - "+t.getTitle()+" - "+t.getStatus());}}
 private void assign(){int pid=integer("Project ID: "),tid=integer("Task ID: ");Task t=tasks.findTaskById(tid);if(t==null)throw new TaskValidationException("Task not found.");projects.assignTaskToProject(pid,t);}
 private void updateProject(){projects.updateProject(integer("Project ID: "),line("New name: "),line("New description: "));}
 private void deleteProject(){projects.deleteProject(integer("Project ID: "));}
 private void addIssue(){issues.addIssue(new Issue(integer("Issue ID: "),line("Title: "),line("Description: "),en("Priority: ",Priority.class)));}
 private void viewIssues(){List<Issue> l=issues.viewIssues();if(l.isEmpty()){System.out.println("No issues.");return;}for(Issue i:l)System.out.println(i.getId()+" - "+i.getTitle()+" - "+i.getPriority()+" - "+i.getStatus()+" - Created: "+i.getCreatedDate()+"\n  "+i.getDescription());}
 private void updateIssue(){issues.updateIssueStatus(integer("Issue ID: "),en("Status OPEN/IN_PROGRESS/RESOLVED: ",IssueStatus.class));}
 private void filterIssues(){Priority p=en("Priority: ",Priority.class);List<Issue> l=issues.filterIssuesByPriority(p);if(l.isEmpty())System.out.println("No matching issues.");else for(Issue i:l)System.out.println(i.getId()+" - "+i.getTitle()+" - "+i.getPriority()+" - "+i.getStatus());}
}