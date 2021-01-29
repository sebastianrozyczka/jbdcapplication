package pl.sebroz.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.sebroz.app.model.Transaction;
import pl.sebroz.app.dao.TransactionDAO;
import pl.sebroz.app.model.Type;

import java.util.List;

@Controller
public class TransactionsController {
    private TransactionDAO transactionDAO;

    public TransactionsController(TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
    }

    @RequestMapping("/")
    public String homePage(Model model, @RequestParam(name = "check", required = false) Type type) {
        List<Transaction> transactionList = transactionDAO.listByType(type);
        model.addAttribute("transactions", transactionList);
        model.addAttribute("check", type);

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
    public String editTransaction(Model model, @PathVariable("id") int id) {
        Transaction transaction = transactionDAO.get(id);
        model.addAttribute("transaction", transaction);

        return "edit";
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
