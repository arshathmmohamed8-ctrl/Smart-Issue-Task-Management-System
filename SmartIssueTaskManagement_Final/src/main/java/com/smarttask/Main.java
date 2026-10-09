package com.smarttask;

import com.smarttask.Service.*;
import com.smarttask.ui.TaskMenu;

public class Main {
    public static void main(String[] args) {
        new TaskMenu(new TaskService(), new ProjectService(), new IssueService()).start();
    }
}