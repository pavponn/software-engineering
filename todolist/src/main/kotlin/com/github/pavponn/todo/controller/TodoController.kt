package com.github.pavponn.todo.controller

import com.github.pavponn.todo.dao.TodoDao
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam

/**
 * @author pavponn
 */
@Controller
class TodoController(val todoDao: TodoDao) {

    @RequestMapping(value = ["/todos"], method = [RequestMethod.POST])
    fun addTodo(
        @RequestParam("description") description: String,
        @RequestParam("listId") listId: Int
    ): String {
        todoDao.addTodo(description, listId)
        return "redirect:/todos"
    }

    @RequestMapping(value = ["/"], method = [RequestMethod.GET])
    fun home(map: ModelMap): String {
        map.addAttribute("todos", todoDao.getTodos())
        map.addAttribute("todoListHeader", "All todos")
        map.addAttribute("todoLists", todoDao.getTodoLists())
        return "index"
    }

    @RequestMapping(value = ["/todos"], method = [RequestMethod.GET])
    fun getTodos(@RequestParam(name = "todoListId", required = false) todoListId: Int?, map: ModelMap): String {
        map.addAttribute("todoLists", todoDao.getTodoLists())
        if (todoListId != null) {
            map.addAttribute("todos", todoDao.getTodos(todoListId))
            map.addAttribute("todoListHeader", todoDao.getTodoLists().filter { it.id == todoListId }[0].name)
        } else {
            map.addAttribute("todos", todoDao.getTodos())
            map.addAttribute("todoListHeader", "All todos")

        }

        return "index"
    }

    @RequestMapping(value = ["/todolists"], method = [RequestMethod.POST])
    fun addTodoList(@RequestParam("name") name: String): String {
        todoDao.addTodoList(name)
        return "redirect:/todos"
    }

    @RequestMapping(value = ["/todolists"], method = [RequestMethod.DELETE])
    fun removeTodoList(@RequestParam("id") todoListId: Int): String {
        todoDao.deleteTodos(todoListId)
        todoDao.deleteTodoList(todoListId)
        return "redirect:/"
    }

    @RequestMapping(value = ["/todos"], method = [RequestMethod.PATCH])
    fun markTodo(
        @RequestParam("id") todoId: Int,
        @RequestParam("status") status: Boolean
    ): String {
        if (status) {
            todoDao.setAsDone(todoId)
        } else {
            todoDao.setAsTodo(todoId)
        }
        return "redirect:/todos"
    }


}