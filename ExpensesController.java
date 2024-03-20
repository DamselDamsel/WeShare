package weshare.controller;

import io.javalin.http.Handler;
import org.javamoney.moneta.function.MonetaryFunctions;
import org.jetbrains.annotations.NotNull;
import weshare.model.DateHelper;
import weshare.model.Expense;
import weshare.model.MoneyHelper;
import weshare.model.Person;
import weshare.persistence.ExpenseDAO;
import weshare.server.Routes;
import weshare.server.ServiceRegistry;
import weshare.server.WeShareServer;

import javax.money.MonetaryAmount;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static weshare.model.MoneyHelper.ZERO_RANDS;
import static weshare.model.MoneyHelper.amountOf;

public class ExpensesController {

    public static final Handler view = context -> {
        ExpenseDAO expensesDAO = ServiceRegistry.lookup(ExpenseDAO.class);
        Person personLoggedIn = WeShareServer.getPersonLoggedIn(context);

        MonetaryAmount totalAmount = amountOf(0);
        Collection<Expense> expenses = expensesDAO.findExpensesForPerson(personLoggedIn);
        for (Expense expense: expenses) {
            totalAmount = totalAmount.add(expense.amountLessPaymentsReceived());

        }


        Map<String, Object> viewMode = Map.of("expenses", expenses, "total", totalAmount);
        context.render("expenses.html", viewMode);
    };

    public static final Handler new_expense = context -> {
        ExpenseDAO expensesDAO = ServiceRegistry.lookup(ExpenseDAO.class);
        Person personLoggedIn = WeShareServer.getPersonLoggedIn(context);

        Collection<Expense> expenses = expensesDAO.findExpensesForPerson(personLoggedIn);
        Map<String, Object> viewMode = Map.of("expenses", expenses);

        context.render("newexpense.html");
    };



    public static Handler addExpense = context -> {
        ExpenseDAO expensesDAO = ServiceRegistry.lookup(ExpenseDAO.class);
        Person personLoggedIn = WeShareServer.getPersonLoggedIn(context);

        String description = context.formParamAsClass("description", String.class)
                .check(Objects::nonNull, "Email is required")
                .get();

        long cost = Long.parseLong(context.formParamAsClass("amount", String.class)
                .check(Objects::nonNull, "Email is required")
                .get());

        String dates = context.formParamAsClass("date", String.class)
                .check(Objects::nonNull, "Email is required")
                .get();

        expensesDAO.save(new Expense(personLoggedIn, description, MoneyHelper.amountOf(cost), DateHelper.TODAY));

        context.sessionAttribute(WeShareServer.SESSION_USER_KEY, personLoggedIn);
        context.redirect(Routes.EXPENSES);
    };

    public static Handler paymentrequest = context -> {
        ExpenseDAO expensesDAO = ServiceRegistry.lookup(ExpenseDAO.class);
        Person personLoggedIn = WeShareServer.getPersonLoggedIn(context);

        Collection<Expense> expenses = expensesDAO.findExpensesForPerson(personLoggedIn);
        System.out.println("hit the payment expense button");
        Map<String, Object> viewMode = Map.of("expenses", expenses);

    };

}
