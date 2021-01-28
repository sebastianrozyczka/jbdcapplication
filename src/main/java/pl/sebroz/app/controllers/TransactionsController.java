package pl.sebroz.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pl.sebroz.app.model.Transaction;
import pl.sebroz.app.dao.TransactionDAO;

import java.util.List;

@Controller
public class TransactionsController {
    private TransactionDAO transactionDAO;

    public TransactionsController(TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
    }

    @GetMapping("/")
    public String homePage(Model model, @RequestParam(name = "firstKeyword", required = false) String firstKeyword,
                           @RequestParam(name = "secondKeyword", required = false) String secondKeyword) {
        List<Transaction> transactionList = transactionDAO.list(firstKeyword, secondKeyword);
        model.addAttribute("transactions", transactionList);
        model.addAttribute("firstKeyword", firstKeyword);
        model.addAttribute("secondKeyword", secondKeyword);

        return "transactions";
    }

    @GetMapping("/new")
    public String newTransaction(Model model) {
        Transaction transaction = new Transaction();
        model.addAttribute("transaction", transaction);

        return "new";
    }

    @PostMapping("/save")
    public String save(Transaction transaction) {
        transactionDAO.save(transaction);

        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editTransaction(@PathVariable("id") int id) {
        ModelAndView modelAndView = new ModelAndView("edit");
        Transaction transaction = transactionDAO.get(id);
        modelAndView.addObject("transaction", transaction);

        return modelAndView;
    }

    @PostMapping("/update")
    public String update(Transaction transaction) {
        transactionDAO.update(transaction);

        return "redirect:/";
    }

    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id) {
        transactionDAO.delete(id);

        return "redirect:/";
    }
}
