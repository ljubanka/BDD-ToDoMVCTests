@clean
Feature: Todos Operations

Background:
    Given open TodoMVC page at All filter

Scenario: add tasks
    When add tasks: a, b, c
    Then tasks are: a, b, c

Scenario: delete task
  Given added ACTIVE tasks: a, b
    When delete task 'a'
    Then tasks are: b

Scenario: complete task
  Given added COMPLETED tasks: a, b
  When toggle task 'a'
  Then tasks are: a, b
  And 1 item(s) left

Scenario: reopen task
  Given added COMPLETED tasks: a, b
  When toggle task 'a'
  Then tasks are: a, b
  And 1 item(s) left

Scenario: complete all tasks
  #Given added ACTIVE tasks: a, b, c
  #And toggle task 'c'
  Given tasks:
  |a|ACTIVE|
  |b|ACTIVE|
  |c|COMPLETED|
  When toggle all tasks
  Then tasks are: a, b, c
  And 0 item(s) left

Scenario: clear completed tasks
  Given added COMPLETED tasks: a, b
  When clear completed tasks
  Then no tasks left
  And clear completed button is not shown

Scenario: cancel edit task
  Given added ACTIVE tasks: a
  When edit task 'a' to have text 'a cancel edit' and press Escape
  Then tasks are: a

Scenario: delete task by emptying its text
  Given added ACTIVE tasks: a
  When edit task 'a' to have text '' and press Enter
  Then no tasks left

Scenario: edit task, click Tab
  Given added ACTIVE tasks: a
  When edit task 'a' to have text 'a edit' and press Tab
  Then tasks are: a edit

